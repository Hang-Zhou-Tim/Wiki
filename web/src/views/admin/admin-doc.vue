<template>
  <a-layout>
      <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
      >
        <a-row :gutter="24">
          <a-col :span="8">
            <p>
              <a-form layout="inline" :model="param">
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
            <a-table
                v-if="level1.length > 0"
                :columns="columns"
                :row-key="record => record.id"
                :data-source="level1"
                :loading="loading"
                :pagination="false"
                size="small"
                :defaultExpandAllRows="true"
            >
              <template #name="{ text, record }">
                {{record.sort}} {{text}}
              </template>
              <template v-slot:action="{ text, record }">
                <a-space size="small">
                  <a-button type="primary" @click="edit(record)" size="small">
                    Edit
                  </a-button>
                  <a-popconfirm
                      title="Confirm Deletion?"
                      ok-text="Yes"
                      cancel-text="No"
                      @confirm="handleDelete(record.id)"
                  >
                    <a-button type="danger" size="small">
                      Delete
                    </a-button>
                  </a-popconfirm>
                </a-space>
              </template>
            </a-table>
          </a-col>
          <a-col :span="16">
            <p>
              <a-form layout="inline" :model="param">
                <a-form-item>
                  <a-button type="primary" @click="handleSave()">
                    Save
                  </a-button>
                </a-form-item>
              </a-form>
            </p>
            <a-form :model="doc" layout="vertical">
              <a-form-item>
                <a-input v-model:value="doc.name" placeholder="Name"/>
              </a-form-item>
              <a-form-item>
                <a-tree-select
                    v-model:value="doc.parent"
                    style="width: 100%"
                    :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                    :tree-data="treeSelectData"
                    placeholder="Select Parent Document"
                    tree-default-expand-all
                    :replaceFields="{title: 'name', key: 'id', value: 'id'}"
                >
                </a-tree-select>
              </a-form-item>
              <a-form-item>
                <a-input v-model:value="doc.sort" placeholder="顺序"/>
              </a-form-item>

              <a-form-item>
                <div id="content"></div>
              </a-form-item>
            </a-form>
          </a-col>
        </a-row>
      </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, reactive, ref} from 'vue';
import axios from 'axios';
import {message} from "ant-design-vue";
import {Tool} from '@/util/tool';
import {useRoute} from "vue-router";
import E from 'wangeditor';

export default defineComponent({
  name: 'AdminDoc',
  setup() {
    const route = useRoute();

    const level1 = ref();
    level1.value = [];

    const docs = ref();

    const loading = ref(false);
    //Define column rules here
    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
        slots: { customRender: 'name' }
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
    const doc = ref();
    doc.value = {
      ebookId: route.query.ebookId
    };
    const param = ref();
    param.value = {};
    const treeSelectData = ref();
    treeSelectData.value = [];
    const modalVisible = ref(false);
    const modalLoading = ref(false);


    const handleSave = () => {
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
     * This is suboptimal. Better way is to find the node with the id, and then recursively delete that sub-tree.
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

    const deleteIds: Array<string> = [];
    const deleteNames: Array<string> = [];
    /**
     * Find all ids to be deleted
     */
    const getDeleteIds = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          console.log("delete", node);
          deleteIds.push(id);
          deleteNames.push(node.name);
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              getDeleteIds(children, children[j].id)
            }
          }
        } else {
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDeleteIds(children, id);
          }
        }
      }
    };


    const handleDelete = (id: number) => {
      // console.log(level1, level1.value, id)

      deleteIds.length = 0;
      deleteNames.length = 0;
      getDeleteIds(level1.value, id);
      axios.delete("/doc/delete/" + deleteIds.join(",")).then((response) => {
        const data = response.data; // data = commonResp
        if (data.success) {
          // reload the list
          handleQuery();
        } else {
          message.error(data.message);
        }
      });
      /*
      Modal.confirm({
        title: 'Note!',
        icon: createVNode(ExclamationCircleOutlined),
        content: 'Are you sure to delete ' + deleteNames.join("，") + "?",
        onOk() {
          // console.log(ids)

        },
      });

       */
    };

    const edit = (record: any) => {
      modalVisible.value = true;
      doc.value = Tool.copy(record);

      treeSelectData.value = Tool.copy(level1.value);
      setDisable(treeSelectData.value, record.id);
      //add a new node at the very beginning of the select panel.
      treeSelectData.value.unshift({id:0,name:'Top-Level Document'});


    };

    const add = () => {
      modalVisible.value = true;
      doc.value = {
        ebookId: route.query.ebookid
      };
    };

    const queryForm = reactive({
      name : ""
    });



    onMounted(() => {
      const editor = new E('#content');
      editor.config.zIndex = 0;
      editor.create();
      handleQuery();

    });

    return {
      docs,
      columns,
      loading,
      edit,
      modalVisible,
      modalLoading,
      handleSave,
      param,
      handleDelete,
      doc,
      add,
      queryForm,
      handleQuery,
      level1,
      treeSelectData,
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
