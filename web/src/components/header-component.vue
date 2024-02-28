<script setup lang="ts">

</script>

<template>
  <a-layout-header class="header">
    <a-row>
      <a-col :span="4">
        <div class="logo" >Hang's Wiki</div>
      </a-col>
      <a-col :span="16">
        <a-menu
            theme="dark"
            mode="horizontal"
            :style="{ lineHeight: '64px' }"
        >
          <a-menu-item key="/">
            <router-link to="/">Home</router-link>
          </a-menu-item>
          <a-menu-item key="/admin/user">
            <router-link to="/admin/user">User Admin</router-link>
          </a-menu-item>
          <a-menu-item key="/admin/ebook">
            <router-link to="/admin/ebook">Ebook Admin</router-link>
          </a-menu-item>
          <a-menu-item key="/admin/category">
            <router-link to="/admin/category">Category Admin</router-link>
          </a-menu-item>
          <a-menu-item key="/about">
            <router-link to="/about">About</router-link>
          </a-menu-item>
        </a-menu>
      </a-col>
      <a-col :span="4" v-show="!user.id">
        <a class="login-menu"  @click="showLoginModal">
          Login
        </a>
      </a-col>
      <a-col :span="2" v-show="user.id">
        <a class="login-menu" >
          <span> Welcome, {{user.name}}</span>
        </a>

      </a-col>
      <a-col :span="2" v-show="user.id">
        <a-popconfirm
            title="Confirm to Logout?"
            ok-text="Yes"
            cancel-text="No"
            @confirm="logout()"
        >
          <a class="login-menu">
            <span>Logout</span>
          </a>
        </a-popconfirm>
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

declare let hexMd5: any;
declare let KEY: any;

export default defineComponent({
  name: 'the-header',
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

.login-menu {
  float: right;
  color: white;
  padding-left: 10px;
}


</style>
