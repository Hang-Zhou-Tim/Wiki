<template>
  <a-layout>
      <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
      >
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
        <!-- Replace slot in column Action by keyword Edit and Delete Button
             Text represents the value of corresponding data index. Record represent the whole object.
        -->
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary" @click = "edit(record)">
              Edit
            </a-button>
            <a-button type="danger">
              Delete
            </a-button>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>

  <a-modal
    title="Ebook Table"
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
      <a-form-item label="Category1">
        <a-input v-model:value="ebook.category1Id" />
      </a-form-item>
      <a-form-item label="Category2">
        <a-input v-model:value="ebook.category2Id" />
      </a-form-item>

      <a-form-item label="Description">
        <a-input v-model:value="ebook.description" type="textarea" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import {message} from "ant-design-vue";

export default defineComponent({
  name: 'AdminEbook',
  setup() {

    const ebooks = ref();
    const pagination = ref({
      current: 1,
      pageSize: 10,
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
        title: 'category1',
        dataIndex: 'category1Id'
      },
      {
        title: 'category2',
        dataIndex: 'category2Id'
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
      axios.get("/ebook/list", {
        params: {
          page: params.page,
          size: params.size
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        ebooks.value = data.content.list;

        // Reset Pagination Button
        pagination.value.current = params.page;
        pagination.value.total = data.content.total;
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

    //Table form
    const ebook = ref({
      name : "default",
      category1Id : 1,
      category2Id : 2,
      cover: "",
      description : ""

    });
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;

      axios.post("/ebook/save", ebook.value).then((response) => {
        modalLoading.value = false;
        const data = response.data; // data = commonResp
        if (data.success) {
          modalVisible.value = false;

          // 重新加载列表
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
      ebook.value = record;
    };

    onMounted(() => {
      handleQuery({
        page:pagination.value.current,
        size:pagination.value.pageSize
      });
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
      ebook
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
