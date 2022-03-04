<template>
  <a-layout>
    <a-layout-content :style="{background: '#fff', padding:'24px', margin:0,minHeight:'280px'}">
      <a-row>
        <a-col :span="6">
          <a-tree
            v-if="level1.length>0"
            :tree-data="level1"
            @select="onSelect"
            :replaceFields="{title:'name',key:'id',value:'id'}"
            :defaultExpandAll="true">

          </a-tree>
        </a-col>
        <a-col :span="18">
          <div :innerHTML="html"></div>
        </a-col>
      </a-row>
    </a-layout-content>
  </a-layout>
</template>
<script lang="ts">
  import {defineComponent,onMounted,ref,createVNode} from "vue";
  import axios from "axios";
  import {message} from "ant-design-vue";
  import {Tool} from "@/util/tool";
  import {useRoute} from "vue-router";

  export default defineComponent({
    name: 'Doc',
    setup(){
      const route=useRoute();
      const docs=ref();
      const html=ref();

      const level1=ref();
      level1.value=[];
      const handleQuery = ()=>{
        // loading.value=true;
        //清空表单内的数据，重新加载得到最新的数据
        level1.value=[];
        axios.get("/doc/all/"+route.query.ebookId).then((res)=>{
              // loading.value=false;
              const data = res.data;
              if(data.success){
                docs.value=data.content;
                console.log("原始数组：",docs.value);
                level1.value=[];
                level1.value=Tool.array2Tree(docs.value,0);
                console.log("树形结构：",level1);
              }else{
                message.error(data.message);
              }
            }
        )
      };

      //树形组件支持多选，selectedKeys就代表选中的id数组
      const onSelect=(selectedKeys:any, info:any)=>{
        console.log('selected',selectedKeys,info);
        if(Tool.isNotEmpty(selectedKeys)){
          //而因为显示内容时只能显示一个，选中第一个进行展示
          handleQueryContent(selectedKeys[0]);
        }
      };


      const handleQueryContent = (id:number)=>{
        axios.get("/doc/listContent/"+doc.value.id).then((res)=>{
              const data = res.data;
              if(data.success){
                html.value=data.content;
              }else{
                message.error(data.message);
              }
            }
        )
      };

      onMounted(()=>{
        handleQuery();
      })

      return{
        level1,
        html,
        onSelect
      }
    }
  });
</script>