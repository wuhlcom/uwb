package com.zhilutec.services.impl;

import com.zhilutec.common.utils.ConstantUtil;
import com.zhilutec.dbs.entities.Warning;
import com.zhilutec.services.IRedisService;
import com.zhilutec.services.IWarningService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class WarningServiceImpl extends IRedisService<Warning> implements IWarningService {

    @Override
    public List<Warning> getRedisWarnings(String key) {
        return this.getAll(key);
    }

    /**
     * @param key   tagId+strategyCode
     * @param field level
     */
    @Override
    public Warning getReidsWarning(String key, String field) {
        return this.get(key, field);
    }

    @Override
    public Warning getReidsWarning(Long tagId, String strategyCode, Integer level) {
        String warningKey = ConstantUtil.FENCE_ALARM_KEY_PRE + ":" + Long.toString(tagId) + ":" + strategyCode;
        return this.get(warningKey, level.toString());
    }

    @Override
    public void deleteRedisWarning(String key, String field) {
        this.remove(key, field);
    }


    @Override
    public void deleteRedisWarning(Long tagId, String strategyCode, Integer level) {
        String warningKey = ConstantUtil.FENCE_ALARM_KEY_PRE + ":" + Long.toString(tagId) + ":" + strategyCode;
        this.deleteRedisWarning(warningKey, level.toString());
    }

    @Override
    public void deleteRedisWarning(Long tagId, Integer warningType) {
        String wType = Integer.toString(warningType);
        String warningKey = ConstantUtil.FENCE_ALARM_KEY_PRE + ":" + Long.toString(tagId) + ":" + wType;
        this.deleteRedisWarning(warningKey, wType);
    }


    @Override
    public void putRedisWarning(Long tagId, String strategyCode, Integer level, Warning warning, long expire) {
        String warningKey = ConstantUtil.FENCE_ALARM_KEY_PRE + ":" + Long.toString(tagId) + ":" + strategyCode;
        this.put(warningKey, level.toString(), warning, expire);
    }

    @Override
    public void addRedisWarning(Long tagId, Integer warningType, Warning warning, long expire) {
        String wType = Integer.toString(warningType);
        String warningKey = ConstantUtil.FENCE_ALARM_KEY_PRE + ":" + Long.toString(tagId) + ":" + wType;
        this.put(warningKey, wType, warning, expire);
    }

    @Override
    public List<Warning> getRedisWarningList(Long tagId) {
        String warningKey = ConstantUtil.FENCE_ALARM_KEY_PRE + ":" + Long.toString(tagId);
        return this.listAll(warningKey);
    }


    @Override
    public List<String> getRedisStrList(Long tagId) {
        String warningKey = ConstantUtil.FENCE_ALARM_KEY_PRE + ":" + Long.toString(tagId);
        return this.listStr(warningKey);
    }



    @Override
    public void addRedisWarning(Long tagId, Warning warning) {
        String warningKey = ConstantUtil.FENCE_ALARM_KEY_PRE + ":" + Long.toString(tagId);
        this.lelfPush(warningKey, warning, ConstantUtil.REDIS_DEFAULT_TTL);
    }

    @Override
    public void addRedisWarning(Long tagId, String str) {
        String warningKey = ConstantUtil.FENCE_ALARM_KEY_PRE + ":" + Long.toString(tagId);
        this.lelfPushStr(warningKey, str, ConstantUtil.REDIS_DEFAULT_TTL);
    }

    //序列化的对象使用lrem key,count obj无法删除
    @Override
    public void deleteRedisWarning(Long tagId, Warning warning) {
        String warningKey = ConstantUtil.FENCE_ALARM_KEY_PRE + ":" + Long.toString(tagId);
        this.remove(warningKey, 0, warning);
    }

    @Override
    public void deleteRedisWarning(Long tagId, String str) {
        String warningKey = ConstantUtil.FENCE_ALARM_KEY_PRE + ":" + Long.toString(tagId);
        this.remove(warningKey, 0, str);
    }

    @Override
    protected String getRedisKey() {
        return null;
    }

}
