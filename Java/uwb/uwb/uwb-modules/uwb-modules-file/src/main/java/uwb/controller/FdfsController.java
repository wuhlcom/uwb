package uwb.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.uwb.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uwb.service.IFdfsClientService;
import uwb.service.IFdfsFileService;

import javax.annotation.Resource;


@RestController
@RequestMapping(value = "/fdfs")
@EnableAutoConfiguration
public class FdfsController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    IFdfsClientService fdfsClientService;

    @Resource
    IFdfsFileService fdfsFileService;


    @RequestMapping(value = "/upload", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String fileUpload(MultipartHttpServletRequest multipartRequest) {
        try {
            return fdfsClientService.uploadFdfsFileRs(multipartRequest);
        } catch (Exception e) {
            logger.error("upload file failed", e);
            return Result.error("文件上传出现异常").toJSONString();
        }
    }


    @RequestMapping(value = "/files", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String files(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return fdfsFileService.getFilesRs(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取文件信息失败").toJSONString();
        }
    }


    @RequestMapping(value = "/update", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String update(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return fdfsFileService.updateDb(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("文件更新失败").toJSONString();
        }
    }

    @RequestMapping(value = "/switch", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String mapSwitch(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return fdfsFileService.mapSwitch(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("修改文件状态失败").toJSONString();
        }
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String delete(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return fdfsFileService.deleteDb(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("文件删除失败").toJSONString();
        }
    }
}
