<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <a-layout>
    <a-layout style="padding: 0 24px 24px">
      <a-layout-content
          :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
      >
        <p>
          <a-form
              layout="inline"
              :model="param"
          >
            <a-form-item>
              <a-input v-model:value="param.name" placeholder="请输入查询名称"/>
            </a-form-item>
            <a-form-item>
              <a-button
                  type="primary"
                  @click="handleQuery()"
              >
                查询
              </a-button>
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="add()">
                新增
              </a-button>
            </a-form-item>
          </a-form>
        </p>
        <a-table :columns="columns"
                 :data-source="level1"
                 :row-key="record => record.id"
                 @resizeColumn="handleResizeColumn"
                 :loading="loading"
                 :pagination="false"
        >
          <template #cover="{text:cover}">
            <img v-if="cover" :src="cover" alt="avatar"/>
          </template>
          <template v-slot:action="{text, record}">
            <a-space size="small">
              <a-button type="primary" @click="edit(record)">
                编辑
              </a-button>
              <a-modal
                  v-model:visible="modalVisible"
                  title="文档表单"
                  :confirm-loading="modalLoading"
                  @ok="handleModalOk"
              >
                <a-form
                    :model="doc"
                    :label-col="{span:6}"
                >
                  <a-form-item label="名称">
                    <a-input v-model:value="doc.name" />
                  </a-form-item>
                  <a-form-item label="父文档">
                    <a-tree-select
                      v-model:value="doc.parent"
                      style="width: 100%"
                      :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                      placeholder="Please select"
                      tree-default-expand-all
                      :tree-data="treeSelectData"
                      :replaceFields="{title:'name',key:'id',value:'id'}"
                    >
                    </a-tree-select>
                  </a-form-item>
                  <a-form-item label="顺序">
                    <a-input v-model:value="doc.sort" />
                  </a-form-item>
                </a-form>
              </a-modal>
              <a-popconfirm
                  title="确认是否删除?"
                  ok-text="是"
                  cancel-text="否"
                  @confirm="del(record.id)"
              >
                <a-button type="danger">
                  删除
                </a-button>
              </a-popconfirm>

            </a-space>
          </template>
        </a-table>
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script lang="ts">
import axios from "axios";
import { defineComponent, ref ,onMounted} from 'vue';
import {message} from "ant-design-vue";
import {Tool} from '@/util/tool.ts';
import { useRoute } from "vue-router";

export default defineComponent({
  name: 'AdminDoc',
  setup() {
    const route=useRoute();

    const param=ref();
    param.value={};
    const docs = ref();
    const loading = ref(false);
    const columns = [
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '父分类',
        key: 'parent',
        dataIndex: 'parent'
      },
      {
        title: '顺序',
        dataIndex: 'sort'
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];

    const level1=ref();
    const handleQuery = ()=>{
      loading.value=true;
      //清空表单内的数据，重新加载得到最新的数据
      level1.value=[];
      axios.get("/doc/all").then((res)=>{
        loading.value=false;
        const data = res.data;

        if(data.success){
          docs.value=data.content;
          console.log("原始数组：",docs.value);
          level1.value=[];
          level1.value=Tool.array2Tree(docs.value,0);
          console.log("树形结构：",level1);
        }else{
          message.error(data.message);
        }
      }
    )
  };

    //因为使用数选择组件的话，会随当前编辑的节点而变化，所以另外声明一个响应式变量
    const treeSelectData=ref();
    treeSelectData.value=[];
    const doc=ref({});
    const modalVisible = ref<boolean>(false);
    const modalLoading = ref<boolean>(false);

    const showModal = () => {
      modalVisible.value = true;
    };

    const handleModalOk = () => {
      modalLoading.value = true;
      axios.post("/doc/save",doc.value).then((res)=>{
        modalLoading.value=false;
        const data=res.data;
        if(data.success){
          modalVisible.value=false;

          //重新加载数据
          handleQuery();
        }else{
          message.error(data.message);
        }
      })
    };

    const add=(record:any)=>{
      modalVisible.value=true;
      doc.value={
        ebookId:route.query.ebookId
      };

      treeSelectData.value=Tool.copy(level1.value);
      treeSelectData.value.unshift({id:0,name:'无'});

    }

    const setDisable=(treeSelectData:any,id:any)=>{
      //遍历数组，即遍历某一层节点
      for(let i=0;i<treeSelectData.length;i++){
        const node=treeSelectData[i];
        if(node.id === id){
          //如果当前节点就是目标节点
          console.log("disabled",node);
          //将目标节点设置为disabled
          node.disabled=true;

          //遍历所有子节点，将所有子节点全部加上disabled
          const children=node.children;
          if(Tool.isNotEmpty(children)){
            for(let j=0;j<children.length;j++){
              setDisable(children,children[j].id);
            }
          }
        }else{
          //如果当前节点不是目标节点，就到子节点找找看
          const children=node.children;
          if(Tool.isNotEmpty(children)){
            setDisable(children,id);
          }
        }
      }
    };

    const ids:Array<string>=[];
    const getDeleteIds=(treeSelectData:any,id:any)=>{
      //遍历数组，即遍历某一层节点
      for(let i=0;i<treeSelectData.length;i++){
        const node=treeSelectData[i];
        if(node.id === id){
          // //将目标节点设置为disabled
          // node.disabled=true;
          ids.push(node.id);

          //遍历所有子节点
          const children=node.children;
          if(Tool.isNotEmpty(children)){
            for(let j=0;j<children.length;j++){
              getDeleteIds(children,children[j].id);
            }
          }
        }else{
          //如果当前节点不是目标节点，就到子节点找找看
          const children=node.children;
          if(Tool.isNotEmpty(children)){
            getDeleteIds(children,id);
          }
        }
      }
    };

    const edit=(record:any)=>{
      modalVisible.value=true;
      doc.value=Tool.copy(record);

      //当前节点不能选择自身或所有子节点，作为父节点，会使树断开
      treeSelectData.value=Tool.copy(level1.value);
      setDisable(treeSelectData.value,record.id);

      //为选择树添加一个无
      treeSelectData.value.unshift({id:0,name:'无'});
    }

    const del=(id:number)=>{
      //level1为整个树的数据
      //id则为当前选择的目标节点
      getDeleteIds(level1.value,id);
      axios.delete("/doc/delete/"+ids.join(",")).then((res)=>{
        const data=res.data;
        if(data.success){
          handleQuery();

        }
      });
    };

    onMounted(()=>{
      handleQuery();
    });



    return {
      docs,
      loading,
      columns,
      add,
      edit,
      del,
      modalVisible,
      modalLoading,
      handleModalOk,
      doc,
      param,
      handleQuery,
      level1,
      treeSelectData
    };
  },
});
</script>