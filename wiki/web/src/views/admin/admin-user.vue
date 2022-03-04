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
              <a-input v-model:value="param.loginName" placeholder="请输入查询用户名"/>
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
                 :data-source="users"
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
<!--                <router-link :to="'/admin/doc?userId='+record.id">-->
<!--                  <a-button type="primary">文档管理</a-button>-->
<!--                </router-link>-->
              <a-button type="primary" @click="edit(record)">
                编辑
              </a-button>
              <a-modal
                  v-model:visible="modalVisible"
                  title="用户表单"
                  :confirm-loading="modalLoading"
                  @ok="handleModalOk"
              >
                <a-form
                    :model="user"
                    :label-col="{span:6}"
                >
                  <a-form-item  label="用户名">
                    <!--disabled属性限制了编辑时用户名被修改，但新增还是需要输入，因此id作为判断条件，有id就隐藏，没有id就新增编辑-->
                    <a-input v-model:value="user.loginName" :disabled="!!user.id"/>
                  </a-form-item>
                  <a-form-item label="昵称">
                    <a-input v-model:value="user.name" />
                  </a-form-item>
<!--                  <a-form-item label="分类">-->
<!--                    <a-cascader v-model:value="categoryIds"-->
<!--                                :field-names="{label:'name',value:'id',children:'children'}"-->
<!--                                :options="level1"-->
<!--                                placeholder="Please select" />-->
<!--                  </a-form-item>-->
                  <a-form-item label="密码">
                    <a-input v-model:value="user.password"/>
<!--                    <a-textarea v-model:value="user.description" type="text" />-->
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

//因为typescript无法识别第三方js，就直接定义个变量防止异常
declare let hexMd5:any;
declare let KEY:any;

export default defineComponent({
  name: 'AdminUser',
  setup() {
    const param=ref();
    param.value={};
    const users = ref();
    const pagination =ref({
      current : 1 ,
      pageSize : 6,
      total : 0
    });
    const loading = ref(false);
    const columns = [
      {
        title: '用户名',
        dataIndex: 'loginName'
      },
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '密码',
        dataIndex: 'password'
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
      users.value=[];
      axios.get("/user/list",{
        params:{
          page:params.page,
          size:params.size,
          loginName:param.value.loginName
        }
      }).then((res)=>{
        loading.value=false;
        const data = res.data;
        if(data.success){
          users.value=data.content.list;
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
    const user=ref();
    const modalVisible = ref<boolean>(false);
    const modalLoading = ref<boolean>(false);

    const showModal = () => {
      modalVisible.value = true;
    };

    const handleModalOk = () => {
      modalLoading.value = true;
      // user.value.category1Id=categoryIds.value[0];
      // user.value.category2Id=categoryIds.value[1];

      user.value.password=hexMd5(user.value.password+KEY);
      axios.post("/user/save",user.value).then((res)=>{
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
      user.value={};
    }
    const edit=(record:any)=>{
      modalVisible.value=true;
      user.value=Tool.copy(record);
      // categoryIds.value=[user.value.category1Id,user.value.category2Id];
    }

    const del=(id:string)=>{
      axios.delete("/user/delete/"+id).then((res)=>{
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
      users,
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
      user,
      param,
      handleQuery,
      categoryIds,
      level1,
      getCategoryName
    };
  },
});
</script>