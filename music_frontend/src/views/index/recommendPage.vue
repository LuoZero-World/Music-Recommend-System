<template>
  <el-container style="background-color: whitesmoke">
    <el-header style="height: 100px; line-height: 100px; text-align: center; font-size: xx-large">
      <h3>猜你喜欢</h3>
    </el-header>
    <el-container>
      <audio ref="audio" @ended="onAudioEnded"></audio>
      <el-main style="display: flex; flex-direction: column; align-items: center">
        <el-row v-for="music in musics" :key="music.id" class="hot-main-item" :gutter="25">
          <el-col :span="6">
            <el-image fit="contain" class="common" :src="decodeURIComponent(`/src/resources/image/${music.musicName}.jpg`)"></el-image>
          </el-col>
          <el-col :span="10" class="hot-item-col2">
            <div class="hot-item-col2-name">{{music.musicName}}</div>
            <div class="hot-item-col2-author">{{music.author}}</div>
          </el-col>
          <el-col :span="4" style="padding:9% 0 0 40px">
            <el-icon size="30" color="#616161">
              <VideoPause v-if="PlayingMap.get(music.musicName)" @click="stop()"/>
              <VideoPlay v-else @click="play(music.musicName, music.id)"/>
            </el-icon>
          </el-col>
          <el-col :span="4" style="padding-top: 9%">
            <el-icon size="30" color="#616161">
              <StarFilled @click="disCollect(music.id)" v-if="collectSet.has(music.id)"/>
              <Star @click="collect(music.id)" v-else/>
            </el-icon>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import {ref, reactive, onBeforeMount, computed} from "vue";
import {get, post, postFile} from "@/net";
import {VideoPlay, VideoPause,Star, StarFilled} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";
import {useBlobStore} from "@/stores";

let collectSet = ref(new Set())
let PlayingMap = ref(new Map())
let PlayingName = ""
const audio = ref()
const user = JSON.parse(sessionStorage.getItem('account'))

const musics = reactive([])

const onAudioEnded = () => {
  PlayingMap.value.set(PlayingName, false)
}
const play = async (name, id)=>{
  let url = useBlobStore().getBlobURL(name)

  if(url === undefined){
    await postFile('/api/media/music', {
      musicName: name
    }, (response)=>{
      // 创建一个Blob URL
      url = useBlobStore().createAndSetBlobURL(name, response.data)
    })
  }
  //去除上一首的状态,设置下一首状态
  PlayingMap.value.set(PlayingName, false)
  PlayingName = name
  PlayingMap.value.set(name, true)
  //播放
  audio.value.src = url;
  audio.value.play();
  //播放数+1
  get(`/api/music/playIt/${id}`, (msg)=>{})
}
const stop = ()=>{
  PlayingMap.value.set(PlayingName, false)
  audio.value.pause()
}

const collect = (id) =>{
  collectSet.value.add(id)
  post('/api/music/collectIt', {
    userId: user.id,
    musicId: id
  }, (msg)=>{
    ElMessage.success(msg)
  })
}
const disCollect = (id) =>{
  collectSet.value.delete(id)
  post('/api/music/disCollectIt', {
    userId: user.id,
    musicId: id
  }, (msg)=>{
    ElMessage.success(msg)
  })
}

onBeforeMount(()=>{
  //获取歌曲
  get('/api/music/recommend5', (msg, data)=>{
    Object.assign(musics, data)
    for(const music in musics){
      PlayingMap.value.set(music.musicName, false)
    }
  })
})
</script>

<style scoped>
.common{
  width: 100%;
  height: 100%;
}

.hot-main-item{
  margin-top: 20px;
  width: 600px;
}

.hot-item-col2{
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.hot-item-col2-name{
  font-size: large;
  margin-bottom: 5px;
  width: 100%;
  overflow:hidden;
  white-space: nowrap;
  text-overflow:ellipsis;
}
.hot-item-col2-author{
  color: #757575;
  width: 100%;   /*一定要设置宽度，或者元素内含的百分比*/
  overflow:hidden; /*溢出的部分隐藏*/
  white-space: nowrap; /*文本不换行*/
  text-overflow:ellipsis;  /*省略号*/
}

</style>

