<template>
  <div>
    <div class="pg-header"><navbar/></div>
    <div v-bind:class="{ 'pg-content': sidebar.opened, 'pg-content-collapse': !sidebar.opened }">
      <div v-bind:class="{ 'menu': sidebar.opened, 'menu-collapse': !sidebar.opened}" class="left"><side-bar-menu></side-bar-menu></div>
      <div v-bind:class="{'content': sidebar.opened, 'content-collapse': !sidebar.opened}" class="content left">
        <status-bar></status-bar>
        <app-main style="padding:1px;"></app-main>
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

<style>
  .left{
    float: left;
  }
  .pg-header{
    height: 64px;
  }
  .pg-content .menu {
    position: absolute;
    top:64px;
    left: 0;
    bottom: 0;
    width: 200px;
    background-color: #dddddd;
  }
  .content{
    position: absolute;
    top: 64px;
    right: 0;
    bottom: 0;
    left: 200px;
    overflow: hidden;
  }
  .pg-content-collapse .menu-collapse {
    position: absolute;
    top:64px;
    left: 0;
    bottom: 0;
    width: 64px;
    background-color: #dddddd;
  }
  .content-collapse {
    position: absolute;
    top: 64px;
    right: 0;
    bottom: 0;
    left: 64px;
    overflow: hidden;
  }
</style>
