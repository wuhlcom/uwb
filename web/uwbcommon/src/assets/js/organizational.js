function organizational (id, obj) {
  function single () {
    let id = 'aa' + this.obj.id
    let chi = document.createElement('div')
    chi.setAttribute('id', id)
    chi.style.width = '100%'
    document.getElementById('view').appendChild(chi)
    this.recursion('aa1', this.obj.id, 'xxx')
    this.parent(this.obj.children, id)
    this.organization(this.obj)
  }
}
