package com.dpitech.edge.wfw.biz.model;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author rusheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SchoolNetworkDeviceFetchModel implements FetchModel {

    private String deviceId;

    private String deviceMac;

    private String deviceIp;

    /**
     * json对象构造实体
     * @param jsonItem json
     * @return model
     */
    public static SchoolNetworkDeviceFetchModel getInstanceFromJson(JSONObject jsonItem) {
        StringBuilder macProcessor = new StringBuilder(jsonItem.getString("device_id"));

        // deviceId -> mac addr
        macProcessor.insert(2, ':');
        macProcessor.insert(4, ':');
        macProcessor.insert(6, ':');
        macProcessor.insert(8, ':');
        macProcessor.insert(10, ':');

        return SchoolNetworkDeviceFetchModel.builder()
                .deviceId(jsonItem.getString("device_id"))
                .deviceIp(jsonItem.getString("ip"))
                .deviceMac(macProcessor.toString())
                .build();
    }

}
