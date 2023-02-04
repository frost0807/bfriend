//로그인 권한 필요 X

<template>
  <h2>회원가입이 완료되었습니다!</h2>
  <p @click="handleGoHome">홈으로</p>
</template>
<script>
export default {
  components: {},
  data() {
    return {
      sampleData: ''
    }
  },
  setup() {},
  created() {},
  mounted() {},
  unmounted() {},
  methods: {
    handleGoHome() {
      this.$router.push({ name: 'home' })
    }
  }
}
</script>
