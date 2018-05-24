<template>
  <div class="app-wrapper">
    <div style="height:64px;"><navbar/></div>
    <div style="position:absolute;top:64px;bottom:0;left:0;right:0;">
      <div v-bind:class="{ 'menu': sidebar.opened, 'menu-collapse': !sidebar.opened}"><side-bar-menu></side-bar-menu></div>
      <div v-bind:class="{'content': sidebar.opened, 'content-collapse': !sidebar.opened}">
        <div>
          <status-bar></status-bar>
          <app-main></app-main>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { Navbar, AppMain, StatusBar, SideBarMenu } from '@/views/layout/components'

export default {
  name: 'layout',
  components: {
    Navbar,
    AppMain,
    StatusBar,
    SideBarMenu
  },
  computed: {
    sidebar() {
      return this.$store.state.app.sidebar
    }
  }
}
</script>

<style rel="stylesheet/scss">
  .app-wrapper {
    @include clearfix;
    height: 100%;
    width: 100%;
    overflow: hidden;
  }
  .menu {
    position:absolute;
    top: 2px;
    left: 0;
    bottom: 0;
    width: 200px;
  }
  .menu-collapse {
    position:absolute;
    top: 2px;
    left: 0;
    bottom: 0;
    width: 64px;

  }
  .content{
    position:absolute;
    top: 2px;
    right: 0;
    bottom: 0;
    left: 200px;
    border-left:1px solid #E0E0E0;
  }
  .content-collapse {
    position:absolute;
    top: 2px;
    right: 0;
    bottom: 0;
    left: 64px;
    border-left:1px solid #E0E0E0;
  }
</style>
