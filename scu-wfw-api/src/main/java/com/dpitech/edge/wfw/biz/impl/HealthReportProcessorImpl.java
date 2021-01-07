package com.dpitech.edge.wfw.biz.impl;

import com.alibaba.fastjson.JSONObject;
import com.dpitech.edge.common.CommonConst;
import com.dpitech.edge.common.CommonUtil;
import com.dpitech.edge.common.HttpUtil;
import com.dpitech.edge.common.log.LogUtil;
import com.dpitech.edge.wfw.biz.exceptions.ApiException;
import com.dpitech.edge.wfw.biz.facade.HealthReportProcessor;
import com.dpitech.edge.wfw.biz.facade.SelfInfoProcessor;
import com.dpitech.edge.wfw.biz.model.SelfInfoModel;
import com.dpitech.edge.wfw.ua.excepton.AuthException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * 健康码上报实现
 *
 * @author rusheng
 */
@Slf4j
public class HealthReportProcessorImpl implements HealthReportProcessor {

   private static final Pattern COMPILED_ID_REG = Pattern.compile("var def = \\{(.*?)};");

    /**
     * self info processor
     */
    private final SelfInfoProcessor selfInfoProcessor = new SelfInfoProcessorImpl();

    /**
     * 江安定位上报
     * @param authCookie 经过鉴权的cookie
     * @return json object
     * @throws AuthException 用户名密码错误
     * @throws IOException api call error.
     * @throws ApiException api exception.
     */
    @Override
    public JSONObject reportJiangAn(String authCookie) throws AuthException, IOException, ApiException {

        SelfInfoModel selfInfoModel = selfInfoProcessor.getInfo(authCookie);
        LogUtil.infof(log, "self info model: {}", selfInfoModel);

        String uid = selfInfoModel.getUid();
        String stuNumber = selfInfoModel.getRole().getStuNumber();

        LogUtil.infof(log, "stuNum: {}, ready to report health.", stuNumber);
        String formDataBody = CommonUtil.fromMapGetFormDataString(getReportFormDataMap(uid, authCookie));

        String respBody;
        try {
            respBody = HttpUtil.postWithFormData(CommonConst.NCOV_REPORT_URL,
                    formDataBody, authCookie, CommonConst.COMMON_REFER).body();
        } catch (InterruptedException e) {
            throw new ApiException(e.toString(), e, "call report ncov error.");
        }

        JSONObject res = JSONObject.parseObject(respBody);
        LogUtil.infof(log, "report health success, response is: {}", res.toJSONString());
        return res;
    }

    /**
     * body form data
     * @param uid uid
     * @param authCookie authed cookie
     * @return map
     * @throws IOException IO exception
     * @throws ApiException api exception
     */
    private Map<String, String> getReportFormDataMap(String uid, String authCookie) throws IOException {

        Map<String, String> res = new HashMap<>();
        res.put("sfjxhsjc", "1");
        res.put("hsjcrq", "2020-09-12");
        res.put("hsjcdd", "四川大学江安校区体育场");
        res.put("hsjcjg", "1");
        res.put("zgfxdq", "0");
        res.put("mjry", "0");
        res.put("csmjry", "0");
        res.put("szxqmc", "江安校区");
        res.put("tw", "2");
        res.put("sfcxtz", "0");
        res.put("sfjcbh", "0");
        res.put("sfcxzysx", "0");
        res.put("qksm", "");
        res.put("sfyyjc", "0");
        res.put("jcjgqr", "0");
        res.put("remark", "");
        res.put("address", "四川省成都市双流区西航港街道川大路三段369号四川大学江安校区");
        res.put("geo_api_info", "{\"type\":\"complete\",\"position\":{\"Q\":30.552435438369,\"R\":103.99267225477502,\"lng\":103.992672,\"lat\":30.552435},\"location_type\":\"html5\",\"message\":\"Get ipLocation failed.Get geolocation success.Convert Success.Get address success.\",\"accuracy\":37,\"isConverted\":true,\"status\":1,\"addressComponent\":{\"citycode\":\"028\",\"adcode\":\"510116\",\"businessAreas\":[{\"name\":\"白家\",\"id\":\"510116\",\"location\":{\"Q\":30.562482,\"R\":104.006821,\"lng\":104.006821,\"lat\":30.562482}}],\"neighborhoodType\":\"\",\"neighborhood\":\"\",\"building\":\"\",\"buildingType\":\"\",\"street\":\"川大路三段\",\"streetNumber\":\"369号\",\"country\":\"中国\",\"province\":\"四川省\",\"city\":\"成都市\",\"district\":\"双流区\",\"township\":\"西航港街道\"},\"formattedAddress\":\"四川省成都市双流区西航港街道川大路三段369号四川大学江安校区\",\"roads\":[],\"crosses\":[],\"pois\":[],\"info\":\"SUCCESS\"}");
        res.put("area", "四川省 成都市 双流区");
        res.put("province", "四川省");
        res.put("city", "成都市");
        res.put("sfzx", "1");
        res.put("sfjcwhry", "0");
        res.put("sfjchbry", "0");
        res.put("sfcyglq", "0");
        res.put("gllx", "");
        res.put("glksrq", "");
        res.put("jcbhlx", "");
        res.put("jcbhrq", "");
        res.put("bztcyy", "4");
        res.put("sftjhb", "0");
        res.put("sftjwh", "0");
        res.put("szcs", "");
        res.put("szgj", "");
        res.put("jcjg", "");
        res.put("date", DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now()));
        res.put("uid", uid);
        res.put("created", String.valueOf(System.currentTimeMillis() / 1000));
        res.put("jcqzrq", "");
        res.put("sfjcqz", "");
        res.put("szsqsfybl", "0");
        res.put("sfsqhzjkk", "0");
        res.put("sqhzjkkys", "");
        res.put("sfygtjzzfj", "0");
        res.put("gtjzzfjsj", "");
        res.put("bzxyy", "");
        res.put("id", getId(authCookie));
        res.put("gwszdd", "");
        res.put("sfyqjzgc", "");
        res.put("jrsfqzys", "");
        res.put("jrsfqzfy", "");
        res.put("szgjcs", "");
        res.put("ismoved", "0");

        return res;
    }

    /**
     * 计算id
     * 这个id字段没看出有很大用，微服务中没有对它有处理，是在页面的vue开头的<code>var def = {...}</code>中定义的，是动态的每次访问都不一样。
     * 尝试过id每次提交都一样没有异常
     * 但保险起见，动态获取 and 正则提取这个id
     * @return String id
     * @throws IOException IO exception
     * @throws ApiException api exception
     */
    private String getId(String authCookie) throws IOException, ApiException {
        String pageHtml;
        try {
            pageHtml = HttpUtil.get(CommonConst.NCOV_PAGE_URL, authCookie, CommonConst.COMMON_REFER).body();
        } catch (InterruptedException e) {
            throw new ApiException(e.toString(), e, "get id error.");
        }

        String afterDealHtml = pageHtml.replaceAll("[\\t\\n\\r]", "");
        LogUtil.debugf(log, "health code page length: {}", afterDealHtml.length());

        Matcher matcher = COMPILED_ID_REG.matcher(afterDealHtml);
        if (matcher.find()) {
            String defJsonInner = matcher.group(1);
            String defJson = "{" + defJsonInner + "}";
            JSONObject jsonObject = JSONObject.parseObject(defJson);
            return jsonObject.getString("id");
        }
        else {
            return CommonUtil.getRandomNumWithLengthNonZeroBegin(6);
        }

    }

}
