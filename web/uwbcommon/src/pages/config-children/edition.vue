<template>
  <div class="edition" ref="content">
    <div class="head">
       <div class="search">
        <el-input placeholder="请输入名称" v-model="getDataObj.name" @keyup.enter.native="getData" class="input-with-select">
          <el-button slot="append" icon="el-icon-search" @click="getData"></el-button>
        </el-input>
      </div>
      <div class="add-dle">
        <!-- <el-dropdown @command="handleCommand">
          <el-button type="primary">
            选择版本<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="1">基站版本</el-dropdown-item>
            <el-dropdown-item command="2">标签版本</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown> -->
        <button @click="deleteMap">批量删除</button>
        <button @click="add">+增加版本文件</button>
      </div>
    </div>
    <div ref="teble" class="teble">
      <el-table :data="tableData" ref="multipleTable" :style="{width: tebleWidth + 'px'}" @selection-change="handleSelectionChange">
        <el-table-column type="selection" label="选择" width="55">
        </el-table-column>
        <el-table-column type="index" label="序号" width="180">
        </el-table-column>
        <el-table-column label="版本类型">
          <template slot-scope="scope">
            <span v-if="scope.row.type === 1">
              基站版本
            </span>
          </template>
        </el-table-column>
        </el-table-column>
        <el-table-column prop="name" label="名称">
        </el-table-column>
        <el-table-column prop="size" label="大小">
        </el-table-column>
        <el-table-column prop="createdAt" label="上传时间">
        </el-table-column>
        <el-table-column label="操作" width="230">
          <template slot-scope="scope">
            <span @click="handleEdit(scope.$index, scope.row)">
              <i class="iconfont icon-bianji"></i>
            </span>
            <span @click="handleDelete(scope.row.id)">
              <i class="iconfont icon-lajitong"></i>
            </span>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-pagination
      background
      @current-change="handleCurrentChange"
      :page-size="getDataObj.listRows"
      layout="total, prev, pager, next"
      :total="paging.total"
      class="pagination">
    </el-pagination>
    <add-edition v-if="addEdition" @closes="closeEdition"></add-edition>
    <modify-versions v-if="modifyShow" :info="modifyInfo" @closeModify="modifyClose"></modify-versions>
  </div>
</template>
<style lang="scss" scoped>
$color: #59b3ff;
.edition {
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
        border: 1px solid #003585;
        font-size: 14px;
        color: $color;
        background: #003585;
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
}
.icon-bianji {
  margin: 0 10px;
}
.el-button{
  border-radius: 0;
  border: 1px solid #003585;
}
</style>
<script>
import AddEdition from '../../components/add-edition'
import ModifyVersions from '../../components/modify-versions'
import {formattingTime} from '../../assets/js/formattingTime'
export default {
  name: "edition",
  components: {
    AddEdition,
    ModifyVersions
  },
  data() {
    return {
      addEdition: false,
      modifyShow: false,
      modifyInfo: {},
      tableData: [],
      select: [],
      search: "",
      tebleWidth: 0,
      modifyInfo: {},
      delArr: [],
      paging: {
        total: 0
      },
      getDataObj: {
        page: 1,
        name: '',
        listRows: 0,
        type: 1
      }
    };
  },
  computed: {
    navShow() {
      return this.$store.state.nav.navShow;
    }
  },
  methods: {
    modifyClose () {
      this.modifyShow = false
      this.getData()
    },
    add () {
      this.addEdition = true
    },
    handleCommand (command) {
      this.getDataObj.type =  Number(command)
      this.getData()
    },
    closeEdition () {
      this.addEdition = false
      this.getData()
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
        .Post(this.$url.allImg, this.getDataObj)
        .then(res => {})
        .catch(rej => {
          this.paging.total = rej.data.totalRows;
          this.tableData = rej.data.result;
          // 清空判断是否开启的数组
          this.select = []
          this.tableData.forEach(e => {
            e.createdAt = formattingTime(e.createdAt)
            if (e.length && e.width) {
              e.size = e.length + "*" + e.width;
            }
            if (e.status === 1) {
              this.select.push({ value: true });
            } else if (e.status === 0) {
              this.select.push({ value: false });
            }
          });
        });
    },
    deleteMap() {
      if (!this.delArr.length) {
        this.$message({
          type: "error",
          message: "请选择至少一版本!"
        });
      } else {
        this.$confirm("此操作将永久删除该项, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
        .then(() => {
          this.$http
            .Post(this.$url.delMap, { ids: this.delArr })
            .then(res => {})
            .catch(rej => {
              if (rej.data.errcode === 10001) {
                this.getData()
                this.$message({
                  type: "success",
                  message: "删除成功!"
                });
              }
            })
        })
        .catch(() => {
            this.toggleSelection()
            this.delArr = [];
            this.$message({
              type: "info",
              message: "已取消删除"
            });
          });
      }
    },
    handleEdit(index, row) {
      this.modifyShow = true;
      this.modifyInfo = this.tableData[index]
    },
    handleDelete(val) {
      this.delArr.push(val);
      this.deleteMap();
    },
    handleSelectionChange(val) {
      if (val.length) {
        val.forEach((e, index) => {
          if (e.status !== 1) {
            this.delArr.push(e.id)
          } else {
            this.toggleSelection([this.tableData[index]])
          }
        })
      } else {
        this.delArr = []
      }
    },
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
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
    handleCurrentChange(val) {
      this.getDataObj.page = val;
      this.getData();
    },
    // 获取显示列表区域的高度，计算出列表每页的显示条数
    getListRows () {
      let height = this.$refs.content.clientHeight - 150
      this.getDataObj.listRows =Math.floor(height/45)
      this.getData()
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
      this.getListRows()
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
