import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus';
import 'element-plus/lib/theme-chalk/index.css';
import './assets/theme/index.css'
import util from './assets/js/util'
import dict from './assets/js/dict'
import http from "./assets/js/request";

const app = createApp(App)
app.use(router)
app.use(ElementPlus, {size: 'mini'})
app.mount('#app')

app.prototype.$util = util;
app.prototype.$dict = dict;
app.prototype.$http = http;


