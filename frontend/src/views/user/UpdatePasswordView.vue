// 로그인 권한 필요

<template>
  <v-form @submit.prevent="changePassword">
    <v-text-field
      v-model="userData.originalPassword"
      :type="'password'"
      label="기존 비밀번호"
    >
    </v-text-field>
    <v-text-field
      v-model="userData.newPassword"
      :type="'password'"
      label="새 비밀번호"
    ></v-text-field>
    <v-text-field
      v-model="userData.newPasswordCheck"
      :type="'password'"
      label="새 비밀번호 확인"
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
