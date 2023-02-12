// 로그인 권한 필요

<template>
  <h2>임시 비밀번호 발급</h2>
  <v-form @submit.prevent="sendTempPassword">
    <p class="label">이메일</p>
    <v-text-field v-model="userData.email" variant="outlined" density="compact">
    </v-text-field>
    <p class="label">이름</p>
    <v-text-field v-model="userData.name" variant="outlined" density="compact">
    </v-text-field>
    <v-btn type="submit" height="50" width="100%" variant="outlined"
      >임시 비밀번호 메일 발송하기</v-btn
    >
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
<style scoped>
.v-btn {
  border-radius: 10px;
}
.label {
  text-align: left;
  font-size: 12px;
  font-weight: bold;
}
</style>
