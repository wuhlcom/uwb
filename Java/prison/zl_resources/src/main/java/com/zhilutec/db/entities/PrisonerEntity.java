/**
 * @author :wuhongliang wuhongliang@zhilutec.com
 * @version :2017年10月18日 下午3:54:32 *
 */
package com.zhilutec.db.entities;

import java.util.Date;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;


/**
 * @author zhilu1234
 *
 */
@Table(name = "pr_prisoners")
public class PrisonerEntity extends BaseEntity {

    private String name;// '姓名',
    private String code;//   '编号',
    private String idcard;// '身份证号码',

    @Temporal(TemporalType.DATE)
    @JSONField(format = "yyyy-MM-dd")
    private Date birth;//  '出生日期',
    private Integer level;// '类别',
    private Integer sex;// '性别 0 男 1女',
    private String remark; // '描述',
    private Long createdAt;// '添加时间',
    private Long tagId;    // COMMENT '手环编号',

    private String nativePlace;// '籍贯',
    private String areaCode;// '监仓号',
    private Integer isdel;// '0 存在 1 不存在',
    private Long inTime;
    private String condemnation; //罪名
    private String imprisonment; //刑期
    private String portrait;

    @Transient
    private Integer age;
    @Transient
    private String msg;
    @Transient
    private Integer flag;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public Long getInTime() {
        return inTime;
    }

    public void setInTime(Long inTime) {
        this.inTime = inTime;
    }

    public String getCondemnation() {
        return condemnation;
    }

    public void setCondemnation(String condemnation) {
        this.condemnation = condemnation;
    }

    public String getImprisonment() {
        return imprisonment;
    }

    public void setImprisonment(String imprisonment) {
        this.imprisonment = imprisonment;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "PrisonerEntity{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", idcard='" + idcard + '\'' +
                ", birth=" + birth +
                ", level=" + level +
                ", sex=" + sex +
                ", remark='" + remark + '\'' +
                ", createdAt=" + createdAt +
                ", tagId=" + tagId +
                ", nativePlace='" + nativePlace + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", isdel=" + isdel +
                ", inTime=" + inTime +
                ", condemnation='" + condemnation + '\'' +
                ", imprisonment='" + imprisonment + '\'' +
                ", portrait='" + portrait + '\'' +
                ", age=" + age +
                ", msg='" + msg + '\'' +
                ", flag=" + flag +
                '}';
    }


}
