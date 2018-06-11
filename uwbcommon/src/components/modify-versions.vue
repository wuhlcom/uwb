<template>
  <div class="map-versions">
    <div class="box">
      <div class="head">
        <div class="title">修改版本信息</div>
        <div @click="close" class="close">x</div>
      </div>
      <div class="main">
        <div class="row">
          <div class="row">
            <span class="describe-one">版本名称</span>
            <input v-model="info.name" type="text" class="max-box">
          </div>
        </div>
      </div>
      <div class="but">
        <button @click="upData" class="up">确定</button>
        <button @click="close">取消</button>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
$color: #59b3ff;
.map-versions {
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
        height: 30px;
        margin-bottom: 19px;
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
</style>
<script>
export default {
  name: "map-versions",
  props: {
    info: { type: Object }
  },
  methods: {
    close() {
      this.$emit("closeModify", false);
    },
    upData() {
      this.$http
        .Post(this.$url.changeImg, {name: this.info.name, id: this.info.id})
        .then(res => {})
        .catch(rej => {
          if (rej.data.errcode === 10001) {
            this.close()
            this.$message({
              type: "success",
              message: "修改成功!"
            })
          } else {
            this.$message({
              type: "error",
              message: rej.data.msg
            })
          } 
        })
    }
  }
};
</script>
