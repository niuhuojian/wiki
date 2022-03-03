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
                 :data-source="ebooks"
                 :row-key="record => record.id"
                 @resizeColumn="handleResizeColumn"
                 :pagination="pagination"
                 :loading="loading"
                 @change="handlePageChange">
          <template #cover="{text:cover}">
            <img v-if="cover" :src="cover" alt="avatar"/>
          </template>
          <template v-slot:category="{text,record}">
            <span>{{getCategoryName(record.category1Id)}} / {{getCategoryName(record.category2Id)}}</span>
          </template>
          <template v-slot:action="{text, record}">
            <a-space size="small">
                <router-link :to="'/admin/doc?ebookId='+record.id">
                  <a-button type="primary">文档管理</a-button>
                </router-link>
              <a-button type="primary" @click="edit(record)">
                编辑
              </a-button>
              <a-modal
                  v-model:visible="modalVisible"
                  title="电子书表单"
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
                  <a-form-item label="分类">
                    <a-cascader v-model:value="categoryIds"
                                :field-names="{label:'name',value:'id',children:'children'}"
                                :options="level1"
                                placeholder="Please select" />
                  </a-form-item>
                  <a-form-item label="描述">
                    <a-textarea v-model:value="ebook.description" type="text" />
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
  name: 'AdminEbook',
  setup() {
    const param=ref();
    param.value={};
    const ebooks = ref();
    const pagination =ref({
      current : 1 ,
      pageSize : 6,
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
        title: '分类',
        slots: { customRender: 'category'}
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
      //为了获取最新的数据，先清空，再赋值
      //原因不详
      ebooks.value=[];
      axios.get("/ebook/list",{
        params:{
          page:params.page,
          size:params.size,
          name:param.value.name
        }
      }).then((res)=>{
        loading.value=false;
        const data = res.data;
        if(data.success){
          ebooks.value=data.content.list;

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

    const categoryIds=ref();
    const ebook=ref();
    const modalVisible = ref<boolean>(false);
    const modalLoading = ref<boolean>(false);

    const showModal = () => {
      modalVisible.value = true;
    };

    const handleModalOk = () => {
      modalLoading.value = true;
      ebook.value.category1Id=categoryIds.value[0];
      ebook.value.category2Id=categoryIds.value[1];
      axios.post("/ebook/save",ebook.value).then((res)=>{
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
      ebook.value={};
    }
    const edit=(record:any)=>{
      modalVisible.value=true;
      ebook.value=Tool.copy(record);
      categoryIds.value=[ebook.value.category1Id,ebook.value.category2Id];
    }

    const del=(id:number)=>{
      axios.delete("/ebook/delete/"+id).then((res)=>{
        const data=res.data;
        if(data.success){
          handleQuery({
            page:pagination.value.current,
            size:pagination.value.pageSize
          });

        }
      });
    };

    const level1=ref();
    let categorys:any;
    const handleQueryCategory = ()=>{
      loading.value=true;
      axios.get("/category/all").then((res)=>{
            loading.value=false;
            const data = res.data;

            if(data.success){
              categorys=data.content;
              console.log("原始数组：",categorys);
              level1.value=[];
              level1.value=Tool.array2Tree(categorys,0);
              console.log("树形结构：",level1.value);
              handleQuery({
                page: 1,
                size: pagination.value.pageSize
              });
            }else{
              message.error(data.message);
            }
          }
      )
    };

    const getCategoryName=(cid:number)=>{
      let result="";
      categorys.forEach((item:any)=>{
        if(item.id===cid){
          result=item.name;
        }
      });
      return result;
    };

    onMounted(()=>{
      handleQueryCategory();

    });



    return {
      ebooks,
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
      ebook,
      param,
      handleQuery,
      categoryIds,
      level1,
      getCategoryName
    };
  },
});
</script>