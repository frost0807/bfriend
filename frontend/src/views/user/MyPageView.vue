// 로그인 권한 필요

<template>
  <v-img
    class="logo-img"
    src="https://frost0807.s3.ap-northeast-2.amazonaws.com/static/bfriend/logo.png"
  ></v-img>
  <v-btn to="question-answer-update">자기소개 수정하기</v-btn>
  <br />
  <h3>자문자답 리스트</h3>
  <p v-for="item in questionAnswerArr" :key="item.id">
    질문 : {{ item.question }} / 답변 : {{ item.answer }}
  </p>
  <br />
  <div class="d-flex flex-no-wrap justify-content-center">
    <div width="27%" class="middle-button">
      <v-icon
        icon="mdi-pencil-box-multiple-outline"
        class="middle-button-icon"
        size="40px"
      ></v-icon>
      <p>작성글 보기</p>
    </div>
    <div width="27%" class="middle-button">
      <v-icon
        icon="mdi-email-open-outline"
        class="middle-button-icon"
        size="40px"
      ></v-icon>
      <p>작성 댓글 보기</p>
    </div>
    <div width="27%" class="middle-button">
      <v-icon
        icon="mdi-medal-outline"
        class="middle-button-icon"
        size="40px"
      ></v-icon>
      <p>작성 리뷰 보기</p>
    </div>
  </div>
  <v-btn
    to="update-password"
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
      to="withdrawal"
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
      questionAnswerArr: []
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
        this.questionAnswerArr = [...res.data]
        console.log(this.questionAnswerArr)
      }
    })
  },
  unmounted() {},
  methods: {
    handleLogout() {
      axios.get(axios.defaults.baseURL + '/users/logout').then((res) => {
        if (res.status === 200) {
          alert('로그아웃 되었습니다.')
          localStorage.removeItem('username')
          window.dispatchEvent(new CustomEvent('logout-event'))
          this.$router.replace({ name: 'login' })
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
