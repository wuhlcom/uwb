<template>
  <div class="pie">
    <div class="pie-box">
      <div id="histogram" class="height"></div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
@import "../assets/css/common.scss";
.pie {
  width: 100%;
  position: relative;
  height: 100%;
  font-size: 0;
  cursor: pointer;
  .pie-box {
    width: 100%;
    height: 100%;
    ul {
      position: absolute;
      right: 0;
      z-index: 1000;
      width: 85px;
      height: 80px;
      font-size: 12px;
      li {
        margin: 10px 0;
        color: #fff;
        span {
          display: inline-block;
          margin-right: 5px;
          width: 13px;
          height: 13px;
          vertical-align: top;
        }
        .severity {
          background: $severity;
        }
        .general {
          background: $general;
        }
        .hint {
          background: $hint;
        }
      }
    }
  }
}
.height {
  height: 100%;
}
</style>
<script>
import echarts from "echarts";
export default {
  name: "histogram",
  props: {
    val: {type: Object},
    setId: {
      type: String
    }
  },
  methods: {
    // 绘图
    drawgraph(id) {
      this.chart = echarts.init(document.getElementById(id));
      this.chart.showLoading();
      let _this = this;
      this.chart.setOption({
        tooltip : {
          trigger: 'axis',
          axisPointer : {            // 坐标轴指示器，坐标轴触发有效
            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis:  {
          type: 'category',
          axisLine: {
            lineStyle: {
              color: '#fff'
            }
          },
          data: _this.val.name,
          axisLabel: {
            interval: 0,
            rotate: 30,
            margin: 10
          }, 
        },
        grid: { // 控制图的大小，调整下面这些值就可以，
             x: 50,
             x2: 20,
             y2: 40,// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
         },
        yAxis: {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: '#fff'
            }
          },
        },
        series: [
          {
            name: '严重',
            type: 'bar',
            stack: '总量',
            barMaxWidth: 30,
            // label: {
            //   normal: {
            //     show: true,
            //     position: 'insideRight'
            //   }
            // },
            data: _this.val.severity
          },
          {
            name: '普通',
            type: 'bar',
            stack: '总量',
            barMaxWidth: 30,
            // label: {
            //   normal: {
            //     show: true,
            //     position: 'insideRight'
            //   }
            // },
            data: this.val.general,
          },
          {
            name: '提示',
            type: 'bar',
            stack: '总量',
            barMaxWidth: 30,
            // label: {
            //   normal: {
            //     show: true,
            //     position: 'insideRight'
            //   }
            // },
            data: this.val.hint,
          },
        ],
        color: ['#f00', '#f1b013', '#0febce']
      });
      this.chart.hideLoading();
      window.addEventListener("resize", function() {
        _this.chart.resize();
      });
    }
  },
  mounted() {
    this.$nextTick(function() {
      this.drawgraph("histogram");
    });
  },
  watch: {
    sum() {
      this.drawgraph("histogram");
    }
  }
};
</script>