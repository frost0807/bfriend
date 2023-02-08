export default [
  {
    path: '/match/list',
    name: 'match-list',
    component: () =>
      import(
        /* webpackChunkName: "match" */ '../views/match/MatchPostListView.vue'
      )
  }
]
