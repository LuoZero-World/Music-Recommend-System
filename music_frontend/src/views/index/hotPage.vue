<template>
<!--  走马灯-->
  <el-carousel :interval="5000" trigger="click" height="auto" motion-blur>
    <el-carousel-item v-for="url in urls" :key="url" style="height: 350px;">
      <el-image :src="url" fit="fill" class="common"></el-image>
    </el-carousel-item>
  </el-carousel>


<!--  热门推荐模块-->
  <el-container class="hot-all">
<!--    正栏-->
    <el-container style="min-height: 510px">
      <el-header height="100px">
        <div class="hot-header"> 热门推荐</div>
      </el-header>
      <el-container>
        <audio ref="audio" @ended="onAudioEnded"></audio>
        <el-main style="display: flex; flex-direction: column; justify-content: space-between">
          <div class="hot-main">
            <el-row v-for="music in filterMusics" :key="music.id" class="hot-main-item" :gutter="15">
              <el-col :span="6">
                <el-image fit="contain" class="common" :src="music.musicURL"></el-image>
              </el-col>
              <el-col :span="10" class="hot-item-col2">
                <div class="hot-item-col2-name">{{music.musicName}}</div>
                <div class="hot-item-col2-author">{{music.author}}</div>
              </el-col>
              <el-col :span="4" style="padding:25px 0 0 40px">
                <el-icon size="24" color="#616161">
                  <VideoPause v-if="PlayingMap.get(music.musicName)" @click="stop()"/>
                  <VideoPlay v-else @click="play(music.musicName, music.id)"/>
                </el-icon>
              </el-col>
              <el-col :span="4" style="padding-top: 25px">
                <el-icon size="24" color="#616161">
                  <StarFilled @click="disCollect(music.id)" v-if="collectSet.has(music.id)"/>
                  <Star @click="collect(music.id)" v-else/>
                </el-icon>
              </el-col>
            </el-row>
          </div>

          <div class="hot-pagination-box">
            <el-pagination
                background
                layout="prev, pager, next"
                :total="musics.length"
                :page-size="state.limit"
                @current-change="handleCurrentChange"
                @size-change="handleSizeChange"
            />
          </div>
        </el-main>
      </el-container>
    </el-container>
<!--    侧边栏-->
    <el-aside width="350px">
      <div class="hot-aside">
        <div class="hot-aside-header">标签选择</div>
        <!--      语言-->
        <div class="hot-aside-tags">
          <el-tag v-for="tag in tags_language" :key="tag.id"
                  class="hot-aside-tag" size="large" @click="getHotMusics(tag.id)">{{tag.tagName}}
          </el-tag>
        </div>
        <!--      风格-->
        <div class="hot-aside-tags">
          <el-tag v-for="tag in tags_style" :key="tag.id"
                  class="hot-aside-tag" size="large" @click="getHotMusics(tag.id)">{{tag.tagName}}</el-tag>
        </div>
      </div>
    </el-aside>
  </el-container>

</template>

<script setup>
import {ref, reactive, onBeforeMount, computed} from "vue";
import {get, post, postFile} from "@/net";
import {VideoPlay, VideoPause,Star, StarFilled} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";
import {useBlobStore, useBlobStore2} from "@/stores";

let collectSet = ref(new Set())
let PlayingMap = ref(new Map())
let PlayingName = ""
const drawingBed = "https://cdn.jsdelivr.net/gh/LuoZero-World/DrawingBed@main/img/";
const audio = ref()
const user = JSON.parse(sessionStorage.getItem('account'))
const urls_name = ['a1', 'a2', 'a3']
const urls = reactive([])

const musics = reactive([])
const tags_language = reactive([])
const tags_style = reactive([])

const state = reactive({
  page: 1,
  limit: 6,
});

//获取走马灯图片
for(let i = 0; i < 3; i++){
  urls.push(drawingBed+urls_name[i]+".jpg");
  // postFile('/api/media/image', {
  //   imageName: urls_name[i]
  // }, (response) =>{
  //   const url = useBlobStore2().createAndSetBlobURL(urls_name[i], response.data)
  //   urls.push(url)
  // })
}

//获取歌曲图片
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

//改变页码
const handleCurrentChange = (e) => {
  state.page = e;
};
//改变页数限制
const handleSizeChange = (e) => {
  state.limit = e;
}
//对数据做过滤
const filterMusics = computed(()=>{
  let start = (state.page-1)*state.limit
  return musics.slice(start, start+state.limit)
})

//根据标签ID获取相应热歌
const getHotMusics = (tagId)=>{
   get(`api/music/hot/${tagId}`, (msg, data)=>{
    musics.length = 0
    for(const d of data){
      d.musicURL = drawingBed+d.musicName+".jpg"
      //await getImage(d.musicName).then(url => d.musicURL=url)
    }
    Object.assign(musics, data)
  })
}

const onAudioEnded = () => {
  PlayingMap.value.set(PlayingName, false)
}
const play = async (name, id)=>{
  let url = useBlobStore().getBlobURL(name)

  if(url === undefined){
    ElMessage.success("音乐加载较慢，请稍候")
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
  get('/api/music/hot18',  (msg, data)=>{
    for(const d of data){
      d.musicURL = drawingBed+d.musicName+".jpg"
      //await getImage(d.musicName).then(url => d.musicURL=url)
    }
    Object.assign(musics, data)
    for(const music in musics){
      PlayingMap.value.set(music.musicName, false)
    }
  })
  //获取用户收藏信息
  get('/api/music/all-collect-id', (msg, data)=>{
    collectSet.value = new Set(data);
  })
  //获取标签内容
  get('/api/tag/1', (msg, data)=>{
    Object.assign(tags_language, data)
  })
  get('/api/tag/2', (msg, data)=>{
    Object.assign(tags_style, data)
  })
})
</script>

<style scoped>
 .common{
   width: 100%;
   height: 100%;
 }

 .hot-all{
   padding: 0 50px;
   background-color: #FAFAFA;
 }

 .hot-header{
   box-sizing: border-box;
   font-size: x-large;
   padding: 80px 0 0 5px;
 }

 .hot-main{
   display: flex;
   flex-wrap: wrap;
 }

 .hot-main-item{
   margin-top: 20px;
   width: 50%;
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

 .hot-pagination-box{
   margin: 0 auto;
 }

 .hot-aside{
   margin-top: 20%;
   padding: 20px;
   border-radius: 4px;
   border: solid 1px;
   height: 300px;
 }

 .hot-aside-header{
   text-align: center;
   font-size: large;
   font-weight: bold;
   color: #6699CC;
 }

 .hot-aside-tags{
   margin-top: 20px;
   display: flex;
   flex-wrap: wrap;
   justify-content: left;
   gap: 8px 16px;
   cursor: pointer;
 }

 .hot-aside-tag{
   font-size: large;
   color: #37474F;
 }
 .hot-aside-tag:hover{
    background-color: #79bbff;
   color: ghostwhite;
 }
</style>

