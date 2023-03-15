<template>
  <div class="d-flex">
    <div class="logo-img">
      <v-img
        src="https://frost0807.s3.ap-northeast-2.amazonaws.com/static/bfriend/logo.png"
      ></v-img>
    </div>
    <h2 class="font-weight-bold">작성 댓글 보기</h2>
  </div>
  <div class="select-box">
    <v-row>
      <v-col cols="6">
        <v-select
          v-model="filter.activity"
          :items="activity"
          label="활동"
          density="compact"
          variant="outlined"
          @change="applyFilter()"
        ></v-select
      ></v-col>
      <v-col cols="6">
        <v-select
          v-model="filter.location"
          :items="location"
          label="지역"
          density="compact"
          variant="outlined"
          @input="applyFilter"
        ></v-select
      ></v-col>
      <v-col cols="8">
        <Datepicker
          v-model="filter.dateRange"
          cancel-text="취소"
          select-text="선택"
          teleport-center
          :enable-time-picker="false"
          placeholder="게시물 작성일"
          range
        ></Datepicker
      ></v-col>
      <v-btn @click="applyFilter" class="search-button">검색</v-btn>
      <v-btn
        @click="resetFilter"
        width="35px"
        height="35px"
        icon="mdi-cached"
        class="reset-button"
        ><v-icon></v-icon
      ></v-btn>
    </v-row>
  </div>
  <div v-for="(item, index) in replyAndMatchPostData" :key="index">
    <v-card class="reply-card">
      <v-row class="reply-row" @click="moveToDetails(item.matchPostId)">
        <v-col cols="2" class="profile-image">
          <v-avatar size="40" rounded="xl">
            <v-img
              src="https://frost0807.s3.ap-northeast-2.amazonaws.com/static/bfriend/default_profile.png"
            ></v-img> </v-avatar
        ></v-col>
        <v-col>
          <div class="d-flex flex-column">
            <div class="d-flex flex-no-wrap comment">
              <v-icon icon="mdi-chat-processing-outline"></v-icon>
              <span v-text="item.comment"></span>
            </div>
            <p v-text="getTime(item.createdAt)" class="reply-created-time"></p>
          </div>
        </v-col>
      </v-row>
    </v-card>
    <v-card class="matchpost-card">
      <v-row class="matchpost-row" @click="moveToDetails(item.matchPostId)">
        <v-col cols="2" class="card-image">
          <v-avatar size="40" rounded="0">
            <v-img
              :src="getTitleFromValue(cardImage, item.activity)"
            ></v-img> </v-avatar
        ></v-col>
        <v-col>
          <div class="d-flex flex-column">
            <div class="d-flex flex-no-wrap justify-space-between">
              <p
                v-text="getTitleFromValue(activity, item.activity)"
                class="card-activity"
              ></p>
              <p
                v-text="
                  getTitleFromValue(location, item.location) +
                  '/' +
                  getTitleFromValue(topic, item.topic)
                "
                class="card-location-topic"
              ></p>
              <p
                v-text="
                  getTitleFromValue(dayOfTheWeek, item.dayOfTheWeek) +
                  (item.daysLeft >= 0
                    ? '/D -' + item.daysLeft
                    : '/D +' + -item.daysLeft)
                "
                class="card-day-dday"
              ></p>
            </div>
            <div class="d-flex flex-no-wrap justify-space-between">
              <p v-text="item.timeFromTo" class="card-fromto"></p>
              <div class="card-reply">
                <v-icon icon="mdi-chat-processing-outline"></v-icon>
                <span v-text="item.replyCount"></span>
              </div>
            </div>
          </div>
        </v-col>
      </v-row>
    </v-card>
  </div>
  <v-btn v-if="!isLast" @click="getNextPage">더 보기</v-btn>
</template>
<script>
import axios from 'axios'

