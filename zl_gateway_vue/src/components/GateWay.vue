<template>
  <div class="gateway">
    <h1>路由列表</h1>
    <header>
      <div class="search">
        <el-input placeholder="请输入内容" prefix-icon="el-icon-search" v-model="params.routeId" @keyup.enter.native="searchData">
        </el-input>
      </div>
      <div class="add">
        <el-button type="primary" @click="initAddRouteForm">添加</el-button>
      </div>
      <el-dialog :title="title" :visible.sync="dialogFormVisible">
       <el-form :model="form" :rules="rules" ref="form">
          <el-form-item label="路由ID" :label-width="formLabelWidth" prop="routeId">
            <el-input :disabled="isDisabled" v-model="form.routeId" auto-complete="off" placeholder="uwb-resources"></el-input>
          </el-form-item>
          <el-form-item label="接口地址" :label-width="formLabelWidth" prop="route">
            <el-input v-model="form.route" auto-complete="off" placeholder="/uwb/resources/**"></el-input>
          </el-form-item>
         <el-form-item label="路由类型" :label-width="formLabelWidth" prop="routeType">
            <el-input disabled v-model="form.routeType" auto-complete="off" placeholder="Path"></el-input>
          </el-form-item>
          <el-form-item label="优先级" :label-width="formLabelWidth"  prop="order">
            <el-input disabled v-model="form.order" auto-complete="off" placeholder="0"></el-input>
          </el-form-item>      
               <el-form-item label="路由地址" :label-width="formLabelWidth"  prop="url">
            <el-input v-model="form.url" auto-complete="off" placeholder="192.168.10.232:11003或uwb-resources"></el-input>
          </el-form-item> 
          <el-form-item label="路由地址类型" :label-width="formLabelWidth"  prop="urlType">
          <el-select v-model="form.urlType" placeholder="请选择">
              <el-option
                v-for="item in urlTypeObj"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>   
        <el-form-item label="启用状态" :label-width="formLabelWidth" prop="available">
          <el-select v-model="form.available" placeholder="请选择">
              <el-option
                v-for="item in status"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>         
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="resetForm('form')">重置</el-button>
          <el-button @click="dialogFormVisible = false">取 消</el-button>      
          <el-button type="primary" @click="changeRoute('form')">确 定</el-button>
  
        </div>
      </el-dialog>
    </header>
    <el-table :data="tableData" style="width: 100%">
   <el-table-column type="index" width="50">
    </el-table-column>
      <el-table-column prop="routeId" label="路由ID" width="">
      </el-table-column>
      <el-table-column prop="route" label="接口地址" width="">
      </el-table-column>    
      <el-table-column prop="routeType" label="路由类型">
      </el-table-column>
      <el-table-column prop="order" label="优先级">
      </el-table-column>
       <el-table-column prop="url" label="路由地址">
      </el-table-column>
      <el-table-column prop="urlType" label="路由地址类型">
      </el-table-column>
        <el-table-column label="启用状态">
          <template slot-scope="scope">
            <div v-if="scope.row.available === 1">启用</div>
            <div v-if="scope.row.available === 0">禁用</div>
          </template>
        </el-table-column>
      <el-table-column prop="route_definition" label="操作">
        <template slot-scope="scope">
          <el-button @click="initUpdateRouteForm(scope.row)" type="text" size="small">修改</el-button>
          <el-button @click="deleteRoute(scope.row)" type="text" size="small">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination  @current-change="handleCurrentChange" class="pagination"  :page-size="params.listRows" background layout="prev, pager, next, total" :total="total">
    </el-pagination>
  </div>
