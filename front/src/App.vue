<script setup lang="ts">
import Header from "@/components/Header.vue";
import Content from "@/components/Content.vue";
</script>

<template>
  <Header />
  <div class="main-content">
      <Content />
    </div>
</template>

<style>

body {
  font-family: Pretendard, -apple-system, BlinkMacSystemFont, system-ui, Roboto,
    "Helvetica Neue", "Segoe UI", "Apple SD Gothic Neo", "Noto Sans KR",
    "Malgun Gothic", "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol",
    sans-serif;
  margin: 0; /* 기본 여백 제거 */
}

.main-content {
  padding-top: 60px; /* 헤더 높이와 동일한 패딩 추가 */
}
</style>
