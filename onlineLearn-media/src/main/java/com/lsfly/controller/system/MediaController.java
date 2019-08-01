package com.lsfly.controller.system;

import com.lsfly.sys.Result;
import com.lsfly.util.*;
import com.lsfly.util.FFmpegUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by huoquan on 18-8-24 下午6:13
 * upload的上传模块
 */

@Controller
@RequestMapping(value = "media")
public class MediaController extends BaseController {
    public static final Logger logger = LoggerFactory.getLogger(MediaController.class);


    /**
     * 视频转码
     *
     * @param request
     * @param httpPath 文件的http地址
     * @param path     文件路径,相对路径
     * @return
     */
    @RequestMapping(value = "/convertFile.do", method = RequestMethod.POST)
    @ResponseBody
    public Result convertFile(HttpServletRequest request, String httpPath, String path) {
        Result result = new Result();
        String fileName = httpPath.substring(httpPath.lastIndexOf("/") + 1, httpPath.lastIndexOf("."));//文件的文件名,例如：sss.mp4
        Map<String, Object> map = new HashMap<String, Object>();
        map = FFmpegUtil.convertFile(httpPath,path, PropertiesUtil.getValue("uploadPath") + "/video/"+DateUtils.dateToString(new Date(),"yyyyMMdd")+"/"+fileName +"/"+ fileName + ".m3u8", fileName);
        if (map == null) {
            result.setRtnCode("1");
            result.setMsg("转码失败");
        } else if (!(Boolean) map.get("success")) {
            result.setRtnCode("1");
            result.setMsg("转码失败");
        }

        result.setBizData(map);
        return result;
    }

    /**
     * 转码成MP4
     * @param request
     * @param ext
     * @param uuid
     * @param filePath
     * @return
     */

    @RequestMapping(value = "/convertFileToMp4.do", method = RequestMethod.POST)
    @ResponseBody
    public Result convertFileToMp4(HttpServletRequest request, @RequestParam("ext") String ext,@RequestParam("uuid") String uuid,@RequestParam("httpPath") String httpPath) {
        Result result = new Result();
       logger.info("fileName="+ext);
//        String path = "/intercom/" + DateUtils.dateToString(new Date(), "yyyyMMdd") + "/"
//                + uuid.replaceAll("-","")+"."+ext;
//        String realPath = PropertiesUtil.getValue("uploadPath") + path;
//        File uploadFile=null;
//        try {
//            uploadFile=new File(realPath);
//            if(FileUtils.createFile(realPath)){
//                file.transferTo(uploadFile);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            logger.error("下载异常");
//            result.setRtnCode("1");
//            result.setMsg("下载异常");
//        }
        //File file1=new File(httpPath);
        //if(file1.exists()){
            Map<String, Object> map = new HashMap<String, Object>();
            if(ext.equals("avi")) {//如果是avi转码，否则不转码
                map = FFmpegUtil.convertFileToMp4(httpPath, PropertiesUtil.getValue("uploadPath") + "/intercom/" + DateUtils.dateToString(new Date(), "yyyyMMdd") + "/" + uuid.replaceAll("-","") + ".mp4", uuid.replaceAll("-",""));
                if (map == null) {
                    result.setRtnCode("1");
                    result.setMsg("转码失败");
                } else if (!(Boolean) map.get("success")) {
                    result.setRtnCode("1");
                    result.setMsg("转码失败");
                }
                result.setBizData(map);
                return result;
            }else {
                //map.put("path",path);
                result.setBizData(map);
            }
        //}
        return result;
    }


    /**
     * 开启直播
     *
     * @param request
     * @param sourcepath
     * @param ip
     * @param port
     * @param id
     * @return
     */
    @RequestMapping(value = "/openLive.do", method = RequestMethod.POST)
    @ResponseBody
    public Result openLive(HttpServletRequest request, String sourcepath, String ip, String port, String id) {
        Result result = new Result();
        String path = FFmpegUtil.openLive(sourcepath, id, ip, port);
        result.setBizData(path);
        return result;
    }

    /**
     * 关闭直播
     *
     * @param request
     * @param id
     * @return
     */
    @RequestMapping(value = "/closeLive.do", method = RequestMethod.POST)
    @ResponseBody
    public Result closeLive(HttpServletRequest request, String id) {
        Result result = new Result();
        FFmpegUtil.closeLive(id);
        return result;
    }

    /**
     * 开启摄像头
     *
     * @param request
     * @param sourcepath
     * @param ip
     * @param port
     * @param id
     * @return
     */
    @RequestMapping(value = "/startLive.do", method = RequestMethod.POST)
    @ResponseBody
    public Result startLive(HttpServletRequest request, String sourcepath, String ip, String port, String id) {
        Result result = new Result();
        String path = FFmpegUtil.startLive(sourcepath, id, ip, port);
        result.setBizData(path);
        return result;
    }

    /**
     * 关闭摄像头
     *
     * @param request
     * @param id
     * @return
     */
    @RequestMapping(value = "/stopLive.do", method = RequestMethod.POST)
    @ResponseBody
    public Result stopLive(HttpServletRequest request, String id) {
        Result result = new Result();
        FFmpegUtil.closeLive(id);
        return result;
    }

    /**
     * 获取直播状态
     *
     * @param request
     * @param ids     id集合，以逗号分割
     * @return
     */
    @RequestMapping(value = "/getLiveStatus.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getLiveStatus(HttpServletRequest request, String ids) {
        Result result = new Result();
        String[] idArray = ids.split(",");
        Map<String, Object> map = new HashMap<String, Object>();
        for (String id : idArray) {
            if (ToolUtil.isNotEmpty(id)) {
                map.put(id, FFmpegUtil.getLiveStatus(id));
            }
        }
        result.setBizData(map);
        return result;
    }
}
