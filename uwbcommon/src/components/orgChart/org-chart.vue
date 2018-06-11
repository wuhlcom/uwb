<template>
  <div id="chart-container"  @click="get($event)" class="vo-basic"></div>
</template>
<script>
import org from "./index";
export default {
  name: "org-chart",
  props: {
    data: { type: Object, default: {} },
  },
  data() {
    return {
      fixed: {
        chartContainer: "#chart-container",
      },
      code: ''
    };
  },
  methods: {
    initOrg() {
      this.orgchart = new org(Object.assign(this.fixed, this.$props));
    },
    get (event) {
      if (event.target.className === 'iconfont icon-bianji') {
        this.code = event.target.getAttribute('data-code')
        this.backCode('modify')
      }
      if (event.target.className === 'iconfont icon-jia') {
        this.code = event.target.getAttribute('data-code')
        this.backCode('add')
      }
      if (event.target.className === 'iconfont icon-lajitong') {
        this.code = event.target.getAttribute('data-code')
        this.backCode('del')
      }
    },
    backCode (fnName) {
      this.$emit('code', {fn: fnName, code: this.code})
    }
  },
  mounted() {
    this.initOrg()
  },
};
</script>
