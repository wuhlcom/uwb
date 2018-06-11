package com.zhilutec.db.daos;
import java.util.List;

import com.zhilutec.common.mappers.*;
import com.zhilutec.db.entities.TagWarningEntity;
import com.zhilutec.db.params.WarningParam;

import com.zhilutec.db.results.WarningResult;
public interface TagWarningDao extends MyMapper<TagWarningEntity>,BaseDao<TagWarningEntity> {
	 List<WarningResult> queryWarnings(WarningParam paramObj);
}
