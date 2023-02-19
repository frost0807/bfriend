//로그인 권한 필요

<template>
  <v-img
    src="https://frost0807.s3.ap-northeast-2.amazonaws.com/static/bfriend/login.jpg"
  ></v-img>
  <v-container>
    <h1 align="left" style="margin: 0 0 20px 0">sign in</h1>
    <v-form @submit.prevent="login">
      <p class="label">이메일</p>
      <v-text-field light v-model="loginData.email" variant="outlined">
      </v-text-field>
      <p class="label">비밀번호</p>
      <v-text-field
        v-model="loginData.password"
        variant="outlined"
        :type="'password'"
        background-color="white"
      >
      </v-text-field>
      <v-btn
        variant="text"
        height="60"
        width="100%"
        class="login-button"
        type="submit"
        >로그인</v-btn
      >
    </v-form>
    <v-btn
      to="/forgot-password"
      variant="text"
      height="40"
      width="100%"
      class="forgot-password-button"
      >비밀번호 찾기</v-btn
    ></v-container
  >
</template>
<script>
import axios from 'axios'

export default {
  components: {},
  data() {
    return {
      loginData: {
        email: '',
        password: ''
      }
    }
  },
  setup() {},
  created() {},
  mounted() {},
  unmounted() {},
  methods: {
    login() {
      if (this.loginData.email === '') {
        alert('이메일을 입력해주세요')
        return
      }
      if (this.loginData.password === '') {
        alert('비밀번호를 입력해주세요')
        return
      }
      const userData = JSON.stringify(this.loginData, null, 2)
      axios
        .post(axios.defaults.baseURL + '/users/login', userData)
        .then((res) => {
          console.log(res)
          if (res.status === 200) {
            localStorage.setItem('username', res.data)
            window.dispatchEvent(
              new CustomEvent('login-event', {
                detail: {
                  storage: localStorage.getItem('username')
                }
              })
            )
            this.$router.replace({ name: 'match-list' })
          }
        })
    }
  }
}
</script>

<style scoped>
.label {
  text-align: left;
  font-size: 12px;
  font-weight: bold;
}
.login-button {
  border-radius: 15px;
  background-color: black;
  color: white;
  font-size: 16px;
  font-weight: bold;
  margin-top: 20px;
  margin-bottom: 10px;
}
.forgot-password-button {
  font-size: 16px;
  font-weight: bold;
}
</style>
