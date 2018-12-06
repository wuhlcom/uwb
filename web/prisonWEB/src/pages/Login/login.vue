<template>
  <div class="login">
    <v-header :actShow="false" class="header"></v-header>
    <div class="main">
      <div class="login-window">
        <div class="log-img">
          <img src="./left.png">
        </div>
        <div class="login-info">
          <h1>欢迎登陆</h1>
          <form action="">
            <div class="zhanghao">
              <div class="icon"><i class="iconfont icon-icon14"></i></div>
              <input v-model="zhanghao" type="text" placeholder="请输入您的账号">  
            </div>
            <div class="mima">
              <div class="icon"><i class="iconfont icon-mima"></i></div>
              <input type="password" style="display:none;width:0;height:0;">
              <input v-model="mima"
              data-placeholder="请输入您的密码" 
              name="password"
              type="password"
              placeholder="请输入您的密码">
            </div>
            <div class="hint">
              <div v-show="prompting">
                <i class="iconfont icon-baocuo2"></i>
                <span>账号或密码错误</span>
              </div>
            </div>
            <div class="mima-option">
              <div class="remember" @click="storage()">
                <span class="case">
                  <img v-if="!remember" src="./no.png">
                  <img v-if="remember" src="./ok.png" alt="">
                </span>
                <span>记住密码</span>
              </div>
              <div class="forget-password">
                <router-link to="/">忘记密码?</router-link>
              </div>
            </div>
            <div @click="login()" class="buttom">
              登录
            </div>
            <div class="register">
              <router-link to="/">立即注册</router-link>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped> 
  .login{
    position: relative;
    height: 100%;
    min-height: 650px;
    .header{
      display: flex;
      align-items: center;
      height: 60px;
      min-height: 40px;
    }
    .main{
      position: absolute;
      top: 60px;
      left: 0;
      bottom: 0;
      width: 100%;
      border-top: 1px solid #4ab0fb;
      background: #409eff url('./footer.png') bottom no-repeat;    
      .login-window{
        position: absolute;
        top: 50%;
        left: 50%;
        margin-left: -525px;
        margin-top: -270px;
        width: 1050px;
        height: 390px;
        .log-img{
          display: inline-block;
          vertical-align: top;
        }
        .login-info{
          display: inline-block;
          width: 400px;
          height: 390px;
          text-align: center;
          background: #fff;
          h1{
            margin: 49px 0 30px;
            font-size: 26px;
            color: #5a5e66;
          }
          form{
            margin: 0 auto;
            width: 300px;
            height: 226px;
            div{
              width: 100%;
              height: 46px;
              line-height: 46px;
              border: 1px solid #c4c4c4;
              font-size: 16px;
              .icon{
                display: inline-block;
                float: left;
                width: 49px;
                height: 44px;
                border: none;
                border-right: 1px solid #c4c4c4;
                i{
                  font-size: 28px;
                }
              }
              input{
                width: 229px;
                height: 30px;
                vertical-align: middle;
                font-size: 14px;
                border:0;
                outline:none;
              }
            }
            .zhanghao{
              margin-bottom: 20px;
            }
            .hint{
              margin-top: 10px;
              height: 25px;
              line-height: 25px;
              text-align: left;
              border: none;
              >div{
                padding: 0 15px; 
                height: 25px;
                line-height: 25px;
                color: #f00;
                i{
                  font-size: 20px;
                  vertical-align: top;
                }
                span{
                  font-size: 12px;
                  vertical-align: top;
                }
              }
            }
            .mima-option{
              display: flex;
              justify-content: space-between;
              border: none;
              .remember{
                display: flex;
                border: none;
                font-size: 14px;
                .case{  
                  margin-right: 5px;
                  img{
                    vertical-align: middle;
                  } 
                }
                span{
                  vertical-align: middle;
                  color: #808080;
                }
              }
              .forget-password{
                display: flex;
                justify-content: flex-end;
                font-size: 14px;
                border: none;
              }
              .remember:hover{
                cursor:pointer;
              }
            }
            .buttom{
              border: none;
              background: #409eff;
              color: #fff;
              letter-spacing: 10px;
              cursor:pointer;
            }
            .register{
              margin-top: 5px;
              height: 20px;
              line-height: 20px;
              text-align: right;
              border: none;
              font-size: 13px;
              a{
                color: #409eff;
                text-decoration: underline;
              }
            }
          }
        }
      }
    }
  }
</style>
<script>
import VHeader from 'com/header/header'
import g from '../../assets/js/global'
import {setCookie, getCookie, delCookie} from '../../assets/js/cookie'
import { PRISON_API } from '../../api/api'

export default {
  name: 'login',
  components: {
    VHeader
  },
  data () {
    return {
      actNum: '',
      cipher: '',
      zhanghao: '',
      mima: '',
      prompting: false,
      remember: true,
    }
  },
  methods: {
    // 登录
    login () {
      if (this.mima === '') {
        this.prompting = true
        return
      }  
      let newMima = g.encode64(this.mima)
      let data ={username: this.zhanghao, password: newMima}
      this.$http.Post(PRISON_API.login, data).then(res => {
        
      }).catch(rej => {
        if (rej.data.errcode === 10001 ) {
          this.$router.replace('/panorama')
          sessionStorage.setItem('zhanghao', rej.data.result.username)
          sessionStorage.setItem('token', rej.data.result.token)
        }else{
          this.prompting = true
        }
      })
      this.check()
    },
    // enter键登录
    enter () {
      document.onkeydown = event => {
        let e =  event || window.event || arguments.callee.caller.arguments[0]
        if (e && e.keyCode == 13 && this.$route.path == '/login') {
          this.login()
        }
      }
    },
    storage () {
      this.remember?this.remember=false:this.remember=true
    },
    check () {
      if(this.remember){
         setCookie('prisonusername', this.zhanghao, 30)
         setCookie('prisonpassword', this.mima, 30)
      } else {
        delCookie('prisonusername')
        delCookie('prisonpassword')
      }
    },
  },
  mounted () {
    this.enter()
    this.zhanghao = getCookie('prisonusername')
    this.mima = getCookie('prisonpassword')
  }
}
</script>
