export default class OrgChart {
  constructor (options) {
    this._name = 'OrgChart'
    Promise.prototype.finally = function (callback) {
      let P = this.constructor

      return this.then(
        value => P.resolve(callback()).then(() => value),
        reason => P.resolve(callback()).then(() => { throw reason })
      )
    }

    let that = this,
      // 基本选项
      defaultOptions = {
        'nodeTitle': 'departmentName',
        'nodeId': 'id',
        'toggleSiblingsResp': false,
        'depth': 999,
        'chartClass': '',
        'exportButton': false,
        'exportFilename': 'OrgChart',
        'parentNodeSymbol': '',
        'draggable': false,
        'direction': 't2b',
        'pan': true,
        'zoom': true,
        'toggleCollapse': true,
        'nodeContent': 'nihao',
        'departmentCode': ''
      },
      opts = Object.assign(defaultOptions, options), // 合并对象
      data = opts.data, // 需要构建组织结构的对象
      chart = document.createElement('div'), // 创建div
      chartContainer = document.querySelector(opts.chartContainer)

    this.options = opts
    delete this.options.data
    this.chart = chart
    this.chartContainer = chartContainer
    chart.dataset.options = JSON.stringify(opts)
    chart.setAttribute('class', 'orgchart' + (opts.chartClass !== '' ? ' ' + opts.chartClass : '') +
      (opts.direction !== 't2b' ? ' ' + opts.direction : ''))
    if (typeof data === 'object') { // local json datasource
      this.buildHierarchy(chart, opts.ajaxURL ? data : this._attachRel(data, '00'), 0)
    } else if (typeof data === 'string' && data.startsWith('#')) { // ul datasource
      this.buildHierarchy(chart, this._buildJsonDS(document.querySelector(data).children[0]), 0)
    } else {
    }
    if (opts.pan) {
      chartContainer.style.overflow = 'hidden'
      chart.addEventListener('mousedown', this._onPanStart.bind(this))
      chart.addEventListener('touchstart', this._onPanStart.bind(this))
      document.body.addEventListener('mouseup', this._onPanEnd.bind(this))
      document.body.addEventListener('touchend', this._onPanEnd.bind(this))
    }
    if (opts.zoom) {
      chartContainer.addEventListener('wheel', this._onWheeling.bind(this))
      chartContainer.addEventListener('touchstart', this._onTouchStart.bind(this))
      document.body.addEventListener('touchmove', this._onTouchMove.bind(this))
      document.body.addEventListener('touchend', this._onTouchEnd.bind(this))
    }
    chartContainer.appendChild(chart)
  }
  _attachRel (data, flags) {
    /* 当data有children并且长度大于0时，data的relationship等于flags加1，反之加0 */
    data.relationship = flags + (data.children && data.children.length > 0 ? 1 : 0)
    /*
    * 如果data有children属性，那么就对data进行递归
    * 递归时第二个参数是根据判断第一个参数的children的长度来判断的
    * 当长度大于1时为'11',小于时为'10'
    */
    if (data.children) {
      for (let item of data.children) {
        this._attachRel(item, '1' + (data.children.length > 1 ? 1 : 0))
      }
    }
    return data
  }
  // 建立层次结构
  buildHierarchy (appendTo, nodeData, level, callback) {
    // 构造节点
    let that = this,
      opts = this.options,
      nodeWrapper,
      childNodes = nodeData.children,
      isVerticalNode = opts.verticalDepth && (level + 1) >= opts.verticalDepth
    if (Object.keys(nodeData).length > 1) { // 如果nodeData具有嵌套结构
      nodeWrapper = isVerticalNode ? appendTo : document.createElement('table')
      if (!isVerticalNode) {
        appendTo.appendChild(nodeWrapper)
      }
      this._createNode(nodeData, level)
        .then(function (nodeDiv) {
          if (isVerticalNode) {
            nodeWrapper.insertBefore(nodeDiv, nodeWrapper.firstChild)
          } else {
            let tr = document.createElement('tr')

            tr.innerHTML = `
            <td ${childNodes ? `colspan="${childNodes.length * 2}"` : ''}>
            </td>
          `
            tr.children[0].appendChild(nodeDiv)
            nodeWrapper.insertBefore(tr, nodeWrapper.children[0] ? nodeWrapper.children[0] : null)
          }
          if (callback) {
            callback()
          }
        })
        .catch(function (err) {
          console.error('Failed to creat node', err)
        })
    }
    // Construct the inferior nodes and connectiong lines
    if (childNodes && childNodes.length !== 0) {
      if (Object.keys(nodeData).length === 1) { // if nodeData is just an array
        nodeWrapper = appendTo
      }
      let isHidden,
        isVerticalLayer = opts.verticalDepth && (level + 2) >= opts.verticalDepth,
        inEdit = that.chart.dataset.inEdit

      if (inEdit) {
        isHidden = inEdit === 'addSiblings' ? '' : ' hidden'
      } else {
        isHidden = level + 1 >= opts.depth ? ' hidden' : ''
      }

      // draw the line close to parent node
      if (!isVerticalLayer) {
        let tr = document.createElement('tr')

        tr.setAttribute('class', 'lines' + isHidden)
        tr.innerHTML = `
          <td colspan="${childNodes.length * 2}">
            <div class="downLine"></div>
          </td>
        `
        nodeWrapper.appendChild(tr)
      }
      // draw the lines close to children nodes
      let lineLayer = document.createElement('tr')
      lineLayer.setAttribute('class', 'lines' + isHidden)
      lineLayer.innerHTML = `
        <td class="rightLine">&nbsp;</td>
        ${childNodes.slice(1).map(() => `
          <td class="leftLine topLine">&nbsp;</td>
          <td class="rightLine topLine">&nbsp;</td>
          `).join('')}
        <td class="leftLine">&nbsp;</td>
      `
      let nodeLayer

      if (isVerticalLayer) {
        nodeLayer = document.createElement('ul')
        if (isHidden) {
          nodeLayer.classList.add(isHidden.trim())
        }
        if (level + 2 === opts.verticalDepth) {
          let tr = document.createElement('tr')

          tr.setAttribute('class', 'verticalNodes' + isHidden)
          tr.innerHTML = `<td></td>`
          tr.firstChild.appendChild(nodeLayer)
          nodeWrapper.appendChild(tr)
        } else {
          nodeWrapper.appendChild(nodeLayer)
        }
      } else {
        nodeLayer = document.createElement('tr')
        nodeLayer.setAttribute('class', 'nodes' + isHidden)
        nodeWrapper.appendChild(lineLayer)
        nodeWrapper.appendChild(nodeLayer)
      }
      // recurse through children nodes
      childNodes.forEach((child) => {
        let nodeCell

        if (isVerticalLayer) {
          nodeCell = document.createElement('li')
        } else {
          nodeCell = document.createElement('td')
          nodeCell.setAttribute('colspan', 2)
        }
        nodeLayer.appendChild(nodeCell)
        that.buildHierarchy(nodeCell, child, level + 1, callback)
      })
    }
  }
  // 创建节点
  _createNode (nodeData, level) {
    let that = this,
      opts = this.options
    return new Promise(function (resolve, reject) {
      if (nodeData.children) {
        for (let child of nodeData.children) {
          child.parentId = nodeData.id
        }
      }
      // 创建DIV
      let nodeDiv = document.createElement('div')
      delete nodeData.children
      // nodeDiv.dataset.source = JSON.stringify(nodeData);
      if (nodeData[opts.nodeId]) {
        nodeDiv.id = nodeData[opts.nodeId]
      }
      let inEdit = that.chart.dataset.inEdit,
        isHidden

      if (inEdit) {
        isHidden = inEdit === 'addChildren' ? ' slide-up' : ''
      } else {
        isHidden = level >= opts.depth ? ' slide-up' : ''
      }
      nodeDiv.setAttribute('class', 'node ' + (nodeData.className || '') + isHidden)
      // nodeDiv.setAttribute('data-code', nodeData.departmentCode)
      /*
      * 创建标题
      * 标题名是传入对象的name属性
      */
      if (nodeData.departmentCode !== '100') {
        nodeDiv.innerHTML = `
        <div class="title" data-code="${nodeData.departmentCode}">${nodeData[opts.nodeTitle]}</div>
        <div class='functional'>
        <i class="iconfont icon-jia" data-code="${nodeData.departmentCode}"></i>
        <i class="iconfont icon-bianji" data-code="${nodeData.departmentCode}"></i>
        <i class="iconfont icon-lajitong" data-code="${nodeData.departmentCode}"></i>
        </div>
        ${opts.nodeContent ? `<div class="content">${nodeData[opts.nodeContent]}</div>` : ''}
        `
     } else {
        nodeDiv.innerHTML = `
        <div class="title" data-code="${nodeData.departmentCode}">${nodeData[opts.nodeTitle]}</div>
        <div class='functional'>
        <i class="iconfont icon-jia" data-code="${nodeData.departmentCode}"></i>
        <i class="iconfont icon-bianji" data-code="${nodeData.departmentCode}"></i>
        </div>
        ${opts.nodeContent ? `<div class="content">${nodeData[opts.nodeContent]}</div>` : ''}
        `
     }
      let flags = nodeData.relationship || ''
      // 给节点添加点击事件
      nodeDiv.addEventListener('click', that._dispatchClickEvent)
      if (opts.createNode) {
        opts.createNode(nodeDiv, nodeData)
      }
      resolve(nodeDiv)
    })
  }
  _onPanStart (event) {
    let chart = event.currentTarget

    if (this._closest(event.target, (el) => el.classList && el.classList.contains('node')) ||
      (event.touches && event.touches.length > 1)) {
      chart.dataset.panning = false
      return
    }
    chart.style.cursor = 'move'
    chart.dataset.panning = true

    let lastX = 0,
      lastY = 0,
      lastTf = window.getComputedStyle(chart).transform

    if (lastTf !== 'none') {
      let temp = lastTf.split(',')

      if (!lastTf.includes('3d')) {
        lastX = Number.parseInt(temp[4], 10)
        lastY = Number.parseInt(temp[5], 10)
      } else {
        lastX = Number.parseInt(temp[12], 10)
        lastY = Number.parseInt(temp[13], 10)
      }
    }
    let startX = 0,
      startY = 0

    if (!event.targetTouches) { // pan on desktop
      startX = event.pageX - lastX
      startY = event.pageY - lastY
    } else if (event.targetTouches.length === 1) { // pan on mobile device
      startX = event.targetTouches[0].pageX - lastX
      startY = event.targetTouches[0].pageY - lastY
    } else if (event.targetTouches.length > 1) {
      return
    }
    chart.dataset.panStart = JSON.stringify({ 'startX': startX, 'startY': startY })
    chart.addEventListener('mousemove', this._onPanning.bind(this))
    chart.addEventListener('touchmove', this._onPanning.bind(this))
  }
  _onPanning (event) {
    let chart = event.currentTarget

    if (chart.dataset.panning === 'false') {
      return
    }
    let newX = 0,
      newY = 0,
      panStart = JSON.parse(chart.dataset.panStart),
      startX = panStart.startX,
      startY = panStart.startY

    if (!event.targetTouches) { // pand on desktop
      newX = event.pageX - startX
      newY = event.pageY - startY
    } else if (event.targetTouches.length === 1) { // pan on mobile device
      newX = event.targetTouches[0].pageX - startX
      newY = event.targetTouches[0].pageY - startY
    } else if (event.targetTouches.length > 1) {
      return
    }
    let lastTf = window.getComputedStyle(chart).transform

    if (lastTf === 'none') {
      if (!lastTf.includes('3d')) {
        chart.style.transform = 'matrix(1, 0, 0, 1, ' + newX + ', ' + newY + ')'
      } else {
        chart.style.transform = 'matrix3d(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, ' + newX + ', ' + newY + ', 0, 1)'
      }
    } else {
      let matrix = lastTf.split(',')

      if (!lastTf.includes('3d')) {
        matrix[4] = newX
        matrix[5] = newY + ')'
      } else {
        matrix[12] = newX
        matrix[13] = newY
      }
      chart.style.transform = matrix.join(',')
    }
  }
  _onPanEnd (event) {
    let chart = this.chart

    if (chart.dataset.panning === 'true') {
      chart.dataset.panning = false
      chart.style.cursor = 'default'
      document.body.removeEventListener('mousemove', this._onPanning)
      document.body.removeEventListener('touchmove', this._onPanning)
    }
  }
  _setChartScale (chart, newScale) {
    let lastTf = window.getComputedStyle(chart).transform

    if (lastTf === 'none') {
      chart.style.transform = 'scale(' + newScale + ',' + newScale + ')'
    } else {
      let matrix = lastTf.split(',')

      if (!lastTf.includes('3d')) {
        matrix[0] = 'matrix(' + newScale
        matrix[3] = newScale
        chart.style.transform = lastTf + ' scale(' + newScale + ',' + newScale + ')'
      } else {
        chart.style.transform = lastTf + ' scale3d(' + newScale + ',' + newScale + ', 1)'
      }
    }
    chart.dataset.scale = newScale
  }
  _onWheeling (event) {
    event.preventDefault()

    let newScale = event.deltaY > 0 ? 0.8 : 1.2

    this._setChartScale(this.chart, newScale)
  }
  _getPinchDist (event) {
    return Math.sqrt((event.touches[0].clientX - event.touches[1].clientX) *
      (event.touches[0].clientX - event.touches[1].clientX) +
      (event.touches[0].clientY - event.touches[1].clientY) *
      (event.touches[0].clientY - event.touches[1].clientY))
  }
  _onTouchStart (event) {
    let chart = this.chart

    if (event.touches && event.touches.length === 2) {
      let dist = this._getPinchDist(event)

      chart.dataset.pinching = true
      chart.dataset.pinchDistStart = dist
    }
  }
  _onTouchMove (event) {
    let chart = this.chart

    if (chart.dataset.pinching) {
      let dist = this._getPinchDist(event)

      chart.dataset.pinchDistEnd = dist
    }
  }
  _onTouchEnd (event) {
    let chart = this.chart

    if (chart.dataset.pinching) {
      chart.dataset.pinching = false
      let diff = chart.dataset.pinchDistEnd - chart.dataset.pinchDistStart

      if (diff > 0) {
        this._setChartScale(chart, 1)
      } else if (diff < 0) {
        this._setChartScale(chart, -1)
      }
    }
  }
  _closest (el, fn) {
    return el && ((fn(el) && el !== this.chart) ? el : this._closest(el.parentNode, fn))
  }
}
