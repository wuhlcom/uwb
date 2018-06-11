/*
* 顶级部门的personCode一定要为''，如果结构不显示，首先查看顶级部门的personCode的值
*/
export default class Tree {
  constructor (options) {
    Promise.prototype.finally = function (callback) {
      let P = this.constructor
      return this.then(
        value => P.resolve(callback()).then(() => value),
        reason => P.resolve(callback()).then(() => { throw reason })
      )
    }
    this.opt = options
    this.opt.department.forEach(e => {
      if (e.parentCode === '') {
        this._createNode(this.opt.id, e.departmentCode, e.departmentName)
        this._createOrg(e.departmentCode, this.opt.department)
      } else {
        this._createOrg(e.departmentCode, this.opt.department)
      }
    })
    if (this.opt.person.length>0) {
      this.opt.person.forEach(e => {
        this._addPerson(e.departmentCode, e.personName, e.personCode)
      })
    }
  }
  // 创建节点
  _createNode (parentID, id, title) {
    if (document.getElementById('department-' + id)) return
    let parentNode = document.getElementById(parentID)
    let titleNode = document.createElement('div')
    let childBoxNode = document.createElement('div')
    let selected = document.createElement('i')
    let titleText = document.createTextNode(title)
    selected.setAttribute('class', 'iconfont icon-fangxingweixuanzhong')
    selected.setAttribute('data-code', id)
    selected.setAttribute('data-name', 'department')
    selected.setAttribute('id', 'selected-' + id)
    titleNode.setAttribute('class', 'tree-title')
    titleNode.setAttribute('data-code', id)
    titleNode.setAttribute('id', 'title-' + id)
    titleNode.appendChild(selected)
    titleNode.appendChild(titleText)
    childBoxNode.setAttribute('id', 'department-' + id)
    childBoxNode.setAttribute('class', 'department hide')
    parentNode.appendChild(titleNode)
    parentNode.appendChild(childBoxNode)
  }
  // 创建结构
  _createOrg (id, arr) {
    if (!document.getElementById('department-' + id)) {
      arr.forEach(e => {
        if (e.departmentCode === id) {
          this._createOrg(e.parentCode, arr)
        }
      })
    } else {
      arr.forEach(e => {
        if (e.parentCode === id) {
          this._createNode('department-' + id, e.departmentCode, e.departmentName)
        }
      })
    }
  }
  // 向部门添加人员
  _addPerson (id, name, code,) {
    let parent = document.getElementById('department-' + id)
    let nameNode = document.createElement('div')
    let nameText = document.createTextNode(name)
    let selected = document.createElement('i')
    selected.setAttribute('class', 'iconfont icon-fangxingweixuanzhong')
    selected.setAttribute('data-code', code)
    selected.setAttribute('data-parentcode', id)
    selected.setAttribute('data-name', 'prson')
    selected.setAttribute('id', 'prson-' + code)
    nameNode.setAttribute('class', 'prson-name')
    nameNode.appendChild(selected)
    nameNode.appendChild(nameText)
    parent.insertBefore(nameNode, parent.children[0])
  }
  // 展开部门
  _unfold (e) {
    let event = e.target
    if (event.className === 'tree-title' && event.nodeName === 'DIV') {
      let code = e.target.getAttribute('data-code')
      let name = document.getElementById('department-' + code).className
      if (name === 'department hide') {
        document.getElementById('department-' + code).className = 'department'
      } else {
        document.getElementById('department-' + code).className = 'department hide'
      }
    }
  }
}
