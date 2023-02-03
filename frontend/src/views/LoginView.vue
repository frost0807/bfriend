//로그인 권한 필요

<template>
  <v-form @submit.prevent="login">
    <v-text-field v-model="loginData.email" label="이메일"> </v-text-field>
    <v-text-field
      v-model="loginData.password"
      :type="'password'"
      label="비밀번호"
    >
    </v-text-field>
    <v-btn type="submit">로그인</v-btn>
  </v-form>
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
      const userData = JSON.stringify(this.loginData, null, 2)
      axios
        .post(axios.defaults.baseURL + '/users/login', userData)
        .then((res) => {
          if (res.status === 200) {
            console.log(res)
            localStorage.setItem('username', res.data)
            window.dispatchEvent(
              new CustomEvent('login-event', {
                detail: {
                  storage: localStorage.getItem('username')
                }
              })
            )
            this.$router.replace({ name: 'home' })
          }
        })
    }
  }
}
</script>
