<template>
  <v-app :theme="theme">
    <v-app-bar>
      <v-btn to="/">Home</v-btn>
      <v-spacer></v-spacer>

      <v-btn
        :prepend-icon="
          theme === 'light' ? 'mdi-weather-sunny' : 'mdi-weather-night'
        "
        @click="onClick"
        >Toggle Theme</v-btn
      >
      <p v-if="username">{{ username }}님 환영합니다.</p>
      <p v-if="!username">로그인이 필요합니다</p>
      <v-spacer></v-spacer>
      <v-btn v-if="username" to="/mypage">마이 페이지</v-btn>
      <v-btn v-if="username" @click="logout">로그아웃</v-btn>
      <v-btn v-if="!username" to="/signup">회원가입</v-btn>
      <v-btn v-if="!username" to="/login">로그인</v-btn>
    </v-app-bar>

    <v-main>
      <v-container>
        <router-view />
      </v-container>
    </v-main>

    <v-bottom-navigation>
      <v-btn to="/match/list">
        <v-icon icon="mdi-vuetify"></v-icon>
        매칭 게시판
      </v-btn>
    </v-bottom-navigation>
  </v-app>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      theme: 'light',
      username: ''
    }
  },
  mounted() {
    this.username = localStorage.getItem('username')
    window.addEventListener('login-event', (event) => {
      this.username = event.detail.storage
    })
    window.addEventListener('logout-event', () => {
      this.username = ''
    })
    window.addEventListener('unauthorized-event', () => {
      this.username = ''
      this.$router.push({ name: 'login' })
    })
  },
  methods: {
    onClick() {
      this.theme = this.theme === 'light' ? 'dark' : 'light'
    },
    logout() {
      axios
        .get(axios.defaults.baseURL + '/users/logout')
        .then((res) => console.log(res))

      localStorage.removeItem('username')
      window.dispatchEvent(new CustomEvent('logout-event'))
      this.$router.replace({ name: 'login' })
    }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>
