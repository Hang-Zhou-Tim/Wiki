<script lang="ts">
import {computed, defineComponent, onMounted} from 'vue';
import store from "@/store";
import {Tool} from "@/util/tool";
import {notification} from "ant-design-vue";

export default defineComponent({
    name:'HeaderComponent',
    setup() {
      const user = computed(() => store.state.user);

      let websocket: any;
      let token: any;
      const onOpen = () => {
        console.log('WebSocket Connection Succeed with State: {}', websocket.readyState)
      };
      const onMessage = (event: any) => {
        console.log('WebSocket Got Message：', event.data);
        notification['info']({
          message: 'Receive Message',
          description: event.data,
        });
      };
      const onError = () => {
        console.log('WebSocket Connection Failed, State: ', websocket.readyState)
      };
      const onClose = () => {
        console.log('WebSocket Connection Failed, State：', websocket.readyState)
      };
      const initWebSocket = () => {
        // Connection Succeed
        websocket.onopen = onOpen;
        // Receive Message
        websocket.onmessage = onMessage;
        // Connection Failed
        websocket.onerror = onError;
        // Close Websocket
        websocket.onclose = onClose;
      };

      onMounted(() => {
        // WebSocket
        if ('WebSocket' in window) {
          token = Tool.uuid(10);
          // Connection Address：ws://127.0.0.1:8880/ws/xxx
          websocket = new WebSocket(process.env.VUE_APP_WS_SERVER + '/ws/' + token);
          initWebSocket()

          // Close
          // websocket.close();
        } else {
          alert('The Navigator does not support websocket connection.')
        }
      });

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