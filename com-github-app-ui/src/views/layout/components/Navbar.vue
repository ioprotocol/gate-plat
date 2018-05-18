<template>
  <el-row type="flex" justify="start" align="middle" class="navbg">
    <el-col xs="12" sm="12" md="9" lg="8" xl="5">
      <div style="margin-left:20px;">
        <img :src="img_logo" style="float:left;display:block;width:125px;height:40px;margin-top:12px;"/><span class="title-font">{{$t("message.title")}}</span>
      </div>
    </el-col>
    <el-col xs="12" sm="12" md="9" lg="8" xl="5">
      <el-menu class="navbar navbg" mode="horizontal">
        <el-dropdown class="avatar-container" trigger="click">
          <div class="avatar-wrapper">
            <img class="user-avatar" :src="avatar" style="border-radius:40px">
            <i class="el-icon-caret-bottom"></i>
          </div>
          <el-dropdown-menu class="user-dropdown" slot="dropdown">
            <router-link class="inlineBlock" to="/">
              <el-dropdown-item>主页</el-dropdown-item>
            </router-link>
            <el-dropdown-item divided>
              <span @click="logout" style="display:block;">退出</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-menu>
    </el-col>
  </el-row>
</template>

<script>
import { mapGetters } from 'vuex'
import img_logo from '@/assets/logo.png'

export default {
  data() {
    return {
      img_logo
    }
  },
  computed: {
    ...mapGetters([
      'avatar'
    ])
  },
  methods: {
    logout() {
      this.$store.dispatch('LogOut').then(() => {
        location.reload() // 为了重新实例化vue-router对象 避免bug
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.title-font {
  font-size: 20px;
  color: white;
  font-weight:normal;
  font-family: "Helvetica Neue";
  height: 64px;
  line-height: 64px;
  padding-left:9px;
}
.navbg {
  background-color: #64a51a;
}
.navbar {
  height: 64px;
  line-height: 64px;
  border-radius: 0px !important;
  .screenfull {
    position: absolute;
    right: 64px;
    top: 16px;
    color: red;
  }
  .avatar-container {
    height: 64px;
    display: inline-block;
    position: absolute;
    right: 35px;
    .avatar-wrapper {
      cursor: pointer;
      margin-top: 12px;
      position: relative;
      .user-avatar {
        width: 40px;
        height: 40px;
        border-radius: 10px;
      }
      .el-icon-caret-bottom {
        position: absolute;
        color: white;
        right: -16px;
        top: 25px;
        font-size: 12px;
      }
    }
  }
}
</style>

