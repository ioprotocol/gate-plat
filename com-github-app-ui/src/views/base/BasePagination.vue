<template>
  <el-pagination
    @size-change="handleSizeChange"
    @current-change="handleCurrentChange"
    :current-page="getCurrentPage"
    :page-sizes="[20, 30, 50, 80, 100]"
    :page-size="getRows"
    layout="total, sizes, prev, pager, next, jumper"
    :total="total">
  </el-pagination>
</template>

<script>
  export default {
    name: 'BasePagination',
    data() {
      return {
        currentPage: 1,
        rows: 20
      }
    },
    props: {
      total: {
        type: Number
      }
    },
    computed: {
      getRows: function() {
        return this.rows
      },
      getCurrentPage: function() {
        return this.currentPage
      },
      getOffset: function() {
        return (this.currentPage - 1) * this.rows
      },
      getPageParam: function() {
        var obj = {}
        obj['offset'] = (this.currentPage - 1) * this.rows
        obj['rows'] = this.rows
        return obj
      }
    },
    methods: {
      handleSizeChange(val) {
        this.rows = val
        this.$emit('change')
      },
      handleCurrentChange(val) {
        this.currentPage = val
        this.$emit('change')
      }
    }
  }
</script>
