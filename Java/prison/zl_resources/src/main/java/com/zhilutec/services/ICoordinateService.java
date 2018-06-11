package com.zhilutec.services;

import java.util.List;

import com.zhilutec.db.results.PointResult;

public interface ICoordinateService {

	List<PointResult> queryPoints(String jsonStr) throws Exception;


}
