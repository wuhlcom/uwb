<template>
  <div class="account-management" ref="content">
    <div v-if="!addFlag" class="head">
      <div class="search">
        <el-input
          placeholder="请输入姓名"
          v-model="getDataObj.personName"
          @keyup.enter.native="getData"
          class="input-with-select">
          <el-button
            slot="append"
            icon="el-icon-search"
            @click="getData">
          </el-button>
        </el-input>
      </div> 
      <div class="add-dle">
        <button @click="delPerson">批量删除</button>
        <button @click="add">+新增账号</button>
      </div>
    </div>
    <div v-show="!addFlag" ref="teble" class="teble">
      <el-table
        :data="tableData"
        @selection-change="handleSelectionChange"
        :style="{width: tableWidth + 'px'}"
        ref="multipleTable">
          <el-table-column type="selection" width="55">
          </el-table-column>
          <el-table-column type="index" label="序号">
          </el-table-column>
          <el-table-column prop="personCode" label="账号">
          </el-table-column>
          <el-table-column prop="personName" label="密码">
          </el-table-column>
          <el-table-column prop="levelName" label="角色">         
          </el-table-column>      
          <el-table-column prop="remark" label="描述" show-overflow-tooltip>       
          </el-table-column>         
      </el-table>
    </div>
    <el-pagination
      v-show="!addFlag"
      class="pagination"
      :page-size="getDataObj.listRows"
      background
      layout="total, prev, pager, next"
      @current-change="handleCurrentChange"
      :total="total">
    </el-pagination>
    <add-account @closes="addAreaClose" v-if="addFlag">
    </add-account>
    <modify-account :data="accountInfo" @modifyAccount="modify" v-if="modifyFlag"></modify-account>
  </div>
</template>
<style lang="scss" scoped>
$color: #59b3ff;
.account-management {
  .head {
    display: flex;
    justify-content: space-between;
    height: 26px;
    margin-bottom: 19px;
    .search {
      width: 265px;
    }
    .add-dle {
      button {
        height: 26px;
        margin-left: 19px;
        padding: 0 20px;
        border: none;
        font-size: 14px;
        color: $color;
        background: #003585;
        cursor: pointer;
      }
    }
  }
  .teble {
    color: $color;
  }
  .add-area {
    width: 100%;
    height: 100%;
    margin: 0 auto;
  }
}
.iconfont {
  font-size: 24px;
  vertical-align: middle;
  margin-right: 10px;
}
</style>
<script>
import AddAccount from "../../components/add-account";
import ModifyAccount from "../../components/modify-account";
export default {
  name: "account-management-arrangement",
  components: {
    AddAccount,
    ModifyAccount
  },
  data() {
    return {
      a: 20,
      b: 70,
      del: false,
      tableData: [],
      search: "",
      addFlag: false,
      modifyFlag: false,
      tableWidth: 0,
      total: 0,
      personCodes: [],
      accountInfo: {},
      getDataObj: {
        page: 1,
        personName: "",
        listRows: 0
      },
      delArr: [],
      arr: [42,37,47,24,22,30,36,27,29,21,22,20,25]
    };
  },
  computed: {
    navShow() {
      return this.$store.state.nav.navShow;
    }
  },
  methods: {
    addAreaClose(msg) {
      this.addFlag = msg;
      this.getData();
    },
    add() {
      this.addFlag = true;
    },
    modify() {
      this.modifyPerson = false;
      this.getData();
    },
    getTableWidth() {
      setTimeout(() => {
        let windowWidth = document.body.clientWidth;
        // 根据左侧导航栏是否显示来判断是否需要减去310px的导航栏宽度
        if (this.$store.state.nav.navShow) {
          this.tableWidth = document.body.clientWidth - 310 - 42 - 71;
        } else if (!this.$store.state.nav.navShow) {
          this.tableWidth = document.body.clientWidth - 42 - 71;
        }
      }, 0);
    },
    getData() {
      this.$http
        .Post(this.$url.personnelList, this.getDataObj)
        .then(res => [])
        .catch(rej => {
          this.tableData = rej.data.result;
          this.total = rej.data.totalRows;
        });
    },
    delPerson() {
      if (!this.personCodes.length) {
        this.$message({
          type: "error",
          message: "请选择至少一人!"
        });
      } else {
        this.$confirm("此操作将永久删除该项, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            this.$http
              .Post(this.$url.delPerson, { personCodes: this.personCodes })
              .then(res => {})
              .catch(rej => {
                this.personCodes=[]
                this.getData();
                this.$message({
                  type: "success",
                  message: "删除成功!"
                });
              });
          })
          .catch(() => {
            this.toggleSelection()
            this.personCodes=[]
            this.$message({
              type: "info",
              message: "已取消删除"
            });
          });
      }
    },
    handleCurrentChange(val) {
      this.getDataObj.page = val;
      this.getData();
    },
    handleDelete(row) {
      this.personCodes = [];
      this.personCodes.push(row.personCode);
      this.delPerson()
    },
    handleEdit(row) {
      this.personInfo = row;
      this.modifyPerson = true;
    },
    handleSelectionChange(val) {
      if (val.length) {
        this.personCodes = [];
        val.forEach(e => {
          this.personCodes.push(e.personCode);
        });
      } else {
        this.personCodes = []
      }
    },
    hint(msg) {
      this.$notify.error({
        title: "错误",
        message: msg,
        position: "bottom-right",
        duration: 2000
      });
    },
    getListRows() {
      let height = this.$refs.content.clientHeight - 150;
      this.getDataObj.listRows = Math.floor(height / 45);
      this.getData();
    },
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    }
  },
  watch: {
    navShow() {
      this.getTableWidth();
    }
  },
  mounted() {
    // setInterval(()=>{
    //   this.getData()
    // },1000)
    this.$nextTick(() => {
      /* 
      * 窗口宽度大于1366时调用getTebleWidth方法
      * 小于1366时将窗口的值固定在1366
      */
      if (document.body.clientWidth > 1366) {
        this.getTableWidth();
      } else {
        // 根据左侧导航栏是否显示来判断是否需要减去310px的导航栏宽度
        if (this.$store.state.nav.navShow) {
          this.tableWidth = 1366 - 310 - 42 - 71;
        } else {
          this.tableWidth = 1366 - 42 - 71;
        }
      }
      this.getListRows();
    });
    window.onresize = () => {
      if (document.body.clientWidth > 1366) {
        this.getTableWidth();
      } else {
        if (this.$store.state.nav.navShow) {
          this.tableWidth = 1366 - 310 - 42 - 71;
        } else {
          this.tableWidth = 1366 - 42 - 71;
        }
      }
    };
  },
  updated() {}
};
</script>
