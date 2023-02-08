import ForgotPasswordView from '../views/user/ForgotPasswordView.vue'
import UpdatePasswordView from '../views/user/UpdatePasswordView.vue'
import WithdrawalView from '../views/user/WithdrawalView.vue'

export default [
  {
    path: '/signup',
    name: 'signup',
    component: () =>
      import(/* webpackChunkName: "signup" */ '../views/user/SignUpView.vue')
  },
  {
    path: '/signup-success',
    name: 'signup-success',
    component: () =>
      import(
        /* webpackChunkName: "signup" */ '../views/user/SignUpSuccessView.vue'
      )
  },
  {
    path: '/login',
    name: 'login',
    component: () =>
      import(/* webpackChunkName: "login" */ '../views/user/LoginView.vue')
  },
  {
    path: '/mypage',
    name: 'mypage',
    component: () =>
      import(/* webpackChunkName: "mypage" */ '../views/user/MyPageView.vue')
  },
  {
    path: '/question-answer-create',
    name: 'question-answer-create',
    component: () =>
      import(
        /* webpackChunkName: "mypage" */ '../views/user/QuestionAnswerCreateView.vue'
      )
  },
  {
    path: '/question-answer-update',
    name: 'question-answer-update',
    component: () =>
      import(
        /* webpackChunkName: "mypage" */ '../views/user/QuestionAnswerUpdateView.vue'
      )
  },
  {
    path: '/forgot-password',
    name: 'forgot-password',
    component: ForgotPasswordView
  },
  {
    path: '/update-password',
    name: 'update-password',
    component: UpdatePasswordView
  },
  {
    path: '/withdrawal',
    name: 'withdrawal',
    component: WithdrawalView
  }
]
