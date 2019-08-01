package com.lsfly.controller.course;

import com.github.pagehelper.PageInfo;
import com.lsfly.controller.system.BaseController;
import com.lsfly.exception.InvalidParamException;
import com.lsfly.sys.Result;
import com.lsfly.bas.model.course.TCourseVideo;
import com.lsfly.bas.model.course.TCourseVideoExample;
import com.lsfly.bas.model.course.ext.TCourseVideoEdit;
import com.lsfly.bas.model.course.ext.TCourseVideoList;
import com.lsfly.bas.service.course.ITCourseVideoService;
import com.lsfly.util.MessageHelper;
import com.lsfly.util.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by on 18-8-24 下午6:13
 * TCourseVideo的控制器业务处理页面
 */

@Controller
@RequestMapping(value = "tCourseVideo")
public class TCourseVideoController extends BaseController{
    public static final Logger logger = LoggerFactory.getLogger(TCourseVideoController.class);

    @Autowired
    private ITCourseVideoService iTCourseVideoService;
    /**
     * 获取全部列表，通常用于下拉框
     * @param request
     * @param tCourseVideo
     * @return
     */
    @RequestMapping(value = "/getLists.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getLists(HttpServletRequest request,TCourseVideo tCourseVideo){
        Result result = new Result();
        TCourseVideoExample example = new TCourseVideoExample();
        TCourseVideoExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteNotEqualTo("1");
        List<TCourseVideo> list=iTCourseVideoService.selectByExample(example);
        result.setBizData(list);
        return result;
    }

    /**
     * 获取分页列表，用于页面的列表查询
     * @param request
     * @param tCourseVideoList
     * @return
     */
    @RequestMapping(value = "/list.do", method = RequestMethod.POST)
    @ResponseBody
    public PageInfo list(HttpServletRequest request,  TCourseVideoList tCourseVideoList){
        Result result = new Result();
        PageInfo pageInfo=iTCourseVideoService.list(tCourseVideoList);
        return pageInfo;
    }

    /**
     * 获取实体信息
     * @param request
     * @param tCourseVideoEdit
     * @return
     */
    @RequestMapping(value = "/getInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getInfo(HttpServletRequest request,  TCourseVideoEdit tCourseVideoEdit){
        Result result = new Result();
        if(ToolUtil.isEmpty(tCourseVideoEdit.getUid())){
            throw new InvalidParamException(MessageHelper.ID_ISNULL);  //抛出异常
        }
        TCourseVideoEdit tCourseVideo=iTCourseVideoService.getInfo(tCourseVideoEdit.getUid());
        result.setBizData(tCourseVideo);
        return result;
    }

    /**
     * 保存实体信息
     * @param request
     * @param tCourseVideoEdit
     * @return
     */
    @RequestMapping(value = "/saveOrEdit.do", method = RequestMethod.POST)
    @ResponseBody
    public Result saveOrEdit(HttpServletRequest request,  TCourseVideoEdit tCourseVideoEdit){
        Result result = new Result();
        if(tCourseVideoEdit==null){
            throw new InvalidParamException(MessageHelper.MODEL_ISNULL);  //抛出异常
        }
        int code=iTCourseVideoService.saveOrEdit(tCourseVideoEdit);
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
        int code=iTCourseVideoService.delete(ids);
        result.setBizData(code);
        return result;
    }
}
