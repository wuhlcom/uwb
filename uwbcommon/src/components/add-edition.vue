<template>
  <div class="add-tactice">
    <div class="box">
      <div class="head">
        <div class="title">增加版本文件</div>
        <div @click="close" class="close">x</div>
      </div>
      <div class="main">
        <el-form
          :model="form"
          :rules="rules"
          ref="form"
          label-width="100px"
          class="demo-ruleForm">
          <div class="row">
            <el-form-item label="版本名称" prop="name">
              <el-input v-model="form.name" class="max-box"></el-input>
            </el-form-item>
          </div>
          <div class="row">
            <el-form-item label="版本类型" prop="type">
              <el-radio-group v-model="form.type">
                <el-radio label="1">基站文件</el-radio>
                <!-- <el-radio label="2">标签文件</el-radio> -->
              </el-radio-group>
            </el-form-item>
          </div>
          <el-upload
            class="upload-demo"
            ref="upload"
            :limit="1"
            :data="{name: form.name, type: form.type}"
            :action="this.$url.upImg"
            :on-success="close"
            :file-list="fileList"
            :on-change="fileStatus"
            :on-error="upError"
            :before-upload="beforeAvatarUpload"
            :auto-upload="false">
            <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
            <div slot="tip" class="el-upload__tip">只能上传.bin文件</div>
          </el-upload>
        </el-form>
      </div>
      <div class="but">
        <button @click="submitForm('form')" class="up">确定</button>
        <button @click="close">取消</button>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
$color: #59b3ff;
.add-tactice {
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  width: 100%;
  background: rgba(0, 4, 41, 0.7);
  z-index: 1000;
  .box {
    position: absolute;
    top: 50%;
    left: 50%;
    width: 520px;
    height: 400px;
    margin-left: -325px;
    margin-top: -229px;
    background: rgba(0, 114, 211, 0.64);
    .head {
      display: flex;
      justify-content: space-between;
      height: 67px;
      padding: 0 29px 0 39px;
      margin-bottom: 8px;
      line-height: 67px;
      .title {
        font-size: 16px;
        color: #fff;
      }
      .close {
        font-size: 24px;
        color: $color;
        user-select: none;
        cursor: pointer;
      }
    }
    .main {
      padding: 0 40px;
      .row {
        height: 62px;
        font-size: 13px;
        color: $color;
        .describe-one {
          display: inline-block;
          width: 58px;
          height: 30px;
          margin-right: 18px;
          line-height: 30px;
        }
        .describe-two {
          display: inline-block;
          width: 35px;
          height: 30px;
          margin-left: 3px;
          margin-right: 2px;
          line-height: 30px;
        }
        input {
          height: 30px;
          padding-left: 11px;
          color: #fff;
          background: rgba(0, 144, 236, 0.6);
          border: 1px solid #2088d7;
        }
        .min-box {
          width: 100px;
        }
        .max-box {
          width: 300px;
        }
        .med-box {
          width: 170px;
        }
      }
    }
    .but {
      position: absolute;
      bottom: 50px;
      left: 50%;
      width: 190px;
      margin-left: -90px;
      button {
        width: 70px;
        height: 31px;
        margin-right: 20px;
        border: 1px solid #016e9c;
        color: $color;
        background: #014c9b;
      }
      .up {
        color: #fff;
        background: #0090ec;
        border: 1px solid #0090ec;
      }
    }
  }
}
.repetition {
  width: 130px;
}
.left{
  width: 300px;
  display: inline-block;
}
.upload-demo{
  margin-left: 30px;
}
.el-button--primary{
  border: none;
  font-size: 14px;
}
.el-upload__tip{
  color: #fff;
}

</style>
<script>
export default {
  name: "add-tactice",
  data() {
    var validatePass2 = (rule, value, callback) => {
       if (value < this.form.startTime) {
        callback(new Error('结束时间不能小于开始时间'));
      } else {
        callback();
      }
    };
    return {
      form: {
        name: '',
        type: '1'
      },
      fileList: [],
      fileArr: [],
      rules: {
        name: [
          { required: true, message: '请输入版本名称', trigger: 'blur' },
        ],
        type:  [
          { required: true, message: '请选择一个版本类型', trigger: 'change' }
        ],
      },
    };
  },
  methods: {
    fileStatus (file, fileList) {
      this.fileArr = fileList
    },
    close() {
      this.$emit("closes", false);
    },
    submitForm(formName) {
      if (this.fileArr.length){
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.submitUpload()
          } else {
            return false;
          }
        });
      } else {
        this.$message({
          type: "error",
          message: "请选择升级文件"
        });
      }
    },
    submitUpload () {
      this.$refs.upload.submit();
    },
    beforeAvatarUpload (file) {
      const isBIN = file.name.split('.')[1] === 'bin'
      if (!isBIN) {
        this.$message.error('上传文件只能是 bin 格式!')
      }
      return isBIN
    },
    upError (err, file, fileList) {
      
    }
  },
  watch: {
    strategyUserId(val) {
      this.form.strategyUserId = val[val.length - 1];
    }
  }
};
</script>
