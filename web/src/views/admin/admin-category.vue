<template>
  <a-layout>
      <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
      >
        <p>
          <a-form layout="inline" :model="queryForm">
            <a-form-item>
              <a-button type="primary" @click="handleQuery()">
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

        <!-- Init a new table. Columns rule based on columns, and data provided by categorys.
          -->
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="categorys"
          :loading="loading"
          :pagination="false"
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
    title="Category Table"
    v-model:visible="modalVisible"
    :confirm-loading="modalLoading"
    @ok="handleModalOk"
  >
    <a-form :model="category" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">

      <a-form-item label="Name">
        <a-input v-model:value="category.name" />
      </a-form-item>

      <a-form-item label="Parent Category">
        <a-input v-model:value="category.parent" />
      </a-form-item>

      <a-form-item label="Sort">
        <a-input v-model:value="category.sort" />
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
  name: 'AdminCategory',
  setup() {

    const categorys = ref();

    const loading = ref(false);
    //Define column rules here
    const columns = [

      {
        title: 'name',
        dataIndex: 'name'
      },
      {
        title: 'parent category',
        key: 'parent',
        dataIndex: 'parent'
      },
      {
        title: 'Sort',
        dataIndex: 'Sort'
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];

    /**
     * Query categories
     **/
    const handleQuery = () => {
      loading.value = true;
      axios.get("/category/all").then((response) => {
        loading.value = false;
        const data = response.data;
        if(data.success) {
          categorys.value = data.content;


        }else{
          message.error(data.message);
        }
      });
    };



    //Category Save Form
    const category = ref({
      name : "",
      parent : 200,
      sort : 100

    });
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;

      axios.post("/category/save", category.value).then((response) => {
        modalLoading.value = false;
        const data = response.data; // data = commonResp
        if (data.success) {
          modalVisible.value = false;

          // Reload Table
          handleQuery();
        } else {
          message.error(data.message);
        }
      });
    }


    const handleDelete = (id : number) => {
      modalLoading.value = true;

      axios.delete("/category/delete/" + id).then((response) => {
        modalLoading.value = false;
        const data = response.data; // data = commonResp
        if (data.success) {
          modalVisible.value = false;

          // Reload the Category list
          handleQuery();
        } else {
          message.error(data.message);
        }
      });
    }

    const edit = (record: any) => {
      modalVisible.value = true;
      category.value = Tool.copy(record);
    };

    const add = () => {
      modalVisible.value = true;
      category.value = {
                           name : "",
                           parent: 0,
                           sort : 0
      };
    };

    const queryForm = reactive({
      name : ""
    });



    onMounted(() => {
      handleQuery();
    });

    return {
      categorys,
      columns,
      loading,
      edit,
      modalVisible,
      modalLoading,
      handleModalOk,
      handleDelete,
      category,
      add,
      queryForm,
      handleQuery,
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
