package com.zhilutec.dao;


import com.zhilutec.entity.FileEntity;

import java.util.List;

public interface FileDao {
    List<FileEntity> getFiles(FileEntity fileEntity);

    Integer updateOne(FileEntity fileEntity);

    Integer batchUpdateIsdel(List ids);
}

