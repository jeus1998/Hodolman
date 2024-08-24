<script setup lang="ts">
import {onMounted, ref} from "vue";
import axios from 'axios';
import router from "@/router";

const post = ref({
  id: 0,
  title: "",
  content: "",
});

const props = defineProps({
  postId:{
    type: [Number, String],
    required: true
  }
});

onMounted(() => {
  axios.get(`/api/posts/${props.postId}`).then((response) => {
      post.value = response.data;
  })
});

const edit = function(){
  axios.patch(`/api/posts/${props.postId}`, post.value)
  .then(() => { // 글 수정 이후 글 리스트 페이지로
    router.replace({ name: "home" });
  });
}
</script>

<template>
  <div>
    <el-input v-model="post.title"/>
  </div>

  <div class="mt-2">
    <el-input v-model="post.content" type="textarea" rows="15"/>
  </div>

  <div class="mt-2">
  <el-button type="warning" @click="edit()">글 수정완료</el-button>
  </div>
</template>

<style>
</style>