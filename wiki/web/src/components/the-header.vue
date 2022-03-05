<template>
  <a-layout-header class="header">
    <div class="logo" />
    <a-menu
        v-model:selectedKeys="selectedKeys1"
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="home">
        <router-link to="/">首页</router-link>
      </a-menu-item>
      <a-menu-item key="AdminUser">
        <router-link to="/admin/user">用户管理</router-link>
      </a-menu-item>
      <a-menu-item key="AdminEbook">
        <router-link to="/admin/ebook">电子书管理</router-link>
      </a-menu-item>
      <a-menu-item key="AdminCategory">
        <router-link to="/admin/category">分类管理</router-link>
      </a-menu-item>
      <a-menu-item key="about">
        <router-link to="/about">关于我们</router-link>
      </a-menu-item>
      <a-menu-item>
        <a class="login-menu" v-show="user.id">
          <span>您好:{{ user.name }}</span>
        </a>
        <a class="login-menu" @click="showLoginModal" v-show="!user.id">
          <span>登录</span>
        </a>
      </a-menu-item>
      <a-menu-item>
        <a-popconfirm
            title="确认是否退出?"
            ok-text="是"
            cancel-text="否"
            @confirm="logout()"
        >
          <a class="login-menu" v-show="user.id">
            <span>退出登录</span>
          </a>
        </a-popconfirm>
      </a-menu-item>
    </a-menu>

    <a-modal title="登录"
             v-model:visible="loginModalVisible"
             :confirm-loading="loginModalLoading"
             @ok="login"
             >
      <a-form :model="loginUser" :label-col="{span:6}" :wrapper-col="{ span: 18 }">
        <a-form-item label="用户名">
          <a-input v-model:value="loginUser.loginName" />
        </a-form-item>
        <a-form-item label="密码">
          <a-input v-model:value="loginUser.password" type="password"/>
        </a-form-item>
      </a-form>
    </a-modal>
  </a-layout-header>
</template>

<script lang="ts">
import axios from 'axios';
import {computed, defineComponent, ref} from 'vue';
import {message} from "ant-design-vue";
import store from "@/store";

declare let hexMd5:any;
declare let KEY:any;

export default defineComponent({
  name: 'the-header',
  setup(){
    //登陆后显示用
    const user=computed(()=>
        store.state.user
    );

    //登陆用
    const loginUser=ref({
      loginName:"test",
      password:"test"
    });

    const loginModalVisible=ref(false);
    const loginModalLoading=ref(false);
    const showLoginModal=()=>{
      loginModalVisible.value=true;
    };


    const login=()=>{
      console.log("开始登录");
      loginModalLoading.value=true;
      loginUser.value.password=hexMd5(loginUser.value.password+KEY);
      axios.post('/user/login',loginUser.value).then((res)=>{
        loginModalLoading.value=false;
        const data=res.data;
        if(data.success){
          loginModalVisible.value=false;
          message.success("登陆成功");

          //通过commit触发mutation内部的方法
          //state为内部变量不需要传入，user则是传入外部的user值
          //在这里发现直接传入user.value无法获取，原因是把user的定义给变化了，不再是ref响应
          //所以根据返回的data，改为data.content
          store.commit("setUser",data.content);
        }else{
          message.error(data.message);
        }
      });
    };

    const logout=()=>{
      console.log("退出登录");
      axios.get('/user/logout/'+user.value.token).then((res)=>{
        const data=res.data;
        if(data.success){
          message.success("退出登录成功");

          //通过commit触发mutation内部的方法
          //state为内部变量不需要传入，user则是传入外部的user值
          //在这里发现直接传入user.value无法获取，原因是把user的定义给变化了，不再是ref响应
          //所以根据返回的data，改为data.content
          store.commit("setUser", {});
        }else{
          message.error(data.message);
        }
      });
    };

    return{
      loginModalVisible,
      loginModalLoading,
      showLoginModal,
      login,
      loginUser,
      user,
      logout
    }
  },

});
</script>

<style>
.login-menu {
  float: right;
  color: white;
  padding-left: 10px;
}
</style>
