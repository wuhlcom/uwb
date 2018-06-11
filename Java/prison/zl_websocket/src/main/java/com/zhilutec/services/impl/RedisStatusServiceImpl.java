package com.zhilutec.services.impl;


import javax.annotation.Resource;

import com.zhilutec.services.IRedisService;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.zhilutec.common.utils.WarningUtils;
import com.zhilutec.db.entities.CoordinateEntity;
import com.zhilutec.db.entities.RedisStatusEnitity;
import com.zhilutec.services.IPrisonerCfgService;
import com.zhilutec.services.IRedisStatusService;

@Service
public class RedisStatusServiceImpl extends IRedisService<RedisStatusEnitity> implements IRedisStatusService {

	private static final String COOR_REDIS_KEY = "TAG_INFO";

	@Resource
	private IPrisonerCfgService prisonerCfgService;

	@Override
	protected String getRedisKey() {
		return COOR_REDIS_KEY;
	}

	/** 刷新单个囚犯在离线状态表 */
	/* * 2017-12-15 在离线与信息消失报警作用相同，这里不关注在离线消息 */
	@Override
	public void updateRedisStatus(CoordinateEntity record) {
		String tagId = record.getTagId().toString();
		Integer type = record.getType();
		Integer op = record.getOp();
		String posCode = record.getPosCode();
		RedisStatusEnitity redisStatus = this.getStatusRedis(tagId);	
		if (redisStatus == null||redisStatus.equals("null")) {
			// 创建一个对象来设置状态表
			redisStatus = new RedisStatusEnitity();
		}

		// 刷新状态表
		redisStatus.setTimestamp(record.getTimestamp());
		redisStatus.setType(type);
		redisStatus.setOp(op);
		if (posCode != null) {
			redisStatus.setPosCode(posCode);
		}
		redisStatus =this.setStatus(record, type, op, redisStatus);
		put(tagId, redisStatus, 20);
	}

	private RedisStatusEnitity setStatus(CoordinateEntity record, Integer type, Integer op, RedisStatusEnitity redisStatus) {
		// 在线离线报警
		if (type == 1) {
			// int status = record.getStatus();
			// redisStatus.setStatus(status);
		} else if (type == 2) {
			// 能发送坐标但有可能会报警
			// statusRedis.setOnline(1);
		} else if (type == 3) {
			// 电子围栏和串仓
			String warningCode = record.getWarningCode();
			// 收到电子围栏设置为out为1
			if (warningCode.equals(WarningUtils.OUT_CODE) && op == 1) {
				redisStatus.setOut(1);
			// 关闭电子围栏out为0
			} else if (warningCode.equals(WarningUtils.OUT_CODE) && op == 0) {
				redisStatus.setOut(0);
			} else if (warningCode.equals(WarningUtils.CROSS_CODE) && op == 1) {
				redisStatus.setCross(1);
			} else if (warningCode.equals(WarningUtils.CROSS_CODE) && op == 0) {
				redisStatus.setCross(0);
			} else if (warningCode.equals(WarningUtils.NOSIGNAL_CODE) && op == 1) {
				redisStatus.setNosignal(1);
			} else if (warningCode.equals(WarningUtils.NOSIGNAL_CODE) && op == 0) {
				redisStatus.setNosignal(0);
			}
		}
		return redisStatus;
	}

	/** 刷新单个囚犯在离线状态表 */
	@Override
	public void updateRedisStatus(String jsonStr) {
		CoordinateEntity record = JSON.parseObject(jsonStr, CoordinateEntity.class);
		this.updateRedisStatus(record);
	}

	/**
	 * 根据tagId查询单个犯人当前考勤状态
	 */
	@Override
	public RedisStatusEnitity getStatusRedis(String tagId) {
		return get(tagId);
	}

	/** 判断是否为缺勤，包括配置的和实时的缺勤 */
	@Override
	public boolean isAbsence(String code, Long tagId) {
		/*** 查看配置表是否存在配置,存在则缺勤 */
		int count = prisonerCfgService.countPrisonerConf(code);
		if (count > 0) {
			return true;
		}

		/** 配置表不存在查看redis中的状态机查看是否离线，串仓，电子围栏 **/
		return this.isAbsence(tagId);
	}


   /**
	* redis中找不到记录,缺勤
	* type不为2有串仓，电子围栏,离线,有则表示缺勤 ,
	* type为2中无串仓，电子围栏,离线,则表示未缺勤
	* true 缺勤
	* false 未缺勤 
	*/
	@Override
	public boolean  isAbsence(Long tagId) {
		RedisStatusEnitity statusRedis = this.getStatusRedis(tagId.toString());
		/* 如果redis找不到记录则认为缺勤 */
		if (statusRedis == null || statusRedis.equals("null")) {
			return true;
		}

		Integer type = statusRedis.getType();
		Integer out = statusRedis.getOut();
		Integer cross = statusRedis.getCross();
		Integer nosignal = statusRedis.getNosignal();
		/*当前收到的是坐标且无报警表示未缺勤**/		
		if (type != null && type.intValue() == 2) {
			if ((nosignal != null && nosignal == 1) || (out != null && out == 1) || (cross != null && cross == 1)) {
				return true;
			} else {
				return false;
			}
		} else {
			if ((nosignal != null && nosignal == 1) || (out != null && out == 1) || (cross != null && cross == 1)) {
				return true;
			} else {
				return false;
			}		
		}
	}

	/*
	 * 判断当前状态是否为信号消息消失* true 信号消失，false信号未消失
	 */
	@Override
	public boolean isOffline(Long tagId) {
		RedisStatusEnitity statusRedis = this.getStatusRedis(tagId.toString());

		/* 如果redis找不到记录则认为缺勤 */
		if (statusRedis == null || statusRedis.equals("null")) {
			return false;
		}

		Integer nosignal = statusRedis.getNosignal();
		if (nosignal == null) {
			return false;
		}
		if (nosignal == 1) {
			return true;
		} else {
			return false;
		}
	}
	
}