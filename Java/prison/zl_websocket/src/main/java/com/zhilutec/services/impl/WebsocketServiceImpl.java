package com.zhilutec.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.common.utils.ArithUtil;
import com.zhilutec.common.utils.DistanceUtil;
import com.zhilutec.common.utils.StatusFlagUtil;
import com.zhilutec.common.utils.WarningUtils;
import com.zhilutec.db.entities.*;
import com.zhilutec.db.results.AbsenceResult;
import com.zhilutec.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Future;

@Service
public class WebsocketServiceImpl implements IWebsocketService {
    // 记录报警
    public static HashMap<Long, WarningStatusEntity> WARNING_MAP = new HashMap<>();
    // 记录最后一次的坐标
    public static HashMap<Long, WarningStatusEntity> COORDINATE_MAP = new HashMap<>();
    // 状态信息
    public static Map<Long, RedisStatusEnitity> STATUS_MAP = new HashMap<>();
    //报警数量统计
    protected static Integer WARNING_COUNT = 0;
    protected static String UNKNOWN_POSITION = "0000000000";
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    IPrisonerCfgService prisonerCfgService;

    @Resource
    IPrisonerService prisonerService;

    @Resource
    IAreaService areaService;


    @Resource
    IWarningStatusService warningStatusService;

    /**
     * 字符串中字符替换
     */
    private static String replaceIndex(int index, String res, String str) {
        return res.substring(0, index) + str + res.substring(index + 1);
    }

