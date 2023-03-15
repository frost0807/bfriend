// 로그인 권한 필요

<template>
  <v-img
    class="logo-img"
    src="https://frost0807.s3.ap-northeast-2.amazonaws.com/static/bfriend/logo.png"
  ></v-img>
  <div class="d-flex">
    <div class="img-box">
      <v-img
        class="profile-img"
        src="https://frost0807.s3.ap-northeast-2.amazonaws.com/static/bfriend/default_profile.png"
      ></v-img>
    </div>
    <div class="top">
      <div class="d-flex flex-no-wrap justify-space-between">
        <v-btn variant="text" height="30px" class="view"
          >방문수 {{ myInformation.view }}</v-btn
        >
        <v-btn
          to="/question-answer-update"
          variant="text"
          height="30px"
          class="question-answer-update-button"
          >자기소개 수정하기</v-btn
        >
      </div>
      <div class="information">
        <p>{{ myInformation.name }}</p>
        <p>
          {{ myInformation.birthDay }} /
          {{ getTitleFromValue(sex, myInformation.sex) }}
        </p>
        <div class="d-flex flex-no-wrap star">
          <v-img
            v-for="star in starCount"
            :key="star"
            src="https://frost0807.s3.ap-northeast-2.amazonaws.com/static/bfriend/star.png"
          ></v-img
          ><v-img
            v-for="emptystar in 5 - starCount"
            :key="emptystar"
            src="https://frost0807.s3.ap-northeast-2.amazonaws.com/static/bfriend/empytstar.png"
          ></v-img>
        </div>
      </div>
    </div>
  </div>
  <div v-if="!spreadQuestionAnswer">
    <div v-for="(item, index) in myInformation.questionAnswers" :key="index">
      <div v-if="index < 4">
        <v-card density="compact" class="question-text">{{
          item.question
        }}</v-card>
        <v-card density="compact" class="answer-text">{{ item.answer }}</v-card>
      </div>
    </div>
  </div>
  <div v-if="spreadQuestionAnswer">
    <div v-for="(item, index) in myInformation.questionAnswers" :key="index">
      <v-card density="compact" class="question-text">{{
        item.question
      }}</v-card>
      <v-card density="compact" class="answer-text">{{ item.answer }}</v-card>
    </div>
  </div>
  <v-btn
    v-if="!spreadQuestionAnswer"
    @click="handleSpreadQuestionAnswer"
    variant="text"
    height="30px"
    class="spread-button"
    >더보기</v-btn
  >
  <div class="d-flex flex-no-wrap justify-content-center">
    <div @click="moveToMyMatchposts" width="27%" class="middle-button">
      <v-icon
        icon="mdi-pencil-box-multiple-outline"
        class="middle-button-icon"
        size="40px"
      ></v-icon>
      <p>작성글 보기</p>
    </div>
    <div @click="moveToMyReplies" width="27%" class="middle-button">
      <v-icon
        icon="mdi-email-open-outline"
        class="middle-button-icon"
        size="40px"
      ></v-icon>
      <p>작성 댓글 보기</p>
    </div>
    <div to="mypage/review-list" width="27%" class="middle-button">
      <v-icon
        icon="mdi-medal-outline"
        class="middle-button-icon"
        size="40px"
      ></v-icon>
      <p>작성 리뷰 보기</p>
    </div>
  </div>
  <v-btn
    to="/update-password"
    variant="outlined"
    width="100%"
    rounded="lg"
    class="bottom-button"
    >비밀번호 변경</v-btn
  >
  <div class="d-flex flex-no-wrap justify-content-center">
    <v-btn
      @click="handleLogout"
      variant="outlined"
      width="49%"
      rounded="lg"
      class="bottom-button"
      >로그아웃</v-btn
    >
    <v-btn
      to="/withdrawal"
      variant="outlined"
      width="49%"
      rounded="lg"
      class="bottom-button"
      >회원 탈퇴</v-btn
    >
  </div>
</template>
<script>
import axios from 'axios'

export default {
  data() {
    return {
      dataLoaded: false,
      myInformation: {},
      starCount: 0,
      spreadQuestionAnswer: false,
      sex: [
        { title: '남자', value: 'MALE' },
        { title: '여자', value: 'FEMALE' }
      ]
    }
  },
  setup() {},
  created() {},
  mounted() {
    axios.get(axios.defaults.baseURL + '/users/mypage').then((res) => {
      if (res.status === 200) {
        if (res.data.questionAnswers.length === 0) {
          alert('자기소개를 입력해주셔야 합니다!')
          this.$router.replace({ name: 'question-answer-create' })
        }
        this.myInformation = res.data
        this.starCount = Math.round(this.myInformation.averageReviewScore)
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
    handleLogout() {
      axios.get(axios.defaults.baseURL + '/users/logout').then((res) => {
        if (res.status === 200) {
          alert('로그아웃 되었습니다.')
          localStorage.removeItem('username')
          window.dispatchEvent(new CustomEvent('logout-event'))
          this.$router.replace({ name: 'login' })
        }
      })
    },
    handleSpreadQuestionAnswer() {
      this.spreadQuestionAnswer = !this.spreadQuestionAnswer
    },
    moveToMyMatchposts() {
      this.$router.push({ name: 'match-list-my' })
    },
    moveToMyReplies() {
      this.$router.push({ name: 'match-reply-list-my' })
    }
  }
}
</script>
<style scoped>
.logo-img {
  width: 100px;
}
.img-box {
  width: 70px;
}
.profile-img {
  margin: 10px 0px;
  border-radius: 50%;
}
.top {
  margin: auto auto auto 10px;
}
.information {
  margin: 10px;
  font-size: 15px;
  font-weight: bold;
  text-align: left;
}
.star {
  margin-right: 50%;
}
.view {
  padding: 4px 8px;
  margin-right: 18%;
  font-size: 13px;
  font-weight: bold;
  border-radius: 13px;
  border: solid 1px black;
  background-color: rgba(255, 192, 203, 0.308);
}
.question-answer-update-button {
  padding: 4px 8px;
  font-size: 13px;
  font-weight: bold;
  border-radius: 13px;
  border: solid 1px black;
  background-color: #fdf6e8;
}
.spread-button {
  padding: 4px 60px;
  margin-bottom: 20px;
  font-size: 13px;
  font-weight: bold;
  border-radius: 13px;
  border: solid 1px black;
  background-color: #fdf6e8;
}
.question-text {
  font-size: 13px;
  font-weight: bold;
  text-align: left;
  border: solid 1px gainsboro;
  background-color: rgb(243, 243, 243);
  padding: 5px;
  margin: 5px 0;
}
.answer-text {
  font-size: 13px;
  font-weight: bold;
  text-align: left;
  border: solid 1px gainsboro;
  background-color: rgb(243, 243, 243);
  padding: 5px;
  margin: 5px 0 15px 20px;
}
.middle-button {
  width: 27%;
  margin: 3%;
  font-size: 13px;
  font-weight: bold;
}
.middle-button-icon {
  margin-bottom: 10px;
}
.bottom-button {
  margin: 1%;
  border: solid 2px gainsboro;
  font-weight: bold;
}
</style>
