<script setup lang="ts">
import {ref} from "vue";
import axios from 'axios';
import router from "@/router";

const title = ref("")
const content = ref("")

const write = function(){
  // console.log(title.value, content.value)
  axios.post("/api/posts", {
      title: title.value,
      content: content.value
  })
  .then(() => { // 글 작성 이후 글 리스트 페이지로
    router.replace({ name: "home" });
  });
}
</script>

<template>
  <div>
    <el-input v-model="title" placeholder="제목을 입력해주세요" />
  </div>

  <div class="mt-2">
    <el-input v-model="content" placeholder="내용을 입력해주세요" type="textarea" rows="15"/>
  </div>

  <div class="mt-2">
  <el-button type="primary" @click="write()">글 작성완료</el-button>
  </div>
</template>

<style>
</style>