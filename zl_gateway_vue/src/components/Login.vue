<template>
  <div class="login">
    <el-form :model="account" status-icon :rules="rules" ref="account" label-width="70px" class="demo-ruleForm">
      <el-form-item label="账号" prop="username">
        <el-input type="text" v-model="account.username" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input type="password" v-model="account.password" auto-complete="off"  @keyup.enter.native="submitForm('account')"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('account')">登录</el-button>
        <el-button @click="resetForm('account')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import { API } from "../assets/api.js";
export default {
  data() {
    return {
      account: {
        username: "",
        password: ""
      },
      rules: {
        username: [
          { required: true, message: "请输入账号", trigger: "blur" },
          {
            min: 5,
            max: 16,
            message: "用户名长度 5 ~ 16 个字符",
            trigger: "blur"
          }
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            min: 6,
            max: 32,
            message: "密码长度 6 ~ 32 个字符",
            trigger: "blur"
          }
        ]
      }
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$http({
            method: "post",
            url: API.userLogin,
            data: this.account
          }).then(res => {
            let data = res.data;
            if (data.errcode == 10001) {
              sessionStorage.setItem('isLogin', true);
              this.$router.push({ name: "gateway" });
            } else {
              this.$message({
                message: data.msg,
                type: "error"
              });
            }
          });
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
};
</script>
<style>
.login {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
}
.demo-ruleForm {
  width: 400px;
  border: 1px solid #ccc;
  padding: 50px;
}
</style>
