import { createRouter, createWebHistory } from 'vue-router'
import LandingView from '../views/LandingView.vue'
import user from './user'
import matchpost from './matchpost'

const routes = [
  {
    path: '/',
    name: 'home',
    component: LandingView
  },
  ...user,
  ...matchpost
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
