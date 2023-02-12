<template>
  <v-img
    src="https://frost0807.s3.ap-northeast-2.amazonaws.com/static/bfriend/main.png"
    class="responsive-img"
  />
  <v-container>
    <v-img
      src="https://frost0807.s3.ap-northeast-2.amazonaws.com/static/bfriend/logo.png"
      class="logo-img"
    ></v-img>
    <p align="left" class="text">
      진지하게 대화할 대화 친구를 만날 수 있는 곳. 남녀만남에 치우친 어플이
      아닌, 대화의 즐거움을 원하는 사람들끼리 만날 수 있는 장입니다.
    </p>
    <v-btn
      to="/signup"
      variant="text"
      height="60"
      width="100%"
      class="signup-button"
      >회원가입하기</v-btn
    >
    <br />
    <v-btn
      to="/login"
      variant="text"
      height="40"
      width="100%"
      class="login-button"
      >로그인하기</v-btn
    ></v-container
  >
</template>

<script>
// @ is an alias to /src

export default {
  name: 'HomeView',
  components: {}
}
</script>
<style>
.responsive-img {
  max-width: 100%;
  height: auto;
  padding: 0;
}
.logo-img {
  width: 100px;
  margin: 25px 0 15px 5px;
}
</style>

<style scoped>
.signup-button {
  border-radius: 15px;
  background-color: black;
  color: white;
  font-size: 16px;
  font-weight: bold;
  margin-top: 20px;
  margin-bottom: 10px;
}
.login-button {
  font-size: 16px;
  font-weight: bold;
}
.text {
  font-size: 15px;
}
</style>
