import {createApp} from 'vue'
import {createPinia} from 'pinia'
import 'element-plus/dist/index.css' //css不能自动导入
import App from './App.vue'
import router from './router'
import axios from "axios";

const app = createApp(App)

axios.defaults.baseURL = 'http://127.0.0.1:8080'

app.use(createPinia())
app.use(router)

app.mount('#app')
