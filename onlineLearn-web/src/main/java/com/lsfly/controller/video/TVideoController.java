package com.lsfly.controller.video;

import com.github.pagehelper.PageInfo;
import com.lsfly.controller.system.BaseController;
import com.lsfly.exception.InvalidParamException;
import com.lsfly.sys.Result;
import com.lsfly.bas.model.video.TVideo;
import com.lsfly.bas.model.video.TVideoExample;
import com.lsfly.bas.model.video.ext.TVideoEdit;
import com.lsfly.bas.model.video.ext.TVideoList;
import com.lsfly.bas.service.video.ITVideoService;
import com.lsfly.util.MessageHelper;
import com.lsfly.util.ToolUtil;
import com.lsfly.util.MediaApiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by on 18-8-24 下午6:13
 * TVideo的控制器业务处理页面
 */

@Controller
@RequestMapping(value = "tVideo")
public class TVideoController extends BaseController{
    public static final Logger logger = LoggerFactory.getLogger(TVideoController.class);

    @Autowired
    private ITVideoService iTVideoService;
    /**
     * 获取全部列表，通常用于下拉框
     * @param request
     * @param tVideo
     * @return
     */
    @RequestMapping(value = "/getLists.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getLists(HttpServletRequest request,TVideo tVideo){
        Result result = new Result();
        TVideoExample example = new TVideoExample();
        TVideoExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteNotEqualTo("1");
        List<TVideo> list=iTVideoService.selectByExample(example);
        result.setBizData(list);
        return result;
    }

    /**
     * 获取分页列表，用于页面的列表查询
     * @param request
     * @param tVideoList
     * @return
     */
    @RequestMapping(value = "/list.do", method = RequestMethod.POST)
    @ResponseBody
    public PageInfo list(HttpServletRequest request,  TVideoList tVideoList){
        Result result = new Result();
        PageInfo pageInfo=iTVideoService.list(tVideoList);
        return pageInfo;
    }

    /**
     * 获取实体信息
     * @param request
     * @param tVideoEdit
     * @return
     */
    @RequestMapping(value = "/getInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getInfo(HttpServletRequest request,  TVideoEdit tVideoEdit){
        Result result = new Result();
        if(ToolUtil.isEmpty(tVideoEdit.getUid())){
            throw new InvalidParamException(MessageHelper.ID_ISNULL);  //抛出异常
        }
        TVideoEdit tVideo=iTVideoService.getInfo(tVideoEdit.getUid());
        result.setBizData(tVideo);
        return result;
    }

    /**
     * 保存实体信息
     * @param request
     * @param tVideoEdit
     * @return
     */
    @RequestMapping(value = "/saveOrEdit.do", method = RequestMethod.POST)
    @ResponseBody
    public Result saveOrEdit(HttpServletRequest request,@RequestBody TVideoEdit tVideoEdit){
        Result result = new Result();
        if(tVideoEdit==null){
            throw new InvalidParamException(MessageHelper.MODEL_ISNULL);  //抛出异常
        }
        //一律转码成m3u8
        for (Map<String,String> object:tVideoEdit.getVideoList()){
        /*    Result result1=MediaApiUtil.convertFile(object.get("path"));
            if (result1.getRtnCode().equals("0")){
                Map<String,Object> map=(Map<String,Object>)result1.getBizData();
                //转码成功
                //m3u8路径
                object.put("path",map.get("path").toString());
                //封面路径
                object.put("videoCover",map.get("postPath").toString());
            }else {
                logger.info("======转码失败");
            }
            //object.put("videoCover",)*/
            //传视频路径和IP和rtmp端口号
            Result result1= MediaApiUtil.convertFile("localhost","8080",object.get("path"));
/*                File file = new File(ReadProperties.getValue("uploadPath")+object.get("path"));
                //转码后删除原文件
                if (file.exists()){
                    file.delete();
                }*/
            //将转码后的路径保存到数据库
            if (result1.getRtnCode().equals("0")){
                Map<String,Object> map=(Map<String,Object>)result1.getBizData();
                //m3u8路径
                object.put("path",map.get("path").toString());
                //封面路径
                object.put("videoCover",map.get("pictureUrl").toString());
            }

        }
        int code=iTVideoService.saveOrEdit(tVideoEdit);
        result.setBizData(code);
        return result;
    }

    /**
     * 删除实体信息
     * @param request
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete.do", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(HttpServletRequest request,@RequestParam("ids[]") String[] ids){
        Result result = new Result();
        if(ids!=null&&ids.length==0){
            throw new InvalidParamException(MessageHelper.ID_ISNULL);  //抛出异常
        }
        int code=iTVideoService.delete(ids);
        result.setBizData(code);
        return result;
    }
}
