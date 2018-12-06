<template>
  <div class="time">
    <div>{{timeText}}</div>
  </div>
</template>
<style lang="scss" scoped>
</style>

<script>
export default {
  name: 'time',
  data () {
    return {
      timeText: '',
    }
  },
  methods: {
    // 时间数小于10的时候补零
    buling (val) {
      if (val < 10) {
        val = "0" + val
      }
      return val
    }
  },
  mounted () { 
    setInterval(()=>{
      let time = new Date()
      let y = time.getFullYear()
      let m = this.buling(time.getMonth() + 1)
      let d = this.buling(time.getDate())
      let h = this.buling(time.getHours())
      let mi = this.buling(time.getMinutes())
      let s = this.buling(time.getSeconds())
      let nowTime = y+"-"+m+"-"+d+" "+h+":"+mi+":"+s;
      this.timeText = nowTime;
    },1000);            
  } 
}
</script>
