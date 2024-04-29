<script setup>
import {ElMessage} from "element-plus";
import router from "@/router";
import axios from "axios";

//因为路由守护需要sessionStorge里面有东西
//比如登陆后，重新起一个网页输入地址，该网页sessionStorge是空的，无法实现路由守护
axios.get('/api/user/me', {
  withCredentials: true
}).then(({data}) => {
  if(data.code === 200) {
    ElMessage.success(data.message)
    sessionStorage.setItem('account', JSON.stringify(data.data))
    router.push('/index')
  }
})

</script>

<template>

  <router-view/>

</template>

<style scoped>

</style>
