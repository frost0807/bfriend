<template>
  <div>
    <v-form v-if="!isMatchPostOfMine" @submit="writeReply">
      <textarea rows="3" placeholder="댓글 쓰기" class="reply-input"></textarea>
      <div class="d-flex justify-end">
        <v-btn density="comfortable" variant="text" class="reply-submit-button"
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
      <div class="divideLine"></div>
      <div v-for="(reply, replyIndex) in replyGroup" :key="replyIndex">
        <div
          v-if="!reply.parentReplyId"
          class="d-flex flex-no-wrap justify-start"
        >
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
              @click="
                toggleChildReplyInput(
                  reply.parentReplyId !== null
                    ? reply.parentReplyId
                    : reply.replyId
                )
              "
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
              @click="
                toggleChildReplyInput(
                  reply.parentReplyId !== null
                    ? reply.parentReplyId
                    : reply.replyId
                )
              "
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
            checkChildReplyInputToggle(
              reply.parentReplyId !== null ? reply.parentReplyId : reply.replyId
            )
          "
        >
          <textarea
            rows="3"
            placeholder="대댓글 쓰기(최대 200자)"
            class="child-reply-input"
          ></textarea>
          <div class="d-flex justify-end">
            <v-btn
              density="comfortable"
              variant="text"
              class="child-reply-submit-button"
              >확인</v-btn
            >
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import axios from 'axios'
export default {
  components: {},
  props: {
    matchPostId: String,
    isMatchPostOfMine: Boolean
  },
  data() {
    return {
      dataLoaded: false,
      replyList: [],
      childReplyInputToggle: [],
      parentRelyInputData: {},
      childReplyInputData: {},
      sex: [
        { title: '남자', value: 'MALE' },
        { title: '여자', value: 'FEMALE' }
      ]
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
          this.replyList.forEach((replyGroup) => {
            this.childReplyInputToggle.push({
              parentReplyId: replyGroup[0].replyId,
              showChildReplyInput: false
            })
          })
          this.dataLoaded = true
        }
      })
  },
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
    toggleChildReplyInput(parentReplyId) {
      this.childReplyInputToggle.forEach((replyGroup) => {
        if (replyGroup.parentReplyId === parentReplyId) {
          replyGroup.showChildReplyInput = !replyGroup.showChildReplyInput
        }
      })
    },
    checkChildReplyInputToggle(parentReplyId) {
      console.log(this.childReplyInputToggle)
      console.log(parentReplyId)
      let result
      this.childReplyInputToggle.forEach((replyGroup) => {
        if (replyGroup.parentReplyId === parentReplyId) {
          result = replyGroup.showChildReplyInput
        }
      })
      return result
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
.time-after-create {
  margin: auto 10px auto 0;
  font-size: 12px;
  font-weight: bold;
  color: gray;
}
.reply-input {
  border: 1px solid gainsboro;
  border-radius: 10px;
  margin-top: 10px;
  width: 100%;
  background-color: #fdf6e8;
}
.child-reply-input {
  border: 1px solid gainsboro;
  border-radius: 10px;
  margin-top: 10px;
  width: 100%;
  background-color: #cdd7fd;
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
</style>
