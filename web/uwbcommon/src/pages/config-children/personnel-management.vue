<template>
  <div class="personnel-management" ref="content">
    <div v-if="!addPeople" class="head">
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
        <button @click="add">+新增人员</button>
      </div>
    </div>
    <div v-show="!addPeople" ref="teble" class="teble">
      <el-table
        :data="tableData"
        @selection-change="handleSelectionChange"
        :style="{width: tebleWidth + 'px'}"
        ref="multipleTable">
          <el-table-column type="selection" width="55">
          </el-table-column>
          <el-table-column type="index" label="序号">
          </el-table-column>
          <el-table-column prop="personCode" label="工号">
          </el-table-column>
          <el-table-column prop="personName" label="姓名">
          </el-table-column>
          <el-table-column prop="levelName" label="级别">
            <!-- <template slot-scope="scope">
              <span>
                男
              </span>
            </template> -->
          </el-table-column>
          <el-table-column prop="positionName" label="职位">
            <!-- <template slot-scope="scope">
              <span>{{arr[scope.$index]}}
              </span>
            </template> -->
          </el-table-column>
          <el-table-column prop="departmentName" label="部门" show-overflow-tooltip>
            <!-- <template slot-scope="scope">
              <span>
                水泥工
              </span>
            </template> -->
          </el-table-column>
          <el-table-column prop="remark" label="描述" show-overflow-tooltip>
            <!-- <template slot-scope="scope">
              <span>
                {{b+Math.ceil(Math.random()*10)}}
              </span>
            </template> -->
          </el-table-column>
          <el-table-column prop="tagId" label="关联标签ID">
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <span @click="handleEdit(scope.row)">
                <i class="iconfont icon-bianji"></i>
              </span>
              <span @click="handleDelete(scope.row)">
                <i class="iconfont icon-lajitong"></i>
              </span>
            </template>
          </el-table-column>
      </el-table>
    </div>
    <el-pagination
      v-show="!addPeople"
      class="pagination"
      :page-size="getDataObj.listRows"
      background
      layout="total, prev, pager, next"
      @current-change="handleCurrentChange"
      :total="total">
    </el-pagination>
    <add-people @closes="addAreaClose" v-if="addPeople">
    </add-people>
    <modify-person :data="personInfo" @modifyPerson="modify" v-if="modifyPerson"></modify-person>
  </div>
</template>
<style lang="scss" scoped>
$color: #59b3ff;
.personnel-management {
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
import AddPeople from "../../components/add-people";
import ModifyPerson from "../../components/modify-person";
export default {
  name: "personnel-management-arrangement",
  components: {
    AddPeople,
    ModifyPerson
  },
  data() {
    return {
      a: 20,
      b: 70,
      del: false,
      tableData: [],
      search: "",
      addPeople: false,
      modifyPerson: false,
      tebleWidth: 0,
      total: 0,
      personCodes: [],
      personInfo: {},
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
      this.addPeople = msg;
      this.getData();
    },
    add() {
      this.addPeople = true;
    },
    modify() {
      this.modifyPerson = false;
      this.getData();
    },
    getTebleWidth() {
      setTimeout(() => {
        let windowWidth = document.body.clientWidth;
        // 根据左侧导航栏是否显示来判断是否需要减去310px的导航栏宽度
        if (this.$store.state.nav.navShow) {
          this.tebleWidth = document.body.clientWidth - 310 - 42 - 71;
        } else if (!this.$store.state.nav.navShow) {
          this.tebleWidth = document.body.clientWidth - 42 - 71;
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
      this.getTebleWidth();
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
        this.getTebleWidth();
      } else {
        // 根据左侧导航栏是否显示来判断是否需要减去310px的导航栏宽度
        if (this.$store.state.nav.navShow) {
          this.tebleWidth = 1366 - 310 - 42 - 71;
        } else {
          this.tebleWidth = 1366 - 42 - 71;
        }
      }
      this.getListRows();
    });
    window.onresize = () => {
      if (document.body.clientWidth > 1366) {
        this.getTebleWidth();
      } else {
        if (this.$store.state.nav.navShow) {
          this.tebleWidth = 1366 - 310 - 42 - 71;
        } else {
          this.tebleWidth = 1366 - 42 - 71;
        }
      }
    };
  },
  updated() {}
};
</script>
