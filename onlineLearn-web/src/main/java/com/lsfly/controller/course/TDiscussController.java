package com.lsfly.controller.course;

import com.github.pagehelper.PageInfo;
import com.lsfly.controller.system.BaseController;
import com.lsfly.exception.InvalidParamException;
import com.lsfly.sys.Result;
import com.lsfly.bas.model.course.TDiscuss;
import com.lsfly.bas.model.course.TDiscussExample;
import com.lsfly.bas.model.course.ext.TDiscussEdit;
import com.lsfly.bas.model.course.ext.TDiscussList;
import com.lsfly.bas.service.course.ITDiscussService;
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
 * TDiscuss的控制器业务处理页面
 */

@Controller
@RequestMapping(value = "tDiscuss")
public class TDiscussController extends BaseController{
    public static final Logger logger = LoggerFactory.getLogger(TDiscussController.class);

    @Autowired
    private ITDiscussService iTDiscussService;
    /**
     * 获取全部列表，通常用于下拉框
     * @param request
     * @param tDiscuss
     * @return
     */
    @RequestMapping(value = "/getLists.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getLists(HttpServletRequest request,TDiscuss tDiscuss){
        Result result = new Result();
        TDiscussExample example = new TDiscussExample();
        TDiscussExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteNotEqualTo("1");
        List<TDiscuss> list=iTDiscussService.selectByExample(example);
        result.setBizData(list);
        return result;
    }

    /**
     * 获取分页列表，用于页面的列表查询
     * @param request
     * @param tDiscussList
     * @return
     */
    @RequestMapping(value = "/list.do", method = RequestMethod.POST)
    @ResponseBody
    public Result list(HttpServletRequest request,  TDiscussList tDiscussList){
        Result result = new Result();
        PageInfo pageInfo=iTDiscussService.list(tDiscussList);
        result.setBizData(pageInfo);
        return result;
    }
    @RequestMapping(value = "/list2.do", method = RequestMethod.POST)
    @ResponseBody
    public Result list2(HttpServletRequest request,  TDiscussList tDiscussList){
        Result result = new Result();
        PageInfo pageInfo=iTDiscussService.list(tDiscussList);
        result.setBizData(pageInfo);
        return result;
    }

    /**
     * 获取实体信息
     * @param request
     * @param tDiscussEdit
     * @return
     */
    @RequestMapping(value = "/getInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getInfo(HttpServletRequest request,  TDiscussEdit tDiscussEdit){
        Result result = new Result();
        if(ToolUtil.isEmpty(tDiscussEdit.getUid())){
            throw new InvalidParamException(MessageHelper.ID_ISNULL);  //抛出异常
        }
        TDiscussEdit tDiscuss=iTDiscussService.getInfo(tDiscussEdit.getUid());
        result.setBizData(tDiscuss);
        return result;
    }

    /**
     * 保存实体信息
     * @param request
     * @param tDiscussEdit
     * @return
     */
    @RequestMapping(value = "/saveOrEdit.do", method = RequestMethod.POST)
    @ResponseBody
    public Result saveOrEdit(HttpServletRequest request,  TDiscussEdit tDiscussEdit){
        Result result = new Result();
        if (ToolUtil.isNotEmpty(tDiscussEdit.getUserId())&&tDiscussEdit.getUserId().equals("user")){
            tDiscussEdit.setUserId(getCurrentUserId());
        }
        if(tDiscussEdit==null){
            throw new InvalidParamException(MessageHelper.MODEL_ISNULL);  //抛出异常
        }
        int code=iTDiscussService.saveOrEdit(tDiscussEdit);
        result.setBizData(code);
        return result;
    }

    @RequestMapping(value = "/saveOrEdit2.do", method = RequestMethod.POST)
    @ResponseBody
    public Result saveOrEdit2(HttpServletRequest request,  TDiscussEdit tDiscussEdit){
        Result result = new Result();
        if (ToolUtil.isNotEmpty(tDiscussEdit.getUserId())&&tDiscussEdit.getUserId().equals("user")){
            tDiscussEdit.setUserId(getCurrentUserId());
        }
        if(tDiscussEdit==null){
            throw new InvalidParamException(MessageHelper.MODEL_ISNULL);  //抛出异常
        }
        TDiscussEdit code=iTDiscussService.saveOrEdit2(tDiscussEdit);
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
        int code=iTDiscussService.delete(ids);
        result.setBizData(code);
        return result;
    }
}
