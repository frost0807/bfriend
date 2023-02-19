// 로그인 권한 필요

<template>
  <h2>마이페이지</h2>
  <br />
  <h3>자문자답 리스트</h3>
  <p v-for="item in questionAnswerArr" :key="item.id">
    질문 : {{ item.question }} / 답변 : {{ item.answer }}
  </p>
  <br />
  <v-btn>내가 작성한 매칭글 보기</v-btn>
  <v-btn>내가 댓글 단 매칭글 보기</v-btn>
  <v-btn>내가 참여중인 채팅방 보기</v-btn>
  <br />
  <br />
  <v-btn to="question-answer-update">자문자답 수정</v-btn>
  <v-btn to="update-password">비밀번호 변경</v-btn>
  <v-btn @click="handleLogout">로그아웃</v-btn>
  <v-btn to="withdrawal">회원 탈퇴</v-btn>
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
    axios
      .get(axios.defaults.baseURL + '/question-answers/mypage')
      .then((res) => {
        if (res.data.length === 0) {
          alert('자문자답 QA를 입력해주셔야 합니다!')
          this.$router.replace({ name: 'question-answer-create' })
        }
        this.questionAnswerArr = [...res.data]
        console.log(this.questionAnswerArr)
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
