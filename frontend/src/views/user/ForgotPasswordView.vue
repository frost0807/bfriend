// 로그인 권한 필요

<template>
  <h2>임시 비밀번호 발급</h2>
  <v-form @submit.prevent="sendTempPassword">
    <v-text-field v-model="userData.email" label="이메일"> </v-text-field>
    <v-text-field v-model="userData.name" label="이름"> </v-text-field>
    <v-btn type="submit">메일 발송하기</v-btn>
  </v-form>
</template>
<script>
import axios from 'axios'

export default {
  components: {},
  data() {
    return {
      userData: { email: '', name: '' }
    }
  },
  setup() {},
  created() {},
  mounted() {},
  unmounted() {},
  methods: {
    sendTempPassword() {
      axios
        .patch(
          axios.defaults.baseURL + '/users/temporary-password',
          JSON.stringify(this.userData)
        )
        .then((res) => {
          console.log(res)
          if (res.status === 200) {
            alert('임시 비밀번호가 메일로 전송되었습니다.')
            this.$router.replace({ name: 'login' })
          }
        })
    }
  }
}
</script>
