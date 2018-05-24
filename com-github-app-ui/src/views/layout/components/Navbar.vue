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
          <el-dropdown size="small" @command="menuCommand">
            <span><svg-icon icon-class="mine" font-size="20px"/></span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="accountSet">账户设置</el-dropdown-item>
              <el-dropdown-item command="passwordEdit">修改密码</el-dropdown-item>
              <el-dropdown-item command="themeSet" divided>皮肤设置</el-dropdown-item>
              <el-dropdown-item command="exit">退出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
        <el-menu mode="horizontal" default-active="vuedashboard" text-color="#FFF" background-color="#64a51a" active-text-color="#ffd04b" class="nav-menu" @select="menuClick">
          <template v-for="item in navMenu">
            <el-menu-item :index="item.code">{{item.name}}</el-menu-item>
          </template>
        </el-menu>
      </el-col></el-row>
    </el-col>
  </el-row>
</template>

<script>
import { mapGetters } from 'vuex'
import img_logo from '@/assets/logo.png'
import { isItemInArray } from '@/utils/index'

export default {
  data() {
    return {
      img_logo
    }
  },
  mounted() {
    this.menuClick('vuedashboard')
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'codes'
    ]),
    navMenu() {
      var myArray = []
      var idx = 0
      for (idx = 0; idx < this.$router.options.routes.length; idx++) {
        if (this.$router.options.routes[idx].hasOwnProperty('code')) {
          if (isItemInArray(this.codes, this.$router.options.routes[idx].code)) {
            var menu = {}
            menu.name = this.$router.options.routes[idx].name
            menu.code = this.$router.options.routes[idx].code
            myArray.push(menu)
          }
        }
      }
      return myArray
    }
  },
  methods: {
    logout() {
      this.$store.dispatch('LogOut').then(() => {
        location.reload() // 为了重新实例化vue-router对象 避免bug
      })
    },
    menuCommand(cmd) {
      console.log(cmd)
    },
    menuClick(index) {
      var myArray = []
      var idx = 0
      for (idx = 0; idx < this.$router.options.routes.length; idx++) {
        if (this.$router.options.routes[idx].hasOwnProperty('code')) {
          if (index === this.$router.options.routes[idx].code) {
            var menu = {}
            menu.path = this.$router.options.routes[idx].path
            menu.component = this.$router.options.routes[idx].component
            menu.redirect = this.$router.options.routes[idx].redirect
            menu.name = this.$router.options.routes[idx].name
            menu.code = this.$router.options.routes[idx].code
            menu.icon = this.$router.options.routes[idx].icon
            if (this.$router.options.routes[idx].children.length <= 1) {
              menu.children = this.$router.options.routes[idx].children
              myArray.push(menu)
            } else {
              var childrens = []
              var cdx = 0
              for (cdx = 0; cdx < this.$router.options.routes[idx].children.length; cdx++) {
                if (isItemInArray(this.codes, this.$router.options.routes[idx].children[cdx].code)) {
                  childrens.push(this.$router.options.routes[idx].children[cdx])
                }
              }
              menu.children = childrens
              myArray.push(menu)
            }
          }
        }
      }
      this.$store.commit('SET_SIDE_MENU', myArray)
      if (index === 'vuedashboard') {
        this.$store.commit('CLOSE_SIDEBAR')
        this.$router.push({ path: '/' })
      }
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

