<template>
  <div class="app-container">
    <el-form inline>
      <el-form-item>
        <el-form-item label="访问率周期" label-width="120px" prop="rateUnit">
          <el-select v-model="rateUnit" placeholder="请选择" size="mini">
            <el-option
              v-for="item in timeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="响应延时周期" label-width="120px" prop="durationUnit">
          <el-select v-model="durationUnit" placeholder="请选择" size="mini">
            <el-option
              v-for="item in timeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-button type="primary" @click="fetchData" size="mini">刷新</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="list" @sort-change=sortChange v-loading.body="listLoading"  :max-height="tableHeight" element-loading-text="Loading" border fit highlight-current-row  :default-sort = "{prop: 'mean', order: 'descending'}">
      <el-table-column label="资源" width="120" align="center" >
        <template slot-scope="scope">
          {{scope.row.name}}
        </template>
      </el-table-column>
      <el-table-column label="编码" width="180" align="center">
        <template slot-scope="scope">
          <span>{{scope.row.code}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="'访问率(events/' + rateUnit + ')'" align="center">
        <el-table-column label="总数" width="90" sortable='custom' align="center" prop="count">
          <template slot-scope="scope">
            {{scope.row.count}}
          </template>
        </el-table-column>
        <el-table-column label="平均" width="110" sortable='custom' align="center" prop="meanRate">
          <template slot-scope="scope">
            {{formatNum(scope.row.meanRate)}}
          </template>
        </el-table-column>
        <el-table-column label="1 x" width="130" sortable='custom' align="center" prop="oneMinuteRate">
          <template slot-scope="scope">
            {{formatNum(scope.row.oneMinuteRate)}}
          </template>
        </el-table-column>
        <el-table-column label="5 x" width="130" sortable='custom' align="center" prop="fiveMinuteRate">
          <template slot-scope="scope">
            {{formatNum(scope.row.fiveMinuteRate)}}
          </template>
        </el-table-column>
        <el-table-column label="15 x" width="140" sortable='custom' align="center" prop="fifteenMinuteRate">
          <template slot-scope="scope">
            {{formatNum(scope.row.fifteenMinuteRate)}}
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column :label="'响应时间(' + durationUnit + ')'" align="center">
        <el-table-column label="最小" width="110" sortable='custom' align="center" prop="min">
          <template slot-scope="scope">
            {{formatNum(scope.row.min)}}
          </template>
        </el-table-column>
        <el-table-column label="最大" width="110" sortable='custom' align="center" prop="max">
          <template slot-scope="scope">
            {{formatNum(scope.row.max)}}
          </template>
        </el-table-column>
        <el-table-column label="平均" width="110" sortable='custom' align="center" prop="mean">
          <template slot-scope="scope">
            {{formatNum(scope.row.mean)}}
          </template>
        </el-table-column>
        <el-table-column label="stddev" width="110" sortable='custom' align="center" prop="stddev">
          <template slot-scope="scope">
            {{formatNum(scope.row.stddev)}}
          </template>
        </el-table-column>
        <el-table-column label="中位" width="110" sortable='custom' align="center" prop="median">
          <template slot-scope="scope">
            {{formatNum(scope.row.median)}}
          </template>
        </el-table-column>
        <el-table-column label="75%" width="110" sortable='custom' align="center" prop="scope">
          <template slot-scope="scope">
            {{formatNum(scope.row.p75)}}
          </template>
        </el-table-column>
        <el-table-column label="95%" width="110" sortable='custom' align="center" prop="p95">
          <template slot-scope="scope">
            {{formatNum(scope.row.p95)}}
          </template>
        </el-table-column>
        <el-table-column label="98%" width="110" sortable='custom' align="center" prop="p98">
          <template slot-scope="scope">
            {{formatNum(scope.row.p98)}}
          </template>
        </el-table-column>
        <el-table-column label="99%" width="110" sortable='custom' align="center" prop="p99">
          <template slot-scope="scope">
            {{formatNum(scope.row.p99)}}
          </template>
        </el-table-column>
        <el-table-column label="99.9%" width="110" sortable='custom' align="center" prop="p999">
          <template slot-scope="scope">
            {{formatNum(scope.row.p999)}}
          </template>
        </el-table-column>
      </el-table-column>
    </el-table>
    <base-pagination :total="total" v-on:change="fetchData" ref="pageinfo"></base-pagination>
  </div>
</template>

<script>
  import { getList } from '@/api/performance'
  import { roundNumber } from '@/utils/numutils'
  import BasePagination from '../base/BasePagination'

  export default {
    components: { BasePagination },
    data() {
      return {
        tableHeight: window.innerHeight - 240,
        timeOptions: [{
          value: 'nanoseconds',
          label: '纳秒'
        }, {
          value: 'microseconds',
          label: '微妙'
        }, {
          value: 'milliseconds',
          label: '毫秒'
        }, {
          value: 'seconds',
          label: '秒'
        }, {
          value: 'minutes',
          label: '分'
        }, {
          value: 'hours',
          label: '小时'
        }, {
          value: 'days',
          label: '天'
        }],
        list: null,
        rateUnit: 'seconds',
        durationUnit: 'milliseconds',
        sort: 'mean',
        isDesc: true,
        listLoading: true,
        total: 0
      }
    },
    mounted() {
      this.fetchData()
    },
    methods: {
      fetchData() {
        this.listLoading = true
        var params = this.$refs.pageinfo.getPageParam
        params.rateUnit = this.rateUnit
        params.durationUnit = this.durationUnit
        params.sort = this.sort
        params.isDesc = this.isDesc
        getList(params).then(response => {
          this.list = response.data.list
          this.total = response.data.total
          this.listLoading = false
        })
      },
      sortChange(column, prop, order) {
        this.sort = column.prop
        if (column.order === 'ascending') {
          this.isDesc = false
        } else {
          this.isDesc = true
        }
        this.fetchData()
      },
      formatNum(value) {
        return roundNumber(value, 6)
      }
    }
  }
</script>
