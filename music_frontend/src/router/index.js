import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [{
    path: '/',
    name: 'welcome',
    component: () => import('@/views/WelcomeView.vue'),
    children: [
      {
        path: '',
        name: 'welcome-login',
        component: () => import('@/views/welcome/LoginPage.vue')
      },
      {
        path: '/register',
        name: 'welcome-register',
        component: () => import('@/views/welcome/RegisterPage.vue')
      },{
        path: '/forget',
        name: 'welcome-forget',
        component: () => import('@/views/welcome/ForgetPage.vue')
      }
    ]
  },{
    path: '/index',
    name: "index",
    component: () => import('@/views/indexView.vue'),
    children:[
      {
        path: '',
        name: 'index-hot',
        component: () => import('@/views/index/hotPage.vue')
      },{
        path: '/recommend',
        name: 'index-recommend',
        component: () => import('@/views/index/recommendPage.vue')
      },{
        path: '/my_music',
        name: 'index-my_music',
        component: () => import('@/views/index/mymusicPage.vue')
      },{
        path: '/userInfo',
        name: 'index-userInfo',
        component: () => import('@/views/index/userInfoPage.vue')
      }
    ]
  }
  ]
})

router.beforeEach((to, from, next) => {

  if (to.matched.length === 0) {   //如果请求的页面不存在，则转index页面，这时候又会再次执行beforeEach
    next('/index')
  } else if (sessionStorage.getItem('account') != null && to.name.startsWith("welcome")) {
    next('/index')
  } else if (sessionStorage.getItem('account') == null && !to.name.startsWith("welcome")) {
    next('/')
  } else {
    next()
  }
})

export default router
