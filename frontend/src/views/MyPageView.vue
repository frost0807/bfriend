<template>
  <div>마이페이지</div>
</template>
<script>
import axios from 'axios'

export default {
  data() {
    return {
      sampleData: ''
    }
  },
  setup() {},
  created() {},
  mounted() {
    axios
      .get(axios.defaults.baseURL + '/question-answers')
      .then((res) => {
        if (res.data.length === 0) {
          alert('자문자답 QA를 입력해주셔야 합니다!')
          this.$router.replace({ name: 'question-answer-input' })
        }
      })
      .catch((err) => {
        console.log(err.response)
        alert(err.response.data.message)
      })
  },
  unmounted() {},
  methods: {}
}
</script>
