//로그인 권한 필요 X

<template>
  <v-img
    src="https://frost0807.s3.ap-northeast-2.amazonaws.com/static/bfriend/logo.png"
    class="logo-img"
  ></v-img>
  <v-img
    src="https://frost0807.s3.ap-northeast-2.amazonaws.com/static/bfriend/welcome.png"
  ></v-img>
  <v-container>
    <p>
      <span>{{ name }}</span
      >님 안녕하세요
    </p>
    <p>비프렌드에 오신 걸 환영합니다.</p>
    <br />
    <p>
      어떤 대화든 좋습니다. 앉아서 하는 대화가 아니더라도 잠깐 동네 산책일 수도
      있어요. 잠깐의 동행이 각자 삶에 영감을 주거나 새로운 삶의 시각을 줄 수도
      있습니다.
    </p>
    <br />
    <p>
      짧은 동네 산책부터 술과 함께하는 깊은 대화도 서로의 취향에 맞게 만나볼 수
      있습니다. 필요한 것은 새로운 친구에 대한 정중함과 예의입니다. 각자 서로
      다른 일상을 사는 사람들이 잠깐 만나가는 자리인 만큼 소중한 서로에 대한
      정중함은 필수입니다. 비프렌드를 통해서 일상 속 대화 친구를 만나 진정한
      친구로 이어질 수 있기를 기원합니다.
    </p>
    <br />

    <v-btn @click="handleGoHome" variant="outlined" width="100%" height="60px"
      >비프렌드 홈으로 이동하기</v-btn
    ></v-container
  >
</template>
<script>
export default {
  components: {},
  data() {
    return {
      name: localStorage.getItem('joined-name')
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
<style scoped>
.logo-img {
  margin-left: 20px;
}
p {
  text-align: left;
  font-size: 14px;
}
span {
  color: coral;
  font-weight: bold;
}
.v-btn {
  background-color: gainsboro;
  border-radius: 12px;
}
</style>
