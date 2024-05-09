<template>

  <div>
    <el-image :src="bacImg" class="header-img"></el-image>
  </div>
  <div style="padding: 20px 40px">
    <div style="padding: 10px">
      <el-button color="#31C27C" style="color: #FAFAFA">播放全部</el-button>
      <el-button>批量操作</el-button>
    </div>
    <audio ref="audio" @ended="onAudioEnded"></audio>
    <el-table :data="musicList" style="width: 100%">
      <el-table-column type="index" width="50" />
      <el-table-column prop="musicName" label="音乐名称"></el-table-column>
      <el-table-column prop="author" label="歌手"></el-table-column>
      <el-table-column prop="duration" label="时长"></el-table-column>
      <el-table-column>
        <template #default="{row}">
          <el-icon size="24" color="#616161" class="icon">
            <VideoPause v-if="PlayingMap.get(row.musicName)" @click="stop()"/>
            <VideoPlay v-else @click="play(row.musicName, row.id)"/>
          </el-icon>
          <el-icon size="24" @click="remove(row)" class="icon"><Delete /></el-icon>
        </template>
      </el-table-column>
    </el-table>
  </div>

</template>

<script setup>
import {ref, reactive, onBeforeMount} from "vue";
import {get, post, postFile} from "@/net";
import {ElMessage} from "element-plus";
import {VideoPlay, VideoPause, Delete} from "@element-plus/icons-vue";
import {useBlobStore, useBlobStore2} from "@/stores";

const user = JSON.parse(sessionStorage.getItem('account'))
const musicList = reactive([])
const audio = ref()
let PlayingName = ""
let PlayingMap = ref(new Map())
let bacImg = ref()

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
const remove = (row)=> {
  PlayingMap.value.set(row.musicName, false)
  audio.value.pause()
  let index = musicList.indexOf(row);
  if (index !== -1) {
    musicList.splice(index, 1);
  }

  post('/api/music/disCollectIt', {
    userId: user.id,
    musicId: row.id
  }, (msg)=>{
    ElMessage.success(msg)
  })
}

async function getImage(name){
  let url = useBlobStore2().getBlobURL(name);
  if(url === undefined){
    await postFile('/api/media/image', {
      imageName: name
    }, (response) =>{
      url = useBlobStore2().createAndSetBlobURL(name, response.data)
    })
  }
  return url
}

onBeforeMount(()=>{
  get('/api/music/all-collect-detail', (msg, data)=>{
    Object.assign(musicList, data)
    for(const music in musicList){
      PlayingMap.value.set(music.musicName, false)
    }
  })

  bacImg.value = "https://cdn.jsdelivr.net/gh/LuoZero-World/DrawingBed@main/img/mymusic-bacimg.jpg";
})
</script>

<style scoped>
  .header-img{
    width: 100%;
    height: 380px;
  }

  .icon{
    margin: 2px 10px 0 0;
  }
</style>
