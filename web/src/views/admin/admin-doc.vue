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

        <!-- Init a new table. Columns rule based on columns, and data provided by docs.
          -->
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="level1"
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
    title="Doc Form"
    v-model:visible="modalVisible"
    :confirm-loading="modalLoading"
    @ok="handleModalOk"
  >
    <a-form :model="doc" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">

      <a-form-item label="Name">
        <a-input v-model:value="doc.name" />
      </a-form-item>

      <a-form-item label="Parent Doc">
        <a-tree-select
            v-model:value="doc.parent"
            style="width: 100%"
            :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
            :tree-data="treeSelectData"
            placeholder="Please select parent document"
            tree-default-expand-all
            :replaceFields="{title: 'name', key: 'id', value: 'id'}"
        >
        </a-tree-select>
      </a-form-item>

      <a-form-item label="Sort">
        <a-input v-model:value="doc.sort" />
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
  name: 'AdminDoc',
  setup() {
    const level1 = ref();

    const docs = ref();

    const loading = ref(false);
    //Define column rules here
    const columns = [

      {
        title: 'name',
        dataIndex: 'name'
      },
      {
        title: 'parent doc',
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
      level1.value = [];
      axios.get("/doc/all").then((response) => {
        loading.value = false;
        const data = response.data;
        if(data.success) {
          docs.value = data.content;
          console.log("Original Array: ", docs.value);

          level1.value = [];
          level1.value = Tool.array2TreeNew(docs.value, 0);
          console.log("Tree Structure", level1);
        }else{
          message.error(data.message);
        }
      });
    };



    //Doc Save Form
    const treeSelectData = ref();
    treeSelectData.value = [];
    const doc = ref({
      id:0,
      name : "",
      parent : 200,
      sort : 100

    });
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;

      axios.post("/doc/save", doc.value).then((response) => {
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

    /**
     * Disabled the targeted node and all corresponding children node.
     */
    const setDisable = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // Find target id on the tree, set it and its children to disabled recursively.
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          console.log("disabled", node);
          node.disabled = true;

          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisable(children, children[j].id)
            }
          }
        } else {
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisable(children, id);
          }
        }
      }
    };


    const handleDelete = (id : number) => {
      modalLoading.value = true;

      axios.delete("/doc/delete/" + id).then((response) => {
        modalLoading.value = false;
        const data = response.data; // data = commonResp
        if (data.success) {
          modalVisible.value = false;

          // Reload the Doc list
          handleQuery();
        } else {
          message.error(data.message);
        }
      });
    }

    const edit = (record: any) => {
      modalVisible.value = true;
      doc.value = Tool.copy(record);

      treeSelectData.value = Tool.copy(level1.value);
      setDisable(treeSelectData.value, record.id);
      //add a new node at the very beginning of the select panel.
      treeSelectData.value.unshift({id:0,name:'Top-Level Document'})
    };

    const add = () => {
      modalVisible.value = true;
      doc.value = {
        id:0,
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
      docs,
      columns,
      loading,
      edit,
      modalVisible,
      modalLoading,
      handleModalOk,
      handleDelete,
      doc,
      add,
      queryForm,
      handleQuery,
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
