<template>
  <a-layout>
    <a-layout-content :style="{background: '#fff', padding:'24px', margin:0,minHeight:'280px'}">
      <h3 v-if="level1.length===0">对不起，找不到相关文档</h3>
      <a-row>
        <a-col :span="6">
          <a-tree
            v-if="level1.length>0"
            :tree-data="level1"
            @select="onSelect"
            :replaceFields="{title:'name',key:'id',value:'id'}"
            :defaultExpandAll="true"
            :defaultSelectedKeys="defaultSelectedKeys">

          </a-tree>
        </a-col>
        <a-col :span="18">
          <div class="wangeditor" :innerHTML="html"></div>
        </a-col>
      </a-row>
    </a-layout-content>
  </a-layout>
</template>


<style>
/*wangeditor默认table样式*/
.wangeditor table {
  border-top: 1px solid #ccc;
  border-left: 1px solid #ccc;
}
.wangeditor table td,
.wangeditor table th {
  border-bottom: 1px solid #ccc;
  border-right: 1px solid #ccc;
  padding: 3px 5px;
}
.wangeditor table th {
  border-bottom: 2px solid #ccc;
  text-align: center;
}
/* blockquote 样式 */
.wangeditor blockquote {
  display: block;
  border-left: 8px solid #d0e5f2;
  padding: 5px 10px;
  margin: 10px 0;
  line-height: 1.4;
  font-size: 100%;
  background-color: #f1f1f1;
}

/* code 样式 */
.wangeditor code {
  display: inline-block;
  *display: inline;
  *zoom: 1;
  background-color: #f1f1f1;
  border-radius: 3px;
  padding: 3px 5px;
  margin: 0 3px;
}
.wangeditor pre code {
  display: block;
}

/* ul ol 样式 */
.wangeditor ul, ol {
  margin: 10px 0 10px 20px;
}

/*和antd p样式冲突，选择覆盖掉*/
.wangeditor blockquote p{
  font-family: "YouYuan";
  margin: 20px 10px !important;
  font-size: 16px !important;
  font-weight: 600;
}
</style>


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
      const defaultSelectedKeys=ref();
      defaultSelectedKeys.value=[];



      const handleQueryContent = (id:number)=>{
        axios.get("/doc/listContent/"+id).then((res)=>{
              const data = res.data;
              if(data.success){
                html.value=data.content;
              }else{
                message.error(data.message);
              }
            }
        )
      };


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
                // console.log("原始数组：",docs.value);
                level1.value=[];
                level1.value=Tool.array2Tree(docs.value,0);
                if(Tool.isNotEmpty(level1)){
                  //为了在默认状态显示第一个文档的内容，选中树形结构的第一个节点
                  defaultSelectedKeys.value=[level1.value[0].id];
                  handleQueryContent(level1.value[0].id);
                }
                // console.log("树形结构：",level1);
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




      onMounted(()=>{
        handleQuery();
      })

      return{
        level1,
        html,
        onSelect,
        defaultSelectedKeys
      }
    }
  });
</script>