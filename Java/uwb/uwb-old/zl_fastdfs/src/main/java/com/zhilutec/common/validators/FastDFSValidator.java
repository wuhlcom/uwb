package com.zhilutec.common.validators;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public class FastDFSValidator {

    public static Result uploadVal(String name, MultipartFile file, Double length, Double width, Integer picLength, Integer picWidth) {
        System.out.println("name：" + name);
        if (RegexUtil.isNull(name) || !isName(name))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "图片名称格式输入错误");

        if (file.isEmpty() || file.getSize() < 20)
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "文件错误");

        System.out.println("length：" + length);
        if (RegexUtil.isNull(length) || length > 9000000 && length < 0)
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "实际长度输入错误");

        System.out.println("width：" + width);
        if (RegexUtil.isNull(width) || width > 9000000 && width < 0)
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "实际宽度输入错误");

        System.out.println("picLength：" + picLength);
        if (RegexUtil.isNull(picLength) || picLength > 9000000 && picLength < 0)
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "图片长度输入错误");

        System.out.println("picWidth：" + picWidth);
        if (RegexUtil.isNull(picWidth) || picWidth > 9000000 && picWidth < 0)
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "图片宽度输入错误");

        System.out.println("验证通过");
        return Result.ok(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getErrmsg());
    }


    public static Result uploadVal(MultipartHttpServletRequest multipartRequest) {
        MultipartFile multipartFile = multipartRequest.getFile("file");
        String name = multipartRequest.getParameter("name");
        String typeStr = multipartRequest.getParameter("type");
        System.out.println("type：" + typeStr);
        if (RegexUtil.isNull(typeStr))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "文件类型不能为空");

        Integer type = Integer.parseInt(typeStr);
        if (type.intValue()!=0&&type.intValue()!=1&&type.intValue()!=2&&type.intValue()!=3)
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "文件类型错误");

        if (type==0) {
            String lengthStr = multipartRequest.getParameter("length");
            Double length = Double.parseDouble(Double.valueOf(lengthStr).toString());

            String widthStr = multipartRequest.getParameter("width");
            Double width = Double.parseDouble(Double.valueOf(widthStr).toString());

            String picLengthStr = multipartRequest.getParameter("picLength");
            Integer picLength = Integer.parseInt(Integer.valueOf(picLengthStr).toString());

            String picWidthStr = multipartRequest.getParameter("picWidth");
            Integer picWidth = Integer.parseInt(Integer.valueOf(picWidthStr).toString());

            System.out.println("length：" + length);
            if (RegexUtil.isNull(length) || length > 9000000 && length < 0)
                return Result.error(ResultCode.PARAMETER_ERR.getCode(), "实际长度输入错误");

            System.out.println("width：" + width);
            if (RegexUtil.isNull(width) || width > 9000000 && width < 0)
                return Result.error(ResultCode.PARAMETER_ERR.getCode(), "实际宽度输入错误");

            System.out.println("picLength：" + picLength);
            if (RegexUtil.isNull(picLength) || picLength > 9000000 && picLength < 0)
                return Result.error(ResultCode.PARAMETER_ERR.getCode(), "图片长度输入错误");

            System.out.println("picWidth：" + picWidth);
            if (RegexUtil.isNull(picWidth) || picWidth > 9000000 && picWidth < 0)
                return Result.error(ResultCode.PARAMETER_ERR.getCode(), "图片宽度输入错误");
        }

        System.out.println("name：" + name);
        if (RegexUtil.isNull(name) || !isName(name))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "文件名格式输入错误");

        if (multipartFile.isEmpty() || multipartFile.getSize() < 20)
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "文件错误");

        System.out.println("验证通过");
        return Result.ok(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getErrmsg());
    }

    public static Result getVal(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        System.out.println("name：" + name);
        if (RegexUtil.isNotNull(name) && !isName(name))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "图片名称格式输入错误");

        Double length = jsonObject.getDouble("length");
        System.out.println("length：" + length);
        if (RegexUtil.isNotNull(length) && length > 1000 && length < 0)
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "实际长度输入错误");

        Double width = jsonObject.getDouble("width");
        System.out.println("width：" + width);
        if (RegexUtil.isNotNull(width) && width > 1000 && width < 0)
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "实际宽度输入错误");

        Integer picLength = jsonObject.getInteger("picLength");
        System.out.println("picLength：" + picLength);
        if (RegexUtil.isNotNull(picLength) && picLength > 1000 && picLength < 0)
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "图片长度输入错误");

        Integer picWidth = jsonObject.getInteger("picWidth");
        System.out.println("picWidth：" + picWidth);
        if (RegexUtil.isNotNull(picWidth) && picWidth > 1000 && picWidth < 0)
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "图片宽度输入错误");

        System.out.println("验证通过");
        return Result.ok(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getErrmsg());
    }

    public static Boolean isName(String name) {
        if (RegexUtil.stringCheckDot(name))
            return true;
        return false;
    }
}
