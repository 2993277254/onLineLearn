package com.lsfly.controller.courseUser;

import com.github.pagehelper.PageInfo;
import com.lsfly.bas.model.course.TCourse;
import com.lsfly.bas.model.courseUser.TCourseUser;
import com.lsfly.bas.model.courseUser.TCourseUserExample;
import com.lsfly.bas.model.courseUser.ext.TCourseUserEdit;
import com.lsfly.bas.model.courseUser.ext.TCourseUserList;
import com.lsfly.bas.service.course.ITCourseService;
import com.lsfly.bas.service.courseUser.ITCourseUserService;
import com.lsfly.controller.system.BaseController;
import com.lsfly.exception.InvalidParamException;
import com.lsfly.sys.Result;

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
 * TCourseUser的控制器业务处理页面
 */

@Controller
@RequestMapping(value = "tCourseUser")
public class TCourseUserController extends BaseController{
    public static final Logger logger = LoggerFactory.getLogger(TCourseUserController.class);

    @Autowired
    private ITCourseUserService iTCourseUserService;

    @Autowired
    private ITCourseService iTCourseService;
    /**
     * 获取全部列表，通常用于下拉框
     * @param request
     * @param tCourseUser
     * @return
     */
    @RequestMapping(value = "/getLists.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getLists(HttpServletRequest request, TCourseUser tCourseUser){
        Result result = new Result();
        TCourseUserExample example = new TCourseUserExample();
        TCourseUserExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteNotEqualTo("1");
        List<TCourseUser> list=iTCourseUserService.selectByExample(example);
        result.setBizData(list);
        return result;
    }

    /**
     * 判断是否参加课程
     * @param request
     * @param tCourseUserEdit
     * @return
     */
    @RequestMapping(value = "/addOrStudyCourse.do", method = RequestMethod.POST)
    @ResponseBody
    public Result addOrStudyCourse(HttpServletRequest request, TCourseUserEdit tCourseUserEdit){
        Result result = new Result();
        String type=tCourseUserEdit.getType();
//        String teacherId=tCourseUserEdit.getTeacherId();
//        if (ToolUtil.isNotEmpty(teacherId)){
//            //判断用户
//        } else
            if (ToolUtil.isNotEmpty(type)&&type.equals("0")){
            tCourseUserEdit.setUserId(getCurrentUserId());//存入用户id
            //表示要参加课程，插入
            result=saveOrEdit(request,tCourseUserEdit);
            if (result.getRtnCode().equals("0")){//加入成功
                TCourse tCourse=iTCourseService.getByPrimaryKey(tCourseUserEdit.getCourseId());
                tCourse.setPersonNum(tCourse.getPersonNum()+1);//更新参加人数
                iTCourseService.updateByPrimaryKey(tCourse);

            }
            return result;
        }
        //result.setBizData();
        return result;
    }

    @RequestMapping(value = "/getList2.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getList2(HttpServletRequest request,TCourseUser tCourseUser){
        Result result = new Result();
        TCourseUserExample example = new TCourseUserExample();
        TCourseUserExample.Criteria criteria = example.createCriteria();

        criteria.andIsDeleteNotEqualTo("1");

        List<TCourseUser> list=iTCourseUserService.selectByExample(example);
        result.setBizData(list);
        return result;
    }

    /**
     * 获取分页列表，用于页面的列表查询
     * @param request
     * @param tCourseUserList
     * @return
     */
    @RequestMapping(value = "/list.do", method = RequestMethod.POST)
    @ResponseBody
    public PageInfo list(HttpServletRequest request,  TCourseUserList tCourseUserList){
        Result result = new Result();
        PageInfo pageInfo=iTCourseUserService.list(tCourseUserList);
        return pageInfo;
    }


    @RequestMapping(value = "/list2.do", method = RequestMethod.POST)
    @ResponseBody
    public Result list2(HttpServletRequest request,  TCourseUserList tCourseUserList){
        Result result = new Result();
        tCourseUserList.setUserId(getCurrentUserId());
        PageInfo pageInfo=iTCourseUserService.list2(tCourseUserList);
        result.setBizData(pageInfo);
        return result;
    }


    /**
     * 获取实体信息
     * @param request
     * @param tCourseUserEdit
     * @return
     */
    @RequestMapping(value = "/getInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getInfo(HttpServletRequest request,  TCourseUserEdit tCourseUserEdit){
        Result result = new Result();
        if(ToolUtil.isEmpty(tCourseUserEdit.getUid())){
            throw new InvalidParamException(MessageHelper.ID_ISNULL);  //抛出异常
        }
        TCourseUserEdit tCourseUser=iTCourseUserService.getInfo(tCourseUserEdit.getUid());
        result.setBizData(tCourseUser);
        return result;
    }

    /**
     * 保存实体信息
     * @param request
     * @param tCourseUserEdit
     * @return
     */
    @RequestMapping(value = "/saveOrEdit.do", method = RequestMethod.POST)
    @ResponseBody
    public Result saveOrEdit(HttpServletRequest request,  TCourseUserEdit tCourseUserEdit){
        Result result = new Result();
        if(tCourseUserEdit==null){
            throw new InvalidParamException(MessageHelper.MODEL_ISNULL);  //抛出异常
        }
        int code=iTCourseUserService.saveOrEdit(tCourseUserEdit);
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
        int code=iTCourseUserService.delete(ids);
        result.setBizData(code);
        return result;
    }
}
