package com.lsfly.util;

import com.google.gson.Gson;
import com.lsfly.exception.InvalidParamException;
import com.lsfly.sys.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 调用流媒体远程服务
 * @author huoquan
 * @date 2018/9/26.
 */
public class MediaApiUtil {
    private static final Logger logger = LoggerFactory.getLogger(MediaApiUtil.class);
    //访问媒体服务器的端口，都统一端口
    private static String serverPort;

    static{
        serverPort=PropertiesUtil.getValue("media.path");
        if(ToolUtil.isEmpty(serverPort)){
            serverPort="8088";
        }
    }


    /**
     * 视频转码
     * @param ip 流媒体的ip
     * @param mediaPort 流媒体的端口
     * @param filePath 需要转化的文件路径,相对路径，比如/upload/XX.mp4
     * @return 返回转码后的文件路径
     */
    public static Result convertFile(String ip,String mediaPort, String filePath){
        Result result=new Result();
        //String path="";
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("path",filePath);
        map.put("httpPath", ReadProperties.getValue("uploadHttpPath")+filePath);
        String data=HttpRequestUtils.doPost(null,map,
                "http://"+ip+":"+mediaPort+"/media/media/convertFile.do", HttpRequestUtils.formUrlencoded, null);
        try{
            Gson gson = new Gson();
            Result jsonResult = gson.fromJson(data, Result.class);
            if(jsonResult!=null){
                if("0".equals(jsonResult.getRtnCode())){
                    //成功
                    Map<String,Object> dataMap=(Map<String,Object>)jsonResult.getBizData();
//                    if(dataMap!=null&&dataMap.containsKey("path")&&ToolUtil.isNotEmpty((String)dataMap.get("path"))){
//                        path=(String)dataMap.get("path");
//                    }
                    result.setBizData(dataMap);

                }
            }
        }catch (Exception e){
            logger.error("获取媒体服务器数据转化异常",e);
            throw new InvalidParamException(e.getMessage());
        }
        return result;
    }


    /**
     * 开启直播
     * @param sourcepath
     * @param ip 流媒体的ip
     * @param mediaPort 流媒体的端口
     * @param port 流媒体的端口
     * @param id 主键
     * @return
     */
    public static Result openLive(String sourcepath,String ip,String mediaPort,String port,String id){
        Result result = new Result();
        Map<String,Object> map = new HashMap<>();
        map.put("ip", ip);
        map.put("port", port);
        map.put("id", id);
        map.put("sourcepath", sourcepath);
        String data=HttpRequestUtils.doPost(null,map,
                "http://"+ip+":"+mediaPort+"/media/media/openLive.do", HttpRequestUtils.formUrlencoded, null);
        try{
            Gson gson = new Gson();
            Result jsonResult = gson.fromJson(data, Result.class);
            if(jsonResult!=null){
                if("0".equals(jsonResult.getRtnCode())){
                    //成功
                    result.setBizData(jsonResult.getBizData());
                }
            }
        }catch (Exception e){
            logger.error("获取媒体服务器数据转化异常",e);
            throw new InvalidParamException(e.getMessage());
        }
        return result;
    }


    /**
     * 关闭直播
     * @param ip 流媒体的ip
     * @param mediaPort 流媒体的端口
     * @param id 主键
     * @return
     */
    public static Result closeLive(String ip,String mediaPort,String id){
        Result result = new Result();
        Map<String,Object> map = new HashMap<>();
        map.put("id", id);
        String data=HttpRequestUtils.doPost(null,map,
                "http://"+ip+":"+mediaPort+"/media/media/closeLive.do", HttpRequestUtils.formUrlencoded, null);
        try{
            Gson gson = new Gson();
            Result jsonResult = gson.fromJson(data, Result.class);
            if(jsonResult!=null){
                if("0".equals(jsonResult.getRtnCode())){
                    //成功
                    result.setBizData(jsonResult.getBizData());
                }
            }
        }catch (Exception e){
            logger.error("获取媒体服务器数据转化异常",e);
            throw new InvalidParamException(e.getMessage());
        }
        return result;
    }


    /**
     * 获取直播状态
     * @param ip 流媒体的ip
     * @param mediaPort 流媒体的端口
     * @param id id主键
     * @return
     */
    public static Boolean getLiveStatus(String ip,String mediaPort,String id){
        Result result = new Result();
        Map<String,Object> map = new HashMap<>();
        map.put("ids", id);
        Boolean live=false;
        try{
            String data=HttpRequestUtils.doPost(null,map,
                    "http://"+ip+":"+mediaPort+"/media/media/getLiveStatus.do", HttpRequestUtils.formUrlencoded, null);
            Gson gson = new Gson();
            Result jsonResult = gson.fromJson(data, Result.class);
            if(jsonResult!=null){
                if("0".equals(jsonResult.getRtnCode())){
                    //成功
                    result.setBizData(jsonResult.getBizData());
                }
            }
            Map<String,Object> dataMap=(Map)result.getBizData();
            live=(Boolean)dataMap.get(id);
        }catch (Exception e){
            logger.error("获取媒体服务器异常",e);
        }
        return live;
    }

    /**
     * 开启摄像头
     * @param sourcepath //摄像头源路径
     * @param ip 流媒体的ip
     * @param mediaPort 流媒体的端口
     * @param port 流媒体的端口
     * @param id 主键
     * @return
     */
    public static Result startLive(String sourcepath,String ip,String mediaPort,String port,String id){
        Result result = new Result();
        Map<String,Object> map = new HashMap<>();
        map.put("ip", ip);
        map.put("port", port);
        map.put("id", id);
        map.put("sourcepath", sourcepath);
        String data=HttpRequestUtils.doPost(null,map,
                "http://"+ip+":"+mediaPort+"/media/media/startLive.do", HttpRequestUtils.formUrlencoded, null);
        try{
            Gson gson = new Gson();
            Result jsonResult = gson.fromJson(data, Result.class);
            if(jsonResult!=null){
                if("0".equals(jsonResult.getRtnCode())){
                    //成功
                    result.setBizData(jsonResult.getBizData());
                }
            }
        }catch (Exception e){
            logger.error("获取媒体服务器数据转化异常",e);
            throw new InvalidParamException(e.getMessage());
        }
        return result;
    }

}
