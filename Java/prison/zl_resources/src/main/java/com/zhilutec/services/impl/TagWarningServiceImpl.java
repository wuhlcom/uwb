package com.zhilutec.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhilutec.db.daos.TagWarningDao;
import com.zhilutec.db.results.WarningStatusResult;
import com.zhilutec.services.ITagWarningService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.utils.WarningUtil;
import com.zhilutec.common.utils.ZlTimeUtil;

@Service
@Transactional
public class TagWarningServiceImpl implements ITagWarningService {

	@Autowired
	private TagWarningDao tagWarningDao;

}