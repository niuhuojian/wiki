<template>
  <a-layout>
    <the-header></the-header>
    <a-layout-content style="padding: 0 50px">
        <router-view/>
    </a-layout-content>
    <the-footer></the-footer>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import theHeader from '@/components/the-header';
import theFooter from '@/components/the-footer';
export default defineComponent({
  name: 'app',
  components:{
    theHeader,
    theFooter,
  }
});
</script>
<style>
#components-layout-demo-top-side .logo {
  float: left;
  width: 120px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}

.ant-row-rtl #components-layout-demo-top-side .logo {
  float: right;
  margin: 16px 0 16px 24px;
}

.site-layout-background {
  background: #fff;
}
</style>
