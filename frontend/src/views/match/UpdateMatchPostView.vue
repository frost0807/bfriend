<template>
  <v-img
    src="https://frost0807.s3.ap-northeast-2.amazonaws.com/static/bfriend/logo.png"
    class="logo-img"
  ></v-img>
  <h2 align="left">매칭글 수정하기</h2>
  <form @submit.prevent="updateMatchPost">
    <v-select
      v-model="matchPostData.activity"
      :items="activityItems"
      label="활동"
      variant="outlined"
      density="compact"
    ></v-select>
    <v-select
      v-model="matchPostData.topic"
      :items="topicItems"
      label="주제"
      variant="outlined"
      density="compact"
    ></v-select>
    <v-select
      v-model="matchPostData.location"
      :items="locationItems"
      label="지역"
      variant="outlined"
      density="compact"
    ></v-select>
    <v-select
      v-model="matchPostData.ageDifference"
      :items="ageDifferenceItems"
      label="나이차 상관여부"
      variant="outlined"
      density="compact"
    ></v-select>
    <v-select
      v-model="matchPostData.budget"
      :items="budgetItems"
      label="예산"
      variant="outlined"
      density="compact"
    ></v-select>
    <Datepicker
      v-model="matchPostData.startAt"
      cancel-text="취소"
      select-text="선택"
      teleport-center
      :clearable="true"
      :is-24="false"
      class="start-time"
    ></Datepicker>
    <Datepicker
      v-model="matchPostData.endAt"
      cancel-text="취소"
      select-text="선택"
      teleport-center
      :clearable="true"
      :is-24="false"
      class="end-time"
    ></Datepicker>
    <v-textarea v-model="matchPostData.text" variant="outlined"></v-textarea>
    <v-btn
      type="submit"
      height="30px"
      width="100%"
      class="write-button"
      variant="outlined"
      >글 수정하기</v-btn
    >
  </form>
</template>
<script>
import '@vuepic/vue-datepicker/dist/main.css'
import axios from 'axios'

export default {
  components: {},
  data() {
    return {
      matchPostData: { startAt: new Date(), endAt: new Date() },
      activityItems: [
        { title: '만나서 까페에서 대화하고 싶어요', value: 'COFFEE' },
        { title: '만나서 좀 걸으면서 대화하고 싶어요', value: 'WALK' },
        { title: '만나서 식사하면서 대화하고 싶어요', value: 'MEAL' },
        { title: '만나서 한 잔 하면서 대화하고 싶어요', value: 'ALCOHOL' },
        { title: '만나서 대화하고 싶어요', value: 'ETC' }
      ],
      topicItems: [
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
      locationItems: [
        { title: '수도권동부', value: 'CAPITAL_EAST' },
        { title: '수도권서부', value: 'CAPITAL_WEST' },
        { title: '수도권남부', value: 'CAPITAL_SOUTH' },
        { title: '수도권북부', value: 'CAPITAL_NORTH' }
      ],
      ageDifferenceItems: [
        { title: '비슷했으면 좋겠어요', value: 'SIMILAR' },
        { title: '상관없어요', value: 'WHATEVER' }
      ],
      budgetItems: [
        { title: '5,000원 이하', value: 'UNDER_5000' },
        { title: '10,000원 이하', value: 'UNDER_10000' },
        { title: '20,000원 이하', value: 'UNDER_20000' },
        { title: '30,000원 이하', value: 'UNDER_30000' },
        { title: '50,000원 이하', value: 'UNDER_50000' },
        { title: '100,000원 이하', value: 'UNDER_100000' },
        { title: '100,000원 이상', value: 'OVER_100000' }
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
          this.matchPostData = res.data
          this.matchPostData.startAt = new Date(res.data.startAt)
          this.matchPostData.endAt = new Date(res.data.endAt)
          this.dataLoaded = true
        }
      })
  },
  unmounted() {},
  methods: {
    updateMatchPost() {
      this.matchPostData.startAt.setHours(
        this.matchPostData.startAt.getHours() + 9
      )
      this.matchPostData.endAt.setHours(this.matchPostData.endAt.getHours() + 9)
      const matchPostData = JSON.stringify(this.matchPostData, null, 2)
      console.log(matchPostData)
      axios
        .put(axios.defaults.baseURL + '/matchposts', matchPostData)
        .then((res) => {
          console.log(res)
          if (res.status === 200) {
            alert('글 수정이 완료되었습니다.')
            this.$router.replace({
              name: 'match-detail',
              query: { id: this.matchPostData.matchPostId }
            })
          }
        })
    }
  }
}
</script>
<style scoped>
.logo-img {
  width: 100px;
}
.description {
  margin: 15px 0 30px 0;
  text-align: left;
  font-size: 12px;
  font-weight: bold;
}
.write-button {
  border-radius: 15px;
  background-color: gainsboro;
}
.start-time {
  margin-bottom: 20px;
}
.end-time {
  margin-bottom: 20px;
}
</style>
