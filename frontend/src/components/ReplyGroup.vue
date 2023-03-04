<template>
  <div class="divideLine"></div>
  <div v-for="(reply, replyIndex) in replyGroup" :key="replyIndex">
    <div v-if="!reply.parentReplyId" class="d-flex flex-no-wrap justify-start">
      <div>
        <v-img
          class="profile-img"
          src="https://frost0807.s3.ap-northeast-2.amazonaws.com/static/bfriend/default_profile.png"
        ></v-img>
      </div>
      <div class="user-info">
        <p class="username">
          {{ reply.username }} ({{ reply.age }}세/{{
            getTitleFromValue(sex, reply.sex)
          }})
        </p>
      </div>
    </div>
    <!-- 여기까지 부모댓글 작성자 정보 -->

    <!-- 여기서부터 댓글 그룹에 대한 댓글들 출력 -->
    <!-- 내 댓글일 경우 -->
    <v-card v-if="reply.replyOfMine" color="#CDD7FD" class="reply my-reply">
      <div v-if="!reply.replyDeleted" class="d-flex flex-no-wrap justify-end">
        <div v-if="reply.createdAt !== reply.updatedAt" class="updated">
          (수정됨)
        </div>
        <v-dialog v-model="updateCheck" persistent width="auto">
          <template v-slot:activator="{ props }"
            ><v-btn
              @click="selectReplyToUpdate(reply)"
              v-bind="props"
              size="20px"
              class="update-button"
              ><v-icon icon="mdi-lead-pencil" size="20px"></v-icon></v-btn
          ></template>
          <v-card>
            <textarea
              class="update-reply-input"
              rows="3"
              v-model="replyToUpdate.comment"
              placeholder="대댓글 쓰기(최대 200자)"
            ></textarea>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn @click="handleUpdate">수정하기</v-btn>
              <v-btn @click="updateCheck = false">취소</v-btn>
            </v-card-actions></v-card
          >
        </v-dialog>

        <v-btn
          @click="handleDelete(reply.replyId)"
          size="20px"
          class="delete-button"
          ><v-icon icon="mdi-delete" size="20px"></v-icon
        ></v-btn>
      </div>
      <v-card-text>{{ reply.comment }}</v-card-text>
      <!-- 댓글 그룹의 마지막 댓글 or 대댓글일 경우 -->
      <div
        v-if="replyIndex === replyGroup.length - 1 && !reply.replySecret"
        class="d-flex flex-no-wrap justify-end"
      >
        <div class="time-after-create">
          {{ timeAfterCreateString(reply.minutesAfterCreate) }}
        </div>
        <v-btn
          @click="toggleChildReplyInput"
          size="30px"
          color="#CDD7FD"
          rounded="circle"
          class="child-reply-button"
          ><v-icon icon="mdi-chat-processing-outline" size="25px"></v-icon
        ></v-btn>
      </div>
    </v-card>
    <!-- 내 댓글이 아닐 경우 -->
    <v-card
      v-if="!reply.replyOfMine"
      color="#FDF6E8"
      class="reply not-my-reply"
    >
      <v-card-text>{{ reply.comment }}</v-card-text>
      <!-- 댓글 그룹의 마지막 댓글 or 대댓글일 경우 -->
      <div
        v-if="replyIndex === replyGroup.length - 1 && !reply.replySecret"
        class="d-flex flex-no-wrap justify-end"
      >
        <div class="time-after-create">
          {{ timeAfterCreateString(reply.minutesAfterCreate) }}
        </div>
        <v-btn
          @click="toggleChildReplyInput"
          size="30px"
          rounded="circle"
          class="child-reply-button"
          color="#FDF6E8"
          ><v-icon icon="mdi-chat-processing-outline" size="25px"></v-icon
        ></v-btn>
      </div>
    </v-card>
    <div
      v-if="
        replyIndex === replyGroup.length - 1 &&
        !reply.replySecret &&
        childReplyInputToggle
      "
    >
      <form @submit.prevent="writeChildReply">
        <textarea
          rows="3"
          v-model="childReplyData.comment"
          placeholder="대댓글 쓰기(최대 200자)"
          class="child-reply-input"
        ></textarea>
        <div class="d-flex justify-end">
          <v-btn
            type="submit"
            density="comfortable"
            variant="text"
            class="child-reply-submit-button"
            >확인</v-btn
          >
        </div>
      </form>
    </div>
  </div>
