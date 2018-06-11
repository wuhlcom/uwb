<template>
  <div class="pie">
    <div class="pie-box">
      <div id="rose" class="height"></div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
  @import '../assets/css/common.scss';
  .pie{
    width: 100%;
    position: relative;
    height: 100%;
    font-size: 0;
    cursor:pointer;
    .pie-box{
      width: 100%;
      height: 100%;
      ul{
        position: absolute;
        right: 0;
        z-index: 1000;
        width: 85px;
        height: 80px;
        font-size: 12px;
        li{
          margin: 10px 0;
          color: #fff;
          span{
            display: inline-block;
            margin-right: 5px;
            width: 13px;
            height: 13px;
            vertical-align: top;
          }
          .severity{
              background: $severity;
            }
            .general{
              background: $general;
            }
            .hint{
              background: $hint;
            }
        }
      }
    }
  }
  .height{
    height: 100%;
  }
</style>
<script>
  import echarts from 'echarts'
  export default {
    name: 'rose',
    props: {
      code: {
        type: String,
      },
      cellNum: {
        type: String,
      },
      setId:{
        type: String
      }
    },
    methods: {  
      // 绘图  
      drawgraph (id) {  
        this.chart = echarts.init(document.getElementById(id))   
        this.chart.showLoading()
        let _this = this  
        this.chart.setOption({  
            tooltip : {
                // trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
              orient: 'vertical',
              x : 'right',
              y : 'top',
              data: [
                {
                  name: '全勤人数',
                  textStyle: {
                    color: '#0ee3ca'
                  }
                },
                {
                  name: '事假人数',
                  textStyle: {
                    color: '#5856c5'
                  }
                },
                {
                  name: '病假人数',
                  textStyle: {
                    color: '#e89d23'
                  }
                },
                {
                  name: '旷工人数',
                  textStyle: {
                    color: '#fc3d3d'
                  }
                }
              ]
            },
            calculable : true,
            series : [
                {
                    type:'pie',
                    radius : [30, 110],
                    center : ['50%', '50%'],
                    roseType : 'area',
                    data:[
                        {value:10, name:'全勤人数'},
                        {value:5, name:'事假人数'},
                        {value:15, name:'病假人数'},
                        {value:25, name:'旷工人数'},
                    ]
                }
            ],
            color: ['#0ee3ca', '#5856c5', '#e89d23', '#fc3d3d']
        }) 
        this.chart.hideLoading()
        window.onresize = () => {
          return(() =>{
            this.chart.resize()
          })()
        }
      },
    },  
    mounted() {  
      this.$nextTick(function() {
        this.drawgraph('rose')
      })
    },
    watch: {
      sum () {
        this.drawgraph('rose')
      }
    }
  }
</script>