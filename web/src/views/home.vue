<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu

          mode="inline"
          :style="{ height: '100%', borderRight: 0 }"
          @click = "handleClick"
      >
        <a-menu-item key="welcome">
          <span>Welcome!</span>
        </a-menu-item>
        <a-sub-menu v-for="item in level1" :key="item.id" :disabled="false">
          <template v-slot:title>
            <span><user-outlined />{{item.name}}</span>
          </template>
          <a-menu-item v-for="child in item.children" :key="child.id">
            <MailOutlined /><span>{{child.name}}</span>
          </a-menu-item>
        </a-sub-menu>
        <a-menu-item key="tip" :disabled="true">
          <span>The menu can be modified in category admin panel.</span>
        </a-menu-item>
      </a-menu>
    </a-layout-sider>

    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-list item-layout="vertical" size="large" :grid="{ gutter: 20, column: 3}" :data-source="ebooks">

        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
              <span v-for="{ type, text } in actions" :key="type">
                <component v-bind:is="type" style="margin-right: 8px" />
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
</template>

<script lang="ts">
import {defineComponent, onMounted, reactive, ref} from 'vue';
import {LikeOutlined, MessageOutlined, StarOutlined} from '@ant-design/icons-vue';
import axios from 'axios';
import {message} from 'ant-design-vue';
import {Tool} from "@/util/tool";


export default defineComponent({
  name: 'Home',
  setup(){
    const level1 =  ref();


    let categorys: any;
    /**
     * Search all available category.
     **/
    const handleQueryCategory = () => {
      axios.get("/category/all").then((response) => {
        const data = response.data;
        if (data.success) {
          categorys = data.content;

          // After loading all categories, expand the menu to deepest level.
          //openKeys.value = [];
          //for (let i = 0; i < categorys.length; i++) {
          //  openKeys.value.push(categorys[i].id)
          //}

          level1.value = [];
          level1.value = Tool.array2TreeNew(categorys, 0);
        } else {
          message.error(data.message);
        }
      });
    };

    const handleClick = (value: any) => {
      console.log("menu click", value)
    }

    const pagination = {
      onChange: (page: number) => {
        console.log(page);
      },
      pageSize: 3,
    };

    const actions: Record<string, string>[] = [
      { type: 'StarOutlined', text: '156' },
      { type: 'LikeOutlined', text: '156' },
      { type: 'MessageOutlined', text: '2' },
    ];

    console.log("setup");
    const ebooks = ref();
    const ebooks1 = reactive({books:[]});

    onMounted( () =>{
      handleQueryCategory();
      console.log("OnMounted");
      axios.get("/ebook/list?name=").then(
          (response) => {
            const data = response.data;
            ebooks.value = data.content;
            ebooks1.books = data.content;
            console.log(response);

          }
      )
    })

    return {
      pagination,
      actions,
      ebooks,
      level1,
      handleClick
    };
  },
  components: {
    StarOutlined,
    LikeOutlined,
    MessageOutlined,

  },
});
</script>

<style scoped>
  .ant-avatar {
    width: 50px;
    height: 50px;
    line-height: 50px;
    border-radius: 8%;
    margin: 5px 0;
  }
</style>