</template>
<script>
import axios from 'axios'
export default {
  components: {},
  emits: ['write-event', 'delete-event', 'update-event'],
  props: {
    matchPostId: String,
    replyGroup: Object
  },
  data() {
    return {
      childReplyInputToggle: false,
      updateCheck: false,
      deleteCheck: false,
      replyToUpdate: {},
      sex: [
        { title: '남자', value: 'MALE' },
        { title: '여자', value: 'FEMALE' }
      ],
      childReplyData: {}
    }
  },
  setup() {},
  created() {},
  mounted() {},
  unmounted() {},
  methods: {
    getTitleFromValue(arr, value) {
      for (const item of arr) {
        if (item.value === value) {
          return item.title
        }
      }
    },
    timeAfterCreateString(minutesAfterCreate) {
      if (minutesAfterCreate >= 60) {
        return Math.floor(minutesAfterCreate / 60) + '시간전 작성'
      }
      if (minutesAfterCreate >= 60 * 24) {
        return Math.floor(minutesAfterCreate / (24 * 60)) + '일전 작성'
      }
      return Math.floor(minutesAfterCreate) + '분전 작성'
    },
    toggleChildReplyInput() {
      this.childReplyInputToggle = !this.childReplyInputToggle
    },
    writeChildReply() {
      this.childReplyData.matchPostId = this.matchPostId
      this.childReplyData.parentReplyId = this.replyGroup[0].replyId
      const reply = JSON.stringify(this.childReplyData, null, 2)

      axios
        .post(axios.defaults.baseURL + '/matchposts/replies', reply)
        .then((res) => {
          console.log(res)
          if (res.status === 200) {
            alert('대댓글이 작성되었습니다.')
            this.childReplyData.comment = ''
            this.$emit('write-event')
          }
        })
    },
    handleDelete(selectedReplyId) {
      if (confirm('정말로 삭제하시겠습니까?')) {
        axios
          .delete(
            axios.defaults.baseURL + '/matchposts/replies/' + selectedReplyId
          )
          .then((res) => {
            console.log(res)
            if (res.status === 200) {
              this.deleteCheck = false
              alert('삭제되었습니다.')
              this.$emit('delete-event')
            }
          })
      }
    },
    selectReplyToUpdate(selectedReply) {
      this.replyToUpdate = JSON.parse(JSON.stringify(selectedReply))
    },
    handleUpdate() {
      this.replyToUpdate.matchPostId = this.matchPostId
      console.log(this.replyToUpdate)
      const reply = JSON.stringify(this.replyToUpdate, null, 2)
      axios
        .put(axios.defaults.baseURL + '/matchposts/replies', reply)
        .then((res) => {
          console.log(res)
          if (res.status === 200) {
            this.updateCheck = false
            alert('댓글이 수정되었습니다.')
            this.$emit('update-event')
          }
        })
    }
  }
}
</script>
<style scoped>
.time-after-create {
  margin: auto 10px auto 0;
  font-size: 12px;
  font-weight: bold;
  color: gray;
}
.child-reply-input {
  border: 1px solid gainsboro;
  border-radius: 10px;
  margin-top: 10px;
  width: 100%;
  background-color: #cdd7fd;
}
.update-reply-input {
  margin: 5px;
  width: 300px;
}
.reply-submit-button {
  float: right;
  font-size: 13px;
  border: 1px solid black;
  border-radius: 15px;
  background-color: #fdf6e8;
  padding: 0 40px;
}
.child-reply-submit-button {
  float: right;
  font-size: 13px;
  border: 1px solid black;
  border-radius: 15px;
  background-color: #cdd7fd;
  padding: 0 40px;
}
.divideLine {
  border: 2px solid black;
  margin: 20px 0;
}
.reply {
  margin-top: 10px;
  margin-bottom: 10px;
}
.my-reply {
  margin-left: 30px;
}
.not-my-reply {
  margin-right: 30px;
}
div.v-card-text {
  padding: 5px;
}
.child-reply-button,
.update-button,
.delete-button {
  margin: 5px;
}
.updated {
  margin: 5px;
  font-size: 13px;
  color: gray;
}
</style>
