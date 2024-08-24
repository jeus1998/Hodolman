<script setup lang="ts">
import axios from "axios";
import {onMounted, ref} from "vue";

const props = defineProps({
  postId: {
    type: [Number, String],
    required: true,
  }
});

const post = ref({
  id: 0,
  title: "",
  content: ""
});

onMounted(() => {
  // console.log(props.postId)
  axios.get(`/api/posts/${props.postId}`)
      .then((response) => {
        // console.log(response)
        post.value = response.data;
      });
});
</script>

<template>
  <h2>{{post.title}}</h2>
  <div>{{post.content}}</div>
</template>