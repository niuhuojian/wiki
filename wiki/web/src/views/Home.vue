<template>
  <div class="home">
    <a-layout>
      <a-layout-sider width="200" style="background: #fff">
        <a-menu
            v-model:selectedKeys="selectedKeys2"
            v-model:openKeys="openKeys"
            mode="inline"
            :style="{ height: '100%', borderRight: 0 }"
        >
          <a-sub-menu key="sub1">
            <template #title>
              <span>
                <user-outlined />
                subnav 1
              </span>
            </template>
            <a-menu-item key="1">option1</a-menu-item>
            <a-menu-item key="2">option2</a-menu-item>
            <a-menu-item key="3">option3</a-menu-item>
            <a-menu-item key="4">option4</a-menu-item>
          </a-sub-menu>
          <a-sub-menu key="sub2">
            <template #title>
              <span>
                <laptop-outlined />
                subnav 2
              </span>
            </template>
            <a-menu-item key="5">option5</a-menu-item>
            <a-menu-item key="6">option6</a-menu-item>
            <a-menu-item key="7">option7</a-menu-item>
            <a-menu-item key="8">option8</a-menu-item>
          </a-sub-menu>
          <a-sub-menu key="sub3">
            <template #title>
              <span>
                <notification-outlined />
                subnav 3
              </span>
            </template>
            <a-menu-item key="9">option9</a-menu-item>
            <a-menu-item key="10">option10</a-menu-item>
            <a-menu-item key="11">option11</a-menu-item>
            <a-menu-item key="12">option12</a-menu-item>
          </a-sub-menu>
        </a-menu>
      </a-layout-sider>
      <a-layout style="padding: 0 24px 24px">
        <a-layout-content
            :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
          <a-list item-layout="vertical" size="large" :pagination="pagination" :grid="{ gutter: 20, column: 3 }" :data-source="ebook">
            <template #renderItem="{ item }">
              <a-list-item key="item.name">
                <template #actions>
                  <span v-for="{ type, text } in actions" :key="type">
                    <component :is="type" style="margin-right: 8px" />
                    {{ text }}
                  </span>
                </template>
                <a-list-item-meta :description="item.description">
                  <template #title>
                    <a :href="item.href">{{ item.name }}</a>
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


export default defineComponent({
  name: 'Home',
  setup(){
    //ref用来定义响应式数据，在这里用来装载返回的ebook数据
    const ebook=ref();
    //const ebook1=reactive({books:[]});
    onMounted(()=>{
      axios.get("/ebook/list",{
        params:{
          page:1,
          size:1000
        }
      }).then((response)=>{
        //获取响应回来的数据，仔细的说就是传回来的EbookResp对象
        const data=response.data;
        ebook.value=data.content.list;
        //ebook1.books=data.content;
      });
    });

    return{
      ebook,
      //books:toRef(ebook1,"books"),
      pagination:{
        pageSize:3
      },
    }
  },
});
</script>
