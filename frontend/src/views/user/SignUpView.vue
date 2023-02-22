//로그인 권한 필요 X

<template>
  <v-img
    src="https://frost0807.s3.ap-northeast-2.amazonaws.com/static/bfriend/logo.png"
    class="logo-img"
  ></v-img>
  <h2 align="left" style="margin: 0 0 20px 0">가입하기</h2>
  <form @submit.prevent="submit">
    <!-- 이하 이메일 -->
    <v-text-field
      v-model="email.value.value"
      :error-messages="email.errorMessage.value"
      label="이메일"
      :readonly="!isEmailDuplicated"
      variant="outlined"
      density="compact"
    ></v-text-field>
    <div class="button-group">
      <v-btn
        @click="checkEmailDuplicated"
        variant="outlined"
        :disabled="!isEmailDuplicated"
        rounded="pill"
        class="button"
      >
        <p v-if="isEmailDuplicated">이메일 중복 확인</p>
        <p v-if="!isEmailDuplicated">이메일 중복 확인 완료</p>
      </v-btn>
      <v-text-field
        v-if="isEmailCodeSent && !isEmailCertified"
        v-model="emailCertificationCode"
        label="이메일 인증코드 입력(5분)"
        variant="outlined"
        density="compact"
      >
      </v-text-field>
      <v-btn
        v-if="isEmailCodeSent"
        @click="sendEmailCertificationCode"
        variant="outlined"
        :disabled="isEmailCertified"
        rounded="pill"
        class="button"
      >
        <p v-if="!isEmailCertified">인증 코드 입력</p>
        <p v-if="isEmailCertified">이메일 인증 완료</p>
      </v-btn>
      <v-btn
        v-if="!isEmailCodeSent"
        @click="sendEmailCertification"
        variant="outlined"
        :disabled="isEmailCertified"
        rounded="pill"
        class="button"
      >
        이메일 인증
      </v-btn>
    </div>

    <!-- 이하 휴대폰 -->

    <v-text-field
      v-model="phone.value.value"
      :error-messages="phone.errorMessage.value"
      label="휴대폰 번호"
      :readonly="!isPhoneDuplicated"
      variant="outlined"
      density="compact"
      rounded="pill"
    ></v-text-field>
    <div class="button-group">
      <v-btn
        @click="checkPhoneDuplicated"
        :disabled="!isPhoneDuplicated"
        variant="outlined"
        rounded="pill"
        class="button"
      >
        <p v-if="isPhoneDuplicated">휴대폰 중복 확인</p>
        <p v-if="!isPhoneDuplicated">유효한 휴대폰 번호입니다.</p>
      </v-btn>
      <v-text-field
        v-if="isPhoneCodeSent && !isPhoneCertified"
        v-model="phoneCertificationCode"
        label="휴대폰 인증코드 입력(5분)"
        variant="outlined"
        density="compact"
      >
      </v-text-field>
      <v-btn
        v-if="isPhoneCodeSent"
        @click="sendPhoneCertificationCode"
        :disabled="isPhoneCertified"
        variant="outlined"
        rounded="pill"
        class="button"
      >
        <p v-if="!isPhoneCertified">인증 코드 입력</p>
        <p v-if="isPhoneCertified">휴대폰 인증 완료</p>
      </v-btn>
      <v-btn
        v-if="!isPhoneCodeSent"
        @click="sendPhoneCertification"
        :disabled="isPhoneCertified"
        variant="outlined"
        rounded="pill"
        class="button"
      >
        휴대폰 인증
      </v-btn>
    </div>

    <v-text-field
      v-model="password.value.value"
      :type="'password'"
      :counter="8"
      :error-messages="password.errorMessage.value"
      label="비밀번호"
      variant="outlined"
      density="compact"
    ></v-text-field>

    <v-text-field
      v-model="passwordCheck.value.value"
      :type="'password'"
      :counter="8"
      :error-messages="passwordCheck.errorMessage.value"
      label="비밀번호 확인"
      variant="outlined"
      density="compact"
    ></v-text-field>

    <v-text-field
      v-model="name.value.value"
      :error-messages="name.errorMessage.value"
      label="이름"
      variant="outlined"
      density="compact"
    ></v-text-field>
    <v-row>
      <v-col xs="6">
        <v-select
          v-model="sex.value.value"
          :items="sexItems"
          :error-messages="sex.errorMessage.value"
          label="성별"
          variant="outlined"
          density="compact"
        ></v-select>
      </v-col>
      <v-col xs="6">
        <Datepicker
          v-model="date.value.value"
          :enable-time-picker="false"
          :format="format"
          :clearable="true"
          placeholder="생년월일"
        ></Datepicker>
      </v-col> </v-row
    ><v-select
      v-model="region.value.value"
      :items="regionItems"
      :error-messages="region.errorMessage.value"
      label="지역"
      variant="outlined"
      density="compact"
    >
    </v-select>
    <v-btn type="submit" variant="outlined" width="100%" rounded="pill">
      가입하기
    </v-btn>
  </form>
