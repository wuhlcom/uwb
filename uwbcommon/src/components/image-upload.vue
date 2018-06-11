<template>
  <div class="image-upload">
    <loading v-if="loading"></loading>
    <div @click="close" class="close">x</div>
    <div class="head">
      <div class="title">新增地图</div>
      <div class="upload-box" :class="{'border-red':fileValue}">
        导入图片
        <input ref="file" type="file" @change="handleFiles" class="upload">
      </div>
      <div class="name">
        地图名称：
        <input v-model="name" type="text" placeholder="请输入名称" :class="{'border-red':nameValue}">
      </div>
      <div class="size">
        <span>区域大小: </span>
        <label>长：<input v-model="mapWidth" type="text" placeholder="请输入长" :class="{'border-red':widthValue}"> 米</label>
        <label>宽：<input v-model="mapHeight" type="text" placeholder="请输入宽" :class="{'border-red':heightValue}"> 米</label>
      </div>
    </div>
    <div class="view">
      <span v-if="!show">图片预览区域</span>
      <img :src="imgUrl" v-if="show" :style="imgSize">
    </div>
    <div class="but">
      <button @click="upImg" class="up">确定</button>
      <button @click="close">取消</button>
    </div>
  </div>
</template>
<style lang="scss" scoped>
$color: #59b3ff;
.image-upload {
  position: relative;
  padding-top: 20px;
  background: url("../assets/img/map-bk.png");
  background-size: 100% 100%;
  font-size: 0;
  .close {
    position: absolute;
    top: 15px;
    right: 50px;
    width: 18px;
    height: 18px;
    font-size: 28px;
    color: $color;
    user-select: none;
    cursor: pointer;
  }
  .head {
    line-height: 30px;
    padding-left: 40px;
    .title {
      margin-bottom: 20px;
      margin-right: 45px;
      float: left;
      color: $color;
      font-size: 14px;
    }
    .upload-box {
      display: block;
      float: left;
      position: relative;
      width: 72px;
      height: 30px;
      line-height: 30px;
      text-align: center;
      font-size: 13px;
      color: $color;
      background: #0053c1;
      input {
        position: absolute;
        top: 0;
        left: 0;
        width: 80px;
        height: 30px;
        line-height: 30px;
        opacity: 0;
      }
    }
    .name {
      float: left;
      margin: 0 20px;
      font-size: 13px;
      input {
        width: 395px;
        height: 30px;
        background: #0075cb;
        padding-left: 5px;
        color: #fff;
      }
    }
    .size {
      float: left;
      font-size: 13px;
      label {
        margin-left: 10px;
        input {
          width: 70px;
          height: 30px;
          background: #0075cb;
          padding-left: 5px;
          color: #fff;
        }
      }
    }
  }
  .view {
    position: absolute;
    top: 50%;
    left: 50%;
    width: 700px;
    height: 410px;
    margin-top: -205px;
    margin-left: -350px;
    text-align: center;
    line-height: 510px;
    font-size: 36px;
    color: #fff;
    img {
      display: inline-block;
      margin: auto;
      vertical-align: middle;
    }
  }
  .but {
    position: absolute;
    bottom: 50px;
    left: 50%;
    width: 180px;
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
.border-red{
  border: 1px solid #ff0000;
}
input::-webkit-input-placeholder {
  color: red;
}
input::-moz-placeholder {
  /* Mozilla Firefox 19+ */
  color: red;
}
input:-moz-placeholder {
  /* Mozilla Firefox 4 to 18 */
  color: red;
}
input:-ms-input-placeholder {
  /* Internet Explorer 10-11 */
  color: red;
}
</style>
<script>
import loading from "../components/loading";
import { accDiv, accSub } from "../assets/js/parseFloat";
export default {
  name: "image-upload",
  components: {
    loading
  },
  data() {
    return {
      imgUrl: null,
      show: false,
      imgWidth: 0, // 图片的实际宽度
      imgHeight: 0, // 图片的实际高度
      imgSize: {
        width: "",
        height: ""
      },
      mapWidth: "",
      mapHeight: "",
      name: "",
      file: {},
      loading: false,
      fileValue: false,
      nameValue: false,
      widthValue: false,
      heightValue: false,
    };
  },
  methods: {
    close() {
      this.$store.state.nav.navShow = true;
      this.$emit("closes", false);
    },
    /* 选择图片之后对图片进行预览展示 */
    handleFiles() {
      let inputFile = this.$refs.file;
      if (!/\.(jpg|jpeg|png|JPG|PNG)$/.test(inputFile.value)) {    
        this.$message({
          type: "error",
          message: '图片类型必须是jpeg,jpg,png中的一种'
        });
        inputFile.value = "";    
        return false;    
      }  
      if (inputFile.files) {
        //读取图片数据
        this.file = inputFile.files[0];
        let reader = new FileReader();
        reader.onload = e => {
          let data = e.target.result;
          //加载图片获取图片真实宽度和高度
          let image = new Image();
          image.onload = () => {
            this.imgWidth = image.width;
            this.imgHeight = image.height;
          };
          image.src = data; // 必须要给image的src进行赋值，不然无法获取上传图片的尺寸
          this.imgUrl = data;
        };
        reader.readAsDataURL(this.file);
      } else {
        let image = new Image();
        image.onload = () => {
          let width = image.width;
          let height = image.height;
          let fileSize = image.fileSize;
        };
        image.src = inputFile.value;
        this.imgUrl = data;
      }
    },
    /* 图片上传 */
    upImg() {
      let param = new FormData(); // 创建form对象
      param.append("file", this.file, this.file.name); // 通过append向form对象添加数据
      param.append("length", this.mapWidth); // 添加form表单中其他数据
      param.append("width", this.mapHeight);
      param.append("picWidth", this.imgHeight);
      param.append("picLength", this.imgWidth);
      param.append("name", this.name);
      param.append('type', 0)
      if (!this.file.name){
        this.fileValue = true
        return
      }
      if (this.name === '') {
        this.nameValue = true
        return
      }
      if (this.mapWidth === '') {
        this.widthValue = true
        return
      }
      if (this.mapHeight === '') {
        this.heightValue = true
        return
      }
      // console.log(param.get('length')) // FormData私有类对象，访问不到，可以通过get判断值是否传进去
      let config = {
        headers: { "Content-Type": "multipart/form-data" }
      };
      this.loading = true;
      // 添加请求头
      this.$http
        .Post(this.$url.upImg, param, config)
        .then(response => {})
        .catch(rej => {
          if (rej.data.errcode === 10001) {
            this.loading = false;
            this.close();
          } else {
            this.$message({
              type: "error",
              message: '地图上传失败'
            })
            this.loading = false;
            this.close();
          }
        });
    }
  },
  watch: {
    imgUrl(val) {
      if (val) {
        this.show = true;
      }
    },
    imgWidth(msg) {
      let result = accDiv(msg, this.imgHeight).toFixed(2);
      if (result < 1.7 && result > 1) {
        this.imgSize.width = 100 - (1.7 - result) * 100 + "%";
      } else if (result <= 1) {
        this.imgSize.width = "";
        this.imgSize.height = "100%";
      } else if (result > 1.7) {
        this.imgSize.width = "100%";
      }
    },
    name (val) {
      this.nameValue = false
    },
    mapWidth () {
      this.widthValue = false
    },
    mapHeight () {
      this.heightValue = false
    },
    file () {
      this.fileValue = false
    }
  }
};
</script>
