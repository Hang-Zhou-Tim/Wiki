<template>
  <a-layout>
      <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
      >
        <p>
          <a-form layout="inline" :model="queryForm">
            <a-form-item>
              <a-input v-model:value="queryForm.name" placeholder="Name">
              </a-input>
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="handleQuery({page: 1, size: pagination.pageSize})">
                Query
              </a-button>
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="add()">
                Add
              </a-button>
            </a-form-item>
          </a-form>
        </p>

        <!-- Init a new table. Columns rule based on columns, and data provided by ebooks.
          -->
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="ebooks"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >

        <!-- Replace slot in column Cover by img.     -->
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar" />
        </template>

        <template v-slot:category="{ text, record }">   <!-- if the dataIndex is not specified, text will be equal to record-->
          <span>{{ getCategoryName(record.category1Id) }} / {{ getCategoryName(record.category2Id) }}</span>
        </template>
        <!-- Replace slot in column Action by keyword Edit and Delete Button
             Text represents the value of corresponding data index. Record represent the whole object.
        -->
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <router-link :to="'/admin/doc?ebookId=' + record.id" >
              <a-button type="primary" >
                Manage This Ebook
              </a-button>
            </router-link>

            <a-button type="primary" @click = "edit(record)">
               Edit
            </a-button>
            <a-popconfirm
                title="This process can't be reverted. Are you sure to remove the id? "
                ok-text="Yes"
                cancel-text="No"
                @confirm="handleDelete(record.id)"
            >
              <a-button type="danger">
                Delete
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>

  <a-modal
    title="Ebook Form"
    v-model:visible="modalVisible"
    :confirm-loading="modalLoading"
    @ok="handleModalOk"
  >
    <a-form :model="ebook" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="Cover">
        <a-input v-model:value="ebook.cover" />
      </a-form-item>
      <a-form-item label="Name">
        <a-input v-model:value="ebook.name" />
      </a-form-item>
      <!-- Category determined by array of {Id1,Id2} and shown in label Name1/Name2 -->
      <a-form-item label="Category">
        <a-cascader
            v-model:value="categoryIds"
            :field-names="{ label: 'name', value: 'id', children: 'children' }"
            :options="level1"
        />
      </a-form-item>

      <a-form-item label="Description">
        <a-input v-model:value="ebook.description" type="textarea" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, reactive, ref} from 'vue';
import axios from 'axios';
import {message} from "ant-design-vue";
import {Tool} from '@/util/tool';

export default defineComponent({
  name: 'AdminEbook',
  setup() {
    const categoryIds = ref();
    const ebooks = ref();
    const pagination = ref({
      current: 1,
      pageSize: 4,
      total: 0
    });
    const loading = ref(false);
    //Define column rules here
    const columns = [
      {
        title: 'cover',
        dataIndex: 'cover',
        slots: { customRender: 'cover' }   //All data in this column except title will be rendered by slot rule above.
      },
      {
        title: 'name',
        dataIndex: 'name'
      },
      {
        title: 'category',
        slots: { customRender: 'category' }
      },
      {
        title: 'docCount',
        dataIndex: 'docCount'
      },
      {
        title: 'viewCount',
        dataIndex: 'viewCount'
      },
      {
        title: 'voteCount',
        dataIndex: 'voteCount'
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];

    /**
     * Query ebooks
     **/
    const handleQuery = (params: any) => {
      loading.value = true;
      // Clear the ebook set, otherwise ebook will not be changed in html due to it is ref() instead of reactive()
      ebooks.value = [];
      axios.get("/ebook/list", {
        params: {
          page: params.page,
          size: params.size,
          name: queryForm.name
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if(data.success) {
          ebooks.value = data.content.list;

          // Reset Pagination Button
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;
        }else{
          message.error(data.message);
        }
      });
    };

    /**
     * Click on Pagination
     */
    const handleTableChange = (pagination: any) => {
      console.log("Check Pagination: " + pagination);
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };

    //Ebook Save Form
    const ebook = ref({
      name : "",
      category1Id : 0,
      category2Id : 0,
      cover: "",
      description : ""

    });
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;
      ebook.value.category1Id = categoryIds.value[0];
      ebook.value.category2Id = categoryIds.value[1];

      axios.post("/ebook/save", ebook.value).then((response) => {
        modalLoading.value = false;
        const data = response.data; // data = commonResp
        if (data.success) {
          modalVisible.value = false;

          // Reload Table
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        } else {
          message.error(data.message);
        }
      });
    }

    const level1 =  ref();
    let categorys: any;
    const handleQueryCategory = () => {
      loading.value = true;
      axios.get("/category/all").then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          categorys = data.content;

          level1.value = [];
          level1.value = Tool.array2TreeNew(categorys, 0);

          // To synchronise the operations. The query to Ebooks should only be made after category details are fetched.
          handleQuery({
            page: 1,
            size: pagination.value.pageSize,
          });
        } else {
          message.error(data.message);
        }
      });
    };

    const getCategoryName = (cid: number) => {
      // console.log(cid)
      let result = "";
      categorys.forEach((item: any) => {
        if (item.id === cid) {
          // return item.name; // 注意，这里直接return不起作用
          result = item.name;
        }
      });
      return result;
    };


    const handleDelete = (id : number) => {
      modalLoading.value = true;

      axios.delete("/ebook/delete/" + id).then((response) => {
        modalLoading.value = false;
        const data = response.data; // data = commonResp
        if (data.success) {
          modalVisible.value = false;

          // Reload the Ebook list
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        } else {
          message.error(data.message);
        }
      });
    }

    const edit = (record: any) => {
      modalVisible.value = true;
      ebook.value = Tool.copy(record);
      categoryIds.value = [ebook.value.category1Id, ebook.value.category2Id];
    };

    const add = () => {
      modalVisible.value = true;
      ebook.value = {
                           name : "",
                           category1Id : 0,
                           category2Id : 0,
                           cover: "",
                           description : ""

      };
    };

    const queryForm = reactive({
      name : ""
    });

    onMounted(() => {

      handleQueryCategory();
    });

    return {
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange,
      edit,
      modalVisible,
      modalLoading,
      handleModalOk,
      handleDelete,
      ebook,
      add,
      queryForm,
      handleQuery,
      getCategoryName,
      categoryIds,
      level1,
    }
  }
});
</script>

<style scoped>
img {
  width: 50px;
  height: 50px;
}
</style>
