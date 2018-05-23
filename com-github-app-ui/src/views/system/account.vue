<template>
  <div class="app-container">

    <el-form inline>
      <el-form-item label="角色">
        <el-select v-model="roleId" placeholder="请选择" clearable size="mini">
          <el-option
            v-for="item in roles"
            :key="item.roleId"
            :label="item.name"
            :value="item.roleId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="关键词">
        <el-input size="mini" placeholder="帐号 姓名 电话 邮箱" clearable v-model="keyword"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchData" size="mini">查询</el-button>
        <el-button type="success" @click="addNew" size="mini">添加</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="list" v-loading.body="listLoading" element-loading-text="Loading" border fit highlight-current-row>
      <el-table-column label="帐号" width="180" align="center">
        <template slot-scope="scope">
          {{scope.row.account}}
        </template>
      </el-table-column>
      <el-table-column label="姓名" width="180" align="center">
        <template slot-scope="scope">
          {{scope.row.name}}
        </template>
      </el-table-column>
      <el-table-column label="角色" width="180" align="center">
        <template slot-scope="scope">
          {{scope.row.role.name}}
        </template>
      </el-table-column>
      <el-table-column label="性别" width="110" align="center">
        <template slot-scope="scope">
          <span>{{scope.row.sex}}</span>
        </template>
      </el-table-column>
      <el-table-column label="邮箱" width="220" align="center">
        <template slot-scope="scope">
          {{scope.row.email}}
        </template>
      </el-table-column>
      <el-table-column label="电话" width="120" align="center">
        <template slot-scope="scope">
          {{scope.row.mobile}}
        </template>
      </el-table-column>
      <el-table-column label="状态" width="110" align="center">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="light" content="点击更改帐号状态" placement="bottom">
            <el-button @click="changeStatus(scope.row)" type="text" size="mini">
              <font :color="paraseColorByStatus(scope.row.isEnable)">{{paraseStatus(scope.row.isEnable)}}</font>
            </el-button>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="地址" align="center">
        <template slot-scope="scope">
          {{scope.row.address}}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160" align="center">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="light" content="重置后密码与账号名相同" placement="bottom">
            <el-button @click="resetpwd(scope.row)" type="text" size="mini">重置密码</el-button>
          </el-tooltip>
          <el-button @click="edit(scope.row)" type="text" size="mini">编辑</el-button>
          <el-button @click="del(scope.row)" type="text" size="mini">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <base-pagination :total="total" v-on:change="fetchData" ref="pageinfo"></base-pagination>

    <el-dialog title="帐号信息" :visible.sync="editDialogVisible" width="30%" center>
      <el-form :model="account" :rules="rules" ref="editForm">
        <el-form-item label="姓名" label-width="120px" prop="name">
          <el-input v-model="account.name" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="帐号" label-width="120px" prop="account">
          <el-input v-model="account.account" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" label-width="120px" prop="email">
          <el-input v-model="account.email" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话" label-width="120px" prop="mobile">
          <el-input v-model="account.mobile" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="性别" label-width="120px">
          <el-radio v-model="account.sex" label="男">男</el-radio>
          <el-radio v-model="account.sex" label="女">女</el-radio>
        </el-form-item>
        <el-form-item label="角色" label-width="120px" prop="roleId">
          <el-select v-model="account.roleId" placeholder="请选择" clearable size="mini">
            <el-option
              v-for="item in roles"
              :key="item.roleId"
              :label="item.name"
              :value="item.roleId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" label-width="120px" prop="isEnable">
          <el-radio-group v-model="account.isEnable" >
            <el-radio :label="0">禁用</el-radio>
            <el-radio :label="1">启用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="地址" label-width="120px">
          <el-input v-model="account.address" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="备注" label-width="120px">
          <el-input v-model="account.remark" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelEdit">取 消</el-button>
        <el-button type="primary" @click="saveForm">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import { getList } from '@/api/account'
  import { changeAccountStatus } from '@/api/account'
  import { deleteAccountById } from '@/api/account'
  import { resetAccountPwdById } from '@/api/account'
  import { saveOrUpdate } from '@/api/account'
  import { getAllRoles } from '@/api/role'
  import BasePagination from '../base/BasePagination'
  import { Message, MessageBox } from 'element-ui'

  export default {
    components: { BasePagination },
    data() {
      return {
        list: [],
        listLoading: true,
        total: 0,
        keyword: '',
        roleId: '',
        roles: [],
        editDialogVisible: false,
        account: {},
        rules: {
          name: [
            { required: true, message: '请输入姓名', trigger: 'blur' },
            { min: 2, max: 8, message: '长度在 2 到 8 个字符', trigger: 'blur' }
          ],
          account: [
            { required: true, message: '请输入账号', trigger: 'blur' },
            { pattern: /^[a-zA-Z0-9]+$/, message: '账号必须由数字字母组成', trigger: 'blur' },
            { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
          ],
          email: [
            { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur,change' }
          ],
          mobile: [
            { type: 'string', pattern: /^1(3|4|5|7|8)\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
          ],
          roleId: [
            { required: true, message: '角色必须选择', trigger: 'blur' }
          ],
          isEnable: [
            { required: true, message: '状态必须选择', trigger: 'blur' }
          ]
        }
      }
    },
    mounted() {
      this.fetchData()
    },
    methods: {
      fetchData() {
        getAllRoles().then(rep => {
          this.roles = rep.data.list
        })
        this.listLoading = true
        var params = this.$refs.pageinfo.getPageParam
        params['keyword'] = this.keyword
        params['roleId'] = this.roleId
        getList(params).then(response => {
          this.list = response.data.list
          this.total = response.data.total
          this.listLoading = false
        })
      },
      paraseStatus(status) {
        if (status === 1) {
          return '已启用'
        } else {
          return '已禁用'
        }
      },
      paraseColorByStatus(status) {
        if (status === 1) {
          return '#67C23A'
        } else {
          return '#E6A23C'
        }
      },
      changeStatus(row) {
        changeAccountStatus(row).then(response => {
          if (row.isEnable === 1) {
            Message({
              message: '帐号禁用成功',
              type: 'success',
              duration: 3 * 1000
            })
          } else {
            Message({
              message: '帐号启用成功',
              type: 'success',
              duration: 3 * 1000
            })
          }
          this.fetchData()
        })
      },
      resetpwd(row) {
        resetAccountPwdById(row.accountId).then(response => {
          Message({
            message: '密码重置成功',
            type: 'success',
            duration: 3 * 1000
          })
          this.fetchData()
        })
      },
      edit(row) {
        this.account = row
        this.editDialogVisible = true
      },
      saveForm() {
        this.$refs['editForm'].validate((valid) => {
          if (valid) {
            saveOrUpdate(this.account).then(response => {
              this.editDialogVisible = false
              this.fetchData()
            })
          } else {
            return false
          }
        })
      },
      cancelEdit() {
        this.editDialogVisible = false
        this.$refs['editForm'].resetFields()
      },
      addNew() {
        this.account = { isEnable: 1, sex: '男' }
        this.editDialogVisible = true
      },
      del(row) {
        MessageBox.confirm('确认删除次记录？', '提示', {
          confirmButtonText: '继续',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteAccountById(row.accountId).then(response => {
            Message({
              message: '帐号删除成功',
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
