//로그인 권한 필요

<template>
  <div>QNA수정</div>
  <v-btn @click="addRow">항목 추가</v-btn>
  <form @submit.prevent="submit">
    <div v-for="(item, index) in questionAndAnswer" :key="index">
      <v-row>
        <v-col cols="12" sm="3">
          <v-select
            v-model="item.categoryId"
            :items="categories"
            item-title="name"
            item-value="questionCategoryId"
            label="카테고리"
            :readonly="item.categoryId !== null"
          >
          </v-select
        ></v-col>
        <v-col cols="12" sm="9">
          <v-select
            v-model="item.questionId"
            :items="questionsByCategoryId(item.categoryId)"
            item-title="content"
            item-value="questionId"
            label="질문"
          >
          </v-select
        ></v-col>
      </v-row>
      <v-text-field
        v-model="item.answer"
        placeholder="답변을 자유롭게 작성해주세요"
      ></v-text-field>
      <v-btn @click="deleteRow(index)">항목 제거</v-btn>
      <br />
    </div>
    <v-btn type="submit">수정완료</v-btn>
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
            alert('자문자답 수정이 완료되었습니다.')
            this.$router.replace({ name: 'mypage' })
          }
        })
    }
  }
}
</script>
