import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import user from './user'
import matchpost from './matchpost'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  ...user,
  ...matchpost
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