</template>
<script>
import { ref } from 'vue'
import { useField, useForm } from 'vee-validate'
import axios from 'axios'
import Datepicker from '@vuepic/vue-datepicker'
import '@vuepic/vue-datepicker/dist/main.css'

export default {
  components: { Datepicker },
  data() {
    return {
      emailCertificationCode: '',
      phoneCertificationCode: '',
      emailCertificationRequest: [],
      phoneCertificationRequest: [],
      sub() {
        alert('aa')
      }
    }
  },
  setup() {
    const format = (date) => {
      const day = date.getDate()
      const month = date.getMonth() + 1
      const year = date.getFullYear()
      return `${year}/${month}/${day}`
    }

    const isEmailDuplicated = ref(true)
    const isPhoneDuplicated = ref(true)
    const isEmailCodeSent = ref(false)
    const isPhoneCodeSent = ref(false)
    const isEmailCertified = ref(false)
    const isPhoneCertified = ref(false)

    const { handleSubmit } = useForm({
      // validationSchema: {
      //   email(value) {
      //     if (/^[a-z0-9._+-]+@[a-z.-]+\.[a-z]{2,}$/i.test(value)) {
      //       return true
      //     }
      //     return '이메일 형식에 맞게 입력해주세요'
      //   },
      //   name(value) {
      //     if (value?.length >= 2) return true
      //     return '이름을 입력해주세요'
      //   },
      //   phone(value) {
      //     if (/^01[016789]\d{3,4}\d{4}$/i.test(value)) {
      //       return true
      //     }
      //     return '휴대폰 번호를 입력해주세요'
      //   },
      //   password(value) {
      //     if (
      //       /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,20}$/i.test(
      //         value
      //       )
      //     ) {
      //       return true
      //     }
      //     return '영어, 숫자, 특수문자를 섞어서 8자 이상 20자 이하'
      //   },
      //   passwordCheck(value) {
      //     if (
      //       !/^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,20}$/i.test(
      //         value
      //       )
      //     ) {
      //       return '영어, 숫자, 특수문자를 섞어서 8자 이상 20자 이하'
      //     }
      //     if (password.value.value !== value) {
      //       return '비밀번호가 일치하지 않습니다.'
      //     }
      //     return true
      //   },
      //   sex(value) {
      //     if (value) return true
      //     return '성별을 선택해주세요'
      //   },
      //   region(value) {
      //     if (value) return true
      //     return '거주지역을 선택해주세요'
      //   },
      //   date(value) {
      //     return true
      //   }
      // }
    })
    const email = useField('email')
    const password = useField('password')
    const passwordCheck = useField('passwordCheck')
    const name = useField('name')
    const phone = useField('phone')
    const sex = useField('sex')
    const region = useField('region')
    const date = useField('birthday')

    const sexItems = ref([
      { title: '남성', value: 'MALE' },
      { title: '여성', value: 'FEMALE' }
    ])
    const regionItems = ref([
      { title: '서울', value: 'SEOUL' },
      { title: '경기', value: 'KYUNGGI' },
      { title: '인천', value: 'INCHEON' }
    ])

    const submit = handleSubmit((values) => {
      // if (isEmailDuplicated.value) {
      //   alert('이메일 중복 확인을 해주세요')
      //   return
      // } else if (!isEmailCodeSent.value) {
      //   alert('이메일 인증하기 버튼을 눌러 인증을 진행해주세요')
      //   return
      // } else if (!isEmailCertified.value) {
      //   alert('이메일을 인증을 완료해주세요')
      //   return
      // } else if (isPhoneDuplicated.value) {
      //   alert('휴대폰 번호 중복확인을 해주세요')
      //   return
      // } else if (!isPhoneCodeSent.value) {
      //   alert('휴대폰 인증하기 버튼을 눌러 인증을 진행해주세요')
      //   return
      // } else if (!isPhoneCertified.value) {
      //   alert('휴대폰 인증을 완료해주세요')
      //   return
      // }
      values.birthday.setHours(values.birthday.getHours() + 9)
      const userData = JSON.stringify(values, null, 2)
      console.log(userData)
      axios.post(axios.defaults.baseURL + '/users', userData).then((res) => {
        if (res.status === 200) {
          console.log(values)
          localStorage.setItem('joined-name', values.name)
          window.location.href = '/signup-success'
        }
      })
    })

    return {
      name,
      password,
      passwordCheck,
      email,
      phone,
      sex,
      region,
      sexItems,
      regionItems,
      submit,
      date,
      format,
      isEmailDuplicated,
      isEmailCodeSent,
      isEmailCertified,
      isPhoneDuplicated,
      isPhoneCodeSent,
      isPhoneCertified
    }
  },
  methods: {
    // 이메일 중복 확인
    checkEmailDuplicated() {
      axios
        .get(axios.defaults.baseURL + '/users/email/' + this.email.value.value)
        .then((res) => {
          this.isEmailDuplicated = res.status !== 200
        })
    },
    // 이메일 인증 메일 전송
    sendEmailCertification() {
      if (this.isEmailDuplicated) {
        alert('중복 확인을 해주세요')
        return
      }
      axios
        .get(
          axios.defaults.baseURL +
            '/users/email/certification/' +
            this.email.value.value
        )
        .then((res) => {
          this.isEmailCodeSent = res.status === 200
        })
    },
    // 이메일 인증 코드 전송
    sendEmailCertificationCode() {
      this.emailCertificationRequest = {
        email: this.email.value.value,
        certificationCode: this.emailCertificationCode
      }
      axios
        .post(
          axios.defaults.baseURL + '/users/email/certification',
          this.emailCertificationRequest
        )
        .then((res) => {
          console.log(res.data)
          this.isEmailCertified = res.status === 200
        })
    },
    checkPhoneDuplicated() {
      axios
        .get(axios.defaults.baseURL + '/users/phone/' + this.phone.value.value)
        .then((res) => {
          this.isPhoneDuplicated = res.status !== 200
        })
    },
    sendPhoneCertification() {
      if (this.isPhoneDuplicated) {
        alert('중복 확인을 해주세요')
        return
      }
      axios
        .get(
          axios.defaults.baseURL +
            '/users/phone/certification/' +
            this.phone.value.value
        )
        .then((res) => {
          this.isPhoneCodeSent = res.status === 200
        })
    },
    sendPhoneCertificationCode() {
      this.phoneCertificationRequest = {
        phone: this.phone.value.value,
        certificationCode: this.phoneCertificationCode
      }
      axios
        .post(
          axios.defaults.baseURL + '/users/phone/certification',
          this.phoneCertificationRequest
        )
        .then((res) => {
          this.isPhoneCertified = res.status === 200
        })
    }
  }
}
</script>

<style scoped>
.logo-img {
  width: 100px;
  margin: 25px 0 15px 5px;
}
.button-group {
  text-align: right;
  margin-bottom: 20px;
}
.button {
  height: 30px;
}
.v-input__details {
  padding: 0 0 0 0 !important;
}
</style>
