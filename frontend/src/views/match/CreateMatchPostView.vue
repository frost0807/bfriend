<template>
  <v-img
    src="https://frost0807.s3.ap-northeast-2.amazonaws.com/static/bfriend/logo.png"
    class="logo-img"
  ></v-img>
  <h2 align="left">친구랑 대화하기</h2>
  <p class="description">좋은 친구를 만날 수 있도록 정성껏 작성해주세요</p>
  <form @submit.prevent="submitMatchPost">
    <v-select
      v-model="matchPostData.activity"
      :items="activityItems"
      placeholder="만나서 이런걸 하고싶어요"
      variant="outlined"
      density="compact"
    ></v-select>
    <v-select
      v-model="matchPostData.topic"
      :items="topicItems"
      placeholder="이런 주제에 대해서 얘기하고 싶어요"
      variant="outlined"
      density="compact"
    ></v-select>
    <v-select
      v-model="matchPostData.location"
      :items="locationItems"
      placeholder="여기서 만나고 싶어요"
      variant="outlined"
      density="compact"
    ></v-select>
    <v-select
      v-model="matchPostData.ageDifference"
      :items="ageDifferenceItems"
      placeholder="만날 분의 나이는 이랬으면 좋겠어요"
      variant="outlined"
      density="compact"
    ></v-select>
    <v-select
      v-model="matchPostData.budget"
      :items="budgetItems"
      placeholder="예산은 이 정도로 생각해요"
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
      placeholder="만나고 싶은 시간은 이때에요"
      class="start-time"
    ></Datepicker>
    <Datepicker
      v-model="matchPostData.endAt"
      cancel-text="취소"
      select-text="선택"
      teleport-center
      :clearable="true"
      :is-24="false"
      placeholder="헤어지고 싶은 시간은 이때에요"
      class="end-time"
    ></Datepicker>
    <v-textarea
      v-model="matchPostData.text"
      variant="outlined"
      placeholder="어떤 친구를 만나고 싶나요?(주관식)"
    ></v-textarea>
    <v-btn
      type="submit"
      height="30px"
      width="100%"
      class="write-button"
      variant="outlined"
      >글 작성하기</v-btn
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
      matchPostData: {},
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
  mounted() {},
  unmounted() {},
  methods: {
    submitMatchPost() {
      console.log(this.matchPostData)
      const matchPostData = JSON.stringify(this.matchPostData, null, 2)
      console.log(matchPostData)
      axios
        .post(axios.defaults.baseURL + '/matchposts', matchPostData)
        .then((res) => {
          console.log(res)
          if (res.status === 200) {
            alert('글 작성이 완료되었습니다.')
            const createdMatchPostId = res.data
            this.$router.replace({
              name: 'match-detail',
              query: { id: createdMatchPostId }
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
