<template>
  <el-row type="flex" justify="end" align="middle" class="nav-bg">
    <el-col :xs="12" :sm="10" :md="9" :lg="8" :xl="5">
      <div style="margin-left:20px;">
        <img :src="img_logo" style="float:left;display:block;width:125px;height:40px;margin-top:12px;"/><span class="title-font">{{$t("message.title")}}</span>
      </div>
    </el-col>
    <el-col :xs="12" :sm="14" :md="15" :lg="16" :xl="19" >
      <el-row type="flex" justify="end" align="middle" ><el-col :span="24">
        <div style="margin-right:24px;margin-left:24px;height:64px;line-height:64px;float:right;text-align:center;">
          <el-dropdown :hide-on-click="false" style="float:right;">
            <span><svg-icon icon-class="mine" font-size="20px"/></span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>黄金糕</el-dropdown-item>
              <el-dropdown-item>狮子头</el-dropdown-item>
              <el-dropdown-item>螺蛳粉</el-dropdown-item>
              <el-dropdown-item divided>双皮奶</el-dropdown-item>
              <el-dropdown-item>蚵仔煎</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
        <el-menu mode="horizontal" text-color="#FFF" background-color="#64a51a" active-text-color="#d1dbe5" class="nav-menu" @select="menuClick">
          <el-menu-item index="1">首页</el-menu-item>
          <el-menu-item index="2">基础配置</el-menu-item>
          <el-menu-item index="3">数据接口</el-menu-item>
          <el-menu-item index="4">平台监控</el-menu-item>
          <el-menu-item index="5">统计分析</el-menu-item>
          <el-submenu index="6">
            <template slot="title">终端运营</template>
            <el-menu-item index="2-1">选项1</el-menu-item>
            <el-menu-item index="2-2">选项2</el-menu-item>
            <el-menu-item index="2-3">选项3</el-menu-item>
          </el-submenu>
        </el-menu>
      </el-col></el-row>
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
    },
    menuClick(index) {
      console.log('open:' + index)
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
.nav-bg {
  background-color: #64a51a;
}
.nav-menu {
  font-weight:bold;
  font-family: "Hiragino Sans GB";
  float: right;
  margin-top:1px!important;
  height:64px;
  line-height:64px;
}
</style>

