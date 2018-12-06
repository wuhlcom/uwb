package com.zhilutec.dbs.daos;

import java.util.List;
import java.util.Map;

public interface BaseDao<T> {

//	void save(FdfsStorageClient t);
//
//	void save(Map<String, Object> map);
//
//	void saveBatch(List<FdfsStorageClient> list);
//
//	int update(FdfsStorageClient t);
//
//	int update(Map<String, Object> map);
//
//	int delete(Object id);
//
//	int delete(Map<String, Object> map);
//
//	int deleteBatch(Object[] id);

    T queryObject(Object id);

    List<T> queryList(Map<String, Object> map);

    List<T> queryList(Object id);

//	int queryTotal(Map<String, Object> map);
//
//	int queryTotal();
}
