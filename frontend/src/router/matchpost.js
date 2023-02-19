import CreateMatchPostView from '../views/match/CreateMatchPostView.vue'

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
  }
]
