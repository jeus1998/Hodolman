<template>
  <el-header class="header">
    <el-menu mode="horizontal" router>
      <el-menu-item index="/">Home</el-menu-item>
      <el-menu-item index="/write">글 작성</el-menu-item>
    </el-menu>
  </el-header>
</template>

<style scoped>
.header {
  position: fixed; /* This keeps the header fixed at the top */
  top: 0; /* Positions the header at the top of the page */
  width: 100%; /* Ensures the header spans the full width */
  z-index: 1000; /* Ensures the header is above other elements */
  padding: 0;
  height: 60px;
  background-color: white; /* Optional: Set a background color */
}
</style>