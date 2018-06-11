<template>
  <div class="loading-box">
    <div class="loading">
      <span></span>
      <span></span>
      <span></span>
      <span></span>
      <span></span>
      <div class="text">
        图片上传中
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.loading-box{
  position: absolute;
  width: 100%;
  height: 100%;
  font-size: 13px;
  z-index: 1100;
}
.text{
  margin-top: 10px; 
  text-align: center;
  color: lightgreen;
}
.loading {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 150px;
  height: 15px;
  margin-left: -75px;
  margin-top: -30px;
}
.loading span {
  display: inline-block;
  width: 15px;
  height: 100%;
  margin-right: 5px;
  background: lightgreen;
  -webkit-transform-origin: right bottom;
  -webkit-animation: load 1s ease infinite;
}
.loading span:last-child {
  margin-right: 0px;
}
@-webkit-keyframes load {
  0% {
    opacity: 1;
  }
  100% {
    opacity: 0;
    -webkit-transform: rotate(90deg);
  }
}
.loading span:nth-child(1) {
  -webkit-animation-delay: 0.13s;
}
.loading span:nth-child(2) {
  -webkit-animation-delay: 0.26s;
}
.loading span:nth-child(3) {
  -webkit-animation-delay: 0.39s;
}
.loading span:nth-child(4) {
  -webkit-animation-delay: 0.52s;
}
.loading span:nth-child(5) {
  -webkit-animation-delay: 0.65s;
}
</style>
<script>
export default {
  name: "loading"
};
</script>
