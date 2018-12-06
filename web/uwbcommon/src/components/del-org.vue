<template>
  <div class="del-org">
    <div class="box">
      <div class="head">
        <div class="title">删除部门</div>
        <div @click="close" class="close">x</div>
      </div>
      <div class="main">
        <div class="row">
          <!-- <span class="describe-one">部门名称</span>
          <input disabled="disabled" v-model="name" type="text" class="min-box"> -->
          <div class="text">是否删除{{name}}?</div>
        </div>
      </div>
      <div class="but">
        <button @click="delOrg" class="up">确定</button>
        <button @click="close">取消</button>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
$color: #59b3ff;
.del-org {
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
        height: 100px;
        margin-bottom: 19px;
        font-size: 24px;
        color: $color;
        text-align: center;
        line-height: 100px;
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
  width: 420px !important;
  color: #fff;
  background: rgba(0, 144, 236, 0.6);
  z-index: 1100;
}
</style>
<script>
export default {
  name: "del-org",
  props: {
    data: { type: Array },
    departmentCode: {type: String}
  },
  data() {
    return {
      name: '',
      parentCode: '',
    };
  },
  methods: {
    close() {
      this.$emit("closedel", false);
    },
    getParentCode (arr) {
      arr.forEach(e => {
        if (e.departmentCode == this.departmentCode) {
          this.parentCode = e.parentCode
          this.name = e.departmentName
        } else if(e.children) {
          this.getParentCode(e.children)
        }
      })
    },
    delOrg() {
      this.$http
        .Post(this.$url.delOrg, {
          departmentCode: this.departmentCode,
          parentCode: this.parentCode
        })
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
    this.getParentCode(this.data)
  }
};
</script>
