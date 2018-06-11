<template>
  <div class="add-peole">
    <div class="box">
      <div class="head">
        <div class="title">新增人员</div>
        <div @click="close" class="close">x</div>
      </div>
      <div class="main">
        <el-form :model="form" :rules="rules" ref="form" label-width="100px" class="demo-ruleForm">
          <div class="row">
            <div class="left">
              <el-form-item label="工号" prop="personCode">
                <el-input v-model="form.personCode"></el-input>
              </el-form-item>
            </div>
            <div class="left">
              <el-form-item label="姓名" prop="personName">
                <el-input v-model="form.personName"></el-input>
              </el-form-item>
            </div>
          </div>
          <div class="row">
            <div class="left">
              <el-form-item label="手环ID" prop="tagId">
                <el-input v-model="form.tagId"></el-input>
              </el-form-item>
            </div>
            <div class="left">
              <el-form-item label="级别" prop="levelCode">
                <el-cascader
                  expand-trigger="hover"
                  :options="level"
                  v-model="levelCode"
                  :props="prop"
                  :show-all-levels="false">
                </el-cascader>
              </el-form-item>
            </div>
          </div>
          <div class="row">
            <div class="left">
              <el-form-item label="所属部门" prop="departmentCode">
                <el-cascader
                  expand-trigger="hover"
                  :options="dataArr"
                  v-model="departmentCode"
                  :props="defaultProps"
                  change-on-select
                  :show-all-levels="false">
                </el-cascader>
              </el-form-item>
            </div>
            <div class="left">
              <el-form-item label="职位" prop="positionCode">
                <el-select v-model="form.positionCode" placeholder="请选择">
              <el-option
                v-for="item in position"
                :key="item.positionCode"
                :label="item.positionName"
                :value="item.positionCode">
              </el-option>
            </el-select>
              </el-form-item>
            </div>
          </div>
          <div class="row">
            <el-form-item label="描述" prop="remark">
              <el-input
              type="textarea"
              resize="none"
              v-model="form.remark"
              class="max-box">
            </el-input>
            </el-form-item>
          </div>
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
.add-peole {
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
    width: 700px;
    height: 420px;
    margin-left: -325px;
    margin-top: -183px;
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
        position: relative;
        height: 62px;
        font-size: 13px;
        color: $color;
        .describe-one {
          display: inline-block;
          width: 56px;
          height: 30px;
          margin-right: 20px;
          line-height: 30px;
        }
        .describe-two {
          display: inline-block;
          width: 26px;
          height: 30px;
          margin-left: 39px;
          margin-right: 9px;
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
          width: 170px;
        }
        .max-box {
          width: 502px;
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
.el-tree {
  position: absolute !important;
  top: 0;
  right: 10px;
  width: 180px !important;
  color: #fff;
  background: rgba(0, 144, 236, 0.6);
  z-index: 1100;
}
.left{
  width: 300px;
  display: inline-block;
}
</style>
<script>
export default {
  name: "add-people",
  data() {
    var validatePass2 = (rule, value, callback) => {
      let z = /^[A-Za-z0-9]+$/
      if (!z.test(value)) {
        callback(new Error('工号只能为英文和数字'));
      } else {
        callback();
      }
    };  
    return {
      form: {
        departmentCode: "",
        personName: "",
        personCode: "",
        // birth: "",
        email: "",
        telephone: "",
        levelName: "",
        levelCode: "",
        positionName: "",
        positionCode: "",
        remark: "",
        sex: "",
        tagId: "",
        portrait: ""
      },
      defaultProps: {
        value: "departmentCode",
        label: "departmentName"
      },
      departmentCode: [],
      levelCode: [],
      dataArr: [],
      level: [],
      position: [],
      prop: {
        value: "levelCode",
        label: "levelName"
      },
      rules: {
        personCode: [
          { required: true, message: '请输入工号', trigger: 'blur' },
          { validator: validatePass2, trigger: 'blur' }
        ],
        personName: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        tagId: [
          // { required: true, message: '请选择策略行为', trigger: 'change' }
        ],
        levelCode: [
          // { required: true, message: '请选择告警级别', trigger: 'change' }
        ],
        departmentCode: [
          // { required: true, message: '请选择关联用户', trigger: 'change' }
        ],
        positionCode: [
          // { required: true, message: '请选择有效区域', trigger: 'change' }
        ],
        remark: [
          // { required: true, message: '请输入开始时间', trigger: 'blur' }
        ],
      },
    };
  },
  methods: {
    close() {
      this.$emit("closes", false);
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.upData()
        } else {
          return false;
        }
      });
    },
    getData() {
      this.$http
        .Get(this.$url.organization)
        .then(res => {})
        .catch(rej => {
          // 必须进转换，不然读取不到children属性
          if (!this.dataArr.length) {
            this.dataArr.push(JSON.parse(JSON.stringify(rej.data.result)));
          }
        });
      this.$http
        .Get(this.$url.level)
        .then(res => {})
        .catch(rej => {
          this.level = rej.data.result.levels;
          this.position = rej.data.result.positions;
        });
    },
    upData() {
      this.form.departmentCode = this.departmentCode[this.departmentCode.length-1];
      this.form.levelCode = this.levelCode[this.levelCode.length-1];
      this.$http
        .Post(this.$url.addPerson, this.form)
        .then(res => {})
        .catch(rej => {
          if (rej.data.errcode === 10001) {
            this.close()
          } else {
            this.$message({
              type: "error",
              message: rej.data.msg
            });
          }
        });
    }
  },
  created() {
    this.getData();
  }
};
</script>
