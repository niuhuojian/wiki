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
                  title="分类表单"
                  :confirm-loading="modalLoading"
                  @ok="handleModalOk"
              >
                <a-form
                    :model="category"
                    :label-col="{span:6}"
                >
                  <a-form-item label="名称">
                    <a-input v-model:value="category.name" />
                  </a-form-item>
                  <a-form-item label="父分类">
                    <a-input v-model:value="category.parent" />
                  </a-form-item>
                  <a-form-item label="顺序">
                    <a-input v-model:value="category.sort" />
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

export default defineComponent({
  name: 'AdminCategory',
  setup() {
    const param=ref();
    param.value={};
    const categorys = ref();
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
      axios.get("/category/all").then((res)=>{
        loading.value=false;
        const data = res.data;

        if(data.success){
          categorys.value=data.content;
          console.log("原始数组：",categorys.value);
          level1.value=[];
          level1.value=Tool.array2Tree(categorys.value,0);
          console.log("树形结构：",level1);
        }else{
          message.error(data.message);
        }
      }
    )
  };


    const category=ref({});
    const modalVisible = ref<boolean>(false);
    const modalLoading = ref<boolean>(false);

    const showModal = () => {
      modalVisible.value = true;
    };

    const handleModalOk = () => {
      modalLoading.value = true;
      axios.post("/category/save",category.value).then((res)=>{
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
      category.value={};
    }
    const edit=(record:any)=>{
      modalVisible.value=true;
      category.value=Tool.copy(record);
    }

    const del=(id:number)=>{
      axios.delete("/category/delete/"+id).then((res)=>{
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
      categorys,
      loading,
      columns,
      add,
      edit,
      del,
      modalVisible,
      modalLoading,
      handleModalOk,
      category,
      param,
      handleQuery,
      level1
    };
  },
});
</script>