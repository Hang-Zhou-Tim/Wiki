<script lang="ts">
import {computed, defineComponent} from 'vue';
import store from "@/store";

export default defineComponent({
    name:'HeaderComponent',
    setup() {
      const user = computed(() => store.state.user);

      return {user};
    }
  });
</script>

<template>
  <a-layout-footer style="text-align: center">
    Hangs HandBook <span v-show="user.id">, welcome: {{user.name}}</span>
  </a-layout-footer>
</template>

<style scoped>

</style>