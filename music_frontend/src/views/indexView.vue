<template>
<!--  导航栏-->
  <el-menu
      :default-active="activeIndex"
      mode="horizontal"
      background-color="#545c64"
      text-color="#fff"
      active-text-color="#ffd04b"
      :ellipsis="false"
      class="header"
  >
    <el-menu-item index="1">
      <el-icon size=x-large><Headset /></el-icon>
    </el-menu-item>
    <el-menu-item class="large-font" index="2" @click="router.push('/');">首页</el-menu-item>
    <el-menu-item class="large-font" index="3" @click="router.push('/recommend');">推荐</el-menu-item>
    <el-menu-item class="large-font" index="4" @click="router.push('/my_music');">我的音乐</el-menu-item>

    <el-sub-menu index="5">
      <template #title>
        <div class="large-font">
          {{user.userName}}
        </div>
      </template>
      <el-menu-item index="5-1" @click="router.push('/userInfo')">个人中心</el-menu-item>
      <el-menu-item index="5-2" @click="logout">退出登录</el-menu-item>
    </el-sub-menu>

  </el-menu>

  <router-view></router-view>

  <div class="footer">
    <div class="footer-text">Music Recommend System-2024.4.30</div>
    <div class="footer-text">Powered by LinQi Li</div>
  </div>
</template>

<script setup>
import {ElMessage, ElMessageBox} from "element-plus";
import router from "@/router";
import {get} from "@/net"
import {ref, onMounted} from "vue";
import {Headset} from "@element-plus/icons-vue";

const user = JSON.parse(sessionStorage.getItem('account'))

const logout = () =>{
  get('/api/auth/logout', (message) =>{
    ElMessage.success(message)
    sessionStorage.removeItem('account')
    router.push('/')
  })
}
const activeIndex = ref('2')
onMounted(()=>{
  get('/api/tag/has-likedTag-id', (msg, flag)=>{
    if(flag === false){
      ElMessageBox.alert('还未添加偏好标签,点击确认后跳转到个人页面进行添加', 'Warning', {
        confirmButtonText: '确认',
        type: 'warning'
      }).then(() => {
        activeIndex.value = '5-1'
        router.push('/userInfo')
      })
    }
  })
})
</script>

<style scoped>
  .header{
    display: flex;
    justify-content: center;
  }
  .large-font{
    font-size: large;
  }
  .footer{
    display: flex;
    flex-direction: column;
    justify-content: flex-end;
    align-items: center;
    background-color: #333333;
    height: 200px;
    width: 100%;
  }
  .footer-text{
    margin-bottom: 10px;
    font-size: small;
    color: #BDBDBD;
  }
</style>
