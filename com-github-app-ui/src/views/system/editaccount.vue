<template>
  <el-dialog title="帐号信息" :visible.sync="editDialogVisible" width="40%" center>
    <el-form :model="account" :rules="rules" ref="editForm" label-width="100px">
      <el-form-item label="姓名" label-width="120px" prop="name">
        <el-input v-model="account.name" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="帐号" label-width="120px" prop="account">
        <el-input v-model="account.account" auto-complete="off" disabled></el-input>
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
        <el-select v-model="account.roleId" placeholder="请选择" clearable size="mini" disabled>
          <el-option
            v-for="item in roles"
            :key="item.roleId"
            :label="item.name"
            :value="item.roleId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="状态" label-width="120px" prop="isEnable">
        <el-radio-group v-model="account.isEnable" disabled>
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
</template>

<script>
  import { updateAccount } from '@/api/account'
  import { getAllRoles } from '@/api/role'
  import { getInfo } from '@/api/login'
  import { Message } from 'element-ui'
  import store from '../../store'
  import Bus from '@/utils/bus'

  export default {
    data() {
      return {
        roles: [],
        account: {},
        editDialogVisible: false,
        rules: {
          name: [
            { required: true, message: '请输入姓名', trigger: 'blur' },
            { min: 2, max: 8, message: '长度在 2 到 8 个字符', trigger: 'blur' }
          ],
          email: [
            { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur,change' }
          ],
          mobile: [
            { type: 'string', pattern: /^1(3|4|5|7|8)\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
          ]
        }
      }
    },
    mounted() {
      this.fetchData()
      Bus.$on('accountSet', (e) => {
        this.editDialogVisible = true
      })
    },
    methods: {
      fetchData() {
        getAllRoles().then(rep => {
          this.roles = rep.data.list
        })
        getInfo().then(rep => {
          this.account = rep.data.account
        })
      },
      saveForm() {
        this.$refs['editForm'].validate((valid) => {
          if (valid) {
            updateAccount(this.account).then(rep => {
              Message({
                message: '账号信息修改成功',
                type: 'success',
                duration: 3 * 1000
              })
              store.dispatch('GetInfo')
              this.editDialogVisible = false
            })
          } else {
            return false
          }
        })
      },
      cancelEdit() {
        this.editDialogVisible = false
        this.$refs['editForm'].resetFields()
      }
    }
  }
</script>
