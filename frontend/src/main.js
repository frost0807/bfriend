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

const vuetify = createVuetify({
  components,
  directives
})

axios.defaults.baseURL = 'http://localhost:8080/'
axios.defaults.withCredentials = true

createApp(App)
  .use(vuetify)
  .component('Datepicker', Datepicker)
  .use(store)
  .use(router)
  .mount('#app')
