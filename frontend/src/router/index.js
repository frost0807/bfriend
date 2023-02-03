import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  {
    path: '/signup',
    name: 'signup',
    component: () =>
      import(/* webpackChunkName: "signup" */ '../views/SignUpView.vue')
  },
  {
    path: '/signup-success',
    name: 'signup-success',
    component: () =>
      import(/* webpackChunkName: "signup" */ '../views/SignUpSuccessView.vue')
  },
  {
    path: '/login',
    name: 'login',
    component: () =>
      import(/* webpackChunkName: "login" */ '../views/LoginView.vue')
  },
  {
    path: '/mypage',
    name: 'mypage',
    component: () =>
      import(/* webpackChunkName: "mypage" */ '../views/MyPageView.vue')
  },
  {
    path: '/question-answer-create',
    name: 'question-answer-create',
    component: () =>
      import(
        /* webpackChunkName: "mypage" */ '../views/QuestionAnswerCreateView.vue'
      )
  },
  {
    path: '/question-answer-update',
    name: 'question-answer-update',
    component: () =>
      import(
        /* webpackChunkName: "mypage" */ '../views/QuestionAnswerUpdateView.vue'
      )
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
