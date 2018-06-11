/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年10月18日 下午7:51:39 * 
*/ 
package com.zhilutec.db.entities;

import javax.persistence.Entity;

@Entity(name="pr_resources")
public class ResourceEntity extends BaseEntity  {

	private String resource_name; //`resource_name` varchar(64) NOT NULL COMMENT '资源名',
	private String resource_url;//`resource_url` varchar(64) NOT NULL,
	private Integer order_num; //`order_num` tinyint(4) DEFAULT NULL COMMENT '排序编码',
	private Long parent_id;//`parent_id` int(20) NOT NULL COMMENT '父资源ID 0表示顶层资源',
	private Integer type ;//`type` tinyint(1) DEFAULT NULL COMMENT '资源类型 0 目录 1 菜单  2 按钮',
	private String premission;//`permission` varchar(255) NOT NULL COMMENT '权限',
	
	public String getResource_name() {
		return resource_name;
	}
	public void setResource_name(String resource_name) {
		this.resource_name = resource_name;
	}
	public String getResource_url() {
		return resource_url;
	}
	public void setResource_url(String resource_url) {
		this.resource_url = resource_url;
	}
	public Integer getOrder_num() {
		return order_num;
	}
	public void setOrder_num(Integer order_num) {
		this.order_num = order_num;
	}
	public Long getParent_id() {
		return parent_id;
	}
	public void setParent_id(Long parent_id) {
		this.parent_id = parent_id;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getPremission() {
		return premission;
	}
	public void setPremission(String premission) {
		this.premission = premission;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ResourceEntity [resource_name=" + resource_name + ", resource_url=" + resource_url + ", order_num="
				+ order_num + ", parent_id=" + parent_id + ", type=" + type + ", premission=" + premission + "]";
	}
	  
}
