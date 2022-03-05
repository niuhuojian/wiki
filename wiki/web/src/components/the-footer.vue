<template>
  <a-layout-footer style="text-align: center">
    <span v-show="user.id">你好,{{user.name}}</span>
  </a-layout-footer>
</template>

<script lang="ts">
import store from '@/store';
import {computed, defineComponent } from 'vue';

export default defineComponent({
  name: 'the-footer',
  setup(){
    //computed表示当前变量是经过框架渲染计算得到的
    const user=computed(()=>
      store.state.user
    );

    return{
      user
    }
  }
});
</script>