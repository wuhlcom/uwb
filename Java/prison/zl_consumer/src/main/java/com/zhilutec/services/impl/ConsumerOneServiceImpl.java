package com.zhilutec.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.utils.ArithUtil;
import com.zhilutec.common.utils.ChangeCharsetUtil;
import com.zhilutec.common.utils.DistanceUtil;
import com.zhilutec.common.utils.WarningUtils;
import com.zhilutec.db.entities.*;
import com.zhilutec.services.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Optional;

/**将单个消息转数据库对象并入库*/
@Service
public class ConsumerOneServiceImpl implements IConsumerOneService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 记录报警
    public static HashMap<Long, WarningStatusEntity> WARNING_MAP = new HashMap<>();
    // 记录最后一次的坐标
    public static HashMap<Long, WarningStatusEntity> COORDINATE_MAP = new HashMap<>();


    @Resource
    IPrisonerService prisonerService;

    @Resource
    IAreaService areaService;

    @Resource
    IWarningStatusService warningStatusService;

    @Resource
    ICoordinateService coordinateService;

    @Resource
    ITagWarningService tagWarningService;

    /**
     * 保存报警到数据库，并会在状态表记录缺勤状态
     * 同一个位置同一种报警只会报一次
     */
    @Override
    public void saveMessage(Long tagId) {
        WarningStatusEntity warningStatus = ConsumerOneServiceImpl.WARNING_MAP.get(tagId);
        // 保存状态到redis
        Integer type = warningStatus.getType();
        // 保存报警和在离线 type 1在离线消息 type 3报警消息
        if (type.intValue() == 1) {
        } else if (type.intValue() == 3) {
            tagWarningService.saveWarning(warningStatus.toString());
        }
    }

    /**
     * 消息处理 ,将消息保存到map中,在消息中附上人名和位置名(如果有的话)
     */
    @Override
    public void saveMessage(ConsumerRecord<?, ?> record) {
        String receiveMsg = null;
        try {
            receiveMsg = this.converMsg(record);
            if (receiveMsg == null) {
                return;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
     * 坐标消息保存到数据库
     */
    private void saveCoordinateMsg(WarningStatusEntity tcpMsgObj) {
        if (tcpMsgObj == null) {
            return;
        }
        //排除报警消息
        if (tcpMsgObj.getType() == 2) {
            CoordinateEntity coordinate = new CoordinateEntity();
            coordinate.setLevel(tcpMsgObj.getLevel());
            coordinate.setOp(tcpMsgObj.getOp());
            coordinate.setPosCode(tcpMsgObj.getPosCode());
            coordinate.setPosX(tcpMsgObj.getPosX());
            coordinate.setPosY(tcpMsgObj.getPosY());
            coordinate.setPosZ(tcpMsgObj.getPosZ());
            coordinate.setStatus(tcpMsgObj.getStatus());
            coordinate.setTagId(tcpMsgObj.getTagId());
            coordinate.setTimestamp(tcpMsgObj.getTimestamp());
            coordinate.setType(tcpMsgObj.getType());
            coordinate.setWarningCode(tcpMsgObj.getWarningCode());
            coordinateService.saveCoordinate(coordinate);
        }
    }

    /**
     * 坐标消息处理
     */
    private  void saveCoordinateMsg(WarningStatusEntity tcpMsgObj, Integer tcpType, Long tagId) {
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
        boolean flag = ConsumerOneServiceImpl.WARNING_MAP.containsKey(tagId);
        // 在报警消息存在，在报警消息中添加报警坐标
        if (flag) {
            this.coordinateWarning(tcpType, tagId, posX, posY, posZ, name, code, posCode, position, areaName, areaCode);
        } else {
            // 记录不存在直接添加
            ConsumerOneServiceImpl.WARNING_MAP.put(tagId, tcpMsgObj);
        }

        /** 保存数据coordinateMap **/
        this.updateCoordinateMsg(tcpMsgObj, tagId, posCode, posX, posY, posZ, tcpType, name, code, areaName,
                areaCode, position);
    }

    /**
     * 合并报警和坐标，使报警消息带有坐标
     */
    private void coordinateWarning(Integer tcpType, Long tagId, Double posX, Double posY, Double posZ, String name, String code, String posCode, String position, String areaName, String areaCode) {
        // 获取上一次报警信息
        WarningStatusEntity dataObj = ConsumerOneServiceImpl.WARNING_MAP.get(tagId);
        Integer hsType = dataObj.getType();
        String hsPosCode = dataObj.getPosCode();
        /** 当上一次记录为报警或离线时不更新坐标,否则改变坐标,用于保留第一次报警时或离线时坐标 **/
        /** 当第一个报文为报警或离线时，要将下一次的坐标保存到报警或离线消息中 */
        if (hsType == 1) {
        } else if (hsType == 3) {
            /** 当上一次记录为报警但不包含坐标(posCode为空时)时要用这次的坐标来更新报警记录 */
            if ((hsPosCode == null || hsPosCode.isEmpty())) {
                dataObj.setPosX(posX);
                dataObj.setPosY(posY);
                dataObj.setPosZ(posZ);
                dataObj.setName(name);
                dataObj.setCode(code);
                dataObj.setType(tcpType);
                dataObj.setAreaCode(areaCode);
                dataObj.setAreaName(areaName);
                dataObj.setPosCode(posCode);
                dataObj.setPosition(position);
                dataObj.setState(0);
                ConsumerOneServiceImpl.WARNING_MAP.put(tagId, dataObj);
                warningStatusService.saveWarningStatus(dataObj);
            }
        }
    }

    /**
     * 坐标消息处理
     * 更新坐标消息Map的坐标
     **/
    private void updateCoordinateMsg(WarningStatusEntity tcpMsgObj, Long tagId, String posCode,
                                                    Double posX, Double posY, Double posZ, Integer tcpType, String name, String code, String areaName,
                                                    String areaCode, String position) {
        boolean coorFlag = ConsumerOneServiceImpl.COORDINATE_MAP.containsKey(tagId);
        // 记录存在
        if (coorFlag) {
            WarningStatusEntity lastCoordinate = ConsumerOneServiceImpl.COORDINATE_MAP.get(tagId);
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

            ConsumerOneServiceImpl.COORDINATE_MAP.put(tagId, lastCoordinate);
            /* 保存到coordinate数据库 */
            this.saveCoordinateMsg(lastCoordinate);
        } else {
            // 记录不存在直接添加
            ConsumerOneServiceImpl.COORDINATE_MAP.put(tagId, tcpMsgObj);
            this.saveCoordinateMsg(tcpMsgObj);
        }
    }

    /**
     * 报警消息处理
     */
    private void saveWarningMsg(WarningStatusEntity tcpMsgObj, Integer tcpType, Long tagId,
                                               String warningCode, String msg) {
        boolean flag = ConsumerOneServiceImpl.WARNING_MAP.containsKey(tagId);
        Long timestamp = tcpMsgObj.getTimestamp();
        Integer op = tcpMsgObj.getOp();
        String level = tcpMsgObj.getLevel();
        String name = tcpMsgObj.getName();
        String code = tcpMsgObj.getCode();
        String areaName = tcpMsgObj.getAreaName();
        String areaCode = tcpMsgObj.getAreaCode();
        tcpMsgObj = this.messagePosition(tcpMsgObj);
        String posCode = tcpMsgObj.getPosCode();
        String position = tcpMsgObj.getPosition();
        // 记录存在
        if (flag) {
            // 更新上一次报警消息
            WarningStatusEntity dataObj = null;
            dataObj = ConsumerOneServiceImpl.WARNING_MAP.get(tagId);
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
            ConsumerOneServiceImpl.WARNING_MAP.put(tagId, dataObj);
            warningStatusService.saveWarningStatus(dataObj);
        } else {
            // 直接添加
            ConsumerOneServiceImpl.WARNING_MAP.put(tagId, tcpMsgObj);
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
        boolean coorFlag = ConsumerOneServiceImpl.COORDINATE_MAP.containsKey(tagId);
        // 记录存在
        if (coorFlag) {
            // 更新上一次记录的报警消息
            WarningStatusEntity lastCoordinate = ConsumerOneServiceImpl.COORDINATE_MAP.get(tagId);
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
            ConsumerOneServiceImpl.COORDINATE_MAP.put(tagId, lastCoordinate);
        } else {
            // 直接添加
            ConsumerOneServiceImpl.COORDINATE_MAP.put(tagId, tcpMsgObj);
        }
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
     * 坐标转换
     */
    private String convertZero(String receiveMsg) {
        JSONObject jsonObj = JSON.parseObject(receiveMsg);
        Integer type = jsonObj.getInteger("type");
        Integer op = jsonObj.getInteger("op");
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

    /**处理kakfka消息*/
    private String converMsg(ConsumerRecord<?, ?> record) throws UnsupportedEncodingException {
        Optional<?> kakfaMsg = Optional.ofNullable(record.value());
        String receiveMsgNew = null;
        if (kakfaMsg.isPresent()) {
            ChangeCharsetUtil strChange = new ChangeCharsetUtil();
            Object message = kakfaMsg.get();
            receiveMsgNew = strChange.toUTF_8(message.toString());
        } else {
            return receiveMsgNew;
        }

        //转换带坐标的消息
        receiveMsgNew = convertZero(receiveMsgNew);
        return receiveMsgNew;
    }

}
