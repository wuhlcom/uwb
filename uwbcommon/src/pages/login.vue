<template>
  <div class="login" id="login">
    <div class="title">PoleStar™位置服务平台</div>
    <div class="login-box">
      <div class="login-title">欢迎登陆</div>
      <el-form :model="ruleForm2" status-icon :rules="rules2" ref="ruleForm2" label-width="0" class="demo-ruleForm">
        <el-form-item prop="username">
          <i class="iconfont icon-quanyonghu"></i>
          <input type="text" v-model="ruleForm2.username" placeholder="请输入账号" auto-complete="off"></input>
        </el-form-item>
        <el-form-item  prop="password">
          <i class="iconfont icon-mima"></i>
          <input type="password" v-model="ruleForm2.password" placeholder="请输入密码" auto-complete="off" @keyup.enter="submitForm('ruleForm2')"></input>
        </el-form-item>
        <el-checkbox v-model="rememberPassword">记住密码</el-checkbox>
        <el-form-item>
          <button type="button" @click="submitForm('ruleForm2')">登陆</button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.login{
  position: relative;
  width: 100%;
  height: 100%;
  background: url("../assets/img/login_bg.jpg") no-repeat;
  font-size: 0;
  .title{
    position: absolute;
    top: 30%;
    left: 10%;
    font-size: 36px;
    color: #fff;
  }
  .login-box{
    position: absolute;
    top: 20%;
    right: 100px;
    width: 430px;
    height: 393px;
    padding: 60px 60px;
    z-index: 1000;
    background: url('../assets/img/lg-bg.png') top center no-repeat;
    .login-title{
      width: 100%;
      height: 40px;
      margin-bottom: 20px;
      text-align: center;
      line-height: 40px;
      font-size: 24px;
      color: #fff;
    }
    input{
      width: 100%;
      height: 40px;
      padding-left: 30px; 
      color: #fff;
      background: rgba($color: #0069f7, $alpha: 0.5)
    }
    button{
      width: 100%;
      height: 40px;
      margin-top: 10px;
      font-size: 16px;
      background: #0069f7;
      color: #fff;
      border: none;
    }
  }
}
.el-form-item{
  margin-bottom: 35px;
}
.iconfont{
  position: absolute;
  left: 5px;
  color: #fff;
  font-size: 20px;
}
.el-checkbox__input{
  vertical-align: middle;
}
</style>
<script>
import g from '../assets/js/global'
import {setCookie, getCookie, delCookie} from '../assets/js/cookie'
export default {
  name: "login",
  data() {
      return {
        rememberPassword: false,
        isEncryption: 0,
        ruleForm2: {
          username: '',
          password: '',
        },
        rules2: {
          username: [
            { required: true, message: '请输入账号', trigger: 'blur' }
          ],
          password: [
            { required: true, message: '请输入密码', trigger: 'blur' }
          ],
        }
      };
  },
  methods: {
    evn_init() {
      const SEPARATION = 100
      const AMOUNTX = 100
      const AMOUNTY = 50
      let container
      let camera, scene, renderer
      let particles
      let particle
      let count = 0
      let mouseX = 85
      let mouseY = -600;
      container = document.createElement("div");
      container.setAttribute("id", "evncavas");
      container.style.position = "absolute";
      container.style.height = "100%";
      container.style.bottom = "0px";
      container.style.backgroundColor = "rgba(255, 255, 255, 0)";
      document.getElementById('login').appendChild(container);
      // document.body
      camera = new THREE.PerspectiveCamera(
        120,
        window.innerWidth / window.innerHeight,
        1,
        10000
      );
      camera.position.z = 1000;
      scene = new THREE.Scene();
      particles = new Array();
      let PI2 = Math.PI * 2;
      let material = new THREE.ParticleCanvasMaterial({
        color: 0xe1e1e1,
        program: function(context) {
          context.beginPath();
          context.arc(0, 0, 0.6, 0, PI2, true);
          context.fill();
        }
      });
      let i = 0;
      for (let ix = 0; ix < AMOUNTX; ix++) {
        for (let iy = 0; iy < AMOUNTY; iy++) {
          particle = particles[i++] = new THREE.Particle(material);
          particle.position.x = ix * SEPARATION - AMOUNTX * SEPARATION / 2;
          particle.position.z = iy * SEPARATION - AMOUNTY * SEPARATION / 2;
          scene.add(particle);
        }
      }
      renderer = new THREE.CanvasRenderer();
      renderer.setSize(window.innerWidth, window.innerHeight);
      container.appendChild(renderer.domElement);
      function animate() {
        requestAnimationFrame(animate);
        render();
      }
      function render() {
        camera.position.x += mouseX - camera.position.x;
        camera.position.y += -mouseY - camera.position.y;
        camera.lookAt(scene.position);
        let i = 0;
        for (let ix = 0; ix < AMOUNTX; ix++) {
          for (let iy = 0; iy < AMOUNTY; iy++) {
            particle = particles[i++];
            particle.position.y =
              Math.sin((ix + count) * 0.3) * 100 +
              Math.sin((iy + count) * 0.5) * 100;
            particle.scale.x = particle.scale.y =
              (Math.sin((ix + count) * 0.3) + 1) * 2 +
              (Math.sin((iy + count) * 0.5) + 1) * 2;
          }
        }
        renderer.render(scene, camera);
        count += 0.1;
      }
      animate();
    },
    login () {
      this.preserve()
      let loginInfo = {
        username: this.ruleForm2.username,
        password: g.encode64(this.ruleForm2.password)
      }
      this.$http
      .Post(this.$url.login, loginInfo)
      .then(res => {})
      .catch(rej => {
        if (rej.data.errcode === 10001) {
          sessionStorage.setItem('username', rej.data.result.username)
          sessionStorage.setItem('token', rej.data.result.token)
          sessionStorage.setItem('menus', JSON.stringify(rej.data.result.menus))
          this.$router.push({name: 'home'})
        } else {
          this.$message({
          type: "error",
          message: rej.data.msg
        });
        }
      })
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.login()
        } else {
          return false
        }
      });
    },
    preserve () {
      if(this.rememberPassword){
         setCookie('username', this.ruleForm2.username, 30)
         setCookie('password', this.ruleForm2.password, 30)
         setCookie('isencryption', 1, 30)
      } else {
        delCookie('username')
        delCookie('password')
        delCookie('isencryption')
      }
    }
  },
  mounted () {
    this.evn_init()
    this.ruleForm2.username = getCookie('username')
    this.ruleForm2.password = getCookie('password')
    if (getCookie('isencryption') !== null) {
      this.isEncryption = getCookie('isencryption')
      this.rememberPassword = true
    } else {
      this.isEncryption = 0
      this.rememberPassword = false
    }
  },
};
</script>
