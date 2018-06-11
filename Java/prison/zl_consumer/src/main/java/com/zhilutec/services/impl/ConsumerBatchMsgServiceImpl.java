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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * 将批量的消息转成数据库表的对应对象List
 */
@Service
public class ConsumerBatchMsgServiceImpl implements IConsumerBatchMsgService {
    // 记录报警
    private static HashMap<Long, WarningStatusEntity> WARNING_MAP = new HashMap<>();
    // 记录最后一次的坐标
    private static HashMap<Long, WarningStatusEntity> COORDINATE_MAP = new HashMap<>();
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final Integer STATE = 0;
    @Resource
    IPrisonerService prisonerService;
    @Resource
    IAreaService areaService;

    private List<CoordinateEntity> coordinateEntityList = new ArrayList<>();
    private List<WarningStatusEntity> warningStatusEntityList = new ArrayList<>();
    private List<TagWarningEntity> tagWarningEntityList = new ArrayList<>();

    /***批量处理消息*/
    @Override
    public void saveMessageBatch(List<ConsumerRecord<?, ?>> records) {
        for (ConsumerRecord<?, ?> record : records) {
            this.saveMessage(record);
        }
    }

    /**
     * 处理单个消息
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

        /** 获取tag对应的人名 **/
        tcpMsgObj = this.messagePrisoner(tcpMsgObj);
        // type 1在离线消息 type 2 坐标 type 3报警消息
        if (tcpType.intValue() == 1) {
        } else if (tcpType.intValue() == 2) {
            this.saveCoordinateMap(tcpMsgObj, tcpType, tagId);
        } else if (tcpType.intValue() == 3) {
            this.saveWarningMap(tcpMsgObj, tcpType, tagId, warningCode, msg);
        }

    }

    /**
     * 坐标消息处理,保存到Map中
     */
    private void saveCoordinateMap(WarningStatusEntity tcpMsgObj, Integer tcpType, Long tagId) {
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

        /**给未添加位置或坐标的报警消息添加位置或坐标*/
        boolean flag = ConsumerBatchMsgServiceImpl.WARNING_MAP.containsKey(tagId);
        this.updateWarningMap(tcpMsgObj, flag, tagId, posX, posY, posZ, name, code, posCode, position, areaName, areaCode);

        /** 保存数据coordinateMap **/
        this.coordinateMap(tcpMsgObj, tagId, posCode, posX, posY, posZ, tcpType, name, code, areaName,
                areaCode, position);

    }

    /**
     * 合并报警和坐标，使报警消息带有坐标和报警位置信息
     */
    private void updateWarningMap(WarningStatusEntity tcpMsgObj, boolean flag, Long tagId, Double posX, Double posY, Double posZ, String name, String code, String posCode, String position, String areaName, String areaCode) {
        // 在报警消息存在，在报警消息中添加报警坐标
        if (flag) {
            // 获取上一次报警信息
            WarningStatusEntity dataObj = ConsumerBatchMsgServiceImpl.WARNING_MAP.get(tagId);
            Integer WarningType = dataObj.getType();
            String hsPosCode = dataObj.getPosCode();
            /** 当上一次记录为报警或离线时不更新坐标,否则改变坐标,用于保留第一次报警时或离线时坐标 **/
            /** 当第一个报文为报警或离线时，要将下一次的坐标保存到报警或离线消息中 */
            if (WarningType == 3) {
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
                    dataObj.setState(STATE);
                    ConsumerBatchMsgServiceImpl.WARNING_MAP.put(tagId, dataObj);
                    this.warningStatusEntityList.add(dataObj);
                }
            }
        } else {
            // 报警记录不存在直接添加当前的坐标到报警记录中
            WarningStatusEntity warningUpdate = this.getWarningStatusEntity(tcpMsgObj);
            ConsumerBatchMsgServiceImpl.WARNING_MAP.put(tagId, warningUpdate);
        }
    }

    /**
     * 坐标消息处理
     * 保存或更新坐标消息Map的坐标
     **/
    private void coordinateMap(WarningStatusEntity tcpMsgObj, Long tagId, String posCode,
                               Double posX, Double posY, Double posZ, Integer tcpType, String name, String code, String areaName,
                               String areaCode, String position) {
        boolean coorFlag = ConsumerBatchMsgServiceImpl.COORDINATE_MAP.containsKey(tagId);
        // 记录存在
        if (coorFlag) {
            WarningStatusEntity lastCoordinate = ConsumerBatchMsgServiceImpl.COORDINATE_MAP.get(tagId);
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
            lastCoordinate.setState(STATE);
            /** 报警取消后要相应的报警编号和报警消息清空 **/
            if (lastCoordinate.getOp() != null && lastCoordinate.getOp() == 0) {
                lastCoordinate.setWarningCode(null);
                lastCoordinate.setMsg(null);
            }
            ConsumerBatchMsgServiceImpl.COORDINATE_MAP.put(tagId, lastCoordinate);
            this.coordinateEntityList.add(this.toCoordinate(lastCoordinate));
        } else {
            // 记录不存在直接添加
            WarningStatusEntity coordinateAdd = this.getWarningStatusEntity(tcpMsgObj);
            ConsumerBatchMsgServiceImpl.COORDINATE_MAP.put(tagId, coordinateAdd);
            this.coordinateEntityList.add(this.toCoordinate(tcpMsgObj));
        }
    }

    private WarningStatusEntity getWarningStatusEntity(WarningStatusEntity tcpMsgObj) {
        WarningStatusEntity warning = new WarningStatusEntity();
        warning.setCode(tcpMsgObj.getCode());
        warning.setTagId(tcpMsgObj.getTagId());
        warning.setType(tcpMsgObj.getType());
        warning.setPosX(tcpMsgObj.getPosX());
        warning.setPosY(tcpMsgObj.getPosY());
        warning.setPosZ(tcpMsgObj.getPosZ());
        warning.setName(tcpMsgObj.getName());
        warning.setState(STATE);
        warning.setAreaCode(tcpMsgObj.getAreaCode());
        warning.setAreaName(tcpMsgObj.getAreaName());
        warning.setPosCode(tcpMsgObj.getPosCode());
        warning.setPosition(tcpMsgObj.getPosition());
        warning.setTimestamp(tcpMsgObj.getTimestamp());
        return warning;
    }

    /**
     * 报警消息处理,保存报警消息到Map中并更新处于报警状态的Tag的报警状态
     */
    private void saveWarningMap(WarningStatusEntity tcpMsgObj, Integer tcpType, Long tagId,
                                String warningCode, String msg) {
        boolean flag = ConsumerBatchMsgServiceImpl.WARNING_MAP.containsKey(tagId);
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

        this.warningMap(tcpMsgObj, tcpType, tagId, warningCode, msg, flag, timestamp, op, level, name, code, areaName, areaCode, posCode, position);

        this.updateCoordinateMap(tcpMsgObj, tcpType, tagId, timestamp, op, warningCode, level, msg, name, code,
                areaName, areaCode, posCode, position);

    }

    private void warningMap(WarningStatusEntity tcpMsgObj, Integer tcpType, Long tagId, String warningCode, String msg, boolean flag, Long timestamp, Integer op, String level, String name, String code, String areaName, String areaCode, String posCode, String position) {
        if (flag) {
            // 更新上一次报警消息
            WarningStatusEntity dataObj = null;
            dataObj = ConsumerBatchMsgServiceImpl.WARNING_MAP.get(tagId);
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
            dataObj.setState(STATE);
            if (posCode != null) {
                dataObj.setPosCode(posCode);
            }
            if (position != null) {
                dataObj.setPosition(position);
            }
             ConsumerBatchMsgServiceImpl.WARNING_MAP.put(tagId, dataObj);
            this.warningStatusEntityList.add(dataObj);
        } else {
            // 直接添加
            WarningStatusEntity warningStatusEntity = this.getWarningEntity(tcpMsgObj);
            ConsumerBatchMsgServiceImpl.WARNING_MAP.put(tagId, warningStatusEntity);
        }
    }

    /**
     * 对于处理报警状态的tag，在期坐标消息中带上报警信息
     **/
    private void updateCoordinateMap(WarningStatusEntity tcpMsgObj, Integer type, Long tagId,
                                     Long timestamp, Integer op, String warningCode, String level, String msg, String name, String code,
                                     String areaName, String areaCode, String posCode, String position) {
        boolean coorFlag = ConsumerBatchMsgServiceImpl.COORDINATE_MAP.containsKey(tagId);
        // 记录存在
        if (coorFlag) {
            WarningStatusEntity lastCoordinate = ConsumerBatchMsgServiceImpl.COORDINATE_MAP.get(tagId);
            lastCoordinate.setOp(op);
            lastCoordinate.setMsg(msg);
            lastCoordinate.setName(name);
            lastCoordinate.setCode(code);
            lastCoordinate.setAreaCode(areaCode);
            lastCoordinate.setAreaName(areaName);
            lastCoordinate.setLevel(level);
            lastCoordinate.setTimestamp(timestamp);
            lastCoordinate.setWarningCode(warningCode);
            lastCoordinate.setState(STATE);
            if (posCode != null) {
                lastCoordinate.setPosCode(posCode);
            }
            if (position != null) {
                lastCoordinate.setPosition(position);
            }
            ConsumerBatchMsgServiceImpl.COORDINATE_MAP.put(tagId, lastCoordinate);
            this.coordinateEntityList.add(this.toCoordinate(lastCoordinate));
        } else {
            WarningStatusEntity warningStatusEntity = this.getWarningEntity(tcpMsgObj);
            ConsumerBatchMsgServiceImpl.COORDINATE_MAP.put(tagId, warningStatusEntity);
        }
    }

    private WarningStatusEntity getWarningEntity(WarningStatusEntity tcpMsgObj) {
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
        warning.setState(STATE);
        return warning;
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
     * 消息格式转换
     **/
    private CoordinateEntity toCoordinate(WarningStatusEntity tcpMsgObj) {
        CoordinateEntity coordinate = new CoordinateEntity();
        if (tcpMsgObj == null) {
            return coordinate;
        }
        //排除报警消息
        if (tcpMsgObj.getType() == 2) {
            coordinate.setLevel(tcpMsgObj.getLevel());
            coordinate.setMsg(tcpMsgObj.getMsg());
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
        }
        return coordinate;
    }

    /**
     * 坐标转换
     */
    private String convertZero(String receiveMsg) {
        JSONObject jsonObj = JSON.parseObject(receiveMsg);
        Integer type = jsonObj.getInteger("type");
        if (type == null) {
            return receiveMsg;
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
     * 处理kakfka消息
     */
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


    @Override
    public List<CoordinateEntity> getCoordinateEntityList() {
        return coordinateEntityList;
    }

    @Override
    public void setCoordinateEntityListEmpty() {
        this.coordinateEntityList = new ArrayList<>();
    }

    @Override
    public List<WarningStatusEntity> getWarningStatusEntityList() {
        return warningStatusEntityList;
    }

    @Override
    public void setWarningStatusEntityListEmpty() {
        this.warningStatusEntityList = new ArrayList<>();
    }


    @Override
    public List<TagWarningEntity> getTagWarningEntityList() {
        return tagWarningEntityList;
    }

    @Override
    public void setTagWarningEntityListEmpty() {
        this.tagWarningEntityList = new ArrayList<>();
    }

}
