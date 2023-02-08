<template>
  <div v-if="isFilterOn">
    <v-row>
      <v-col cols="12" sm="4">
        <v-select
          v-model="filter.activity"
          :items="activity"
          label="활동"
        ></v-select>
      </v-col>
      <v-col cols="12" sm="4">
        <v-select
          v-model="filter.duration"
          :items="duration"
          label="여유시간"
        ></v-select>
      </v-col>
      <v-col cols="12" sm="4">
        <v-select
          v-model="filter.location"
          :items="location"
          label="지역"
        ></v-select>
      </v-col> </v-row
    ><v-btn @click="applyFilter">검색</v-btn>
    <v-btn @click="resetFilter">초기화</v-btn>
    <v-btn @click="toggleFilter">조건 검색 닫기</v-btn>
  </div>
  <v-btn v-if="!isFilterOn" @click="toggleFilter">조건 검색 열기</v-btn>

  <v-col v-for="(item, index) in matchPosts" :key="index" cols="12">
    <v-card :color="blue">
      <div class="d-flex flex-no-wrap justify-space-between">
        <div>
          <v-card-title
            class="text-h5"
            v-text="getTitleFromValue(activity, item.activity)"
          ></v-card-title>
          <v-card-subtitle
            v-text="getTitleFromValue(duration, item.duration)"
          ></v-card-subtitle>
          <v-card-subtitle
            v-text="getTitleFromValue(location, item.location)"
          ></v-card-subtitle>
          <v-card-subtitle v-text="item.startAt"></v-card-subtitle>
          <v-card-subtitle v-text="item.comment"></v-card-subtitle>
          <v-card-subtitle v-text="item.replyCount"></v-card-subtitle>
        </div>
      </div>
    </v-card>
  </v-col>
  <v-btn v-if="!isLast" @click="getNextPage">더 보기</v-btn>
</template>
<script>
import axios from 'axios'
export default {
  components: {},
  data() {
    return {
      isFilterOn: false,
      filter: { activity: '', duration: '', location: '' },
      pageCondition: { size: 5, page: 0 },
      isLast: false,
      matchPosts: [],
      activity: [
        { title: '대화', value: 'CHAT' },
        { title: '운동', value: 'WORKOUT' },
        { title: '영화', value: 'MOVIE' },
        { title: '콘서트', value: 'CONCERT' },
        { title: '전시회', value: 'EXHIBITION' },
        { title: '맛집', value: 'MEAL' },
        { title: '여행', value: 'TRAVEL' },
        { title: '기타', value: 'ETC' }
      ],
      duration: [
        { title: '1시간', value: 'HOUR_1' },
        { title: '2시간', value: 'HOUR_2' },
        { title: '3시간', value: 'HOUR_3' },
        { title: '기타', value: 'ETC' }
      ],
      location: [
        { title: '서울', value: 'SEOUL' },
        { title: '경기동부', value: 'EAST_GYEONGGI' },
        { title: '경기서부', value: 'WEST_GYEONGGI' },
        { title: '경기남부', value: 'SOUTH_GYEONGGI' },
        { title: '경기북부', value: 'NORTH_GYEONGGI' }
      ]
    }
  },
  setup() {},
  created() {},
  mounted() {
    axios
      .get(axios.defaults.baseURL + '/matchposts?size=5&page=0')
      .then((res) => {
        console.log(res)
        if (res.status === 200) {
          this.matchPosts = res.data.content
          this.isLast = res.data.last
        }
      })
  },
  unmounted() {},
  methods: {
    getPage() {
      axios
        .get(
          axios.defaults.baseURL +
            '/matchposts?' +
            'size=' +
            this.pageCondition.size +
            '&page=' +
            this.pageCondition.page +
            (this.filter.activity ? '&activity=' + this.filter.activity : '') +
            (this.filter.duration ? '&duration=' + this.filter.duration : '') +
            (this.filter.location ? '&location=' + this.filter.location : '')
        )
        .then((res) => {
          console.log(res)
          if (res.status === 200) {
            this.matchPosts.push(...res.data.content)
            this.isLast = res.data.last
          }
        })
    },
    getNextPage() {
      this.pageCondition.page++
      this.getPage()
    },
    applyFilter() {
      this.pageCondition.page = 0
      this.matchPosts = []
      this.getPage()
    },
    resetFilter() {
      this.filter = { activity: '', duration: '', location: '' }
      this.pageCondition.page = 0
      this.matchPosts = []
      this.getPage()
    },
    getTitleFromValue(arr, value) {
      for (const item of arr) {
        if (item.value === value) {
          return item.title
        }
      }
    },
    toggleFilter() {
      this.isFilterOn = !this.isFilterOn
    }
  }
}
</script>
