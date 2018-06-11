package com.zhilutec.dbs.daos;

import com.zhilutec.common.mappers.MyMapper;
import com.zhilutec.dbs.entities.FileEntity;

import java.util.List;

public interface FileDao extends MyMapper<FileEntity>, BaseDao<FileEntity> {
    List<FileEntity> getFiles(FileEntity fileEntity);

    Integer updateOne(FileEntity fileEntity);

    Integer batchUpdateIsdel(List ids);
}

