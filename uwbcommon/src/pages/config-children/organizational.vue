<template>
  <div class="org" id="people">
    <div class="modify">
      <!-- <button @click="addShow(true)">增加部门</button> -->
      <!-- <button @click="modifyShow(true)">修改部门</button> -->
      <!-- <button @click="delShow(true)">删除部门</button> -->
    </div>
    <add-org :data="dataArr" :departmentCode="departmentCode" v-if="add" @closeadd="addShow"></add-org>
    <modify-org :data="dataArr" :departmentCode="departmentCode" v-if="modify" @closemodify="modifyShow"></modify-org>
    <del-org :data="dataArr" :departmentCode="departmentCode" v-if="del" @closedel="delShow"></del-org>
    <org-chart :data="data" @code="getCode" v-if="orgShow"></org-chart>
  </div>
</template>
<style lang="scss" scoped>
@import '../../assets/css/getorgchart.css';
  .org{
    width: 100%;
    height: 100%;
    overflow: auto;
    .modify{
      button{
        height: 26px;
        margin-left: 19px;
        padding: 0 20px;
        border: none;
        font-size: 14px;
        color: #59b3ff;
        background-color: rgba(0, 83, 193, 0.5);
      }
    }
  }
</style>
<script>
import OrgChart from '../../components/orgChart/org-chart'
import AddOrg from '../../components/add-org'
import ModifyOrg from '../../components/modify-org'
import DelOrg from '../../components/del-org'
import {URL} from '../../api/url'
export default {
  components: { OrgChart, AddOrg, ModifyOrg, DelOrg },
  data () {
    return {
      data:{},
      dataArr: [],
      orgShow: false,
      add: false,
      modify: false,
      del: false,
      departmentCode: ''
    }
  },
  methods: {
    // 添加部门成功后要对组织结构图重新渲染
    addShow (msg) {
      this.add = msg
      this.getData()
      this.orgShow = false
      setTimeout(() => {
        this.orgShow = true
      }, 500);
    },
    modifyShow (msg) {
      this.modify = msg
      this.getData()
      this.orgShow = false
      setTimeout(() => {
        this.orgShow = true
      }, 500);
    },
    delShow (msg) {
      this.del = msg
      this.getData()
      this.orgShow = false
      setTimeout(() => {
        this.orgShow = true
      }, 500);
    },
    getData () {
      this.$http.Get(URL.organization).then(res => {
      }).catch(rej => {
        this.dataArr = []
        this.data = rej.data.result
        // 必须进转换，不然读取不到children属性
        this.dataArr.push(JSON.parse(JSON.stringify(rej.data.result))) 
        this.orgShow = true
      })
    },
    getCode (msg) {
      this.departmentCode = msg.code
      if (msg.fn === 'modify') {
        this.modify = true
      }
      if (msg.fn === 'del') {
        this.del = true
      }
      if (msg.fn === 'add') {
        this.add = true
      }
    }
  },
  created () {
    this.getData()
  },
  mounted () {
  }
}
</script>
