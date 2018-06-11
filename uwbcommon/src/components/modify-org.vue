<template>
  <div class="modify-org">
    <div class="box">
      <div class="head">
        <div class="title">修改部门信息</div>
        <div @click="close" class="close">x</div>
      </div>
      <div class="main">
        <div class="row">
          <span class="describe-one must">部门名称</span>
          <input @blur="nameRepetition()" v-model="oneselfName" type="text" class="max-box">
          <span v-if="hint" class="hint">部门名称不能为空</span>
        </div>
        <div class="row" v-if="departmentCode !== '100'">
          <span class="describe-one">上级部门</span>
          <input disabled="disabled" v-model="superiorName" type="text" class="min-box">
          <el-tree :data="data" :props="defaultProps" :expand-on-click-node="false" accordion @node-click="handleNodeClick">
          </el-tree>
        </div>
        <div class="row">
          <span class="describe-one">添加人员</span>
          <el-select
            v-model="value"
            multiple
            collapse-tags
            placeholder="请选择">
            <el-option v-for="item in personnel" :key="item.personCode" :label="item.personName" :value="item.personCode">
            </el-option>
          </el-select>
        </div>
        <div class="row">
          <span class="describe-one">部门描述</span>
          <input v-model="remark" type="text" class="max-box">
        </div>
      </div>
      <div class="but">
        <button @click="modifyOrg" class="up">确定</button>
        <button @click="close">取消</button>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
$color: #59b3ff;
.modify-org {
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
    width: 650px;
    height: 366px;
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
      padding: 0 70px;
      .row {
        position: relative;
        height: 30px;
        margin-bottom: 19px;
        font-size: 13px;
        color: $color;
        .describe-one {
          display: inline-block;
          width: 60px;
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
          width: 422px;
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
.hint{
  margin-left: 80px;
  color: #f00;
}
</style>
<script>
export default {
  name: "modify-org",
  props: {
    data: { type: Array },
    departmentCode: {type: String}
  },
  data() {
    return {
      oneselfName: '',
      superiorName: '',
      parentCode: '',
      personnel: [],
      value: [],
      remark: '',
      defaultProps: {
        children: "children",
        label: "departmentName"
      },
      hint: false
    };
  },
  methods: {
    close() {
      this.$emit("closemodify", false);
    },
    nameRepetition(arr) {
      if (this.oneselfName === '') {
        this.hint = true
      }
    },  
    handleNodeClick(data) {
      this.superiorName = data.departmentName
      this.parentCode = data.departmentCode
    },
    getOneselfName (arr) {
      arr.forEach(e => {
        if (e.departmentCode === this.departmentCode) {
          this.oneselfName = e.departmentName
          this.parentCode = e.parentCode
          this.remark = e.remark
        } else if (e.children) {
          this.getOneselfName(e.children)
        }
      })
    },
    getParentName (arr) {
       arr.forEach(e => {
        if (e.departmentCode == this.parentCode) {
          this.superiorName = e.departmentName
        } else if (e.children) {
          this.getParentName(e.children)
        }
      })
    },
    getData() {
      this.$http
        .Get(this.$url.topPersons)
        .then(res => {})
        .catch(rej => {
          if (rej.data.errcode === 10001) {
            this.personnel = rej.data.result;
          }
        });
    },
    modifyOrg () {
      let obj
      if (this.departmentCode === '100') {
        obj = {
          departmentCode: this.departmentCode,
          departmentName: this.oneselfName,
          parentCode: '',
          persons: this.value,
          remark: this.remark
        }
      } else {
        obj = {
          departmentCode: this.departmentCode,
          departmentName: this.oneselfName,
          parentCode: this.parentCode,
          persons: this.value,
          remark: this.remark
        }
      }
      this.$http.Post(this.$url.modifyOrg, obj)
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
      })
    }
  },
  created () {
    this.getOneselfName(this.data)
    this.getParentName(this.data)
    this.getData()
  },
  watch: {
    oneselfName (val) {
      if (val !== '') {
        this.hint = false
      }
    }
  }
};
</script>
