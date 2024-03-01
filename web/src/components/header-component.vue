<script setup lang="ts">

</script>

<template>
  <a-layout-header class="header">
    <a-row>
      <a-col :span="4">
        <div class="logo"><router-link to="/about">Hang's Wiki</router-link></div>
      </a-col>
      <a-col :span="14">
        <a-menu
            theme="dark"
            mode="horizontal"
            :style="{ lineHeight: '64px' }"
        >
          <a-menu-item key="/">
            <router-link to="/">Home</router-link>
          </a-menu-item>
          <a-menu-item key="/admin/user" :style="user.id ? {} : {display:'none'}">
            <router-link to="/admin/user">User Admin</router-link>
          </a-menu-item>
          <a-menu-item key="/admin/ebook" :style="user.id ? {} : {display:'none'}">
            <router-link to="/admin/ebook">Ebook Admin</router-link>
          </a-menu-item>
          <a-menu-item key="/admin/category" :style="user.id ? {} : {display:'none'}">
            <router-link to="/admin/category">Category Admin</router-link>
          </a-menu-item>
        </a-menu>
      </a-col>
      <a-col :span="6" v-show="!user.id">
        <a class="login-menu"  @click="showLoginModal">
          Login
        </a>
      </a-col>
      <a-col :span="6" v-show="user.id">
        <a-dropdown>
          <a class="logout">
            Welcome, {{ user.name }}
            <DownOutlined/>
          </a>
          <template #overlay>
            <a-menu>
              <a-menu-item key="logout">
                <a-popconfirm
                    title="Are you sure to logout? "
                    ok-text="Yes"
                    cancel-text="No"
                    @confirm="logout"
                >
                  <span>Logout</span>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </a-col>
    </a-row>

    <a-modal
        title="Login"
        v-model:visible="loginModalVisible"
        :confirm-loading="loginModalLoading"
        @ok="login"
    >
      <a-form :model="loginUser" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="Username">
          <a-input v-model:value="loginUser.loginName" />
        </a-form-item>
        <a-form-item label="Password">
          <a-input v-model:value="loginUser.password" type="password" />
        </a-form-item>
      </a-form>
    </a-modal>
  </a-layout-header>
</template>

<script lang="ts">
import { defineComponent, ref, computed } from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue';
import store from "@/store";
import { DownOutlined } from '@ant-design/icons-vue';

declare let hexMd5: any;
declare let KEY: any;

export default defineComponent({
  name: 'the-header',
  components: {
    DownOutlined
  },
  setup () {
    // Save after login
    const user = computed(() => store.state.user);

    // User to login
    const loginUser = ref({
      loginName: "test",
      password: "test"
    });
    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);
    const showLoginModal = () => {
      loginModalVisible.value = true;
    };

    // Login
    const login = () => {
      console.log("Start Login");
      loginModalLoading.value = true;
      loginUser.value.password = hexMd5(loginUser.value.password + KEY);
      axios.post('/user/login', loginUser.value).then((response) => {
        loginModalLoading.value = false;
        const data = response.data;
        if (data.success) {
          loginModalVisible.value = false;
          message.success("Login Successfully！");
          store.commit("setUser", data.content);
        } else {
          message.error(data.message);
        }
      });
    };

    // Logout
    const logout = () => {
      console.log("Start Logout");
      axios.get('/user/logout/' + user.value.token).then((response) => {
        const data = response.data;
        if (data.success) {
          message.success("User Logout Successfully.");
          store.commit("setUser", {});
        } else {
          message.error(data.message);
        }
      });
    };

    return {
      loginModalVisible,
      loginModalLoading,
      showLoginModal,
      loginUser,
      login,
      user,
      logout
    }
  }
});
</script>

<style>
.logo {
  width: 120px;
  height: 31px;
  /*background: rgba(255, 255, 255, 0.2);*/
  /*margin: 16px 28px 16px 0;*/
  float: left;
  color: white;
  font-size: 18px;
}

.logout {
  float: right;
  color: white;
  font-size: 14px;
}

.login-menu {
  float: right;
  color: white;
  padding-left: 10px;
}


</style>
