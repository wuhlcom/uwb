/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年10月18日 下午3:55:47 * 
*/
package com.zhilutec.dbs.entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

public class Base {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Transient
	private Integer page;

	@Transient
	private Integer listRows;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getListRows() {
		return listRows;
	}

	public void setListRows(Integer listRows) {
		this.listRows = listRows;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

}
