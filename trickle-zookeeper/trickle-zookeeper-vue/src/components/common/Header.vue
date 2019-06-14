<template>
  <el-header>
    <el-row>
      <el-col :span="3" class="logo">Logo</el-col>
      <el-col :span="6" :offset="15">
        <div class="" v-show="!isLogin">
          <router-link to="/">[登录]</router-link>
          <router-link to="/register">[注册]</router-link>
        </div>
        <div class="" v-show="isLogin">
          <router-link to="/logout">[退出]</router-link>
          <router-link to="/register">
            <img src=""/>
          </router-link>
        </div>
      </el-col>
    </el-row>
  </el-header>
</template>
<script>
  export default {
    data() {
      return {
        username: this.$store.getters.getUser,
        isLogin: this.$store.getters.isLogin
      }
    }
  }
</script>
