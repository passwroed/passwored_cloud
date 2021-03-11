import { createRouter,createWebHistory} from 'vue-router'

const routes = [{
  path: '',
  redirect: '/login'
}, {
  path: '/login',
  component: () => import('../views/login.vue')
}, {
  path: '/manage',
  component: () => import('../views/manage.vue')
}]

const router = createRouter({
  mode: 'history',
  base: '/eparty/',
  history: createWebHistory(),
  routes
})

export default router
