<template>
  <div>
    <v-form v-if="!isMatchPostOfMine" @submit.prevent="writeReply">
      <textarea
        v-model="parentReplyData.comment"
        rows="3"
        placeholder="댓글 쓰기"
        class="reply-input"
      ></textarea>
      <div class="d-flex justify-end">
        <v-btn
          type="submit"
          density="comfortable"
          variant="text"
          class="reply-submit-button"
          >확인</v-btn
        >
      </div>
    </v-form>
  </div>
  <!-- 댓글 리스트 시작 -->
  <div v-if="dataLoaded">
    <div
      v-for="(replyGroup, replyGroupIndex) in replyList"
      :key="replyGroupIndex"
    >
      <ReplyGroup
        :matchPostId="matchPostId"
        :replyGroup="replyGroup"
        @write-event="handleWriteEvent"
        @delete-event="handleDeleteEvent"
        @update-event="handleUpdateEvent"
      />
    </div>
  </div>
</template>
<script>
import axios from 'axios'
import ReplyGroup from './ReplyGroup.vue'

export default {
  components: { ReplyGroup },
  props: {
    matchPostId: String,
    isMatchPostOfMine: Boolean
  },
  data() {
    return {
      dataLoaded: false,
      replyList: [],
      parentReplyData: {}
    }
  },
  setup() {},
  created() {},
  mounted() {
    axios
      .get(
        axios.defaults.baseURL + '/matchposts/' + this.matchPostId + '/replies'
      )
      .then((res) => {
        console.log(res)
        if (res.status === 200) {
          this.replyList = res.data
          this.dataLoaded = true
          this.parentReplyData.matchPostId = this.matchPostId
        }
      })
  },
  unmounted() {},
  methods: {
    writeReply() {
      const reply = JSON.stringify(this.parentReplyData, null, 2)
      console.log(this.matchPostId)
      axios
        .post(axios.defaults.baseURL + '/matchposts/replies', reply)
        .then((res) => {
          console.log(res)
          if (res.status === 200) {
            alert('댓글이 작성되었습니다.')
            this.parentReplyData.comment = ''
            axios
              .get(
                axios.defaults.baseURL +
                  '/matchposts/' +
                  this.matchPostId +
                  '/replies'
              )
              .then((res) => {
                if (res.status === 200) {
                  this.dataLoaded = false
                  this.replyList = res.data
                  this.dataLoaded = true
                }
              })
          }
        })
    },
    getReplies() {
      axios
        .get(
          axios.defaults.baseURL +
            '/matchposts/' +
            this.matchPostId +
            '/replies'
        )
        .then((res) => {
          if (res.status === 200) {
            this.replyList = res.data
          }
        })
    },
    handleWriteEvent() {
      this.getReplies()
    },
    handleDeleteEvent() {
      this.getReplies()
    },
    handleUpdateEvent() {
      this.getReplies()
    }
  }
}
</script>
<style scoped>
.profile-img {
  width: 40px;
  border-radius: 50%;
}
.user-info {
  margin-left: 10px;
  text-align: left;
}
.username {
  font-size: 13px;
  font-weight: bold;
}
.reply-input {
  border: 1px solid gainsboro;
  border-radius: 10px;
  margin-top: 10px;
  width: 100%;
  background-color: #fdf6e8;
}
.time-after-create {
  margin: auto 10px auto 0;
  font-size: 12px;
  font-weight: bold;
  color: gray;
}
.reply-submit-button {
  float: right;
  font-size: 13px;
  border: 1px solid black;
  border-radius: 15px;
  background-color: #fdf6e8;
  padding: 0 40px;
}
</style>
