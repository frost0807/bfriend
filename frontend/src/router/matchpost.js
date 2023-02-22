import CreateMatchPostView from '../views/match/CreateMatchPostView.vue'
import UpdateMatchPostView from '../views/match/UpdateMatchPostView.vue'

export default [
  {
    path: '/match/list',
    name: 'match-list',
    component: () =>
      import(
        /* webpackChunkName: "match" */ '../views/match/MatchPostListView.vue'
      )
  },
  {
    path: '/match/detail',
    name: 'match-detail',
    component: () =>
      import(
        /* webpackChunkName: "match" */ '../views/match/MatchPostDetailView.vue'
      )
  },
  {
    path: '/match/create',
    name: 'match-create',
    component: CreateMatchPostView
  },
  {
    path: '/match/update',
    name: 'match-update',
    component: UpdateMatchPostView,
    props: true
  }
]
