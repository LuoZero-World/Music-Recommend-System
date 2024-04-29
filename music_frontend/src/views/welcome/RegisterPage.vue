<template>
  <div style="text-align: center;">
    <div>
      <h2>注册</h2>
      <div style="font-size: 13px; color: gray">欢迎注册,请在下方填写对应信息</div>
    </div>

    <el-form style="margin-top: 30px" :model="form" :rules="rules" @validate="onValidate" ref="formRef">
      <el-form-item prop="username">
        <el-input v-model="form.username" :maxlength="8" :prefix-icon="User" placeholder="用户名"/>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="form.password" :maxlength="16" :prefix-icon="Lock" type="password" placeholder="密码" show-password/>
      </el-form-item>
      <el-form-item prop="password_repeat">
        <el-input v-model="form.password_repeat" :maxlength="16" :prefix-icon="Lock" type="password" placeholder="重复密码" show-password/>
      </el-form-item>
      <el-form-item prop="email">
        <el-input v-model="form.email" :prefix-icon="Message" type="" placeholder="电子邮件地址" />
      </el-form-item>
      <el-form-item prop="code">
        <el-row style="width: 100%" :gutter="5">
          <el-col :span="15">
            <el-input v-model="form.code" :maxlength="6" placeholder="电子邮件验证码"></el-input>
          </el-col>
          <el-col :span="9">
            <el-button @click="sendEmail" type="success" :disabled="!isEmailValid || coldTime > 0">
              {{coldTime > 0 ? "请稍后"+coldTime+"秒" : "点击发送"}}
            </el-button>
          </el-col>
        </el-row>
      </el-form-item>
    </el-form>

    <el-button @click="register" style="margin-top: 10px; width: 270px; font-weight: bold" type="warning" plain>
      立即注册
    </el-button>
    <div style="font-size: 13px; margin-top: 5px">
      <span style="color: gray">已有账号?</span>
      <el-link type="primary" style="translate: 0 -2px" @click="router.push('/')">立即登录</el-link>
    </div>
  </div>

</template>

<script setup>
import {Lock, Message, User} from "@element-plus/icons-vue";
import router from "@/router";
import {reactive, ref} from "vue";
import {ElMessage} from "element-plus";
import {post} from '@/net'

const form = reactive({
  username: '',
  password: '',
  password_repeat: '',
  email: '',
  code: ''
})

const validateUsername = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入用户名'))
  } else if (!/^[a-zA-Z0-9-_]+$/.test(value)) {    //正则
      callback(new Error('格式错误'))
    }
  else callback()
}

const validateRepeatUsername = (rule, value, callback) => {
  post('/api/auth/valid-repeat-username', {
    username: value
  }, () => {
    callback()
  }, (msg) => {
    callback(new Error(msg))
  }, (msg) => {
    callback(new Error(msg))
  })

}

const validatePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== form.password) {
    callback(new Error("两次密码不一致"))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { validator: validateUsername, trigger: ['blur', 'change'] },
    { validator: validateRepeatUsername, trigger: 'blur'},
    { min: 6, max: 8, message: '长度需在6-8之间', trigger: ['blur', 'change'] }
  ],
  password:[
    { required: true, message: '请输入密码', trigger: 'blur'},
    { min: 6, max: 16, message: '长度需在6-16之间', trigger: ['blur', 'change'] }
  ],
  password_repeat:[
    {validator: validatePassword, trigger: ['blur', 'change']}
  ],
  email:[
    { required: true, message: '请输入邮箱地址', trigger: 'blur'},
    { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change']},
  ],
  code:[
    { required: true, message: '请输入验证码', trigger: 'blur'},
    { pattern: /^\d+$/, message: '格式错误，只能包含数字', trigger: 'change'}
  ]
}

const isEmailValid = ref(false)
const formRef = ref()
const coldTime = ref(0)

//实现功能：当邮箱号正确时，可以点击"获取验证码"
const onValidate = (prop, isValid) => {
  if(prop === 'email') isEmailValid.value = isValid
}

//点击注册时，先校验各表单项是否遵循规则，而后再发送请求
const register = () =>{
  formRef.value.validate((isValid)=>{
    if(isValid){
      post('/api/auth/register', {
        username: form.username,
        password: form.password,
        email: form.email,
        code: form.code
      }, (msg) => {
        ElMessage.success(msg)
        router.push("/")
      })
    } else ElMessage.warning('请完整填写表单内容')
  })
}

const sendEmail = () =>{
  coldTime.value = 60
  post('/api/auth/valid-email', {
    email: form.email
  }, (msg) => {
    setInterval(() => {
      coldTime.value--;
    }, 1000)
    ElMessage.success(msg)
  }, (msg) =>{
    ElMessage.success(msg);
    coldTime.value = 0
  })
}
</script>

<style scoped>

</style>
