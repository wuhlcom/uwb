package com.zhilutec.uwb.dao;

import com.zhilutec.uwb.common.MyMapper;
import com.zhilutec.uwb.entity.FileEntity;

import java.util.List;

public interface FileDao extends MyMapper<FileEntity> {
    List<FileEntity> getFiles(FileEntity fileEntity);

    Integer updateOne(FileEntity fileEntity);

    Integer batchUpdateIsdel(List ids);
}

