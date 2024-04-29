<template>

  <div>
    <div style="width: 400px; margin: -50px 0 40px -70px">
      <el-steps :active="active" finish-status="success" align-center>
        <el-step title="获取验证码" />
        <el-step title="更改密码" />
      </el-steps>
    </div>

    <div style="text-align: center;" v-if="active === 0">
      <div>
        <h2>重置密码</h2>
        <div style="font-size: 13px; color: gray">请输入需要重置密码的邮箱地址</div>
      </div>

      <el-form style="margin-top: 40px" :model="form" :rules="rules" @validate="onValidate" ref="formRef">

        <el-form-item prop="email">
          <el-input v-model="form.email" :prefix-icon="Message" type="" placeholder="电子邮件地址" />
        </el-form-item>
        <el-form-item prop="code">
          <el-row style="width: 100%" :gutter="5">
            <el-col :span="15">
              <el-input v-model="form.code" :maxlength="4" placeholder="电子邮件验证码"></el-input>
            </el-col>
            <el-col :span="9">
              <el-button @click="sendEmail" type="success" :disabled="!isEmailValid || coldTime > 0">
                {{coldTime > 0 ? "请稍后"+coldTime+"秒" : "点击发送"}}
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>

      <el-button @click="next" style="width: 260px; margin-top: 8px; font-weight: bold" type="danger" plain>下一步</el-button>

    </div>

    <transition name="el-fade-in-linear" mode="out-in">
      <div style="text-align: center;height: 100%" v-if="active === 1">
        <div>
          <h2>重置密码</h2>
          <div style="font-size: 13px; color: gray">请输入您的新密码，务必牢记，防止丢失</div>
        </div>

        <el-form style="margin-top: 40px" :model="form2" :rules="rules2" @validate="onValidate" ref="formRef2">
          <el-form-item prop="password">
            <el-input v-model="form2.password" :maxlength="16" :prefix-icon="Lock" type="password" placeholder="新密码" show-password/>
          </el-form-item>
          <el-form-item prop="password_repeat">
            <el-input v-model="form2.password_repeat" :maxlength="16" :prefix-icon="Lock" type="password" placeholder="重复新密码" show-password/>
          </el-form-item>
        </el-form>

        <el-button @click="doReset" style="width: 260px; margin-top: 8px; font-weight: bold" type="danger" plain>立即重置密码</el-button>

      </div>
    </transition>
  </div>


</template>

<script setup>
import {Lock, Message} from "@element-plus/icons-vue";
import {reactive, ref} from "vue";
import {post} from "@/net";
import {ElMessage} from "element-plus";
import router from "@/router";

const isEmailValid = ref(false)
const coldTime = ref(0)
const formRef = ref()
const formRef2 = ref()
const active = ref(0)

const form = reactive({
  email: '',
  code: ''
})
const form2 = reactive({
  password: '',
  password_repeat: ''
})

const validatePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== form2.password) {
    callback(new Error("两次密码不一致"))
  } else {
    callback()
  }
}

const rules ={
  email:[
    { required: true, message: '请输入邮箱地址', trigger: 'blur'},
    { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change']},
  ],
  code:[
    { required: true, message: '请输入验证码', trigger: 'blur'},
    { pattern: /^\d+$/, message: '格式错误，只能包含数字', trigger: 'change'}
  ]
}

const rules2 = {
  password:[
    { required: true, message: '请输入密码', trigger: 'blur'},
    { min: 6, max: 16, message: '长度需在6-16之间', trigger: ['blur', 'change'] }
  ],
  password_repeat:[
    {validator: validatePassword, trigger: ['blur', 'change']}
  ],
}

const onValidate = (prop, isValid) => {
  if(prop === 'email') isEmailValid.value = isValid
}

const sendEmail = ()=>{
  post('/api/auth/validate-reset-email', {
    email: form.email
  }, (msg) => {
    coldTime.value = 60
    setInterval(() => {
      coldTime.value--;
    }, 1000)
    ElMessage.success(msg)
  })
}

const next = ()=>{
  formRef.value.validate((isValid) =>{
    if(isValid){
      post('/api/auth/reset-first', {
        email: form.email,
        code: form.code
      }, (msg) =>{
        active.value=1
        ElMessage.success(msg)
      })
    }else{
      ElMessage.warning('请先完整填写地址和验证码');
    }
  })
}

const doReset = () =>{
  formRef2.value.validate((isValid) =>{
    if(isValid){
      post('/api/auth/reset-second', {
        password: form2.password
      }, (msg) =>{
        active.value=2
        ElMessage.success(msg)
        router.push('/')
      })
    }else{
      ElMessage.warning('请填写密码');
    }
  })
}

</script>

<style scoped>

</style>
