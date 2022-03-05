import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Home from '../views/Home.vue'
import About from '../views/About.vue'
import AdminUser from '../views/admin/admin-user.vue'
import AdminEbook from '../views/admin/admin-ebook.vue'
import AdminCategory from '../views/admin/admin-category.vue'
import AdminDoc from '../views/admin/admin-doc.vue'
import Doc from '../views/doc.vue'
import {Tool} from "@/util/tool";
import store from "@/store";

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/about',
    name: 'About',
    component: About
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    // component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  {
    path: '/doc',
    name: 'Doc',
    component: Doc
  },
  {
    path: '/admin/user',
    name: 'AdminUser',
    component: AdminUser,
    meta:{
      loginRequire:true
    }
  },
  {
    path: '/admin/ebook',
    name: 'AdminEbook',
    component: AdminEbook,
    meta:{
      loginRequire:true
    }
  },
  {
    path: '/admin/category',
    name: 'AdminCategory',
    component: AdminCategory,
    meta:{
      loginRequire:true
    }
  },
  {
    path: '/admin/doc',
    name: 'AdminDoc',
    component: AdminDoc,
    meta:{
      loginRequire:true
    }
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

//路由拦截，每次跳转之前
//to：跳转到的新路由
//from：拦截的旧路由
//next：拦截路由方法，参数就是具体的拦截路由，无参数则是不拦截
router.beforeEach((to,from,next)=>{
  //item表示当前跳转路由的数据
  if(to.matched.some(function (item) {
    console.log(item,"是否需要登录校验:",item.meta.loginRequire);
    //true则表示需要校验，拦截执行
    return item.meta.loginRequire;
  })){
    const loginUser=store.state.user;
    if(Tool.isEmpty(loginUser)){
      console.log("用户未登录");
      //这里就表示拦截后并跳回首页/
      next('/');
    }else{
      next();
    }
  }else{
    next();
  }
});

export default router
