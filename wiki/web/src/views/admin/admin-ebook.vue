<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <a-layout>
    <a-layout style="padding: 0 24px 24px">
      <a-layout-content
          :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
      >
        <a-table :columns="columns"
                 :data-source="ebooks"
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
                  title="编辑确认"
                  :confirm-loading="modalLoading"
                  @ok="handleModalOk"
              >
                <a-form
                    :model="ebook"
                    :label-col="{span:6}"
                >
                  <a-form-item  label="封面">
                    <a-input v-model:value="ebook.cover" />
                  </a-form-item>
                  <a-form-item label="名称">
                    <a-input v-model:value="ebook.name" />
                  </a-form-item>
                  <a-form-item label="分类一">
                    <a-input-number v-model:value="ebook.category1Id" />
                  </a-form-item>
                  <a-form-item label="分类二">
                    <a-input v-model:value="ebook.category2Id" />
                  </a-form-item>
                  <a-form-item label="描述">
                    <a-textarea v-model:value="ebook.desc" type="text" />
                  </a-form-item>
                </a-form>
              </a-modal>
              <a-button type="danger">
                  删除
              </a-button>
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

export default defineComponent({
  name: 'AdminEbook',
  setup() {
    const ebooks = ref();
    const pagination =ref({
      current : 1 ,
      pageSize : 3,
      total : 0
    });
    const loading = ref(false);
    const columns = [
      {
        title: '封面',
        dataIndex: 'cover',
        slots: { customRender: 'cover' }
      },
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '分类1',
        key: 'catrgory1Id',
        dataIndex: 'category1Id'
      },
      {
        title: '分类2',
        dataIndex: 'category1Id'
      },
      {
        title: '文档数',
        dataIndex: 'docCount'
      },
      {
        title: '阅读数',
        dataIndex: 'viewCount'
      },
      {
        title: '点赞数',
        dataIndex: 'voteCount'
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];

    const handleQuery = (params: any)=>{
      loading.value=true;
      axios.get("/ebook/list",{
        params:{
          page:params.page,
          size:params.size
        }
      }).then((res)=>{
        loading.value=false;
        const data = res.data;
        ebooks.value=data.content.list;

        //重置分页按钮
        pagination.value.current=params.page;
        pagination.value.total=data.content.total;
        }
    )
  };
    const handlePageChange = (pagination : any)=>{
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });

    };


    const ebook=ref({});
    const modalVisible = ref<boolean>(false);
    const modalLoading = ref<boolean>(false);

    const showModal = () => {
      modalVisible.value = true;
    };

    const handleModalOk = () => {
      modalLoading.value = true;
      modalVisible.value=false;
    };

    const edit=(record:any)=>{
      modalVisible.value=true;
      ebook.value=record;
    }

    onMounted(()=>{
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    });



    return {
      ebooks,
      pagination,
      loading,
      columns,
      handlePageChange,
      edit,
      modalVisible,
      modalLoading,
      handleModalOk,
      ebook
    };
  },
});
</script>