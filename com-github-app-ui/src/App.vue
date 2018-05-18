<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>

export default {
  name: 'app',
  mounted() {
    this.setTitle()
  },
  methods: {
    setTitle() {
      document.title = this.$t('message.title')
    }
  }
}
</script>
