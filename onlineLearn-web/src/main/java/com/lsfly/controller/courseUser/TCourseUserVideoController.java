package com.lsfly.controller.courseUser;

import com.github.pagehelper.PageInfo;
import com.lsfly.controller.system.BaseController;
import com.lsfly.exception.InvalidParamException;
import com.lsfly.sys.Result;
import com.lsfly.bas.model.courseUser.TCourseUserVideo;
import com.lsfly.bas.model.courseUser.TCourseUserVideoExample;
import com.lsfly.bas.model.courseUser.ext.TCourseUserVideoEdit;
import com.lsfly.bas.model.courseUser.ext.TCourseUserVideoList;
import com.lsfly.bas.service.courseUser.ITCourseUserVideoService;
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
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by on 18-8-24 下午6:13
 * TCourseUserVideo的控制器业务处理页面
 */

@Controller
@RequestMapping(value = "tCourseUserVideo")
public class TCourseUserVideoController extends BaseController{
    public static final Logger logger = LoggerFactory.getLogger(TCourseUserVideoController.class);

    @Autowired
    private ITCourseUserVideoService iTCourseUserVideoService;
    /**
     * 获取全部列表，通常用于下拉框
     * @param request
     * @param tCourseUserVideo
     * @return
     */
    @RequestMapping(value = "/getLists.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getLists(HttpServletRequest request,TCourseUserVideo tCourseUserVideo){
        Result result = new Result();
        TCourseUserVideoExample example = new TCourseUserVideoExample();
        TCourseUserVideoExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteNotEqualTo("1");
        List<TCourseUserVideo> list=iTCourseUserVideoService.selectByExample(example);
        result.setBizData(list);
        return result;
    }
    @RequestMapping(value = "/getList2.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getList2(HttpServletRequest request,TCourseUserVideo tCourseUserVideo){
        Result result = new Result();
        TCourseUserVideoExample example = new TCourseUserVideoExample();
        TCourseUserVideoExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteNotEqualTo("1");
        criteria.andCourseIdEqualTo(tCourseUserVideo.getCourseId());
        if (ToolUtil.isNotEmpty(tCourseUserVideo.getUserId())&&tCourseUserVideo.getUserId().equals("user")){
            criteria.andUserIdEqualTo(getCurrentUserId());
        }
        List<TCourseUserVideo> list=iTCourseUserVideoService.selectByExample(example);
        result.setBizData(list);
        return result;
    }
    /**
     * 获取分页列表，用于页面的列表查询
     * @param request
     * @param tCourseUserVideoList
     * @return
     */
    @RequestMapping(value = "/list.do", method = RequestMethod.POST)
    @ResponseBody
    public PageInfo list(HttpServletRequest request,  TCourseUserVideoList tCourseUserVideoList){
        Result result = new Result();
        PageInfo pageInfo=iTCourseUserVideoService.list(tCourseUserVideoList);
        return pageInfo;
    }

    /**
     * 获取实体信息
     * @param request
     * @param tCourseUserVideoEdit
     * @return
     */
    @RequestMapping(value = "/getInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getInfo(HttpServletRequest request,  TCourseUserVideoEdit tCourseUserVideoEdit){
        Result result = new Result();
        if(ToolUtil.isEmpty(tCourseUserVideoEdit.getUid())){
            throw new InvalidParamException(MessageHelper.ID_ISNULL);  //抛出异常
        }
        TCourseUserVideoEdit tCourseUserVideo=iTCourseUserVideoService.getInfo(tCourseUserVideoEdit.getUid());
        result.setBizData(tCourseUserVideo);
        return result;
    }

    /**
     * 保存实体信息
     * @param request
     * @param tCourseUserVideoEdit
     * @return
     */
    @RequestMapping(value = "/saveOrEdit.do", method = RequestMethod.POST)
    @ResponseBody
    public Result saveOrEdit(HttpServletRequest request,  TCourseUserVideoEdit tCourseUserVideoEdit){
        Result result = new Result();
        if(tCourseUserVideoEdit==null){
            throw new InvalidParamException(MessageHelper.MODEL_ISNULL);  //抛出异常
        }
        System.out.println("观看时间小于数据库记录时间，无需更新");
        int code=iTCourseUserVideoService.saveOrEdit(tCourseUserVideoEdit);
        result.setBizData(code);
        return result;
    }
    @RequestMapping(value = "/saveOrEdit2.do", method = RequestMethod.POST)
    @ResponseBody
    public Result saveOrEdit2(HttpServletRequest request,  TCourseUserVideoEdit tCourseUserVideoEdit){
        Result result = new Result();
        if(tCourseUserVideoEdit==null){
            throw new InvalidParamException(MessageHelper.MODEL_ISNULL);  //抛出异常
        }
        if(ToolUtil.isNotEmpty(tCourseUserVideoEdit.getUserId())&&tCourseUserVideoEdit.getUserId().equals("user")){
            tCourseUserVideoEdit.setUserId(getCurrentUserId());

        }
        //如果观看时间小于数据库记录时间，那就不需要更新进度
        if (ToolUtil.isNotEmpty(tCourseUserVideoEdit.getUid())){//保存数据
            if (tCourseUserVideoEdit.getIsEnd().equals("0")) {
                TCourseUserVideo tCourseUserVideo = iTCourseUserVideoService.getByPrimaryKey(tCourseUserVideoEdit.getUid());
                BigDecimal hascurrTime = new BigDecimal(tCourseUserVideo.getCurrentTime());
                BigDecimal currTime = new BigDecimal(tCourseUserVideoEdit.getCurrentTime());
                int r = currTime.compareTo(hascurrTime);
                if (r > 0) {
                    System.out.println("观看时间大于数据库记录时间，需更新");
                    int code=iTCourseUserVideoService.saveOrEdit(tCourseUserVideoEdit);
                    result.setBizData(code);
                } else {
                    System.out.println("观看时间小于数据库记录时间，无需更新");
                    result.setBizData("noUpdate");
                }
            }else if (tCourseUserVideoEdit.getIsEnd().equals("1")){
                System.out.println("观看完成");
                int code=iTCourseUserVideoService.saveOrEdit(tCourseUserVideoEdit);
                result.setBizData(code);
            }
        }else {//表示插入数据
            TCourseUserVideoEdit code = iTCourseUserVideoService.saveOrEdit2(tCourseUserVideoEdit);
            result.setBizData(code.getUid());
        }
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
        int code=iTCourseUserVideoService.delete(ids);
        result.setBizData(code);
        return result;
    }
}
