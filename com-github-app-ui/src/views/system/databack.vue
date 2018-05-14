<template>
  <div class="app-container">
    <el-row type="flex"  justify="end">
      <el-col :span="1">
        <el-button type="success" @click="backup" size="mini">备份</el-button>
      </el-col>
      <el-col :span="1">
        <el-button type="primary" @click="uploadSqlFile" size="mini">上传</el-button>
      </el-col>
      <el-col :span="1">
        <el-button type="warning" @click="sysPopedomRepaire" size="mini">资源修复</el-button>
      </el-col>
    </el-row>

    <el-table :data="list" v-loading.body="listLoading" element-loading-text="Loading" border fit highlight-current-row
              max-height="760" style="width: 100%">
      <el-table-column align="center" label='ID' width="95">
        <template slot-scope="scope">
          {{scope.$index}}
        </template>
      </el-table-column>
      <el-table-column label="文件名" width="180" align="center">
        <template slot-scope="scope">
          {{scope.row.name}}
        </template>
      </el-table-column>
      <el-table-column label="已用空间(字节)" align="center">
        <template slot-scope="scope">
          {{formatFileSize(scope.row.size/1024)}} Kb
        </template>
      </el-table-column>
      <el-table-column label="时间" align="center">
        <template slot-scope="scope">
          {{formattimeStr(scope.row.createTime)}}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160" align="center">
        <template slot-scope="scope">
          <el-button @click="restore(scope.row)" type="text" size="mini">恢复</el-button>
          <el-button @click="download(scope.row)" type="text" size="mini">下载</el-button>
          <el-button @click="del(scope.row)" type="text" size="mini">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <base-pagination :total="total" v-on:change="fetchData" ref="pageinfo"></base-pagination>
    <base-upload-file :uploadUrl="uploadUrl" v-on:onClose="fetchData" :extensions="fileType" ref="uploadfile"></base-upload-file>
  </div>
</template>

<script>
  import { deleteSqlFile, popedomrepaire, restoreDatabase, backupDatabase, getSqlFileList } from '@/api/sysdatabackup'
  import { formatMs2String } from '@/utils/timeutils'
  import BasePagination from '../base/BasePagination'
  import BaseUploadFile from '../base/BaseUploadFile'
  import { Message, MessageBox } from 'element-ui'

  export default {
    components: { BasePagination, BaseUploadFile },
    data() {
      return {
        fileType: 'sql',
        uploadUrl: '',
        list: [],
        listLoading: true,
        total: 0
      }
    },
    mounted() {
      this.fetchData()
      this.uploadUrl = process.env.BASE_API + '/api/sysbackup'
    },
    methods: {
      formatFileSize(size) {
        return Math.round(size * 100) / 100
      },
      fetchData() {
        this.listLoading = true
        var params = this.$refs.pageinfo.getPageParam
        getSqlFileList(params).then(response => {
          this.list = response.data.list
          this.total = response.data.total
          this.listLoading = false
        })
      },
      uploadSqlFile() {
        this.$refs.uploadfile.beginUpload()
      },
      formattimeStr(cellValue) {
        return formatMs2String(cellValue)
      },
      download(row) {
        window.open(process.env.BASE_API + '/open/download?fileName=' + row.name + '&code=' + row.code)
      },
      restore(row) {
        MessageBox.confirm('确认要恢复所选数据？', '提示', {
          confirmButtonText: '继续',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.listLoading = true
          restoreDatabase(row.name).then(response => {
            this.listLoading = false
            Message({
              message: '数据恢复成功',
              type: 'success',
              duration: 3 * 1000
            })
            this.fetchData()
          })
        })
      },
      sysPopedomRepaire() {
        MessageBox.confirm('确认要对系统资源进行修复？', '提示', {
          confirmButtonText: '继续',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.listLoading = true
          popedomrepaire().then(rep => {
            this.listLoading = false
            Message({
              message: '系统资源修复成功',
              type: 'success',
              duration: 3 * 1000
            })
          })
        })
      },
      backup() {
        this.listLoading = true
        backupDatabase().then(rep => {
          this.listLoading = false
          Message({
            message: '备份文件成功',
            type: 'success',
            duration: 3 * 1000
          })
          this.fetchData()
        })
      },
      del(row) {
        MessageBox.confirm('确认删除次记录？', '提示', {
          confirmButtonText: '继续',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteSqlFile(row.name).then(response => {
            Message({
              message: '删除文件成功',
              type: 'success',
              duration: 3 * 1000
            })
            this.fetchData()
          })
        })
      }
    }
  }
</script>
