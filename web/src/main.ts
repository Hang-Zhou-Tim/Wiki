import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css'
import * as Icons from '@ant-design/icons-vue';
import axios from "axios";
import {Tool} from "@/util/tool";

axios.defaults.baseURL = process.env.VUE_APP_SERVER;
axios.interceptors.request.use(function (config) {
    console.log('Requested ', config);

    const token = store.state.user.token;
    if(Tool.isNotEmpty(token)){
        config.headers.token = token;
    }

    return config;
}, error => {
    return Promise.reject(error);
});
axios.interceptors.response.use(function (response) {
    console.log('Result: ', response);

    return response;
}, error => {
    console.log('Error:', error);

    return Promise.reject(error);
});


const app = createApp(App);
app.use(store).use(router).use(Antd).mount('#app');

const icons:any = Icons;
for (const i in icons){
    app.component(i,icons[i]);  //load every icon components globally so that it can be used everywhere.
}

console.log('environment:', process.env.NODE_ENV);
console.log('server:', process.env.VUE_APP_SERVER);

