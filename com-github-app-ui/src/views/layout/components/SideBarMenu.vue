<template>
  <el-menu :collapse="!sidebar.opened" class="side-menu">
    <template v-for="item in sideMenu" v-if="item.children">
      <router-link v-if="item.children.length===1 && !item.children[0].children" :to="item.path+item.children[0].path" :key="item.name">
        <el-menu-item :index="item.path+'/'+item.children[0].path">
          <svg-icon v-if="item.icon" :icon-class="item.icon" font-size="16px"></svg-icon>
          <span v-bind:class="{ 'hide-menu-name': !sidebar.opened, 'show-menu-name': sidebar.opened}" style="padding-left:16px;" v-if="item.name">{{item.name}}</span>
        </el-menu-item>
      </router-link>

      <el-submenu v-else :index="item.name||item.path" :key="item.name">
        <template slot="title">
          <svg-icon v-if="item.icon" :icon-class="item.icon" font-size="16px"></svg-icon>
          <span style="padding-left:16px;" v-if="item.name">{{item.name}}</span>
        </template>

        <template v-for="child in item.children" v-if="!child.hidden">
          <router-link :to="item.path+'/'+child.path" :key="child.name">
            <el-menu-item :index="item.path+'/'+child.path">
              <svg-icon v-if="child.icon" :icon-class="child.icon" font-size="16px"></svg-icon>
              <span style="padding-left:16px;" v-if="child.name">{{child.name}}</span>
            </el-menu-item>
          </router-link>
        </template>
      </el-submenu>
    </template>
  </el-menu>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'SideBarMenu',
  computed: {
    ...mapGetters([
      'sideMenu'
    ]),
    sidebar() {
      return this.$store.state.app.sidebar
    }
  }
}
</script>
<style>
  .side-menu {
    background-color: #FFF;
    unique-opened: true;
  }
  .hide-menu-name {
    visibility: hidden;
  }
  .show-menu-name {
    visibility: visible;
  }
</style>
