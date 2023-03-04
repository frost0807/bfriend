<template>
  <v-img
    class="logo-img"
    src="https://frost0807.s3.ap-northeast-2.amazonaws.com/static/bfriend/logo.png"
  ></v-img>
  <div class="d-flex flex-no-wrap justify-space-between">
    <div class="d-flex flex-no-wrap justify-start">
      <div>
        <v-img
          class="profile-img"
          src="https://frost0807.s3.ap-northeast-2.amazonaws.com/static/bfriend/default_profile.png"
        ></v-img>
      </div>
      <div class="user-info">
        <p class="username">
          {{ matchPost.username }} ({{ matchPost.age }}세/{{
            getTitleFromValue(sex, matchPost.sex)
          }})
        </p>
        <p class="time-after-create">
          {{ timeAfterCreateString(matchPost.minutesAfterCreate) }}
        </p>
      </div>
    </div>
    <div v-if="matchPost.matchPostOfMine">
      <v-btn
        @click="handleUpdate"
        rounded="pill"
        variant="outlined"
        density="comfortable"
        class="update-button"
        >수정</v-btn
      >
      <v-dialog v-model="deleteCheck" persistent width="auto">
        <template v-slot:activator="{ props }">
          <v-btn
            v-bind="props"
            rounded="pill"
            variant="outlined"
            density="comfortable"
            class="delete-button"
            >삭제</v-btn
          ></template
        ><v-card>
          <v-card-text>정말로 삭제하시겠습니까?</v-card-text>

          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn @click="handleDelete">네</v-btn>
            <v-btn @click="deleteCheck = false">아니요</v-btn>
          </v-card-actions></v-card
        >
      </v-dialog>
    </div>
  </div>
  <v-card density="compact" class="text">
    {{ getTitleFromValue(activity, matchPost.activity) }}
  </v-card>
  <v-card density="compact" class="text"
    >만나고 싶은 곳은 [{{ getTitleFromValue(location, matchPost.location) }}]
    에요</v-card
  >
  <v-card density="compact" class="text">
    [{{ getTitleFromValue(topic, matchPost.topic) }}] 이야기를 하고
    싶어요</v-card
  >
  <v-card density="compact" class="text">
    [{{ calculateTimeString(matchPost.startAt, matchPost.endAt) }}]에 대화하고
    싶어요</v-card
  >
  <v-card density="compact" class="text">
    <p>
      만날 분의 나이는 [{{
        getTitleFromValue(ageDifference, matchPost.ageDifference)
      }}]
    </p></v-card
  >
  <v-card density="compact" class="text"
    >제가 생각하는 예산대는
    {{ getTitleFromValue(budget, matchPost.budget) }}에요</v-card
  >
  <v-card density="compact" class="text">{{ matchPost.text }}</v-card>
  <ReplyList
    v-if="dataLoaded"
    :key="componentKey"
    :matchPostId="this.$route.query.id"
    :isMatchPostOfMine="matchPost.matchPostOfMine"
  />
</template>
<script>
import axios from 'axios'
import ReplyList from '../../components/ReplyList.vue'

export default {
  components: {
    ReplyList
  },
  data() {
    return {
      componentKey: 0,
      deleteCheck: false,
      dataLoaded: false,
      matchPost: {},
      replies: [],
      activity: [
        { title: '만나서 까페에서 대화하고 싶어요', value: 'COFFEE' },
        { title: '만나서 좀 걸으면서 대화하고 싶어요', value: 'WALK' },
        { title: '만나서 식사하면서 대화하고 싶어요', value: 'MEAL' },
        { title: '만나서 한 잔 하면서 대화하고 싶어요', value: 'ALCOHOL' },
        { title: '만나서 대화하고 싶어요', value: 'ETC' }
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
      ageDifference: [
        { title: '비슷했으면 좋겠어요', value: 'SIMILAR' },
        { title: '상관없어요', value: 'WHATEVER' }
      ],
      budget: [
        { title: '5,000원 이하', value: 'UNDER_5000' },
        { title: '10,000원 이하', value: 'UNDER_10000' },
        { title: '20,000원 이하', value: 'UNDER_20000' },
        { title: '30,000원 이하', value: 'UNDER_30000' },
        { title: '50,000원 이하', value: 'UNDER_50000' },
        { title: '100,000원 이하', value: 'UNDER_100000' },
        { title: '100,000원 이상', value: 'OVER_100000' }
      ],
      sex: [
        { title: '남자', value: 'MALE' },
        { title: '여자', value: 'FEMALE' }
      ]
    }
  },
  setup() {},
  created() {},
  mounted() {
    axios
      .get(axios.defaults.baseURL + '/matchposts/' + this.$route.query.id)
      .then((res) => {
        console.log(res)
        if (res.status === 200) {
          this.matchPost = res.data
          this.dataLoaded = true
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
    timeAfterCreateString(minutesAfterCreate) {
      if (minutesAfterCreate >= 60) {
        return Math.floor(minutesAfterCreate / 60) + '시간전 작성'
      }
      if (minutesAfterCreate >= 60 * 24) {
        return Math.floor(minutesAfterCreate / (24 * 60)) + '일전 작성'
      }
      return Math.floor(minutesAfterCreate) + '분전 작성'
    },
    calculateTimeString(startAt, endAt) {
      const yearMonthDay = startAt?.split('T')[0]
      const timeFrom = startAt?.split('T')[1].split(':')[0]
      const timeTo = endAt?.split('T')[1].split(':')[0]
      return yearMonthDay + ' ' + timeFrom + '시~' + timeTo + '시'
    },
    handleUpdate() {
      this.$router.push({
        name: 'match-update',
        query: { id: this.matchPost.matchPostId }
      })
    },
    handleDelete() {
      axios
        .delete(
          axios.defaults.baseURL + '/matchposts/' + this.matchPost.matchPostId
        )
        .then((res) => {
          if (res.status === 200) {
            this.deleteCheck = false
            alert('삭제되었습니다.')
            this.$router.replace({ name: 'match-list' })
          }
        })
    }
  }
}
</script>
<style scoped>
.text {
  font-size: 13px;
  font-weight: bold;
  text-align: left;
  border: solid 1px gainsboro;
  background-color: rgb(243, 243, 243);
  padding: 5px;
  margin: 10px 0;
}
.logo-img {
  width: 100px;
}
.profile-img {
  width: 40px;
  border-radius: 50%;
}
.update-button {
  margin-right: 5px;
  width: 50px;
}
.delete-button {
  width: 50px;
}
.user-info {
  margin-left: 10px;
  text-align: left;
}
.username {
  font-size: 13px;
  font-weight: bold;
}
.time-after-create {
  font-size: 13px;
  font-weight: bold;
  color: gray;
}
</style>
