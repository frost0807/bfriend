//로그인 권한 필요

<template>
  <div class="d-flex">
    <div class="logo-img">
      <v-img
        src="https://frost0807.s3.ap-northeast-2.amazonaws.com/static/bfriend/logo.png"
      ></v-img>
    </div>
    <h2 class="font-weight-bold">회원 탈퇴</h2>
  </div>
  <v-form @submit.prevent="withdrawal">
    <v-text-field
      v-model="userData.password"
      :type="'password'"
      label="비밀번호"
      variant="outlined"
    ></v-text-field>
    <v-text-field
      v-model="checkMessage"
      label="'탈퇴합니다'를 입력해주세요"
      variant="outlined"
    >
    </v-text-field>
    <v-btn
      type="submit"
      height="30px"
      width="100%"
      class="delete-button"
      variant="outlined"
      >탈퇴하기</v-btn
    >
  </v-form>
</template>
<script>
import axios from 'axios'

export default {
  components: {},
  data() {
    return {
      userData: { password: '' },
      checkMessage: ''
    }
  },
  setup() {},
  created() {},
  mounted() {},
  unmounted() {},
  methods: {
    withdrawal() {
      console.log(JSON.stringify(this.userData))
      if (this.checkMessage !== '탈퇴합니다') {
        alert('탈퇴문구를 정확히 입력해주세요')
        return
      }
      axios
        .delete(axios.defaults.baseURL + '/users', {
          headers: {
            'Content-Type': 'application/json'
          },
          data: JSON.stringify(this.userData)
        })
        .then((res) => {
          if (res.status === 200) {
            console.log(res)
            alert('탈퇴가 완료되었습니다.')
            localStorage.removeItem('username')
            window.dispatchEvent(new CustomEvent('logout-event'))
            this.$router.replace({ name: 'home' })
          }
        })
    }
  }
}
</script>
<style scoped>
h2 {
  margin: 25px;
}
.logo-img {
  width: 100px;
}
.delete-button {
  border-radius: 15px;
  background-color: gainsboro;
}
</style>
