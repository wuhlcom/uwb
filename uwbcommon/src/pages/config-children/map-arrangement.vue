<template>
  <div class="map" ref="content">
    <div v-if="!addArea" class="head">
       <div class="search">
        <el-input
          placeholder="请输入名称"
          v-model="getDataObj.name"
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
        <button @click="deleteMap">批量删除</button>
        <button @click="add">+增加地图</button>
      </div>
    </div>
    <div v-show="!addArea" ref="teble" class="teble">
      <el-table
        :data="tableData"
        ref="multipleTable"
        :style="{width: tebleWidth + 'px'}"
        @selection-change="handleSelectionChange">
          <el-table-column type="selection" label="选择" width="55">
          </el-table-column>
          <el-table-column type="index" label="序号" width="180">
          </el-table-column>
          <el-table-column prop="name" label="名称">
          </el-table-column>
          <el-table-column prop="size" label="大小">
          </el-table-column>
          <el-table-column label="操作" width="230">
            <template slot-scope="scope">
              <el-switch
                v-model="select[scope.$index].value"
                active-color="#13ce66"
                @change="open(scope.$index)"
                inactive-color="#ff4949"
                active-text="启用"
                inactive-text="关闭">
              </el-switch>
              <span @click="handleEdit(scope.$index, scope.row)">
                <i class="iconfont icon-bianji"></i>
              </span>
              <span @click="handleDelete(scope.row.id, scope.row)">
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
      v-show="!addArea"
      class="pagination">
    </el-pagination>
    <add-area @closes="addAreaClose" class="add-area" v-if="addArea">
    </add-area>
    <map-modify v-if="modifyShow" :info="modifyInfo" @closeModify="mapModifyClose"></map-modify>
  </div>
</template>
<style lang="scss" scoped>
$color: #59b3ff;
.map {
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
</style>
<script>
import AddArea from "../../components/image-upload";
import MapModify from "../../components/map-modify";
export default {
  name: "map-arrangement",
  components: {
    AddArea,
    MapModify
  },
  data() {
    return {
      tableData: [],
      select: [],
      search: "",
      addArea: false,
      tebleWidth: 0,
      modifyShow: false,
      modifyInfo: {},
      delArr: [],
      paging: {
        total: 0
      },
      getDataObj: {
        page: 1,
        name: '',
        listRows: 0,
        type: 0
      }
    };
  },
  computed: {
    navShow() {
      return this.$store.state.nav.navShow;
    }
  },
  methods: {
    addAreaClose(msg) {
      this.addArea = msg;
      this.getData();
    },
    mapModifyClose(msg) {
      this.modifyShow = msg;
      this.getData();
    },
    open(index) {
      this.select.forEach((e, i) => {
        if (i !== index) e.value = false;
      });
      let num = this.select[index].value ? 1 : 0;
      this.$http
        .Post(this.$url.fdfsSwitch, {
          id: this.tableData[index].id,
          status: num
        })
        .then(res => {})
        .catch(rej => {
          if (rej.data.errcode !== 10001) {
            this.$message({
              type: "error",
              message: "必须启用一张地图!"
            });
          }
          setTimeout(() => {
            this.getData();
          }, 200);
        });
    },
    add() {
      this.$store.state.nav.navShow = false;
      this.addArea = true;
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
          message: "请选择至少一张地图!"
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
              this.delArr = [];
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
    handleDelete(val, row) {
      if (row.status === 1) {
        this.$message({
          type: "info",
          message: '启用地图无法删除'
        });
      } else {
        this.delArr = []
        this.delArr.push(val);
        this.deleteMap();
      }
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
