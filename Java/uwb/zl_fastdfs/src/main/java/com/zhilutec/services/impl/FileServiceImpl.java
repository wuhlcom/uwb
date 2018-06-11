package com.zhilutec.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.common.validators.FastDFSValidator;
import com.zhilutec.common.validators.FileValidator;
import com.zhilutec.dbs.daos.FileDao;
import com.zhilutec.dbs.entities.FileEntity;
import com.zhilutec.services.IFastDFSFileService;
import com.zhilutec.services.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FileServiceImpl implements IFileService {

    @Autowired
    FileDao fileDao;

    @Resource
    IFastDFSFileService fastDFSFileService;


    /**
     * 根据状态查询文件信息
     */
    private List<FileEntity> getFileInfoByStatus(Integer integer) {
        Example example = new Example(FileEntity.class);
        Example.Criteria criteria = example.createCriteria();
        example.orderBy("name asc");
        criteria.andEqualTo("status", integer).andEqualTo("isdel", 1);
        List<FileEntity> fileEntities = fileDao.selectByExample(example);
        for (FileEntity fileEntity : fileEntities) {
            fileEntity.setIsdel(null);
        }
        return fileEntities;
    }


    /**
     * 添加文件,文件上传时添加到库
     */
    @Override
    public Integer add(String jsonStr) {
        FileEntity record = new FileEntity();
        record = JSONObject.parseObject(jsonStr, FileEntity.class);
        return fileDao.insertSelective(record);
    }

    /**
     * 删除文件,数据库记录删除，但文件服务器不删除
     * 文件处理启用不能删除
     */
    @Override
    public String delete(JSONObject jsonObject) {
        List ids = jsonObject.getJSONArray("ids");
        // 创建example
        Example example = new Example(FileEntity.class);
        // 创建查询条件
        Example.Criteria criteria = example.createCriteria();
        // 设置查询条件 多个andEqualTo串联表示 and条件查询
        criteria.andIn("id", ids).andEqualTo("isdel", 1).andEqualTo("status", 0);
        List<FileEntity> fileEntities = fileDao.selectByExample(example);
        if (fileEntities != null && fileEntities.size() > 0) {
            int deleteRs = fileDao.batchUpdateIsdel(ids);
            if (deleteRs > 0) {
                return Result.ok("删除文件成功").toJSONString();
            } else {
                return Result.error("未找到对应的文件").toJSONString();
            }
        } else {
            return Result.error("文件正在使用不允许删除").toJSONString();
        }
    }

    /**
     * @param jsonObject 修改文件
     *                   文件启用时也可以修改,但不能修改文件状态
     */
    @Transactional
    @Override
    public String update(JSONObject jsonObject) {
        Result valRs = FileValidator.updateVal(jsonObject);
        if ((Integer) valRs.get("errcode") != ResultCode.SUCCESS.getCode()) {
            return valRs.toJSONString();
        }
        FileEntity fileEntity = JSONObject.parseObject(jsonObject.toJSONString(), FileEntity.class);
        fileEntity.setStatus(null);
        Long id = fileEntity.getId();
        // 创建example
        Example example = new Example(FileEntity.class);
        // 创建查询条件
        Example.Criteria criteria = example.createCriteria();
        // 设置查询条件 多个andEqualTo串联表示 and条件查询
        criteria.andEqualTo("id", id).andEqualTo("isdel", 1);
        int updateRs = fileDao.updateByExampleSelective(fileEntity, example);
        if (updateRs > 0) {
            return Result.ok("修改图片信息成功").toJSONString();
        } else {
            return Result.error("修改图片信息失败").toJSONString();
        }
    }


    /**
     * 动态sql查询地图
     */
    @Override
    public String getFilesRs(JSONObject jsonObject) {
        Map<String, Object> rsMap = new HashMap<>();
        //参数校验
        Result validator = FastDFSValidator.getVal(jsonObject);
        if (Integer.valueOf((Integer) validator.get("errcode")) != ResultCode.SUCCESS.getCode())
            return validator.toJSONString();
        FileEntity fileEntityParam = JSONObject.parseObject(jsonObject.toJSONString(), FileEntity.class);

        Integer page = fileEntityParam.getPage();
        Integer listRows = fileEntityParam.getListRows();
        String order = fileEntityParam.getOrder();
        if (order == null || order.isEmpty()) {
            order = "desc";
            fileEntityParam.setOrder(order);
        }

        //初始化分页参数
        page = page == null ? 1 : page;
        listRows = listRows == null ? 10 : listRows;

        //进行分页
        Page<FileEntity> pageObj = PageHelper.startPage(page, listRows);
        List<FileEntity> fileEntities = fileDao.getFiles(fileEntityParam);
        //获取总数
        long count = pageObj.getTotal();
        //生成地图url
        String ipPort = fastDFSFileService.getTrackerAddrUrl();

        for (FileEntity fileEntity : fileEntities) {
            String downLoadPath = fileEntity.getRemotePath();
            fileEntity.setDownPath("/" + downLoadPath);
            if (ipPort != null && !ipPort.isEmpty()) {
                fileEntity.setPath(ipPort + downLoadPath);
            }
            fileEntity.setRemotePath(null);
        }


        rsMap.put("result", fileEntities);
        rsMap.put("totalRows", count);
        return Result.ok(rsMap).toJSONString();
    }

    /**
     * 修改文件信息和状态
     * 只允许有一个地图启用,启用新地图,旧地图要自动禁用
     */
    @Transactional
    @Override
    public String mapSwitch(JSONObject jsonObject) {
        Result valRs = FileValidator.swVal(jsonObject);
        if ((Integer) valRs.get("errcode") != ResultCode.SUCCESS.getCode()) {
            return valRs.toJSONString();
        }

        Long id = jsonObject.getLong("id");
        Integer status = jsonObject.getInteger("status");
        FileEntity fileEntityParam = new FileEntity();
        fileEntityParam.setStatus(status);
        fileEntityParam.setId(id);

        //打开地图，将其它地图设置为不启用
        if (status.intValue() == 1) {
            //查出所有启用状态的地图
            List<FileEntity> fileEntities = this.getFileInfoByStatus(status);
            FileEntity fileEntity1 = new FileEntity();
            fileEntity1.setStatus(0);
            Example example = new Example(FileEntity.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("status", status);
            //将原有的status为1的改为0
            fileDao.updateByExampleSelective(fileEntity1, example);
        }
        int updateRs = fileDao.updateOne(fileEntityParam);
        if (updateRs > 0) {
            return Result.ok("操作成功").toJSONString();
        } else {
            return Result.error("操作失败").toJSONString();
        }
    }

    private String replaceIndex(int index, String res, String str) {
        return str + res.substring(index + 1);
    }


}
