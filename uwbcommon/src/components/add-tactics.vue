<template>
  <div class="add-tactice">
    <div class="box">
      <div class="head">
        <div class="title">围栏策略</div>
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
            <el-form-item label="策略名称" prop="strategyName">
              <el-input v-model="form.strategyName" class="max-box"></el-input>
            </el-form-item>
          </div>
          <div class="row">
            <div class="left">
              <el-form-item label="策略行为" prop="forbidden">
                <el-select
                  v-model="form.forbidden"
                  placeholder="请选择"
                  class="med-box">
                    <el-option
                      v-for="item in behavior"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                    </el-option>
                </el-select>
              </el-form-item> 
            </div>
            <div class="left">
              <el-form-item label="告警级别" prop="level">
                <el-select
                  v-model="form.level"
                  placeholder="请选择"
                  class="med-box">
                    <el-option
                      v-for="item in level"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                    </el-option>
                </el-select>
              </el-form-item> 
            </div>
          </div>
          <div class="row">
            <div class="left">
              <el-form-item label="关联用户" prop="strategyUserId">
                <el-cascader
                  :options="dataArr"
                  v-model="strategyUserId"
                  :props="defaultProps"
                  change-on-select
                  :show-all-levels="false"
                  class="med-box">
                </el-cascader>
              </el-form-item>
            </div>
            <div class="left">
              <el-form-item label="有效区域" prop="fenceCode">
                <el-select
                  v-model="form.fenceCode"
                  placeholder="请选择"
                  class="med-box">
                  <el-option
                    v-for="item in area"
                    :key="item.fenceCode"
                    :label="item.fenceName"
                    :value="item.fenceCode">
                  </el-option>
                </el-select>
              </el-form-item>
            </div>
          </div>
          <div class="row">
            <div class="left">
              <el-form-item label="有效时间" prop="startTime">
                <el-time-picker
                  v-model="form.startTime"
                  value-format="HH:mm:ss"
                  placeholder="开始时间">
                </el-time-picker>
              </el-form-item>
            </div>
            <div class="left">
              <el-form-item prop="finishTime" :style="{'margin-left':'-50px'}">
                <el-time-picker
                  v-model="form.finishTime"
                  value-format="HH:mm:ss"
                  placeholder="结束时间">
                  </el-time-picker>
                </el-time-picker>
              </el-form-item>
            </div>
          </div>
          <div class="row">
            <el-form-item label="重复" prop="timeValues">
                <el-checkbox-group v-model="form.timeValues">
                  <el-checkbox
                    v-for="item in repetitionArr"
                    :label="item.value"
                    :key="item.value">
                      {{item.label}}
                    </el-checkbox>
                </el-checkbox-group>
              </el-form-item>
          </div>
          <div class="row">
            <el-form-item label="策略描述" prop="remark">
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
    width: 700px;
    height: 540px;
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
          width: 475px;
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
        available: 1,
        fenceCode: "",
        finishTime: "23:59:59",
        startTime: "00:00:00",
        strategyName: "",
        strategyUserId: "",
        level: 0,
        userType: 1,
        timeValues: ['1','2','3','4','5','6','0'],
        forbidden: 0
      },
      strategyUserId: "",
      repetition: "",
      level: [
        {
          label: "提示",
          value: 0
        },
        {
          label: "普通",
          value: 1
        },
        {
          label: "严重",
          value: 2
        }
      ],
      repetitionArr: [
        {
          label: "周一",
          value: '1'
        },
        {
          label: "周二",
          value: '2'
        },
        {
          label: "周三",
          value: '3'
        },
        {
          label: "周四",
          value: '4'
        },
        {
          label: "周五",
          value: '5'
        },
        {
          label: "周六",
          value: '6'
        },
        {
          label: "周日",
          value: '0'
        }
      ],
      behavior: [
        {
          label: "禁止进入",
          value: 0
        },
        {
          label: "禁止离开",
          value: 1
        }
      ],
      dataArr: [],
      defaultProps: {
        value: "departmentCode",
        label: "departmentName"
      },
      departmentCode: "",
      err: 0,
      area: [],
      rules: {
        strategyName: [
          { required: true, message: '请输入策略名称', trigger: 'blur' },
          // { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ],
        remark: [
          // { required: true, message: '请输入策略描述', trigger: 'blur' }
        ],
        forbidden: [
          { required: true, message: '请选择策略行为', trigger: 'change' }
        ],
        level: [
          { required: true, message: '请选择告警级别', trigger: 'change' }
        ],
        strategyUserId: [
          { required: true, message: '请选择关联用户', trigger: 'change' }
        ],
        fenceCode: [
          { required: true, message: '请选择有效区域', trigger: 'change' }
        ],
        startTime: [
          { required: true, message: '请输入开始时间', trigger: 'change' }
        ],
        finishTime: [
          { required: true, message: '请输入结束时间', trigger: 'change' },
          { validator: validatePass2, trigger: 'change' }
        ],
        timeValues:  [
          { type: 'array', required: true, message: '请选择至少一个重复时间', trigger: 'change' }
        ],
      },
    };
  },
  methods: {
    reset() {
      this.err = 0;
    },
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
        .Post(this.$url.fences, {})
        .then(res => {})
        .catch(rej => {
          this.area = rej.data.result;
        });
    },
    upData() {
      this.$http
        .Post(this.$url.strategyAdd, this.form)
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
  },
  watch: {
    strategyUserId(val) {
      this.form.strategyUserId = val[val.length - 1];
    }
  }
};
</script>
