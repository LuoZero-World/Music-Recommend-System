<template>
  <div style="text-align: center;">
    <div>
      <h2>登录</h2>
      <div style="font-size: 13px; color: gray">进入系统前请输入用户名和密码</div>
    </div>

    <div style="margin-top: 30px">
      <el-input v-model="form.username" :prefix-icon="User" placeholder="用户名邮箱"/>
      <el-input v-model="form.password" :prefix-icon="Lock" type="password" placeholder="密码" style="margin-top: 10px" show-password/>
    </div>

    <el-row>
      <el-col :span="12" style="text-align: left">
        <el-checkbox v-model="form.remember" label="记住我" size="large" />
      </el-col>
      <el-col :span="12" style="text-align: right;margin-top: 8px">
        <el-link @click="router.push('/forget')">忘记密码</el-link>
      </el-col>
    </el-row>

    <div style="margin-top: 40px">
      <el-button @click="login()" style="width: 270px; font-weight: bold" type="success" plain>立即登录</el-button>
    </div>
    <el-divider>
      <span style="font-size: 12px; color: gray">没有账号</span>
    </el-divider>
    <div style="margin-top: 20px">
      <el-button @click="router.push('/register')" style="width: 270px; font-weight: bold" type="warning" plain>点击注册</el-button>
    </div>
  </div>

</template>

<script setup>
import {Lock, User} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";
import {get, post} from "@/net"
import router from "@/router";
import {reactive} from "vue";


const form = reactive({
  username: '',
  password: '',
  remember: false
})

const login = () =>{
  if(!form.username || !form.password)
    ElMessage.warning('请填写用户名和密码')
  else{
    //发送登录请求
    post('/api/auth/login', {
      username: form.username,
      password: form.password,
      remember: form.remember
    }, (message) =>{
      ElMessage.success(message)
      get('/api/user/me', (msg, data)=>{
        ElMessage.success(msg)
        sessionStorage.setItem('account',JSON.stringify(data))
        router.push('/index')
      })
    })
  }
}
</script>

<style scoped>

</style>
