<template>
  <el-container class="container-all">
    <el-aside style="background-color: #F5F5F5">
      <div class="aside-box">
        <div class="aside-box-name">{{user.userName}}</div>
        <div class="aside-box-collect">收藏歌曲{{collectNum}}首</div>
      </div>
    </el-aside>
    <el-main>

      <el-form style="margin-top: 30px" :model="form" :rules="rules" ref="formRef">

        <el-form-item prop="email">
          <el-input
              style="max-width: 600px"
              :placeholder="user.email"
              disabled
          >
            <template #prepend>
              <div class="info-box-pretext">邮箱</div>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="password_old">
          <el-input
              v-model="form.password_old"
              style="max-width: 600px"
              placeholder="请输入旧密码"
              clearable
              show-password
          >
            <template #prepend>
              <div class="info-box-pretext">旧密码</div>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="password_new">
          <el-input
              v-model="form.password_new"
              style="max-width: 500px"
              placeholder="请输入新密码"
              clearable
              show-password
          >
            <template #prepend>
              <div class="info-box-pretext">新密码</div>
            </template>
          </el-input>
          <el-button @click="changePassword" :type="'warning'" class="info-box-button" plain>确认修改</el-button>
        </el-form-item>

        <el-form-item>
          <el-select
              v-model="likedTags"
              value-key="id"
              placeholder=" "
              style="width: 500px"
              multiple
          >
            <template #prefix>
              <div class="info-box-pretext">喜好标签</div>
            </template>
            <el-option-group
                v-for="group in options"
                :key="group.label"
                :label="group.label"
            >
              <el-option
                  v-for="item in group.options"
                  :key="item.id"
                  :label="item.tagName"
                  :value="item.id"
              />
            </el-option-group>

          </el-select>
          <el-button @click="changeLikedTag" :type="'warning'" class="info-box-button" plain>确认修改</el-button>
        </el-form-item>

      </el-form>
    </el-main>
  </el-container>
</template>

<script setup>
import {ElMessage} from "element-plus";
import {get, post} from "@/net"
import {reactive, ref, onBeforeMount} from "vue";

const user = JSON.parse(sessionStorage.getItem('account'))
const collectNum = ref(0)
const likedTags = ref([])
const formRef = ref()
//喜好标签
const options = reactive([
  {
    label: 'Language',
    options: [],
  },
  {
    label: 'Style',
    options: [],
  },
])
const form = reactive({
  username: '',
  password_old: '',
  password_new: '',
})

const rules = {
  password_old:[
    { required: true, message: '请输入旧密码', trigger: 'blur'},
    { min: 6, max: 16, message: '长度需在6-16之间', trigger: ['blur', 'change'] }
  ],
  password_new:[
    { required: true, message: '请输入新密码', trigger: 'blur'},
    { min: 6, max: 16, message: '长度需在6-16之间', trigger: ['blur', 'change'] }
  ],
}

const changePassword = ()=>{
  formRef.value.validate((isValid)=>{
    if(isValid){
      post('/api/auth/internal-reset', {
        email: user.email,
        password_old: form.password_old,
        password_new: form.password_new
      }, (msg) => {
        ElMessage.success(msg)
      })
    } else ElMessage.warning('请完整填写表单内容')
  })
}

const changeLikedTag = ()=>{
 post('api/tag/update-all-likedTag', {
   tagList: likedTags.value
 }, (msg)=>{
   ElMessage.success(msg)
 })
}

onBeforeMount(()=>{
  let tag_category = 2
  //获取所有tag
  for(let i = 1; i <= tag_category; i++){
    get(`/api/tag/${i}`, (msg, data)=>{
      options[i-1].options = data
    })
  }

  get('/api/music/all-collect-number', (msg, data)=>{
    collectNum.value=data
  })

  get('/api/tag/all-likedTag-id', (msg, data)=>{
    likedTags.value=data
  })

})
</script>

<style scoped>
  .container-all{
    width: 100%;
    height: 600px;
    padding: 30px;
  }

  .aside-box{
    margin: 30% auto;
  }

  .aside-box-name{
    font-size: xxx-large;
    color: #424242;
    font-weight: bold;
    text-align: center;
  }

  .aside-box-collect{
    text-align: center;
    font-size: small;
    color: #9E9E9E;
    margin-top: 5px;
  }

  .info-box-pretext{
    text-align: center;
    width: 50px;
    color: #95989D;
  }

  .info-box-button{
    width: 90px;
    margin-left: 10px;
    font-weight: bold;
  }
</style>