</template>
<script>
import { API } from "../assets/api.js";
export default {
  name: "GateWay",
  data() {
    return {
      status: [{ label: "启用", value: 1 }, { label: "禁用", value: 0 }],
      urlTypeObj: [
        { label: "HTTP", value: "HTTP" },
        { label: "LB", value: "LB" }
      ],
      params: { page: 1, listRows: 10 },
      formLabelWidth: "120px",
      dialogFormVisible: false,
      dialogFormType: 1,
      httpUrl: "",
      failedMsg: "",
      search: "",
      total: 0,
      title: "",
      isDisabled: false,
      form: {
        routeId: "",
        route: "",
        routeType: "Path",
        url: "",
        urlType: "HTTP",
        order: 0,
        available: 1
      },
      rules: {
        routeId: [
          { required: true, message: "请输入路由ID", trigger: "blur" },
          {
            min: 3,
            max: 64,
            message: "路由ID长度 3 ~ 64 个字符",
            trigger: "blur"
          }
        ],
        route: [
          { required: true, message: "请输入路由接口地址", trigger: "blur" },
          {
            min: 3,
            max: 64,
            message: "路由接口长度 3 ~ 64 个字符",
            trigger: "blur"
          }
        ],
        routeType: [
          { required: true, message: "请输入路由类型", trigger: "blur" },
          {
            min: 1,
            max: 32,
            message: "路由类型长度 3 ~ 32 个字符",
            trigger: "blur"
          }
        ],
        url: [
          {
            required: true,
            message: "请输入路由地址或服务名",
            trigger: "blur"
          },
          {
            min: 5,
            max: 64,
            message: "路由地址 5 ~ 64 个字符",
            trigger: "blur"
          }
        ],
        urlType: [
          { required: true, message: "请选择URL类型", trigger: "blur" }
        ],
        order: [
          { required: true, message: "请输入路由接口优先级", trigger: "blur" }
        ],
        available: [
          { required: true, message: "请输入选择路由状态", trigger: "blur" }
        ]
      },
      tableData: []
    };
  },
  methods: {
    //初始化修改路由的form参数
    initUpdateRouteForm(row) {
      this.title = "修改路由";
      this.dialogFormVisible = true;
      this.dialogFormType = 2;
      this.form.routeId = row.routeId;
      this.form.route = row.route;
      this.form.routeType = row.routeType;
      this.form.url = row.url;
      this.form.order = row.order;
      this.form.available = row.available;
      this.form.urlType = row.urlType;
      this.isDisabled = true;
    },

    //初始化添加路由时的form属性
    initAddRouteForm() {
      this.title = "添加路由";
      this.dialogFormType = 1;
      this.dialogFormVisible = true;
      for (const key in this.form) {
        if (this.form.hasOwnProperty(key)) {
          this.form[key] = "";
        }
      }
      //设置默认选项
      this.form.available = 1;
      this.form.order = 0;
      this.form.routeType = "Path";
      this.form.urlType = "HTTP";
      this.isDisabled = false;
    },

    //重置
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },

    searchData() {
      this.getRoutes();
    },

    // 删除
    deleteRoute(row) {
      this.form.routeId = row.routeId;
      this.$http({
        method: "post",
        url: URL.delete,
        data: this.form
      }).then(res => {
        this.getRoutes();
      });
    },

    //添加或修改路由
    changeRoute(formName) {
      if (this.dialogFormType === 1) {
        this.httpUrl = API.add;
        this.failedMsg = "添加路由失败";
      } else {
        this.httpUrl = API.update;
        this.failedMsg = "更新路由失败";
      }
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$http({
            method: "post",
            url: this.httpUrl,
            data: this.form
          }).then(res => {
            let data = res.data;
            if (data.errcode === 10001) {
              this.dialogFormVisible = false;
              this.getRoutes();
            } else {
              this.$message({
                message: this.failedMsg,
                type: "error"
              });
            }
          });
        } else {
          return false;
        }
      });
    },

    // 翻页
    handleCurrentChange(val) {
      this.params.page = val;
      this.getRoutes();
    },

    //查询路由列表
    getRoutes() {
      this.$http({
        method: "post",
        url: API.query,
        data: this.params
      }).then(res => {
        if (res.data.errcode === 10001) {
          let data = res.data.result;
          this.tableData = data;
          this.total = res.data.totalRows;
        } else {
          this.tableData = "";
          this.total = 0;
        }
      });
    }
  },
  created() {
    this.getRoutes();
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
header {
  display: flex;
  justify-content: space-between;
}
.pagination {
  text-align: center;
}
</style>
