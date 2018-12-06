<template>
  <div class="upgrade">
    <div class="title">设备升级</div>
    <div class="row">
      <span class="edition">当前基站版本：{{baseName}}</span>
      <span class="time">上次升级时间：{{baseTime}}</span>
    </div>
    <div class="row">
      <span class="edition">当前标签版本：{{labelName}}</span>
      <span class="time">上次升级时间：{{labelTime}}</span>
    </div>
    <el-form :model="form" :rules="rules" ref="ruleForm" label-width="110px" class="demo-ruleForm">
      <el-form-item label="基站升级文件" prop="base">
        <el-select v-model="form.downPath" placeholder="请选择">
          <el-option  v-for="(item, index) in baseList" :key="index" :value="item.downPath" :label="item.name"></el-option>
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="标签升级文件" prop="label">
        <el-select v-model="form.label" placeholder="请选择">
          <el-option label="" value=""></el-option>
        </el-select>
      </el-form-item> -->
      <!-- <div class="site">
        <el-form-item label="服务器地址" prop="site">
          <el-input v-model="form.serverIP" :disabled="true"></el-input>
        </el-form-item>
      </div>
      <div class="site port">
        <el-form-item label="端口" prop="port">
          <el-input v-model="form.serverPort" :disabled="true"></el-input>
        </el-form-item>
      </div> -->
    </el-form>
    <button @click="submitForm('ruleForm')">确认升级</button>
  </div>
</template>
<style lang="scss" scoped>
  .upgrade{
    .title{
      margin-bottom: 20px;
    }
    .row{
      font-size: 14px;
      color: #59b3ff;
      margin: 10px 0;
      .edition{
        margin-right: 20px;
      }
    }
    .site{
      display: inline-block;
      width: 250px; 
    }
    .port{
      width: 205px;
      margin-left: -50px;
    }
    button{
      width: 70px;
      height: 31px;
      margin-top: 50px;
      margin-left: 340px;
      color: #fff;
      background: #0090ec;
      border: 1px solid #0090ec;
      cursor: pointer;
    }
  }
  .el-select{
    width: 300px;
  }
</style>
<script>
import {formattingTime} from '../../assets/js/formattingTime'
export default {
  name: 'upgrade',
  data () {
    // let regSite = (rule, value, callback) => {
    //   let reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
    //   if (!reg.test(value)) {
    //     return callback(new Error('请输入正确的IP地址'));
    //   }
    // }
    // let regPort = (rule, value, callback) => {
    //   let reg = /^([0-9]|[1-9]\d{1,3}|[1-5]\d{4}|6[0-5]{2}[0-3][0-5])$/
    //   if (!reg.test(value)) {
    //     return callback(new Error('请输入正确的端口号'));
    //   }
    // }
    return {
      form: {
        cmdDir: 'query',
        cmdType: 'upgrade',
        downPath: '',
        serverIP: '',
        serverPort: '',
        name: ''
      },
      baseName: '',
      baseTime: '',
      labelName: '',
      labelTime: '',
      baseList: [],
      rules: {
        // site: [
        //   { required: true, message: '请输入服务器地址', trigger: 'blur' },
        //   { validator: regSite, trigger: 'blur' }
        // ],
        // port: [
        //   { required: true, message: '请输入端口', trigger: 'blur' },
        //   { validator: regPort, trigger: 'blur' }
        // ]
      }
    }
  },
  methods: {
    submitForm(formName) {
      if (this.form.downPath === '') {
        this.$message({
          type: "error",
          message: '请选择升级文件'
        })
      } else {
        // this.$refs[formName].validate((valid) => {
        //   if (valid) {
        //     alert('submit!');
        //   } else {
        //     console.log('error submit!!');
        //     return false;
        //   }
        // })
        this.$http
          .Post(this.$url.upgrade, this.form)
          .then(res => {})
          .catch(rej => {
            if (rej.data.errcode === 10001) {
              this.$message({
                type: "success",
                message: '配置下发成功，设备开始升级'
              })
            } else {
              this.$message({
                type: "error",
                message: rej.data.msg
              })
            }
          })
      }
    },
  },
  created () {
    this.$http
      .Get(this.$url.version)
      .then(res => {})
      .catch(rej => {
        let data = rej.data.result
        this.baseName = data.ancVer
        this.baseTime = formattingTime(data.ancUpTime)
        this.labelName = data.tagVer
        this.labelTime = formattingTime(data.tagUpTime)
        this.form.serverIP = data.serverIP
        this.form.serverPort = data.serverPort
      })
    this.$http
      .Post(this.$url.allImg, {type: 1})
      .then(res => {})
      .catch(rej => {
        this.baseList = rej.data.result
      });
  },
  watch: {
    'form.downPath' () {
      if (this.form.downPath === '')  return
      this.baseList.forEach(e => {
        if (e.downPath === this.form.downPath) {
          this.form.name = e.name
        }
      })
    }
  }
}
</script>
