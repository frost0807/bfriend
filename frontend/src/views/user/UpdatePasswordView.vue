// 로그인 권한 필요

<template>
  <div class="d-flex">
    <div class="logo-img">
      <v-img
        src="https://frost0807.s3.ap-northeast-2.amazonaws.com/static/bfriend/logo.png"
      ></v-img>
    </div>
    <h2 class="font-weight-bold">비밀번호 변경</h2>
  </div>
  <v-form @submit.prevent="changePassword">
    <v-text-field
      v-model="userData.originalPassword"
      :type="'password'"
      label="기존 비밀번호"
      variant="outlined"
    >
    </v-text-field>
    <v-text-field
      v-model="userData.newPassword"
      :type="'password'"
      label="새 비밀번호"
      variant="outlined"
    ></v-text-field>
    <v-text-field
      v-model="userData.newPasswordCheck"
      :type="'password'"
      label="새 비밀번호 확인"
      variant="outlined"
    >
    </v-text-field>
    <v-btn
      type="submit"
      height="30px"
      width="100%"
      class="update-button"
      variant="outlined"
      >변경하기</v-btn
    >
  </v-form>
</template>
<script>
import axios from 'axios'
export default {
  components: {},
  data() {
    return {
      userData: {
        originalPassword: '',
        newPassword: '',
        newPasswordCheck: ''
      }
    }
  },
  setup() {},
  created() {},
  mounted() {},
  unmounted() {},
  methods: {
    changePassword() {
      if (this.userData.newPassword !== this.userData.newPasswordCheck) {
        alert('새 비밀번호가 일치하지 않습니다.')
        return
      }
      axios
        .patch(
          axios.defaults.baseURL + '/users/password',
          JSON.stringify(this.userData)
        )
        .then((res) => {
          console.log(res)
          if (res.status === 200) {
            alert('비밀번호가 성공적으로 변경되었습니다.')
            this.$router.replace({ name: 'mypage' })
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
.update-button {
  border-radius: 15px;
  background-color: gainsboro;
}
</style>
