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
                  @click="handleQuery({page:1,size:pagination.pageSize})"
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
                 :data-source="categorys"
                 :row-key="record => record.id"
                 @resizeColumn="handleResizeColumn"
                 :pagination="pagination"
                 :loading="loading"
                 @change="handlePageChange">
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
    const pagination =ref({
      current : 1 ,
      pageSize : 6,
      total : 0
    });
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

    const handleQuery = (params: any)=>{
      loading.value=true;
      axios.get("/category/list",{
        params:{
          page:params.page,
          size:params.size,
          name:param.value.name
        }
      }).then((res)=>{
        loading.value=false;
        const data = res.data;
        if(data.success){
          categorys.value=data.content.list;

          //重置分页按钮
          pagination.value.current=params.page;
          pagination.value.total=data.content.total;
        }else{
          message.error(data.message);
        }
      }
    )
  };
    const handlePageChange = (pagination : any)=>{
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });

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
          handleQuery({
            page:pagination.value.current,
            size:pagination.value.pageSize,
          });
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
          handleQuery({
            page:pagination.value.current,
            size:pagination.value.pageSize
          });

        }
      });
    };

    onMounted(()=>{
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    });



    return {
      categorys,
      pagination,
      loading,
      columns,
      handlePageChange,
      add,
      edit,
      del,
      modalVisible,
      modalLoading,
      handleModalOk,
      category,
      param,
      handleQuery
    };
  },
});
</script>