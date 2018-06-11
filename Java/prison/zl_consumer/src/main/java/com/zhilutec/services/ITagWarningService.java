package com.zhilutec.services;

import java.util.List;

import com.zhilutec.common.result.Result;
import com.zhilutec.db.entities.CoordinateEntity;
import com.zhilutec.db.entities.TagWarningEntity;

public interface ITagWarningService {
	int saveWarning(TagWarningEntity warning);

	int saveWarning(String jsonStr);
    void saveBatch(List<TagWarningEntity> records);
}
