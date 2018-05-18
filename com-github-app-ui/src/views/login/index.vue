<template>
  <div class="login-container">
    <el-form autoComplete="on" :model="loginForm" :rules="loginRules" ref="loginForm" label-position="left" label-width="0px"
             class="card-box login-form">
      <h3 class="title">{{$t("message.title")}}</h3>

      <el-form-item prop="account">
        <span class="svg-container svg-container_login">
          <svg-icon icon-class="user" />
        </span>
        <el-input name="account" type="text" v-model="loginForm.account" autoComplete="on" :placeholder="this.$t('message.account')" />
      </el-form-item>

      <el-form-item prop="password">
            <span class="svg-container">
              <svg-icon icon-class="password"></svg-icon>
            </span>
        <el-input name="password" :type="pwdType" @keyup.enter.native="handleLogin" v-model="loginForm.password" autoComplete="on"  :placeholder="this.$t('message.password')"></el-input>
        <span class="show-pwd" @click="showPwd"><svg-icon icon-class="eye" /></span>
      </el-form-item>

      <el-row :gutter="5" v-show="isCaptchaShow">
        <el-col :span="8">
          <img :src="captchaUrl" style="width:100%;height:100%;border-radius:4px;margin-top: 5px;" @click="refreshCaptcha">
        </el-col>
        <el-col :span="16">
          <el-form-item prop="validateCode">
            <el-input name="validateCode" type="text" v-model="loginForm.validateCode" autoComplete="on" :placeholder="this.$t('message.captchaCode')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item>
        <el-button type="primary" style="width:100%;" :loading="loading" @click.native.prevent="handleLogin">{{$t("message.login")}}</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { isvalidUsername } from '@/utils/validate'

export default {
  name: 'login',
  computed: {
    ...mapGetters([
      'captchaModel',
      'captchaLength'
    ]),
    isCaptchaShow() {
      return this.captchaLength !== 0
    }
  },
  data() {
    const validateAccount = (rule, value, callback) => {
      if (!isvalidUsername(value)) {
        callback(new Error(this.$t('message.errMsgUserName')))
      } else {
        callback()
      }
    }
    const validatePass = (rule, value, callback) => {
      if (value.length < 5) {
        callback(new Error(this.$t('message.errMsgPwdLength')))
      } else {
        callback()
      }
    }
    const validateCode = (rule, value, callback) => {
      if (this.captchaLength === 0) {
        callback()
      }
      if (value.length !== this.captchaLength) {
        callback(new Error(this.$t('message.errMsgCaptchCodeLength').replace('length', this.captchaLength)))
      } else {
        if (this.captchaModel === 'number') {
          if (/^[0-9]+$/.test(this.loginForm.validateCode)) {
            callback()
          } else {
            callback(new Error(this.$t('message.errMsgCaptchMustNumber')))
          }
        } else if (this.captchaModel === 'letter') {
          if (/^[a-z]+$/.test(this.loginForm.validateCode)) {
            callback()
          } else {
            callback(new Error(this.$t('message.errMsgCaptchMustChar')))
          }
        } else if (this.captchaModel === 'varchar') {
          if (/^[a-z0-9]+$/.test(this.loginForm.validateCode)) {
            callback()
          } else {
            callback(new Error(this.$t('message.errMsgCaptchMustVarchar')))
          }
        }
      }
    }
    return {
      captchaUrl: '',
      loginForm: {
        account: '',
        password: '',
        validateCode: ''
      },
      loginRules: {
        account: [{ required: true, trigger: 'blur', validator: validateAccount }],
        password: [{ required: true, trigger: 'blur', validator: validatePass }],
        validateCode: [{ required: true, trigger: 'blur', validator: validateCode }]
      },
      loading: false,
      pwdType: 'password'
    }
  },
  mounted() {
    this.refreshCaptcha()
  },
  methods: {
    showPwd() {
      if (this.pwdType === 'password') {
        this.pwdType = ''
      } else {
        this.pwdType = 'password'
      }
    },
    refreshCaptcha() {
      this.captchaUrl = process.env.BASE_API + '/open/auth?' + Math.random()
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('Login', this.loginForm).then(() => {
            this.loading = false
            this.$router.push({ path: '/' })
          }).catch(() => {
            this.loading = false
            this.refreshCaptcha()
            this.loginForm.validateCode = ''
          })
        } else {
          return false
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  $bg:#2d3a4b;
  $dark_gray:#889aa4;
  $light_gray:#eee;

  .login-container {
    position: fixed;
    height: 100%;
    width:100%;
    background-color: $bg;
    input:-webkit-autofill {
      -webkit-box-shadow: 0 0 0px 1000px #293444 inset !important;
      -webkit-text-fill-color: #fff !important;
    }
    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
    }
    .el-input {
      display: inline-block;
      height: 47px;
      width: 85%;
    }
    .tips {
      font-size: 14px;
      color: #fff;
      margin-bottom: 10px;
    }
    .svg-container {
      padding: 6px 5px 6px 15px;
      color: $dark_gray;
      vertical-align: middle;
      width: 30px;
      display: inline-block;
      &_login {
        font-size: 20px;
      }
    }
    .title {
      font-size: 26px;
      font-weight: 400;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
    .login-form {
      position: absolute;
      left: 0;
      right: 0;
      width: 400px;
      padding: 35px 35px 15px 35px;
      margin: 120px auto;
    }
    .el-form-item {
      border: 1px solid rgba(255, 255, 255, 0.1);
      background: rgba(0, 0, 0, 0.1);
      border-radius: 5px;
      color: #454545;
    }
    .show-pwd {
      position: absolute;
      right: 10px;
      top: 7px;
      font-size: 16px;
      color: $dark_gray;
      cursor: pointer;
      user-select:none;
    }
    .thirdparty-button{
      position: absolute;
      right: 35px;
      bottom: 28px;
    }
  }
</style>
