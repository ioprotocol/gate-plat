<template>
  <div class="app-container">
    <el-form inline>
      <el-form-item label="帐号">
        <el-input size="mini" clearable placeholder="帐号" v-model="account"/>
      </el-form-item>
      <el-form-item>
        <el-date-picker
          v-model="searchTime"
          type="datetimerange"
          :picker-options="pickerOptions"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          size="mini"
          align="right">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchData" size="mini">查询</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="danger" @click="deleteLog" size="mini">清空所有日志</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="list" v-loading.body="listLoading" element-loading-text="Loading" border fit highlight-current-row>
      <el-table-column label="时间" width="180" align="center">
        <template slot-scope="scope">
          {{formattimeStr(scope.row.logId)}}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" align="center" >
        <template slot-scope="scope">
          {{scope.row.code}}
        </template>
      </el-table-column>
      <el-table-column label="帐号" width="110" align="center">
        <template slot-scope="scope">
          <span>{{scope.row.opAccount}}</span>
        </template>
      </el-table-column>
      <el-table-column label="姓名" width="110" align="center">
        <template slot-scope="scope">
          {{scope.row.opName}}
        </template>
      </el-table-column>
      <el-table-column label="角色" width="110" align="center">
        <template slot-scope="scope">
          {{scope.row.roleName}}
        </template>
      </el-table-column>
      <el-table-column label="操作记录" align="center">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="light"  placement="right-start" :content="scope.row.description"><el-button>{{scope.row.description.substring(0, 130)}}</el-button></el-tooltip>
        </template>
      </el-table-column>
    </el-table>
    <base-pagination :total="total" v-on:change="fetchData" ref="pageinfo"></base-pagination>
  </div>
</template>

<script>
  import { getList } from '@/api/log'
  import { deleteAllLog } from '@/api/log'
  import { formatUs2String } from '@/utils/timeutils'
  import BasePagination from '../base/BasePagination'
  import { MessageBox } from 'element-ui'

  export default {
    components: { BasePagination },
    data() {
      return {
        list: null,
        listLoading: true,
        total: 0,
        account: '',
        searchTime: [],
        pickerOptions: {
          shortcuts: [{
            text: '最近一小时',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000)
              picker.$emit('pick', [start, end])
            }
          }, {
            text: '最近一天',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24)
              picker.$emit('pick', [start, end])
            }
          }, {
            text: '最近一周',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
              picker.$emit('pick', [start, end])
            }
          }]
        }
      }
    },
    mounted() {
      this.fetchData()
    },
    methods: {
      fetchData() {
        this.listLoading = true
        var params = this.$refs.pageinfo.getPageParam
        params['account'] = this.account
        if (this.searchTime !== null && this.searchTime.length !== 0) {
          params['startTime'] = new Date(this.searchTime[0]).getTime()
          params['endTime'] = new Date(this.searchTime[1]).getTime()
        }
        console.log(params)
        getList(params).then(response => {
          this.list = response.data.list
          this.total = response.data.total
          this.listLoading = false
        })
      },
      formattimeStr(cellValue) {
        return formatUs2String(cellValue)
      },
      deleteLog() {
        MessageBox.confirm('确认要清除全部日志？', '提示', {
          confirmButtonText: '继续',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteAllLog().then(response => {
            this.fetchData()
          })
        })
      }
    }
  }
</script>
