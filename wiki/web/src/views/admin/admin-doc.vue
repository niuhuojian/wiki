<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <a-layout>
    <a-layout style="padding: 0 24px 24px">
      <a-layout-content
          :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
      >
        <a-row :gutter="24">
          <a-col :span="8">
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
                     size="small"
                     v-if="level1.length>0"
                     :defaultExpandAllRows="true"
            >
              <template #name="{text,record}">
                {{record.sort}}、{{text}}
              </template>
              <template v-slot:action="{text, record}">
                <a-space size="small">
                  <a-button type="primary" @click="edit(record)" size="small">
                    编辑
                  </a-button>

                  <a-popconfirm
                      title="确认是否删除?"
                      ok-text="是"
                      cancel-text="否"
                      @confirm="del(record.id)"
                  >
                    <a-button type="danger" size="small">
                      删除
                    </a-button>
                  </a-popconfirm>
                </a-space>
              </template>
            </a-table>
          </a-col>
          <a-col :span="16">
            <p>
              <a-form layout="incline" :model="param">
                <a-form-item>
                  <a-button type="primary" @click="handleSave()">
                    保存
                  </a-button>
                </a-form-item>
              </a-form>
            </p>
            <a-form
                :model="doc"
                layout="vertical"
            >
              <a-form-item>
                <a-input v-model:value="doc.name" placeholder="名称"/>
              </a-form-item>
              <a-form-item label="父文档">
                <a-tree-select
                    v-model:value="doc.parent"
                    style="width: 100%"
                    :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                    placeholder="请选择父文档"
                    tree-default-expand-all
                    :tree-data="treeSelectData"
                    :replaceFields="{title:'name',key:'id',value:'id'}"
                >
                </a-tree-select>
              </a-form-item>
              <a-form-item>
                <a-input v-model:value="doc.sort" placeholder="顺序"/>
              </a-form-item>
              <a-form-item>
                <a-button type="primary" @click="handlePreviewContent()">
                  <EyeOutlined />内容预览
                </a-button>
              </a-form-item>
              <a-form-item>
                <div id="content"></div>
              </a-form-item>
            </a-form>
<!--            <a-modal-->
<!--                v-model:visible="modalVisible"-->
<!--                title="文档表单"-->
<!--                :confirm-loading="modalLoading"-->
<!--                @ok="handleModalOk"-->
<!--            >-->
<!--              -->
<!--            </a-modal>-->
          </a-col>
        </a-row>

        <a-drawer width="900" placement="right" :closable="false" :visible="drawerVisible"
                  @close="onDrawClose">
          <div class="wangeditor" :innerHTML="previewHtml"></div>
        </a-drawer>

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
import E from 'wangeditor'

export default defineComponent({
  name: 'AdminDoc',
  setup() {
    const route=useRoute();
    const editor = new E('#content');
    //修改富文本输入框的覆盖层级大小，越大越顶级（类似于ps中的图层），防止覆盖住其他元素
    editor.config.zIndex=0;

    const param=ref();
    param.value={};
    const docs = ref();
    const loading = ref(false);
    const treeSelectData=ref();
    treeSelectData.value=[];

    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
        slots: {customRender: 'name'}
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];

    const level1=ref();
    level1.value=[];
    const handleQuery = ()=>{
      loading.value=true;
      //清空表单内的数据，重新加载得到最新的数据
      level1.value=[];
      axios.get("/doc/all/"+route.query.ebookId).then((res)=>{
        loading.value=false;
        const data = res.data;
        if(data.success){
          docs.value=data.content;
          console.log("原始数组：",docs.value);
          level1.value=[];
          level1.value=Tool.array2Tree(docs.value,0);
          console.log("树形结构：",level1);

          //父文档下拉框初始化，相当于点击新增
          treeSelectData.value=Tool.copy(level1.value)||[];
          //为选择添加无选项
          treeSelectData.value.unshift({id:0,name:'无'});
        }else{
          message.error(data.message);
        }
      }
    )
  };
    const handleQueryContent = ()=>{
      axios.get("/doc/listContent/"+doc.value.id).then((res)=>{
        const data = res.data;
        if(data.success){
          editor.txt.html(data.content)
        }else{
          message.error(data.message);
        }
      }
    )
  };

    //因为使用数选择组件的话，会随当前编辑的节点而变化，所以另外声明一个响应式变量

    const doc=ref();
    doc.value={
      ebookId:route.query.ebookIda,
      id:route.query.id
    };
    // const modalVisible = ref<boolean>(false);
    // const modalLoading = ref<boolean>(false);

    // const showModal = () => {
    //   modalVisible.value = true;
    // };

    const handleSave = () => {
      // modalLoading.value = true;
      doc.value.content=editor.txt.html();
      axios.post("/doc/save",doc.value).then((res)=>{
        // modalLoading.value=false;
        const data=res.data;
        if(data.success){
          // modalVisible.value=false;
          message.success("保存成功");

          //重新加载数据
          handleQuery();
        }else{
          message.error(data.message);
        }
      })
    };

    const add=(record:any)=>{
      //清空富文本框
      editor.txt.html("");
      // modalVisible.value=true;
      doc.value={
        ebookId:route.query.ebookId,
      };

      //避免空指针报错
      treeSelectData.value=Tool.copy(level1.value)||[];
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
      //清空富文本框
      editor.txt.html("");
      // modalVisible.value=true;
      doc.value=Tool.copy(record);
      //点击编辑按钮后，等到doc从数据库获取值后，再进行查询内容回显
      handleQueryContent();
      //当前节点不能选择自身或所有子节点，作为父节点，会使树断开
      treeSelectData.value=Tool.copy(level1.value);
      setDisable(treeSelectData.value,record.id);

      //为选择树添加一个无
      treeSelectData.value.unshift({id:0,name:'无'});

    }

    const del=(id:string)=>{
      //level1为整个树的数据
      //id则为当前选择的目标节点
      ids.splice(0,ids.length);
      console.log("number="+ids.toString());
      getDeleteIds(level1.value,id);
      console.log("number="+ids.toString());
      axios.delete("/doc/delete/"+ids.join(",")).then((res)=>{
        const data=res.data;
        if(data.success){
          handleQuery();
        }
      });
    };

    //富文本预览
    const drawerVisible=ref(false);
    const previewHtml=ref();
    const handlePreviewContent=()=>{
      const html=editor.txt.html();
      previewHtml.value=html;
      drawerVisible.value=true;
    };
    const onDrawClose=()=>{
      drawerVisible.value=false;
    };

    onMounted(()=>{
      handleQuery();
      //因为修改了布局，加载就能看到输入页面，所以可以直接创建
      editor.create();
    });



    return {
      docs,
      loading,
      columns,
      add,
      edit,
      del,
      // modalVisible,
      // modalLoading,
      handleSave,
      doc,
      param,
      handleQuery,
      level1,
      treeSelectData,
      drawerVisible,
      previewHtml,
      handlePreviewContent,
      onDrawClose
    };
  },
});
</script>