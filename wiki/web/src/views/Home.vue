<template>
  <div class="home">
    <a-layout>
      <a-layout-sider width="200" style="background: #fff">
        <a-menu
            mode="inline"
            :style="{ height: '100%', borderRight: 0 }"
            @click="handleClick"
        >
          <a-menu-item key="welcome">
            <MailOutlined />
            <span>欢迎</span>
          </a-menu-item>
          <a-sub-menu v-for="item in level1" :key="item.id">
            <template v-slot:title>
              <span><user-outlined />{{item.name}}</span>
            </template>
            <a-menu-item v-for="child in item.children" :key="child.id">
              <MailOutlined /><span>{{child.name}}</span>
            </a-menu-item>
          </a-sub-menu>
        </a-menu>
      </a-layout-sider>
      <a-layout style="padding: 0 24px 24px">
        <a-layout-content
            :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
          <div class="welcome" v-show="isShowWelcome">
            <h1>欢迎</h1>
          </div>
          <a-list v-show="!isShowWelcome" item-layout="vertical" size="large" :pagination="pagination" :grid="{ gutter: 20, column: 3 }" :data-source="ebook">
            <template #renderItem="{ item }">
              <a-list-item key="item.name">
                <template #actions>
                  <span>
                    <component :is="'FileOutlined'" style="margin-right: 8px" />
                    {{ item.docCount }}
                  </span>
                  <span>
                    <component :is="'UserOutlined'" style="margin-right: 8px" />
                    {{ item.viewCount }}
                  </span>
                  <span>
                    <component :is="'LikeOutlined'" style="margin-right: 8px" />
                    {{ item.voteCount }}
                  </span>
                </template>
                <a-list-item-meta :description="item.description">
                  <template #title>
                    <router-link :to="'/doc?ebookId='+item.id">
                      {{item.name}}
                    </router-link>
                  </template>
                  <template #avatar><a-avatar :src="item.cover" /></template>
                </a-list-item-meta>
              </a-list-item>
            </template>
          </a-list>
        </a-layout-content>
      </a-layout>
    </a-layout>

  </div>
</template>

<style scoped>
  .ant-avatar{
    width: 50px;
    height: 50px;
    line-height: 50px;
    border-radius: 8%;
    margin: 5px 0;
  }
</style>
<script lang="ts">
import { defineComponent,onMounted,ref } from 'vue';
import axios from 'axios';
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";
import {LikeOutlined} from "@ant-design/icons-vue";

export default defineComponent({
  name: 'Home',
  setup(){
    //ref用来定义响应式数据，在这里用来装载返回的ebook数据
    const ebook=ref();
    //const ebook1=reactive({books:[]});

    const level1=ref();
    let categorys:any;
    const handleQueryCategory = ()=>{
      axios.get("/category/all").then((res)=>{
            const data = res.data;

            if(data.success){
              categorys=data.content;
              console.log("原始数组：",categorys);
              level1.value=[];
              level1.value=Tool.array2Tree(categorys,0);
              console.log("树形结构：",level1);
            }else{
              message.error(data.message);
            }
          }
      )
    };

    const isShowWelcome=ref(true);
    let categoryId2=0;
    const handleQueryEbook=()=>{
      axios.get("/ebook/list",{
        params:{
          page:1,
          size:1000,
          categoryId2: categoryId2
        }
      }).then((response)=>{
        //获取响应回来的数据，仔细的说就是传回来的EbookResp对象
        const data=response.data;
        ebook.value=data.content.list;
        //ebook1.books=data.content;
      });
    };

    const handleClick=(value:any)=>{
      console.log("click",value);
      if (value.key === 'welcome'){
        isShowWelcome.value = true;
      }else{
        categoryId2 = value.key;
        isShowWelcome.value = false;
        handleQueryEbook();
      }
    }



    onMounted(()=>{
      handleQueryCategory();
    });


    return{
      ebook,
      //books:toRef(ebook1,"books"),
      pagination:{
        pageSize:3
      },
      handleClick,
      isShowWelcome,
      level1
    }
  },
});
</script>
