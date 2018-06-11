<template>
  <div class="pie">
    <div class="pie-box">
      <ul>
        <li><span class="severity"></span>严重:{{val.severity}}</li>
        <li><span class="general"></span>普通:{{val.general}}</li>
        <li><span class="hint"></span>提示:{{val.hint}}</li>
      </ul>
      <div :id="setId" class="height"></div>
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
    name: 'pie',
    props: {
      val: {type: Object},
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
          tooltip: {
            trigger: 'item',
            // formatter: "{b}: {c} ({d}%)"
          },
          title : {  
            text: _this.val.severity + _this.val.general + _this.val.hint,
            subtext: '报警数',  
            itemGap: -10,
            top:'center'  ,
            left: 'center',
            textStyle:{
              color: '#fff',
              fontSize: '30',
              fontWeight: '100',
            },
            subtextStyle: {
              color: '#fff',
            }
          },  
          series: [
            {
              type:'pie',
              radius: ['50%', '70%'],
              avoidlabeloverlap: false,
              data:[
                {value: _this.val.severity, name:'严重'},
                {value: _this.val.general, name:'普通'},
                {value: _this.val.hint, name:'提示'},
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
          color: ['#f00', '#f1b013', '#0febce'],
        }) 
        this.chart.hideLoading()
        window.addEventListener("resize",function(){
          _this.chart.resize()
        })
      },
    },  
    mounted() {  
      this.$nextTick(function() {
        this.drawgraph(this.setId)
      })
    },
    watch: {
      sum () {
        this.drawgraph(this.setId)
      }
    }
  }
</script>
