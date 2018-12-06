package com.zhilutec.uwb.entity;

import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "uwb_files")
public class FileEntity extends Base {
    private static final long serialVersionUID = 1L;
    private String name;
    private String fileName;
    private String filePath;
    private String groupName;
    private String remoteName;
    private String remotePath;
    private Double length;
    private Double width;
    private Integer picLength;
    private Integer picWidth;
    private String remark;
    private Integer status;
    private Integer isdel;
    private Long createdAt;
    private Integer type;
    private Long size;
    @Transient
    private String path;


    @Transient
    private String downPath;


    public String getDownPath() {
        return downPath;
    }

    public void setDownPath(String downPath) {
        this.downPath = downPath;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }


    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRemoteName() {
        return remoteName;
    }

    public void setRemoteName(String remoteName) {
        this.remoteName = remoteName;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Integer getPicLength() {
        return picLength;
    }

    public void setPicLength(Integer picLength) {
        this.picLength = picLength;
    }

    public Integer getPicWidth() {
        return picWidth;
    }

    public void setPicWidth(Integer picWidth) {
        this.picWidth = picWidth;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "FileEntity{" +
                "name='" + name + '\'' +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", groupName='" + groupName + '\'' +
                ", remoteName='" + remoteName + '\'' +
                ", remotePath='" + remotePath + '\'' +
                ", length=" + length +
                ", width=" + width +
                ", picLength=" + picLength +
                ", picWidth=" + picWidth +
                ", remark='" + remark + '\'' +
                ", status=" + status +
                ", isdel=" + isdel +
                ", createdAt=" + createdAt +
                ", type=" + type +
                ", size=" + size +
                ", path='" + path + '\'' +
                '}';
    }
}
