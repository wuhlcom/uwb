package com.zhilutec.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.pojos.UpgradeInfo;
import com.zhilutec.common.pojos.VersionInfo;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.common.utils.ConstantUtil;
import com.zhilutec.common.utils.RestUtil;
import com.zhilutec.common.utils.UpgradeValidator;
import com.zhilutec.netty.tcpServer.TcpHandler;
import com.zhilutec.services.IRedisService;
import com.zhilutec.services.IUpgradeService;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Map;


@Service
public class UpgradeServiceImpl extends IRedisService<UpgradeInfo> implements IUpgradeService {

    @Autowired
    TcpHandler tcpHandler;

    public Map<String, Channel> getActiveChannel() {
        Map<String, Channel> concurrentHashMap = TcpHandler.getSessionChMap();
        for (Map.Entry<String, Channel> entry : concurrentHashMap.entrySet()) {
            if (!entry.getValue().isActive()) {
                concurrentHashMap.remove(entry.getKey());
            }
        }
        return concurrentHashMap;
    }

    @Override
    public String getVersion() {
        Map<String, Channel> concurrentHashMap = this.getActiveChannel();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cmd_type", "check_ver");
        jsonObject.put("cmd_dir", "query");

        for (Map.Entry<String, Channel> entry : concurrentHashMap.entrySet()) {
            Channel channel = entry.getValue();
            channel.writeAndFlush(jsonObject.toJSONString());
        }

        // Thread.sleep(500);
        VersionInfo versionInfo = TcpHandler.getVersionInfo();
        Map<String, Object> serverInfo = RestUtil.getServerinfo();
        Integer errcode = (Integer) serverInfo.get("errcode");
        if (errcode.intValue() != ResultCode.SUCCESS.getCode()) {
            return serverInfo.toString();
        }
        versionInfo.setServerIP((String) serverInfo.get("serverIP"));
        versionInfo.setServerPort((Integer) serverInfo.get("serverPort"));

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

        UpgradeInfo upgradeInfo = getUpgradeStatus();
        Integer status = upgradeInfo.getUpStatus();
        if (status == ConstantUtil.UPGRADE_STAT_RUNN) {
            return Result.error("正在升级中，请稍后再试...").toJSONString();
        } else if (status == ConstantUtil.UPGRADE_STAT_REPEAT) {
            return Result.error("当前版本与升级的版本相同不需要升级").toJSONString();
        }else {
            UpgradeInfo upgradeInfoChange = new UpgradeInfo();
            upgradeInfo.setUpStatus(ConstantUtil.UPGRADE_STAT_RUNN);
            upgradeInfo.setCmdType(ConstantUtil.UPGRADE_TYPE);
            upgradeInfo.setCmdDir(ConstantUtil.UPGRADE_RESP);
            upgradeInfo.setType(ConstantUtil.UPGRADE_MSG_TYPE);
            this.updateUpgradeStatus(ConstantUtil.UPGRADE, ConstantUtil.UPGRADE, upgradeInfoChange);
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
        // UpgradeInfo upgradeInfo1 = TcpHandler.getUpgradeInfo();
        UpgradeInfo upgradeInfo1 = getUpgradeStatus();
        return Result.ok(upgradeInfo1).toJSONString();
    }

    @Override
    public void updateUpgradeStatus(String key, String field, UpgradeInfo upgradeInfo) {
        put(key, field, upgradeInfo, -1L);
    }

    @Override
    public void initUpgradeStatus() {
        flushDb();
        UpgradeInfo upgradeInfo = new UpgradeInfo();
        upgradeInfo.setUpStatus(ConstantUtil.UPGRADE_STAT_DEFAULT);
        upgradeInfo.setCmdType(ConstantUtil.UPGRADE_TYPE);
        upgradeInfo.setCmdDir(ConstantUtil.UPGRADE_RESP);
        upgradeInfo.setType(ConstantUtil.UPGRADE_MSG_TYPE);
        this.updateUpgradeStatus(ConstantUtil.UPGRADE, ConstantUtil.UPGRADE, upgradeInfo);
    }

    private UpgradeInfo getUpgradeStatus() {
        String jsonStr = get(ConstantUtil.UPGRADE, ConstantUtil.UPGRADE);
        return JSONObject.parseObject(jsonStr, UpgradeInfo.class);
    }

}
