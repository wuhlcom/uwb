package com.zhilutec.uwb.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


import com.zhilutec.uwb.common.pojo.UpgradeInfo;
import com.zhilutec.uwb.common.pojo.VersionInfo;
import com.zhilutec.uwb.common.util.UpgradeValidator;
import com.zhilutec.uwb.config.UpgradeConfig;
import com.zhilutec.uwb.netty.tcpServer.TcpHandler;
import com.zhilutec.uwb.result.Result;
import com.zhilutec.uwb.result.ResultCode;
import com.zhilutec.uwb.service.IRedisService;
import com.zhilutec.uwb.service.IUpgradeService;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhilutec.uwb.util.ConstantUtil;


import javax.annotation.Resource;
import java.util.Map;


@Service
public class UpgradeServiceImpl implements IUpgradeService {

    @Autowired
    TcpHandler tcpHandler;

    @Autowired
    UpgradeConfig upgradeConfig;

    @Resource
    IRedisService redisService;

    @Override
    public void initUpgradeStatus() {
        redisService.delAll();
        UpgradeInfo upgradeInfo = new UpgradeInfo();
        upgradeInfo.setUpStatus(ConstantUtil.UPGRADE_STAT_DEFAULT);
        upgradeInfo.setCmdType(ConstantUtil.UPGRADE_TYPE);
        upgradeInfo.setCmdDir(ConstantUtil.UPGRADE_RESP);
        upgradeInfo.setType(ConstantUtil.UPGRADE_MSG_TYPE);

        String key = ConstantUtil.UPGRADE;
        String str = JSON.toJSONString(upgradeInfo);
        Map map = JSONObject.parseObject(str, Map.class);
        redisService.hashAddMap(key, map, ConstantUtil.REDIS_DEFAULT_TTL);
    }


    @Override
    public Map<String, Channel> getActiveChannel() {
        Map<String, Channel> concurrentHashMap = TcpHandler.getSessionChMap();
        for (Map.Entry<String, Channel> entry : concurrentHashMap.entrySet()) {
            //去除无效会话
            if (!entry.getValue().isActive()) {
                concurrentHashMap.remove(entry.getKey());
            }
        }
        return concurrentHashMap;
    }

    @Override
    public String getVersion() {
        //获取tcp会话
        Map<String, Channel> concurrentHashMap = this.getActiveChannel();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cmd_type", "check_ver");
        jsonObject.put("cmd_dir", "query");

        //通过tcp channel发送请求,获取版本信息
        for (Map.Entry<String, Channel> entry : concurrentHashMap.entrySet()) {
            Channel channel = entry.getValue();
            channel.writeAndFlush(jsonObject.toJSONString());
        }

        //获取版本信息
        VersionInfo versionInfo = TcpHandler.getVersionInfo();
        versionInfo.setServerIP(upgradeConfig.getUpgradeIP());
        versionInfo.setServerPort(upgradeConfig.getUpgradePort());

        if (versionInfo.getAncVer() == null) {
            versionInfo.setAncVer("");
        }
        if (versionInfo.getTagVer() == null) {
            versionInfo.setTagVer("");
        }

        if (versionInfo.getAncUpTime() == null) {
            versionInfo.setAncUpTime(0L);
        }

        if (versionInfo.getTagUpTime() == null) {
            versionInfo.setTagUpTime(0L);
        }

        return Result.ok(versionInfo).toJSONString();
    }

    /**
     * 升级前要判断当前是否正在升级，防止重复升级
     * 当前版本与要升级的版本相同时，不允许升级
     */
    @Override
    public String upgrade(JSONObject jsonObject) throws InterruptedException {
        Result rs = UpgradeValidator.upgradeVal(jsonObject);
        if ((Integer) rs.get("errcode") != ResultCode.SUCCESS.getCode()) {
            return rs.toJSONString();
        }

        UpgradeInfo upgradeInfo = this.getUpgradeStatus();
        Integer status = upgradeInfo.getUpStatus();
        if (status == ConstantUtil.UPGRADE_STAT_RUNN) {
            return Result.error("正在升级中，请稍后再试...").toJSONString();
        } else if (status == ConstantUtil.UPGRADE_STAT_REPEAT) {
            return Result.error("当前版本与升级的版本相同不需要升级").toJSONString();
        } else {
            UpgradeInfo upgradeInfoChange = new UpgradeInfo();
            upgradeInfoChange.setUpStatus(ConstantUtil.UPGRADE_STAT_RUNN);
            upgradeInfoChange.setCmdType(ConstantUtil.UPGRADE_TYPE);
            upgradeInfoChange.setCmdDir(ConstantUtil.UPGRADE_RESP);
            upgradeInfoChange.setType(ConstantUtil.UPGRADE_MSG_TYPE);
            this.updateUpgradeStatus(upgradeInfoChange);
        }

        Map<String, Channel> concurrentHashMap = this.getActiveChannel();
        String serverIp = jsonObject.getString("serverIP");
        Long serverPort = jsonObject.getLong("serverPort");
        String filePath = jsonObject.getString("downPath");
        String name = jsonObject.getString("name");
        String server = null;

        if (serverIp != null && serverPort != null) {
            server = serverIp + ":" + serverPort.toString();
        } else {
            return Result.error("服务器地址错误").toJSONString();
        }

        jsonObject.put("cmd_type", ConstantUtil.UPGRADE_TYPE);
        jsonObject.put("cmd_dir", ConstantUtil.UPGRADE_QUERY);
        jsonObject.put("server", server);
        jsonObject.put("file_path", filePath);
        jsonObject.put("anc_ver", name);
        for (Map.Entry<String, Channel> entry : concurrentHashMap.entrySet()) {
            Channel channel = entry.getValue();
            channel.writeAndFlush(jsonObject.toJSONString());
        }

        Thread.sleep(500);

        UpgradeInfo upgradeInfo1 = this.getUpgradeStatus();
        return Result.ok(upgradeInfo1).toJSONString();
    }


    private UpgradeInfo getUpgradeStatus() {
        Map upgradeMap = redisService.hashGetMap(ConstantUtil.UPGRADE);
        String jsonStr = JSONObject.toJSONString(upgradeMap);
        return JSONObject.parseObject(jsonStr, UpgradeInfo.class);
    }


    @Override
    public void updateUpgradeStatus(UpgradeInfo upgradeInfo) {
        String key = ConstantUtil.UPGRADE;
        String str = JSON.toJSONString(upgradeInfo);
        Map map = JSONObject.parseObject(str, Map.class);
        redisService.hashAddMap(key, map, ConstantUtil.REDIS_DEFAULT_TTL);
    }

}
