package com.lsfly.util;

/**
 * Created by weicheng on 2017/10/26.
 */
public class ReadProperties {

    /**
     * 根据配置文件的key值获取value值
     * @param key
     * @return
     */
    public static String getValue(String key){
        return PropertiesUtil.getValue(key);
    }
}
