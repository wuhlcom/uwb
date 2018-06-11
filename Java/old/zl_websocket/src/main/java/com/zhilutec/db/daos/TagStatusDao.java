package com.zhilutec.db.daos;
import java.util.List;

import com.zhilutec.common.mappers.*;
import com.zhilutec.db.entities.TagStatusEntity;
import com.zhilutec.db.params.StatusParam;
import com.zhilutec.db.results.StatusResult;
public interface TagStatusDao extends MyMapper<TagStatusEntity>,BaseDao<TagStatusEntity> {
	 List<StatusResult> queryStatus(StatusParam paramObj);
//	 StatusResult queryStatus(@Param(value="tagId") Long tagId);
}
