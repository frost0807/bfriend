<template>
  <v-app>
    <v-main>
      <v-container
        v-if="
          $route.path !== '/' &&
          $route.path !== '/login' &&
          $route.path !== '/signup-success'
        "
      >
        <router-view />
      </v-container>
      <router-view
        v-if="
          $route.path === '/' ||
          $route.path === '/login' ||
          $route.path === '/signup-success'
        "
      />
    </v-main>

    <v-bottom-navigation
      v-if="
        $route.path !== '/' &&
        $route.path !== '/login' &&
        $route.path !== '/signup' &&
        $route.path !== '/forgot-password' &&
        $route.path !== '/signup-success'
      "
    >
      <v-btn to="/match/list"
        ><v-icon icon="mdi-home" size="x-large"></v-icon> 홈</v-btn
      >
      <v-btn><v-icon icon="mdi-chat" size="x-large"></v-icon>채팅</v-btn>
      <v-btn to="/mypage"
        ><v-icon icon="mdi-tune" size="x-large"></v-icon>마이페이지</v-btn
      >
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
      this.$router.push({ name: 'home' })
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
