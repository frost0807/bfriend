//로그인 권한 필요

<template>
  <v-form @submit.prevent="withdrawal">
    <v-text-field
      v-model="userData.password"
      :type="'password'"
      label="비밀번호"
    ></v-text-field>
    <v-text-field v-model="checkMessage" label="'탈퇴합니다'를 입력해주세요">
    </v-text-field>
    <v-btn type="submit">탈퇴하기</v-btn>
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
