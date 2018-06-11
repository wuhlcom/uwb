package com.zhilutec.services.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhilutec.db.entities.RedisStatusEnitity;
import com.zhilutec.services.IPrisonerCfgService;
import com.zhilutec.services.IRedisService;
import com.zhilutec.services.IRedisStatusService;

/**
 * out cross status online posCode nosignal 1 1 1 0 0 1 1 0 1 0 1 0 x x 0 0 0 0
 * 1 1 out 电子围栏 0 无报警 1有报警 cross 串仓 0 无报警 1有报警 nosignal 0 无报警 1有报警 status 在线离线 0
 * 离线 1在线 level 报警级别 postCode 最后一次的位置 timestamp 时间
 */
@Service
@Transactional
public class RedisStatusServiceImpl extends IRedisService<RedisStatusEnitity> implements IRedisStatusService {

	private static final String COOR_REDIS_KEY = "TAG_INFO";

	@Resource
	private IPrisonerCfgService prisonerCfgService;

	@Override
	protected String getRedisKey() {
		return COOR_REDIS_KEY;
	}

	/**
	 * 根据tagId查询单个犯人当前考勤状态
	 */
	@Override
	public RedisStatusEnitity getStatusRedis(String tagId) {
		return get(tagId);
	}

	/* 缺勤包括配置的和实时的缺勤 */
	@Override
	public boolean absenceStatus(String code, Long tagId) {
		/*** 查看配置表是否存在配置,存在则缺勤 */
		int count = prisonerCfgService.countPrisonerConf(code);
		if (count > 0) {
			return true;
		}

		/** 配置表不存在查看redis中的状态机查看是否离线，串仓，电子围栏 **/
		return this.redisStatus(tagId);
	}

	/**
	 * out 电子围栏 0 无报警 1有报警 cross 串仓 0 无报警 1有报警 nosignal 0 无报警 1有报警 level 报警级别
	 * postCode 最后一次的位置 timestamp 时间
	 */
	/** 查看redis中的状态机查看是否有串仓，电子围栏,离线,有则认为缺勤 **/
	/*** true 缺勤*** false 实到 **/
	@Override
	public boolean redisStatus(Long tagId) {
		RedisStatusEnitity statusRedis = this.getStatusRedis(tagId.toString());
		/* 如果redis找不到记录则认为缺勤 */
		if (statusRedis == null || statusRedis.equals("null")) {
			return true;
		}

		Integer type = statusRedis.getType();
		Integer out = statusRedis.getOut();
		Integer cross = statusRedis.getCross();
		Integer nosignal = statusRedis.getNosignal();
		/* 当前收到的是坐标且无报警表示此人已到 **/
		if (type != null && type.intValue() == 2) {
			if ((nosignal != null && nosignal == 1) || (out != null && out == 1) || (cross != null && cross == 1)) {
				return true;
			} else {
				return false;
			}
		} else {
			// if (op == null||op == 1) {
			if ((nosignal != null && nosignal == 1) || (out != null && out == 1) || (cross != null && cross == 1)) {
				return true;
			} else {
				return false;
			}		
		}
	}

}