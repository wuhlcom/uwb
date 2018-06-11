<template>
  <div class="pie">
    <div class="pie-box">
      <ul>
        <li><span class="severity"></span>严重:{{serious}}</li>
        <li><span class="general"></span>普通:{{common}}</li>
        <li><span class="hint"></span>提醒:{{prompting}}</li>
      </ul>
      <div id="pie"></div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
  .pie{
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
          span{
            display: inline-block;
            margin-right: 5px;
            width: 13px;
            height: 13px;
            vertical-align: top;
          }
          .severity{
              background: #fa5555;
            }
            .general{
              background: #ff8c00;
            }
            .hint{
              background: #f5c124;
            }
        }
      }
    }
    #pie{
      height: 100%;
    }
  }
</style>
<script>
  import echarts from 'echarts'
  export default {
    name: 'pie',
    computed: {
      serious () {
        if (this.$store.state.warningPie['01']) {
          return this.$store.state.warningPie['01']
        } else {
          return 0
        } 
      },
      common () {
        if (this.$store.state.warningPie['02']) {
          return this.$store.state.warningPie['02']
        } else {
          return 0
        }
      },
      prompting () {
        if (this.$store.state.warningPie['03']) {
          return this.$store.state.warningPie['03']
        } else {
          return 0
        }
      },
      sum () {
        return this.serious + this.common + this.prompting
      }
    },
    props: {
      code: {
        type: String,
      },
      cellNum: {
        type: String,
      } 
    },
    methods: {  
      // 绘图  
      drawgraph (id) {  
        this.chart = echarts.init(document.getElementById(id))   
        this.chart.showLoading()
        let _this = this  
        this.chart.setOption({  
          tooltip: {
            trigger: 'item',
            // formatter: "{b}: {c} ({d}%)"
          },
          title : {  
            text: _this.sum,  
            subtext: '报警数',  
            itemGap: -10,
            top:'center'  ,
            left: 'center',
            textStyle:{
              color: '#fa5555',
              fontSize: '30',
              fontWeight: '100',
            },
            subtextStyle: {
              color: '#000',
            }
          },  
          // legend: {
          //   orient: 'vertical',
          //   x: 'right',
          //   y: 'center',
          //   data:['严重', '普通', '提示']
          // },
          series: [
            {
              type:'pie',
              radius: ['50%', '70%'],
              avoidlabeloverlap: false,
              data:[
                {value: _this.serious, name:'严重'},
                {value: _this.common, name:'普通'},
                {value: _this.prompting, name:'提示'},
              ],
              itemStyle:{ 
                normal:{ 
                  label:{ 
                    show: false, 
                    formatter: '{b} : {c}' 
                  }, 
                  labelLine :{show: false},
                },
              }
            }
          ],
          color: ['#fa5555', '#ff8c00', '#f5c124'],
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
        this.drawgraph('pie')  
      })
    },
    watch: {
      sum () {
        this.drawgraph('pie')
      }
    }
  }
</script>
