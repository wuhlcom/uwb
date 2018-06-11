<template>
  <div class="absenteeism">
    <div class="content">
      <div class="content-one">
        <div class="flex-box">
          <div class="box-text">住院</div>
        </div>
        <div class="flex-box">
          <div class="box-num">{{abStatistics['1']|| '0'}}</div>
        </div>
      </div>
      <div class="content-one">
        <div class="flex-box">
          <div class="box-text">外诊</div>
        </div>
        <div class="flex-box">
          <div class="box-num">{{abStatistics['2']|| '0'}}</div>
        </div>   
      </div>
      <div class="content-one">
        <div class="flex-box">
          <div class="box-text">看病</div>
        </div>
       <div class="flex-box">
         <div class="box-num">{{abStatistics['3']|| '0'}}</div>
       </div>
      </div>
      <div class="content-one">
        <div class="flex-box">
          <div class="box-text">禁闭</div>
        </div>
        <div class="flex-box">
          <div class="box-num">{{abStatistics['4']|| '0'}}</div>
        </div>
      </div>
      <div class="content-one">
        <div class="flex-box">
          <div class="box-text">会见</div>
        </div>
        <div class="flex-box">
          <div class="box-num">{{abStatistics['5']|| '0'}}</div>
        </div>
      </div>
      <div class="content-one">
        <div class="flex-box">
          <div class="box-text">教学楼</div>
        </div>
        <div class="flex-box">
          <div class="box-num">{{abStatistics['6']|| '0'}}</div>
        </div>
      </div>
      <div class="content-one">
        <div class="flex-box">
          <div class="box-text">留仓</div>
        </div>
        <div class="flex-box">
          <div class="box-num">{{abStatistics['7']|| '0'}}</div>
        </div>
      </div>
      <div class="content-one">
        <div class="flex-box">
          <div class="box-text">单关</div>
        </div>
        <div class="flex-box">
          <div class="box-num">{{abStatistics['8']|| '0'}}</div>
        </div>
      </div>
     <div class="content-one">
        <div class="flex-box">
          <div class="box-text">其他</div>
        </div>
        <div class="flex-box">
          <div class="box-num">{{abStatistics['9']|| '0'}}</div>
        </div>
      </div>  
    </div>
  </div>
</template>
<style lang="scss" scoped>
  .absenteeism{
    width: 100%;
    height: 100%;
    font-size: 0;
    .content{
      width: 100%;
      height: 100%;
      margin-top: 40px;
      .content-one{
        width: 33%;
        height: 33%;
        display: inline-block;
        .flex-box{
          display: flex;
          align-items: center;
          justify-content: center;
          height: 45%;
          font-size: 14px;
          .box-text{
            display: flex;
            align-items: center;
            justify-content: center;
            width: 50%;
            min-width: 60px;
            height: 100%;
            background: #f6f7fb;
            border: 1px solid #f0f0f0;
          }
        }
        .box-num{
          display: flex;
          align-items: center;
          justify-content: center;
          width: 50%;
          min-width: 60px;
          height: 100%;
          border-left: 1px solid #f0f0f0;
          border-bottom: 1px solid #f0f0f0;
          border-right: 1px solid #f0f0f0;
        }
      }
    }
    .others{
      display: inline-block;
      width: 75%;  
      font-size: 14px;
    }
  }
</style>
<script>
export default {
  name: 'absenteeism',
  computed: {
    abStatistics () {
      return this.$store.state.abStatistics
    }
  }
}
</script>

