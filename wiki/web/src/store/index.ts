import { createStore } from 'vuex'

declare let SessionStorage:any;
//定义一个变量用于sessionstorage的get方法参数
const USER="USER";

const store = createStore({
  state: {
    //获取USER，获取不到就定义为一个空对象
    user:SessionStorage.get(USER)||{}
  },

  //对变量的操作（同步）
  mutations: {
    //通过全局变量state操作内部的user
    //user则是指外部传入的user
    setUser(state,user){
      state.user=user;
      //除了登录后将user值放入全局，还放入缓存中，刷新后从缓存取值
      SessionStorage.set(USER,user);
    }
  },

  //对变量的操作（异步）
  actions: {
  },
  modules: {
  }
})


export default store;
