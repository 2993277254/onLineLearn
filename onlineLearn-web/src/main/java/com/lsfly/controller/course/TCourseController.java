package com.lsfly.controller.course;

import com.github.pagehelper.PageInfo;
import com.lsfly.controller.system.BaseController;
import com.lsfly.exception.InvalidParamException;
import com.lsfly.sys.Result;
import com.lsfly.bas.model.course.TCourse;
import com.lsfly.bas.model.course.TCourseExample;
import com.lsfly.bas.model.course.ext.TCourseEdit;
import com.lsfly.bas.model.course.ext.TCourseList;
import com.lsfly.bas.service.course.ITCourseService;
import com.lsfly.util.MessageHelper;
import com.lsfly.util.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by on 18-8-24 下午6:13
 * TCourse的控制器业务处理页面
 */

@Controller
@RequestMapping(value = "tCourse")
public class TCourseController extends BaseController{
    public static final Logger logger = LoggerFactory.getLogger(TCourseController.class);

    @Autowired
    private ITCourseService iTCourseService;
    /**
     * 获取全部列表，通常用于下拉框
     * @param request
     * @param tCourse
     * @return
     */
    @RequestMapping(value = "/getLists.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getLists(HttpServletRequest request,TCourse tCourse){
        Result result = new Result();
        TCourseExample example = new TCourseExample();
        TCourseExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteNotEqualTo("1");
        List<TCourse> list=iTCourseService.selectByExample(example);
        result.setBizData(list);
        return result;
    }

    /**
     * 获取分页列表，用于页面的列表查询
     * @param request
     * @param tCourseList
     * @return
     */
    @RequestMapping(value = "/list.do", method = RequestMethod.POST)
    @ResponseBody
    public PageInfo list(HttpServletRequest request,  TCourseList tCourseList){
        Result result = new Result();
        PageInfo pageInfo=iTCourseService.list(tCourseList);
        return pageInfo;
    }
    @RequestMapping(value = "/list2.do", method = RequestMethod.POST)
    @ResponseBody
    public Result list2(HttpServletRequest request,  TCourseList tCourseList){
        Result result = new Result();
        //查询讲师开始的课程
        if (ToolUtil.isNotEmpty(tCourseList.getTeacherId())&&tCourseList.getTeacherId().equals("teacher")){
            tCourseList.setTeacherId(getCurrentUserId());
        }
        PageInfo pageInfo=iTCourseService.list(tCourseList);
        result.setBizData(pageInfo);
        return result;
    }

    /**
     * 获取实体信息
     * @param request
     * @param tCourseEdit
     * @return
     */
    @RequestMapping(value = "/getInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getInfo(HttpServletRequest request,  TCourseEdit tCourseEdit){
        Result result = new Result();
        if(ToolUtil.isEmpty(tCourseEdit.getUid())){
            throw new InvalidParamException(MessageHelper.ID_ISNULL);  //抛出异常
        }
        TCourseEdit tCourse=iTCourseService.getInfo(tCourseEdit.getUid(),tCourseEdit.getType());
        result.setBizData(tCourse);
        return result;
    }


    /**
     * 保存实体信息
     * @param request
     * @param tCourseEdit
     * @return
     */
    @RequestMapping(value = "/saveOrEdit.do", method = RequestMethod.POST)
    @ResponseBody
    public Result saveOrEdit(HttpServletRequest request,TCourseEdit tCourseEdit){
        Result result = new Result();
        if(tCourseEdit==null){
            throw new InvalidParamException(MessageHelper.MODEL_ISNULL);  //抛出异常
        }
        int code=iTCourseService.saveOrEdit(tCourseEdit);
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
        int code=iTCourseService.delete(ids);
        result.setBizData(code);
        return result;
    }
}
