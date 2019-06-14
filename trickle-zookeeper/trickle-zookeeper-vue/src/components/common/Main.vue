<template>
  <el-container>
    <el-aside width="200px">
      <el-menu default-active="1" class="el-menu-vertical-demo">
        <el-menu-item index="1">
          <i class="el-icon-menu"></i>
          <router-link to="/food">我的主页</router-link>
        </el-menu-item>
        <el-menu-item index="2">
          <i class="el-icon-document"></i>
          <router-link to="/food">集群管理</router-link>
        </el-menu-item>
        <el-menu-item index="3">
          <i class="el-icon-setting"></i>
          <router-link to="/food">节点管理</router-link>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-main>
      <router-view></router-view>
    </el-main>
  </el-container>
</template>
<style>

</style>
<script>
  export default {
  }
</script>
