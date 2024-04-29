<template>

  <div style="width:100vw;height:100vh;overflow:hidden;display:flex; position: relative">
    <div style="flex:1;">
      <el-image style="width: 100%; height: 100%; overflow: auto" fit="cover"
           src="http://img.netbian.com/file/2023/0912/small161308tSznV1694506388.jpg"/>
    </div>
    <div class="welcome-title">
      <h2>Welcome, My Friend</h2>
      <div>This is My Music Recommend System!</div>
    </div>
    <div style="padding: 100px 20px; box-sizing: border-box; z-index: 1;width:300px;">
      <router-view v-slot="{Component}">
        <transition name="el-fade-in-linear" mode="out-in">
          <component :is="Component"></component>
        </transition>
      </router-view>
    </div>

  </div>
</template>

<script setup>

</script>

<style scoped>
  .welcome-title{
    position: absolute;
    bottom: 20px;
    left: 20px;
    color: white;
    text-shadow: 0 0 10px black;
  }
</style>
