<template>
  <div class="attendance">
    <div class="head">
      <div class="box">
        <div class="title">实时考勤</div>
        <div class="statistics">
          <div class="details">
            <div class="num">174</div>
            <div class="tetx">
              <i class="iconfont icon-weibiaoti-"></i>
              大厅
            </div>
          </div>
          <div class="details">
            <div class="num">38</div>
            <div class="tetx">
              <i class="iconfont icon-huiyishi"></i>
              会议室
            </div>
          </div>
          <div class="details">
            <div class="num">62</div>
            <div class="tetx">
              <i class="iconfont icon-cantingrestaurant42"></i>
              餐厅
            </div>
          </div>
        </div>
      </div>
      <div class="box">
        <div class="title">月底考勤</div>
        <div class="echart">
          <rose></rose>
        </div>
      </div>
      <div class="box">
        <div class="title">工时考勤</div>
        <div class="echart">
          <histogram></histogram>
        </div>
      </div>
    </div>
    <div class="teble">
      <el-table
        :data="tableData"
        stripe
        style="width: 100%">
        <el-table-column
          prop="date"
          label="序号"
          width="180">
        </el-table-column>
        <el-table-column
          prop="name"
          label="姓名"
          width="180">
        </el-table-column>
        <el-table-column
          prop="address"
          label="工号">
        </el-table-column>
        <el-table-column
          prop="date"
          label="归属">
        </el-table-column>
        <el-table-column
          prop="name"
          label="考勤区">
        </el-table-column>
        <el-table-column
          prop="address"
          label="开始时间">
        </el-table-column>
        <el-table-column
          prop="date"
          label="结束时间">
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<style lang="scss" scoped>
  .attendance{
    height: 78%;
    margin-top: 50px;
    padding: 0 56px;
    .head{
      display: flex;
      width: 100%;
      height: 273px;
      .box{
        position: relative;
        flex: 1;
        background: rgba(0, 83, 193, 0.5);
        margin: 0 15px;
        .title{
          position: absolute;
          top: 21px;
          left: 20px;
          font-size: 14px;
          color: #fff;
        }
        .statistics{
          display: flex;
          width: 411px;
          height: 141px;
          margin: 92px auto 0;
          .details{
            flex: 1;
            margin: 0 14px;
            background: rgba(0, 83, 193, 0.5);
            .num{
              margin-top: 33px;
              text-align: center;
              font-size: 36px;
              color: #fff;
            }
            .tetx{
              margin-top: 32px; 
              text-align: center;
              font-size: 14px;
              color: #59b3ff;
            }
          }
        }
        .echart{
          width: 100%;
          height: 273px;
        }
      }
    }
    .teble{
      margin-top: 27px;
      padding: 0 15px;
    }
  }
  @media screen and (max-height: 820px) {
    .attendance{
      margin-top: 20px;
    }
  }
</style>
<script>
import Rose from '../components/rose'
import Histogram from '../components/histogram'
export default {
  name: 'attendance',
  components:{
    Rose,
    Histogram
  }
}
</script>
