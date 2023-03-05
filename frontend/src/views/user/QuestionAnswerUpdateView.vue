//로그인 권한 필요

<template>
  <v-img
    class="logo-img"
    src="https://frost0807.s3.ap-northeast-2.amazonaws.com/static/bfriend/logo.png"
  ></v-img>
  <h2 align="left" class="title">자기소개 수정하기</h2>
  <form @submit.prevent="submit">
    <div v-for="(item, index) in questionAndAnswer" :key="index" class="item">
      <div class="d-flex flex-no-wrap">
        <v-select
          v-model="item.categoryId"
          :items="categories"
          item-title="name"
          item-value="questionCategoryId"
          label="카테고리"
          :readonly="item.categoryId !== null"
          variant="outlined"
          density="compact"
          hide-details
          class="category"
        >
        </v-select>
        <v-btn @click="deleteRow(index)" size="30px" class="delete-button"
          ><v-icon icon="mdi-delete" size="25px"></v-icon
        ></v-btn>
      </div>
      <v-select
        v-model="item.questionId"
        :items="questionsByCategoryId(item.categoryId)"
        item-title="content"
        item-value="questionId"
        label="질문"
        variant="outlined"
        density="compact"
        hide-details
        class="question"
      >
      </v-select>
      <v-text-field
        v-model="item.answer"
        placeholder="답변을 자유롭게 작성해주세요"
        variant="outlined"
        density="compact"
        hide-details
        class="answer"
      ></v-text-field>
      <br />
    </div>
    <v-btn @click="addRow" size="30px"
      ><v-icon icon="mdi-plus" size="30px"></v-icon
    ></v-btn>
    <v-spacer></v-spacer>
    <v-btn
      type="submit"
      height="35px"
      width="100%"
      variant="text"
      class="submit-button"
      >수정완료</v-btn
    >
  </form>
</template>
<script>
import axios from 'axios'

export default {
  components: {},
  data() {
    return {
      categories: [],
      questions: [],
      questionAndAnswer: [{ categoryId: null, questionId: null, answer: null }],
      count: 1
    }
  },
  setup() {},
  created() {},
  mounted() {
    axios
      .get(
        axios.defaults.baseURL + '/question-answers/categories-and-questions'
      )
      .then((res) => {
        this.categories = [...res.data.categories]
        this.questions = [...res.data.questions]
        console.log(this.categories)
        console.log(this.questions)
      })
      .catch((err) => {
        console.log(err.response)
        alert(err.response.data.message)
      })
    axios.get(axios.defaults.baseURL + '/question-answers').then((res) => {
      if (res.data.length === 0) {
        alert('자문자답 QA를 작성하신적이 없습니다.')
        this.$router.replace({ name: 'question-answer-input' })
      }
      this.questionAndAnswer = res.data
    })
  },
  unmounted() {},
  methods: {
    addRow() {
      this.questionAndAnswer.push({
        categoryId: null,
        questionId: null,
        answer: null
      })
    },
    deleteRow(index) {
      console.log(index)
      this.questionAndAnswer.splice(index, 1)
    },
    questionsByCategoryId(categoryId) {
      const result = this.questions.filter(
        (item) => item.categoryId === categoryId
      )
      return [...result]
    },
    submit() {
      const questionAnswerData = JSON.stringify(this.questionAndAnswer, null, 2)
      axios
        .patch(axios.defaults.baseURL + '/question-answers', questionAnswerData)
        .then((res) => {
          if (res.status === 200) {
            alert('자기소개 수정이 완료되었습니다.')
            this.$router.replace({ name: 'mypage' })
          }
        })
    }
  }
}
</script>
<style scoped>
.logo-img {
  width: 100px;
}
.title {
  margin: 0 0 20px 0;
}
.cateogry,
.question,
.answer {
  margin: 10px 0 0 0;
}
.delete-button {
  margin: auto 30px;
}
.submit-button {
  margin: 30px 0;
  border: solid 1px black;
  border-radius: 20px;
}
</style>