export default {
  components: {},
  data() {
    return {
      isLast: false,
      filter: { activity: '', location: '', dateRange: [] },
      replyAndMatchPostData: [],
      pageCondition: { size: 10, page: 0 },
      activity: [
        { title: '커피', value: 'COFFEE' },
        { title: '산책', value: 'WALK' },
        { title: '식사', value: 'MEAL' },
        { title: '술', value: 'ALCOHOL' },
        { title: '기타', value: 'ETC' }
      ],
      topic: [
        { title: '일상', value: 'COMMON' },
        { title: '여행', value: 'TRAVEL' },
        { title: '스포츠', value: 'SPORT' },
        { title: '영화', value: 'MOVIE' },
        { title: '책', value: 'BOOK' },
        { title: '음악', value: 'MUSIC' },
        { title: '사업', value: 'BUSINESS' },
        { title: '연애', value: 'LOVE' },
        { title: '기타', value: 'ETC' }
      ],
      location: [
        { title: '수도권동부', value: 'CAPITAL_EAST' },
        { title: '수도권서부', value: 'CAPITAL_WEST' },
        { title: '수도권남부', value: 'CAPITAL_SOUTH' },
        { title: '수도권북부', value: 'CAPITAL_NORTH' }
      ],
      dayOfTheWeek: [
        { title: '월', value: 'MONDAY' },
        { title: '화', value: 'TUESDAY' },
        { title: '수', value: 'WEDNESDAY' },
        { title: '목', value: 'THURSDAY' },
        { title: '금', value: 'FRIDAY' },
        { title: '토', value: 'SATURDAY' },
        { title: '일', value: 'SUNDAY' }
      ],
      cardImage: [
        {
          title:
            'https://frost0807.s3.ap-northeast-2.amazonaws.com/static/bfriend/match_card_coffee.png',
          value: 'COFFEE'
        },
        {
          title:
            'https://frost0807.s3.ap-northeast-2.amazonaws.com/static/bfriend/match_card_walk.png',
          value: 'WALK'
        },
        {
          title:
            'https://frost0807.s3.ap-northeast-2.amazonaws.com/static/bfriend/match_card_meal.jpg',
          value: 'MEAL'
        },
        {
          title:
            'https://frost0807.s3.ap-northeast-2.amazonaws.com/static/bfriend/match_card_alcohol.jpg',
          value: 'ALCOHOL'
        },
        {
          title:
            'https://frost0807.s3.ap-northeast-2.amazonaws.com/static/bfriend/match_card_etc.jpg',
          value: 'ETC'
        }
      ]
    }
  },
  setup() {},
  created() {},
  mounted() {
    axios
      .get(axios.defaults.baseURL + '/matchposts/replies/my?size=10&page=0')
      .then((res) => {
        console.log(res)
        if (res.status === 200) {
          this.replyAndMatchPostData = res.data.content
        }
      })
  },
  unmounted() {},
  methods: {
    getPage() {
      let fromDate = null
      let toDate = null
      if (this.filter.dateRange[0]) {
        this.filter.dateRange[0].setHours(
          this.filter.dateRange[0].getHours() + 9
        )
        fromDate = this.filter.dateRange[0].toISOString().split('T')[0]
      }
      if (this.filter.dateRange[0]) {
        this.filter.dateRange[1].setHours(
          this.filter.dateRange[1].getHours() + 9
        )
        toDate = this.filter.dateRange[1].toISOString().split('T')[0]
      }
      axios
        .get(
          axios.defaults.baseURL +
            '/matchposts/replies/my?' +
            'size=' +
            this.pageCondition.size +
            '&page=' +
            this.pageCondition.page +
            (this.filter.activity ? '&activity=' + this.filter.activity : '') +
            (this.filter.location ? '&location=' + this.filter.location : '') +
            (fromDate ? '&fromDate=' + fromDate : '') +
            (toDate ? '&toDate=' + toDate : '')
        )
        .then((res) => {
          console.log(res)
          if (res.status === 200) {
            this.replyAndMatchPostData.push(...res.data.content)
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
      this.pageCondition.size = 10
      this.replyAndMatchPostData = []
      this.getPage()
    },
    resetFilter() {
      this.filter = {
        activity: '',
        location: '',
        dateRange: []
      }
      this.pageCondition.page = 0
      this.pageCondition.size = 10
      this.replyAndMatchPostData = []
      this.getPage()
    },
    getTitleFromValue(arr, value) {
      for (const item of arr) {
        if (item.value === value) {
          return item.title
        }
      }
    },
    moveToDetails(matchPostId) {
      console.log(matchPostId)
      this.$router.push({
        name: 'match-detail',
        query: { id: matchPostId }
      })
    },
    getTime(dateTime) {
      return (
        dateTime.split('T')[0] + ' ' + dateTime.split('T')[1].substring(0, 5)
      )
    }
  }
}
</script>
<style scoped>
h2 {
  margin: 25px;
}
.v-col {
  padding: 0 12px 0 12px;
}
.logo-img {
  width: 100px;
}
.search-button {
}
.reset-button {
  border-radius: 50%;
  margin-left: 10px;
}
.select-box {
  margin: 15px 0 30px 0;
}
.v-label {
  font-size: 10px;
}
.v-card {
  padding: 8px;
  margin: 10px 0;
}
.card-activity {
  margin-left: 5px;
  font-size: 15px;
  font-weight: bold;
  color: orange;
}
.card-location-topic {
  font-weight: bold;
  font-size: 14px;
}
.card-day-dday {
  padding-top: 2px;
  font-size: 11px;
  font-weight: bold;
  color: #5a4ab3;
}
.card-fromto {
  font-size: 11px;
  margin-left: 7px;
}
.card-reply {
  font-size: 12px;
}
.comment {
  font-size: 11px;
}
.reply-created-time {
  font-size: 11px;
  text-align: left;
  margin: 10px 0 0 0;
}
.reply-card {
  background-color: #fcf5e6;
  border-radius: 12px;
  margin: 20px 20px 0 0;
}
.reply-row {
  padding: 10px 0px;
}
.matchpost-card {
  border-radius: 12px;
  margin-left: 20px;
}
.matchpost-row {
  padding: 10px 0px;
}
</style>
