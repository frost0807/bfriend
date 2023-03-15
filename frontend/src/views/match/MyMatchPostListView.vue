<template>
  <div class="d-flex">
    <div class="logo-img">
      <v-img
        src="https://frost0807.s3.ap-northeast-2.amazonaws.com/static/bfriend/logo.png"
      ></v-img>
    </div>
    <h2 class="font-weight-bold">작성글 보기</h2>
  </div>
  <v-card v-for="(item, index) in matchPosts" :key="index">
    <v-row class="card-row" @click="moveToDetails(item.matchPostId)">
      <v-col cols="2" class="card-image">
        <v-avatar size="50" rounded="0">
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
            <div class="card-reply">
              <v-icon icon="mdi-message-processing-outline"></v-icon>
              <span v-text="item.replyCount"></span>
            </div>
          </div>
          <div class="d-flex flex-no-wrap justify-space-between">
            <p class="card-datetime">
              {{ item.startAt.split('T')[0] }} / {{ getTime(item.startAt) }} ~
              {{ getTime(item.endAt) }}
            </p>
          </div>
        </div>
      </v-col>
    </v-row>
  </v-card>
</template>
<script>
import axios from 'axios'
export default {
  components: {},
  data() {
    return {
      matchPosts: [],
      isLast: false,
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
      .get(axios.defaults.baseURL + '/matchposts/my?size=10&page=0')
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
    getTitleFromValue(arr, value) {
      for (const item of arr) {
        if (item.value === value) {
          return item.title
        }
      }
    },
    getTime(dateTime) {
      const time = dateTime.split('T')[1]
      const hour = time.split(':')[0]
      const min = time.split(':')[1]

      return hour + '시' + min + '분'
    },
    moveToDetails(matchPostId) {
      console.log(matchPostId)
      this.$router.push({
        name: 'match-detail',
        query: { id: matchPostId }
      })
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
.card-row {
  padding: 15px 0px;
}
.reset-button {
  border-radius: 50%;
  margin-left: 10px;
  width: 35px;
  height: 35px;
}
.select-box {
  margin: 15px 0 15px 0;
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
  font-size: 16px;
  font-weight: bold;
  color: orange;
}
.card-location-topic {
  font-size: 15px;
}
.card-datetime {
  margin: auto;
  font-size: 14px;
  margin-left: 7px;
}
.card-reply {
  font-size: 12px;
}
</style>
