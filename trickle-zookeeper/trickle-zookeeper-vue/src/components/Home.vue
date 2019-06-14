<template>
<el-container direction="vertical">
    <Header></Header>
    <Main></Main>
    <Footer></Footer>
</el-container>
</template>
<style>
  .el-container{}
  .el-header, .el-footer{line-height:60px;padding:0px;background: #2f2f2f}
  .el-header .logo{}
  .el-menu {border-right: none;}
</style>

<script>
  import Header from "@/components/common/Header"
  import Main from "@/components/common/Main"
  import Footer from "@/components/common/Footer"

  export default {
    components: {
      Header, Main, Footer
    }
  }
</script>