    /**
     * 多监仓坐标推送
     **/
    @Override
    public void sendMultiAreaMsg(Session wsSession, JSONObject wsMsgObj, WarningStatusEntity tcpData) {
        // data如果为空表示用户请求的数据与收到数据不相关，则不发送
        if (tcpData != null) {
            Integer wsDataType = wsMsgObj.getInteger("dataType");
            if (wsDataType != null && wsDataType == 2) {
                List<AreaEntity> areas = areaService.queryAreas();
                List<Map> rsList = new ArrayList<>();
                Map coordinate = WebsocketServiceImpl.COORDINATE_MAP;

                List<WarningStatusEntity> warningStatusEntities = new ArrayList<>(coordinate.values());
                for (AreaEntity area : areas) {
                    Map areaInfo = new HashMap();
                    List coordinateList = new ArrayList();
                    areaInfo.put("areaName", area.getName());
                    areaInfo.put("areaCode", area.getAreaCode());

                    for (int i = 0; i < warningStatusEntities.size(); i++) {
                        WarningStatusEntity warningStatusEntity = warningStatusEntities.get(i);

                        Integer op = warningStatusEntity.getOp();
                        String warningCode = warningStatusEntity.getWarningCode();
                        String posCode = warningStatusEntity.getPosCode();
                        Long tagId = warningStatusEntity.getTagId();

                        /**当前处于电子围栏和离线报警时,不推送坐标消息*/
                        /**posCode为空或为未定义的位置则跳过*/
                        if (posCode == null || posCode.equals(WebsocketServiceImpl.UNKNOWN_POSITION) || (op != null && op.intValue() == 1 && (warningCode != null && warningCode.equals(WarningUtils.NOSIGNAL_CODE) || warningCode != null && warningCode.equals(WarningUtils.OUT_CODE)))) {
                            continue;
                        } else {
                            /**厕所坐标转监仓*/
                            posCode = convertArea(posCode);
                            if (posCode != null) {
                                warningStatusEntity.setPosCode(posCode);
                            }

                            if (posCode.equals(area.getAreaCode())) {
                            /* 当上一条消息是信号消失时推送一条特殊的坐标用以取消离线tag的坐标 */
                                WarningStatusEntity generateData = this.genCoordinate(tagId);
                                if (generateData != null) {
                                    generateData.setTimestamp(tcpData.getTimestamp() + 1);
                                    generateData.setName(tcpData.getName());
                                    coordinateList.add(generateData);
                                } else {
                                    coordinateList.add(warningStatusEntity);
                                }
                            }
                        }
                    }
                    areaInfo.put("prisoners", coordinateList);
                    rsList.add(areaInfo);
                }
                String rs = Result.ok(ResultCode.SUCCESS.getCode(), rsList).toJSONString();
                // logger.info("=======多监仓准备推送数据,数据如下：=====");
                // logger.info(rs);
                // this.wsSendMsg(wsSession, rs);// 异步
                try {
                    this.wsSendMsgBase(wsSession, rs);// 同步
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 单监仓数据推送
     * 2017-12-15 在离线与信息消失报警作用相同，这里不关注在离线消息
     */
    @Override
    public void sendAreaMsg(Long tcpTagId, Session wsSession, JSONObject wsMsgObj) throws Exception {
        String wsAreaCode = wsMsgObj.getString("areaCode");
        if (wsAreaCode == null) {
            logger.info("=======监仓编号未提供=====");
            return;
        }
        String rs = this.getStatisticsInfo(wsMsgObj);
        if (rs != null) {
            // logger.info("=======单监仓准备推送数据,数据如下：=====");
            // logger.info(rs);
            // this.wsSendMsg(wsSession, rs);// 异步
            this.wsSendMsgBase(wsSession, rs);// 异步
        }
    }

    /**
     * 推送单个消息,纯报警和个人信息,
     *
     * @throws Exception
     **/
    @Override
    public void sendPrisonerMsg(Long tcpTagId, Session wsSession, JSONObject wsMsgObj, WarningStatusEntity tcpData,
                                JSONObject tcpMsgJson) throws Exception {
        Long wsTagId = wsMsgObj.getLong("tagId");
        Integer wsDataType = wsMsgObj.getInteger("dataType");

        boolean tagFlag = false;
        if (wsTagId != null) {
            tagFlag = wsTagId.longValue() == tcpTagId.longValue();
        }
        String tcpPosCode = tcpData.getPosCode();
        // data如果为空表示用户请求的数据与收到数据不相关，则不发送
        if (tcpData != null) {
            if (tcpPosCode != null && tcpData.getPosCode().equals(WebsocketServiceImpl.UNKNOWN_POSITION)) {
                logger.info("======位置未定义的不发给前端=====");
            }
            // 此处返回所有消息类型
            if ((wsDataType == null || wsDataType == 0) && tagFlag) {
                tcpData = this.getWarningStatusEntity(tcpData);
                String rs = Result.ok(ResultCode.SUCCESS.getCode(), tcpData).toJSONString();
                // logger.info("=======囚犯数据准备推送,数据如下：=====");
                // logger.info(rs);
                // this.wsSendMsg(wsSession, rs);// 异步
                this.wsSendMsgBase(wsSession, rs);// 异步
            } else if (wsDataType == null || wsDataType == 4) {
                this.sendWarningMsg(tcpTagId, wsSession, tcpMsgJson);
            }
        }
    }

    /**
     * 发送数据给前端
     */
    @Override
    public Future<Void> wsSendMsg(Session wsSession, String rs) {
        return wsSession.getAsyncRemote().sendText(rs);
    }

    @Override
    public void  wsSendMsgBase(Session wsSession, String rs) throws IOException {
        wsSession.getBasicRemote().sendText(rs);
    }

    @Override
    public String getPrisonerMsg(JSONObject jsonObject) {
        WarningStatusEntity prisonerInfo = WebsocketServiceImpl.COORDINATE_MAP.get(jsonObject.getLong("tagId"));
        String rs;
        if (prisonerInfo != null) {
            prisonerInfo = this.getWarningStatusEntity(prisonerInfo);
            rs = Result.ok(ResultCode.SUCCESS.getCode(), prisonerInfo).toJSONString();
        } else {
            rs = Result.error("此设备无数据").toJSONString();
        }
        return rs;
    }

    /**
     * 报警弹窗消息推送,仅推送op为1的消息
     **/
    private void sendWarningMsg(Long tcpTagId, Session wsSession, JSONObject tcpMsgJson) {
        Integer op = tcpMsgJson.getInteger("op");
        String warningCode = tcpMsgJson.getString("warning_code");
        String level = tcpMsgJson.getString("level");
        HashMap<String, Object> prisonerWarning = prisonerService.queryByTagId(tcpTagId);
        if (prisonerWarning != null && !prisonerWarning.isEmpty()) {
            String warningMsg = "未知报警";
            if (op != null && op.intValue() == 1) {
                warningMsg = WarningUtils.warningMsg(warningCode, op);
                prisonerWarning.put("msg", warningMsg);
                prisonerWarning.put("level", level);
                prisonerWarning.put("op", op);
                String rs = Result.ok(ResultCode.SUCCESS.getCode(), prisonerWarning).toJSONString();
                // this.wsSendMsg(wsSession, rs);// 异步
                try {
                    this.wsSendMsgBase(wsSession, rs);//
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 消息处理 ,处理报警和在离线消息，将消息保存到map中,在消息中附上人名和位置名(如果有的话)
     */
    @Override
    public void saveMessage(String receiveMsg) {
        /** 将消息转为 WarningStatusEntity 对象 */
        WarningStatusEntity tcpMsgObj = JSON.parseObject(receiveMsg, WarningStatusEntity.class);

        /** 提取消息内容 */
        Integer tcpType = tcpMsgObj.getType();
        Long tagId = tcpMsgObj.getTagId();

        Integer op = tcpMsgObj.getOp();
        String warningCode = tcpMsgObj.getWarningCode();

        /** 生成报警消息 **/
        String msg = null;
        if (warningCode != null) {
            msg = WarningUtils.warningMsg(warningCode, op);
        }
        if (msg != null && !msg.isEmpty()) {
            tcpMsgObj.setMsg(msg);
        }

        /** 获取报警对应的人名 **/
        tcpMsgObj = this.messagePrisoner(tcpMsgObj);

        // type 1在离线消息 type 2 坐标 type 3报警消息
        if (tcpType.intValue() == 1) {
        } else if (tcpType.intValue() == 2) {
            this.saveCoordinateMsg(tcpMsgObj, tcpType, tagId);
        } else if (tcpType.intValue() == 3) {
            this.saveWarningMsg(tcpMsgObj, tcpType, tagId, warningCode, msg);
        }

    }

    /**
     * 坐标消息处理
     */
    /* * 2017-12-15 在离线与信息消失报警作用相同，这里不关注在离线消息 */
    private void saveCoordinateMsg(WarningStatusEntity tcpMsgObj, Integer tcpType, Long tagId) {
        // 处理posCode对应的位置名称
        tcpMsgObj = this.messagePosition(tcpMsgObj);
        Double posX = tcpMsgObj.getPosX();
        Double posY = tcpMsgObj.getPosY();
        Double posZ = tcpMsgObj.getPosZ();
        String name = tcpMsgObj.getName();
        String code = tcpMsgObj.getCode();
        String posCode = tcpMsgObj.getPosCode();
        String position = tcpMsgObj.getPosition();
        String areaName = tcpMsgObj.getAreaName();
        String areaCode = tcpMsgObj.getAreaCode();
        boolean flag = WebsocketServiceImpl.WARNING_MAP.containsKey(tagId);
        // 在报警消息存在，在报警消息中添加报警坐标
        if (flag) {
            // 获取上一次报警信息
            WarningStatusEntity dataObj = WebsocketServiceImpl.WARNING_MAP.get(tagId);
            Integer hsType = dataObj.getType();
            String hsPosCode = dataObj.getPosCode();
            /** 当上一次记录为报警或离线时不更新坐标,否则改变坐标,用于保留第一次报警时或离线时坐标 **/
            /** 当第一个报文为报警或离线时，要将下一次的坐标保存到报警或离线消息中 */
            if (hsType == 3) {
                /** 当上一次记录为报警但不包含坐标(posCode为空时)时要用这次的坐标来更新报警记录 */
                if ((hsPosCode == null || hsPosCode.isEmpty())) {
                    dataObj.setPosX(posX);
                    dataObj.setPosY(posY);
                    dataObj.setPosZ(posZ);
                    dataObj.setName(name);
                    dataObj.setCode(code);
                    dataObj.setAreaCode(areaCode);
                    dataObj.setAreaName(areaName);
                    dataObj.setPosCode(posCode);
                    dataObj.setPosition(position);
                    WebsocketServiceImpl.WARNING_MAP.put(tagId, dataObj);
                }
            }
        } else {
            // 记录不存在直接添加
            WarningStatusEntity warning = this.getWarningStatus(tcpMsgObj);
            WebsocketServiceImpl.WARNING_MAP.put(tagId, warning);
        }

        /** 保存数据coordinateMap **/
        this.updateCoordinateMsg(tcpMsgObj, tagId, posCode, posX, posY, posZ, tcpType, name, code, areaName,
                areaCode, position);

    }

    /**
     * 坐标消息处理
     * 更新坐标消息中的坐标
     **/
    private WarningStatusEntity updateCoordinateMsg(WarningStatusEntity tcpMsgObj, Long tagId, String posCode,
                                                    Double posX, Double posY, Double posZ, Integer tcpType, String name, String code, String areaName,
                                                    String areaCode, String position) {
        boolean coorFlag = WebsocketServiceImpl.COORDINATE_MAP.containsKey(tagId);
        // 记录存在
        if (coorFlag) {
            WarningStatusEntity lastCoordinate = WebsocketServiceImpl.COORDINATE_MAP.get(tagId);
            lastCoordinate.setPosX(posX);
            lastCoordinate.setPosY(posY);
            lastCoordinate.setPosZ(posZ);
            lastCoordinate.setName(name);
            lastCoordinate.setCode(code);
            lastCoordinate.setType(tcpType);
            lastCoordinate.setAreaCode(areaCode);
            lastCoordinate.setAreaName(areaName);
            lastCoordinate.setPosCode(posCode);
            lastCoordinate.setPosition(position);
            /** 报警取消后要相应的报警编号和报警消息清空 **/
            if (lastCoordinate.getOp() != null && lastCoordinate.getOp() == 0) {
                lastCoordinate.setWarningCode(null);
                lastCoordinate.setMsg(null);
            }
            WebsocketServiceImpl.COORDINATE_MAP.put(tagId, lastCoordinate);
        } else {
            // 记录不存在直接添加
            WarningStatusEntity warning = this.getWarningStatus(tcpMsgObj);
            WebsocketServiceImpl.COORDINATE_MAP.put(tagId, warning);
        }
        return tcpMsgObj;
    }

    private WarningStatusEntity getWarningStatus(WarningStatusEntity tcpMsgObj) {
        WarningStatusEntity warning = new WarningStatusEntity();
        warning.setCode(tcpMsgObj.getCode());
        warning.setTagId(tcpMsgObj.getTagId());
        warning.setType(tcpMsgObj.getType());
        warning.setPosX(tcpMsgObj.getPosX());
        warning.setPosY(tcpMsgObj.getPosY());
        warning.setPosZ(tcpMsgObj.getPosZ());
        warning.setName(tcpMsgObj.getName());
        warning.setAreaCode(tcpMsgObj.getAreaCode());
        warning.setAreaName(tcpMsgObj.getAreaName());
        warning.setPosCode(tcpMsgObj.getPosCode());
        warning.setPosition(tcpMsgObj.getPosition());
        warning.setTimestamp(tcpMsgObj.getTimestamp());
        return warning;
    }

    /**
     * 报警消息处理
     */
    private void saveWarningMsg(WarningStatusEntity tcpMsgObj, Integer tcpType, Long tagId,
                                String warningCode, String msg) {
        boolean flag = WebsocketServiceImpl.WARNING_MAP.containsKey(tagId);
        Long timestamp = tcpMsgObj.getTimestamp();
        Integer op = tcpMsgObj.getOp();
        String level = tcpMsgObj.getLevel();
        String name = tcpMsgObj.getName();
        String code = tcpMsgObj.getCode();
        String areaName = tcpMsgObj.getAreaName();
        String areaCode = tcpMsgObj.getAreaCode();
        tcpMsgObj = this.messagePosition(tcpMsgObj);//报警信息位置转换
        String posCode = tcpMsgObj.getPosCode();
        String position = tcpMsgObj.getPosition();
        // 记录存在
        if (flag) {
            // 更新上一次报警消息
            WarningStatusEntity dataObj = null;
            dataObj = WebsocketServiceImpl.WARNING_MAP.get(tagId);
            dataObj.setOp(op);
            dataObj.setMsg(msg);
            dataObj.setLevel(level);
            dataObj.setType(tcpType);
            dataObj.setName(name);
            dataObj.setCode(code);
            dataObj.setAreaCode(areaCode);
            dataObj.setAreaName(areaName);
            dataObj.setTimestamp(timestamp);
            dataObj.setWarningCode(warningCode);
            if (posCode != null) {
                dataObj.setPosCode(posCode);
            }
            if (position != null) {
                dataObj.setPosition(position);
            }
            WebsocketServiceImpl.WARNING_MAP.put(tagId, dataObj);
        } else {
            // 直接添加
            WarningStatusEntity warning = this.getWarning(tcpMsgObj);
            WebsocketServiceImpl.WARNING_MAP.put(tagId, warning);
        }
        this.updateWarningMsg(tcpMsgObj, tcpType, tagId, timestamp, op, warningCode, level, msg, name, code,
                areaName, areaCode, posCode, position);
    }

    /**
     * 在实时消息中更新报警信息
     **/
    private void updateWarningMsg(WarningStatusEntity tcpMsgObj, Integer type, Long tagId,
                                  Long timestamp, Integer op, String warningCode, String level, String msg, String name, String code,
                                  String areaName, String areaCode, String posCode, String position) {
        boolean coorFlag = WebsocketServiceImpl.COORDINATE_MAP.containsKey(tagId);
        // 记录存在
        if (coorFlag) {
            // 更新上一次记录的报警消息
            WarningStatusEntity lastCoordinate = WebsocketServiceImpl.COORDINATE_MAP.get(tagId);
            lastCoordinate.setOp(op);
            lastCoordinate.setMsg(msg);
            lastCoordinate.setName(name);
            lastCoordinate.setCode(code);
            lastCoordinate.setAreaCode(areaCode);
            lastCoordinate.setAreaName(areaName);
            lastCoordinate.setType(type);
            lastCoordinate.setLevel(level);
            lastCoordinate.setTimestamp(timestamp);
            lastCoordinate.setWarningCode(warningCode);
            if (posCode != null) {
                lastCoordinate.setPosCode(posCode);
            }
            if (position != null) {
                lastCoordinate.setPosition(position);
            }
            WebsocketServiceImpl.COORDINATE_MAP.put(tagId, lastCoordinate);
        } else {
            // 直接添加
            WarningStatusEntity warning = this.getWarning(tcpMsgObj);
            WebsocketServiceImpl.COORDINATE_MAP.put(tagId, warning);
        }

    }

    private WarningStatusEntity getWarning(WarningStatusEntity tcpMsgObj) {
        WarningStatusEntity warning = new WarningStatusEntity();
        warning.setCode(tcpMsgObj.getCode());
        warning.setTagId(tcpMsgObj.getTagId());
        warning.setType(tcpMsgObj.getType());
        warning.setOp(tcpMsgObj.getOp());
        warning.setName(tcpMsgObj.getName());
        warning.setAreaCode(tcpMsgObj.getAreaCode());
        warning.setAreaName(tcpMsgObj.getAreaName());
        warning.setPosCode(tcpMsgObj.getPosCode());
        warning.setPosition(tcpMsgObj.getPosition());
        warning.setTimestamp(tcpMsgObj.getTimestamp());
        return warning;
    }

    /**
     * 将收到原始消息处理，添加上实际位置名称
     **/
    private WarningStatusEntity messagePosition(WarningStatusEntity tcpMsgObj) {
        String tcpPosCode = tcpMsgObj.getPosCode();
        if (tcpPosCode == null) {
            return tcpMsgObj;
        }

        /** 查询囚犯名和areaCode对应的位置名 **/
        AreaEntity positionArea = areaService.queryArea(tcpPosCode);
        String position = "未知位置";
        if (positionArea != null && positionArea.getName() != null) {
            position = positionArea.getName();
        }
        tcpMsgObj.setPosition(position);
        return tcpMsgObj;
    }

    /**
     * 将收到原始消息处理，添加上囚犯编码,名称,配置的位置
     **/
    private WarningStatusEntity messagePrisoner(WarningStatusEntity tcpMsgObj) {
        Long tcpTagId = tcpMsgObj.getTagId();
        PrisonerEntity prisoner = prisonerService.query(tcpTagId);
        String prisonerName = "未知囚犯";
        String prisonerCode = "";
        String prisonerAreaCode = "";
        // 预期位置
        String expectPosition = "未分配位置";

        if (prisoner != null && prisoner.getName() != null) {
            prisonerName = prisoner.getName();
        }

        if (prisoner != null && prisoner.getAreaCode() != null) {
            /*** 查询配置的位置 ***/
            prisonerAreaCode = prisoner.getAreaCode();
            AreaEntity expectArea = areaService.queryArea(prisonerAreaCode);
            if (expectArea != null && expectArea.getName() != null) {
                expectPosition = expectArea.getName();
            }
        }

        if (prisoner != null && prisoner.getCode() != null) {
            prisonerCode = prisoner.getCode();
        }

        tcpMsgObj.setAreaName(expectPosition);
        tcpMsgObj.setName(prisonerName);
        tcpMsgObj.setCode(prisonerCode);
        tcpMsgObj.setAreaCode(prisonerAreaCode);
        return tcpMsgObj;
    }

    /**
     * 坐标转换，将坐标根据不同的坐标原点进行转换
     */
    @Override
    public String convertZero(String receiveMsg) {
        JSONObject jsonObj = JSON.parseObject(receiveMsg);
        Integer type = jsonObj.getInteger("type");
        Integer op = jsonObj.getInteger("op");
        if (type == 3 && op != null && op == 1) {
            WebsocketServiceImpl.WARNING_COUNT++;
        }
        if (type == 2) {
            String posCode = jsonObj.getString("pos_code");
            Double realPosX = jsonObj.getDouble("pos_x");
            String key = posCode.substring(6);
            // logger.info("=====坐标转换的参考系key为:" + key);
            boolean flag = DistanceUtil.distanceMap.containsKey(key);
            if (flag) {
                /* 转换坐标 **/
                Double distance = DistanceUtil.distanceMap.get(key);
                Double newPosX = ArithUtil.subScal3(realPosX, distance);
                jsonObj.put("pos_x", newPosX);
            } else {
                return receiveMsg;
            }
        } else {
            return receiveMsg;
        }
        return jsonObj.toString();
    }

    /**
     * 判断是否为缺勤，包括配置的和实时缺勤
     */
    @Override
    public boolean isAbsence(String code, Long prisonerTagId) {
        /*** 查看配置表是否存在配置,存在则缺勤 */
        int count = prisonerCfgService.countPrisonerConf(code);
        if (count > 0) {
            return true;
        }

        /** 配置表不存在查看redis中的状态机查看是否离线，串仓，电子围栏 **/
        return this.isAbsence(prisonerTagId);
    }

    /**
     * 将消息中的状态信息分类保存在redis中
     */
    @Override
    public void saveStatus(String jsonStr) {
        JSONObject content = JSON.parseObject(jsonStr);
        Long tagId = content.getLong("tag_id");
        Integer type = content.getInteger("type");
        Integer op = content.getInteger("op");
        String posCode = content.getString("pos_code");
        RedisStatusEnitity redisStatus = WebsocketServiceImpl.STATUS_MAP.get(tagId);
        if (redisStatus == null || redisStatus.equals("null")) {
            // 创建一个对象来设置状态表
            redisStatus = new RedisStatusEnitity();
        }

        // 刷新状态表
        redisStatus.setTimestamp(content.getLong("timestamp"));
        redisStatus.setType(type);
        redisStatus.setOp(op);
        if (posCode != null) {
            redisStatus.setPosCode(posCode);
        }
        redisStatus = this.setStatus(content, type, op, redisStatus);
        WebsocketServiceImpl.STATUS_MAP.put(tagId, redisStatus);
    }

    /**
     * 单监仓页面所有信息
     **/
    @Override
    public String getStatisticsInfo(JSONObject wsMsgObj) throws Exception {
        Map<String, Object> statistics = this.getAreaMsg(wsMsgObj);
        List<WarningStatusEntity> cross = this.getCross(wsMsgObj);
        Map<String, Long> warnings = warningStatusService.areaWarningsMap(wsMsgObj);
        statistics.put("warnings", warnings);
        statistics.put("cross", cross);
        String rs = Result.ok(ResultCode.SUCCESS.getCode(), statistics).toJSONString();
        return rs;
    }

    /**
     * 获取未查看报警数量
     */
    private Integer getWaningCount() {
        return WebsocketServiceImpl.WARNING_COUNT;
    }

    /**
     * 获取未查看报警数量
     */
    @Override
    public String getWaningAmount() {
        Map rsMap = new HashMap();
        Integer count = this.getWaningCount();
        rsMap.put("amount", count);
        return Result.ok(ResultCode.SUCCESS.getCode(), rsMap).toJSONString();
    }

    /**
     * 重置报警数量
     */
    @Override
    public String resetWarning(JSONObject jsonParam) {
        Map<String, String> rsMap = new HashMap<>();
        try {
            this.reset(jsonParam);
            return Result.ok("success").toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("failed").toJSONString();
        }
    }

    private RedisStatusEnitity setStatus(JSONObject content, Integer type, Integer op, RedisStatusEnitity redisStatus) {
        // 在线离线报警
        if (type == 1) {
        } else if (type == 2) {
        } else if (type == 3) {
            // 电子围栏和串仓
            String warningCode = content.getString("warning_code");
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

    /**
     * 如果消息是信号消失和电子围栏则认为离线
     * true 离线
     * false 未离线
     */
    private boolean isOffline(Long prisonerTagId) {
        /* 如果redis找不到记录则认为缺勤 */
        RedisStatusEnitity statusRedis = WebsocketServiceImpl.STATUS_MAP.get(prisonerTagId);
        if (statusRedis == null || statusRedis.equals("null")) {
            return false;
        }
        Integer nosignal = statusRedis.getNosignal();
        Integer out = statusRedis.getOut();
        if ((nosignal != null && nosignal == 1) || (out != null && out == 1)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 处理一个监仓的囚犯信息
     **/
    private Map<String, Object> getAreaMsg(JSONObject wsMsgObj) {
        // data如果为空表示用户请求的数据与收到数据不相关，则不发送
        List<WarningStatusEntity> prisonerLs = new ArrayList<>();
        Map<String, Integer> checkingIn = new HashMap<>();
        Map<String, Long> warnings = new HashMap<>();
        Map<String, Integer> absences = new HashMap<>();

        Map<String, Object> rsMap = new HashMap<>();
        // 初始化返回结果
        rsMap.put("warnings", warnings);
        rsMap.put("areaCode", "");
        rsMap.put("areaName", "");

        String wsAreaCode = wsMsgObj.getString("areaCode");
        if (wsAreaCode == null) {
            logger.info("=======监仓编号未提供=====");
            return rsMap;
        }

        rsMap.put("areaCode", wsAreaCode);
        AreaEntity area = areaService.queryArea(wsAreaCode);
        String areaName = area.getName();

        if (areaName == null) {
            rsMap.put("areaName", "未知位置");
            return rsMap;
        }
        rsMap.put("areaName", areaName);
        int other = 0;
        List<PrisonerEntity> prisoners = prisonerService.queryByArea(wsAreaCode);
        for (PrisonerEntity prisonerEntity : prisoners) {
            Long prisonerTagId = prisonerEntity.getTagId();
            String code = prisonerEntity.getCode();
            WarningStatusEntity statusEnity = WebsocketServiceImpl.COORDINATE_MAP.get(prisonerTagId);
            statusEnity = this.getWarningStatusEntity(prisonerEntity, statusEnity);
            prisonerLs.add(statusEnity);
            boolean absenceStatus = this.isAbsence(code, prisonerTagId);
            if (absenceStatus) {
                other++;
            }
        }

        /** 考勤人数统计 **/
        checkingIn = this.getCheckingIn(checkingIn, wsAreaCode, other);
        /** 将其它类型缺勤的合并到一起 **/
        absences = this.getAbsence(wsAreaCode, other);
        rsMap.put("checkingIn", checkingIn);
        rsMap.put("abStatistics", absences);
        rsMap.put("prisoners", prisonerLs);
        return rsMap;
    }

    private Map<String, Integer> getAbsence(String wsAreaCode, int other) {
        Map<String, Integer> absences;
        List<AbsenceResult> absenceResults = prisonerCfgService.absence(wsAreaCode);
        for (AbsenceResult absenceResult : absenceResults) {
            if (absenceResult.getType().equals("9")) {
                other = other + absenceResult.getAmount();
            }
        }

        /** 缺勤分类统计*/
        AbsenceResult absenceResult = new AbsenceResult();
        absenceResult.setType("9");
        absenceResult.setAmount(other);
        absenceResults.add(absenceResult);
        absences = areaService.absenceMap(absenceResults);
        return absences;
    }

    private Map<String, Integer> getCheckingIn(Map<String, Integer> checkingIn, String wsAreaCode, int other) {
        int total = prisonerService.countArea(wsAreaCode);
        int attendances = total - other;
        checkingIn.put("total", total);
        checkingIn.put("attendances", attendances);
        checkingIn.put("absence", other);
        return checkingIn;
    }

    /**
     * 获取串入本监仓的信息
     */
    private List<WarningStatusEntity> getCross(JSONObject wsMsgObj) {
        List<WarningStatusEntity> crossList = new ArrayList();
        String wsAreaCode = wsMsgObj.getString("areaCode");
        Map<Long, WarningStatusEntity> coordinateMap = WebsocketServiceImpl.COORDINATE_MAP;
        Collection<WarningStatusEntity> warningStatusEntities = coordinateMap.values();
        for (WarningStatusEntity warningStatusEntity : warningStatusEntities) {
            String posCode = warningStatusEntity.getPosCode();
            Integer op = warningStatusEntity.getOp();
            if (op != null && op == 1) {
                String areaCode = warningStatusEntity.getAreaCode();
                String warningCode = warningStatusEntity.getWarningCode();
                if (warningCode != null) {
                    if (posCode != null && posCode.equals(wsAreaCode) && !posCode.equals(areaCode) && warningCode.equals(WarningUtils.CROSS_CODE)) {
                        crossList.add(warningStatusEntity);
                    }
                }
            }
        }
        return crossList;
    }

    /**
     * 将信息类型使用flag标识来分类
     */
    private WarningStatusEntity getWarningStatusEntity(PrisonerEntity prisonerEntity, WarningStatusEntity statusEnity) {
        Integer flag = StatusFlagUtil.ONLINE;
        if (statusEnity != null) {
            flag = this.classifyMsg(statusEnity);
            statusEnity.setFlag(flag);
        } else {
            //当为空时表示离线
            statusEnity = new WarningStatusEntity();
            flag = StatusFlagUtil.OFFLINE;
            statusEnity.setName(prisonerEntity.getName());
            statusEnity.setCode(prisonerEntity.getCode());
            statusEnity.setMsg(WarningUtils.OFFLINE);
            statusEnity.setFlag(flag);
        }
        if (flag == StatusFlagUtil.ONLINE) {
            statusEnity.setMsg(WarningUtils.ONLINE);
        } else if (flag == StatusFlagUtil.OFFLINE) {
            statusEnity.setMsg(WarningUtils.OFFLINE);
        }
        return statusEnity;
    }

    private WarningStatusEntity getWarningStatusEntity(WarningStatusEntity statusEnity) {
        Integer flag = 4;
        if (statusEnity != null) {
            flag = this.classifyMsg(statusEnity);
            statusEnity.setFlag(flag);
        }
        return statusEnity;
    }

    /**
     * 设置flag
     * 将信息类型使用flag标识来分类
     * 1 严重报警
     * 2 普通报警
     * 3 提示
     * 4 在线
     * 5 离线(信号消失)
     * 6 串仓，离开本监仓
     * 7 电子围栏
     */
    private Integer classifyMsg(WarningStatusEntity statusEnity) {
        Integer flag = StatusFlagUtil.ONLINE;
        Integer op = statusEnity.getOp();
        String warningCode = statusEnity.getWarningCode();
        String posCode = statusEnity.getPosCode();
        String level = statusEnity.getLevel();
        //posCode为空则表示离线
        if (posCode == null) {
            flag = StatusFlagUtil.OFFLINE;
        } else {
            //posCode全0表示未知位置也认为离线
            // if (posCode.equals(WebsocketServiceImpl.UNKNOWN_POSITION)) {
            //     flag = 5;
            // } else {
            if (op != null && op == 1) {
                        /*报警级别为01，非串仓且非电子围栏*/
                if (level != null && level.equals(WarningUtils.URGENCY_CODE) && !warningCode.equals(WarningUtils.CROSS_CODE) && !warningCode.equals(WarningUtils.OUT_CODE)) {
                    flag = StatusFlagUtil.URGENCY;
                        /*报警级别为01，串仓，串出所属监仓*/
                } else if (level != null && level.equals(WarningUtils.URGENCY_CODE) && warningCode.equals(WarningUtils.CROSS_CODE)) {
                    flag = StatusFlagUtil.CROSS;
                        /*报警级别为01，电子围栏，个人页面标为信号消失，监仓业务显示为红色,考勤状态为缺勤*/
                } else if (level != null && level.equals(WarningUtils.URGENCY_CODE) && warningCode.equals(WarningUtils.OUT_CODE)) {
                    flag = StatusFlagUtil.OUT;
                        /*报警级别为02，非信号消失类报警*/
                } else if (level != null && level.equals(WarningUtils.COMMON_CODE) && !warningCode.equals(WarningUtils.NOSIGNAL_CODE)) {
                    flag = StatusFlagUtil.COMMON;
                      /*报警级别为02，信号消失类报警*/
                } else if (level != null && level.equals(WarningUtils.COMMON_CODE) && warningCode.equals(WarningUtils.NOSIGNAL_CODE)) {
                    flag = StatusFlagUtil.OFFLINE;
                        /*报警级别为03*/
                } else if (level != null && level.equals(WarningUtils.TIP_CODE)) {
                    flag = StatusFlagUtil.TIP;
                }
            }
            // }
        }
        return flag;
    }

    /**
     * 重置报警数量
     */
    private Integer reset(JSONObject jsonParam) {
        Integer amount = jsonParam.getInteger("amount");
        if (amount == null) {
            amount = 0;
        }
        WebsocketServiceImpl.WARNING_COUNT = amount;
        return amount;
    }

    /**
     * 将首页要将厕所数据转为监仓
     */
    private String convertArea(String dataPosCode) {
        // wsType==1要将厕所坐标转为监仓坐标
        if (dataPosCode != null) {
            String lastCharacter = dataPosCode.substring(dataPosCode.length() - 1);
            if (lastCharacter.equals("1")) {
                dataPosCode = replaceIndex(9, dataPosCode, "0");
            }
        } else {
            dataPosCode = null;
            logger.info("=======多监仓推送时区域编号为空=====");
        }
        return dataPosCode;
    }

    /**
     * 如果当前状态为信号消失则或电子围栏时发送一个虚拟坐标
     **/
    private WarningStatusEntity genCoordinate(Long prisonerTagId) {
        boolean offline = this.isOffline(prisonerTagId);
        WarningStatusEntity generateData = null;
        if (offline) {
            generateData = new WarningStatusEntity();
            generateData.setTagId(prisonerTagId);
            generateData.setPosX(-100.0);
            generateData.setPosY(-100.0);
            generateData.setPosZ(-100.0);
            generateData.setWarningCode("0202");
            generateData.setType(2);
            generateData.setFlag(StatusFlagUtil.OFFLINE);
            generateData.setMsg("构造数据");
        }
        return generateData;
    }

    /**
     * 判断是否为缺勤，实时缺勤
     */
    public boolean isAbsence(Long prisonerTagId) {
        RedisStatusEnitity statusRedis = WebsocketServiceImpl.STATUS_MAP.get(prisonerTagId);
        /* 如果redis找不到记录则认为缺勤 */
        if (statusRedis == null || statusRedis.equals("null")) {
            return true;
        }

        Integer type = statusRedis.getType();
        Integer out = statusRedis.getOut();
        Integer cross = statusRedis.getCross();
        Integer nosignal = statusRedis.getNosignal();
        /* 当前收到的是坐标且无报警表示未缺勤 **/
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

}
