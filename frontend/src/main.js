import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'

// Vuetify
import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import Datepicker from '@vuepic/vue-datepicker'
import '@vuepic/vue-datepicker/dist/main.css'
import 'material-design-icons-iconfont/dist/material-design-icons.css'
import '@mdi/font/css/materialdesignicons.css'

const vuetify = createVuetify({
  components,
  directives
})
// axios.defaults.baseURL = 'http://localhost:8080'
axios.defaults.baseURL = 'http://43.201.136.225:8080'
axios.defaults.withCredentials = true
axios.defaults.headers.post['Content-Type'] = 'application/json'
axios.defaults.headers.put['Content-Type'] = 'application/json'
axios.defaults.headers.patch['Content-Type'] = 'application/json'
axios.interceptors.response.use(
  (res) => res,
  (err) => {
    console.log(err.response)
    alert(err.response.data.message)
    if (err.response.status === 401) {
      window.dispatchEvent(
        new CustomEvent('unauthorized-event', {
          detail: {
            storage: localStorage.removeItem('username')
          }
        })
      )
    }
    return Promise.reject(err)
  }
)

createApp(App)
  .use(vuetify)
  .component('Datepicker', Datepicker)
  .use(store)
  .use(router)
  .mount('#app')
