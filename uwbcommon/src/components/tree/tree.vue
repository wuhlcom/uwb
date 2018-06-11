<template>
  <div id="tree" @click="unfold($event)"></div>
</template>
<script>
import Tree from './index'
export default {
  name: 'tree',
  props: {
    department: {type: Array},
    person: {type: Array}
  },
  data () {
    return {
      id: {id: 'tree'},
      departmentSelected: [],
      personSelected: [],
      personSelectedArr: []
    }
  },
  methods: {
    initTree () {
      this.tree = new Tree(Object.assign(this.id, this.$props))
    },
    backPerson () {
      this.$emit('getPerson', this.personSelected)
    },
    unfold (e) {
      this.tree._unfold(e)
      let event = e.target
      if (event.nodeName === 'I') {
        if (event.className === 'iconfont icon-fangxingweixuanzhong') {
          let code = event.getAttribute('data-code')
          let parentCode = event.getAttribute('data-parentcode')
          let name = event.getAttribute('data-name')
          // 如果勾选框是部门的话，就将子部门全部展开并选中
          if (name === 'department') {
            this.addDepartment(code)
            this.departmentSelected.forEach(e => {
              document.getElementById('department-' + e).className = 'department'
              document.getElementById('department-' + e).style.color = '#fff'
              document.getElementById('title-' + e).style.color = '#fff'
            })
            this.departmentHook()
            this.personCodeHook()
          }
          if (name === 'prson') {
            this.personSelectedArr.push({code: code, parentCode: parentCode})
            document.getElementById('prson-' + code).parentNode.style.color = '#fff'
          }
          event.className = 'iconfont icon-fangxingxuanzhong'
        } else {
          let code = event.getAttribute('data-code')
          let name = event.getAttribute('data-name')
          // 取消部门选择
          if (name === 'department') {
            this.departmentSelected.forEach(e => {
              document.getElementById('department-' + e).style.color = '#59b3ff'
              document.getElementById('title-' + e).style.color = '#59b3ff'
            })
            this.delDepartment(code)
            this.departmentSelected.forEach(e => {
              document.getElementById('department-' + e).style.color = '#fff'
              document.getElementById('title-' + e).style.color = '#fff'
            })
            this.department.forEach((e, i) => {
              document.getElementById('selected-' + e.departmentCode).className = 'iconfont icon-fangxingweixuanzhong'
            })
            this.person.forEach(e => {
              document.getElementById('prson-' + e.personCode).className = 'iconfont icon-fangxingweixuanzhong'
            })
            this.personSelectedArr.forEach(e => {
              document.getElementById('prson-' + e.code).className = 'iconfont icon-fangxingxuanzhong'
            })
            this.departmentHook()
          }
          // 取消人员选择
          if (name === 'prson') {
            this.personSelectedArr.forEach(e => {
              document.getElementById('prson-' + e.code).className = 'iconfont icon-fangxingweixuanzhong'
              document.getElementById('prson-' + e.code).parentNode.style.color = '#59b3ff'
            })
            this.personSelectedArr.forEach((e, index) => {
              if (e.code === code) {
                this.personSelectedArr.splice(index, 1)
                this.personCodeHook()
              }
            })
          }
          event.className = 'iconfont icon-fangxingweixuanzhong'
        }
      }
    },
    // 勾选项目，其子项目全部添加到选中的部门当中
    addDepartment (code) {
      this.departmentSelected.push(code)
      // 将属于该部门的人员添加进personSelectedArr数组中
      this.person.forEach(e => {
        if (e.departmentCode === code) {
          this.personSelectedArr.push({code: e.personCode, parentCode: e.departmentCode})
        }
      })
      // 使用递归讲该部门下的所有子部门全部勾选
      this.department.forEach(e => {
        if (e.parentCode === code) {
          this.addDepartment(e.departmentCode)
        }
      })
      this.departmentSelected = [...new Set(this.departmentSelected)]
      this.personCodeHook()
    },
    /**
     * 取消部门选中
     * 使用递归将部门的内部部门一起取消选中
     * @param code：取消选中部门的code
     */
    delDepartment (code) {
      this.departmentSelected.forEach((e, i) => {
        if (e === code) {
          let val = this.departmentSelected.splice(i, 1)
          this.department.forEach(el => {
            if (el.parentCode === val[0]) {
              this.delDepartment(el.departmentCode)
            }
          })
        }
      })
      this.delPerson(code)
      this.personCodeHook()
    },
    /** 
     * 取消人员选中
     * 使用递归才能将所有取消选中的人员从personSelectedArr数组中删除
     * @param code：人员所属部门的code
    */
    delPerson (code) {
      this.personSelectedArr.forEach((e, i) => {
        if (e.parentCode == code) {
          this.personSelectedArr.splice(i, 1)
          this.delPerson(code)
        }
      })    
    },
    // 勾选部门改变样式
    departmentHook () {
      this.departmentSelected.forEach(e => {
        document.getElementById('selected-' + e).className = 'iconfont icon-fangxingxuanzhong'
      })
    },
    // 勾选人员改变样式
    personCodeHook () {
      /* 
       * 先将所有人员的字体颜色变为未选中的颜色
       * 然后将选中的人员变为选中的样式
       */
      this.person.forEach(e => {
        document.getElementById('prson-' + e.personCode).parentNode.style.color = '#59b3ff'
      })
      this.personSelectedArr.forEach(e => {
        document.getElementById('prson-' + e.code).className = 'iconfont icon-fangxingxuanzhong'
        document.getElementById('prson-' + e.code).parentNode.style.color = '#fff'
      })
    }
  },
  mounted () {
    this.initTree()
    this.addDepartment('100')
    this.departmentHook()
    this.departmentSelected.forEach(e => {
      document.getElementById('department-' + e).className = 'department'
      document.getElementById('department-' + e).style.color = '#fff'
      document.getElementById('title-' + e).style.color = '#fff'
    })
  },
  watch: {
    personSelectedArr (val) {
      this.personSelected = []
      val.forEach(e => {
        this.personSelected.push(e.code)
      })
      this.backPerson()
    }
  }
}
</script>
