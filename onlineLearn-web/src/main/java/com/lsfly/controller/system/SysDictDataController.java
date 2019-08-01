package com.lsfly.controller.system;

import com.github.pagehelper.PageInfo;
import com.lsfly.exception.InvalidParamException;
import com.lsfly.sys.Result;
import com.lsfly.bas.model.system.SysDictData;
import com.lsfly.bas.model.system.SysDictDataExample;
import com.lsfly.bas.model.system.ext.SysDictDataEdit;
import com.lsfly.bas.model.system.ext.SysDictDataList;
import com.lsfly.bas.service.system.ISysDictDataService;
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
 * SysDictData的控制器业务处理页面
 */

@Controller
@RequestMapping(value = "sysDictData")
public class SysDictDataController extends BaseController{
    public static final Logger logger = LoggerFactory.getLogger(SysDictDataController.class);

    @Autowired
    private ISysDictDataService iSysDictDataService;
    /**
     * 获取全部列表，通常用于下拉框
     * @param request
     * @param sysDictData
     * @return
     */
    @RequestMapping(value = "/getLists.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getLists(HttpServletRequest request,SysDictData sysDictData){
        Result result = new Result();
        SysDictDataExample example = new SysDictDataExample();
        SysDictDataExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteNotEqualTo("1");

        List<SysDictData> list=iSysDictDataService.selectByExample(example);
        result.setBizData(list);
        return result;
    }

    /**
     * 获取分页列表，用于页面的列表查询
     * @param request
     * @param sysDictDataList
     * @return
     */
    @RequestMapping(value = "/list.do", method = RequestMethod.POST)
    @ResponseBody
    public PageInfo list(HttpServletRequest request,  SysDictDataList sysDictDataList){
        Result result = new Result();
        PageInfo pageInfo=iSysDictDataService.list(sysDictDataList);
        return pageInfo;
    }

    @RequestMapping(value = "/list2.do", method = RequestMethod.POST)
    @ResponseBody
    public Result list2(HttpServletRequest request,  SysDictDataList sysDictDataList){
        Result result = new Result();
        List<SysDictDataList> list=iSysDictDataService.list2(sysDictDataList);
        result.setBizData(list);
        return result;
    }
    /**
     * 获取实体信息
     * @param request
     * @param sysDictDataEdit
     * @return
     */
    @RequestMapping(value = "/getInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getInfo(HttpServletRequest request,  SysDictDataEdit sysDictDataEdit){
        Result result = new Result();
        if(ToolUtil.isEmpty(sysDictDataEdit.getId())){
            throw new InvalidParamException(MessageHelper.ID_ISNULL);  //抛出异常
        }
        SysDictDataEdit sysDictData=iSysDictDataService.getInfo(sysDictDataEdit.getId());
        result.setBizData(sysDictData);
        return result;
    }

    /**
     * 保存实体信息
     * @param request
     * @param sysDictDataEdit
     * @return
     */
    @RequestMapping(value = "/saveOrEdit.do", method = RequestMethod.POST)
    @ResponseBody
    public Result saveOrEdit(HttpServletRequest request,  SysDictDataEdit sysDictDataEdit){
        Result result = new Result();
        if(sysDictDataEdit==null){
            throw new InvalidParamException(MessageHelper.MODEL_ISNULL);  //抛出异常
        }
        int code=iSysDictDataService.saveOrEdit(sysDictDataEdit);
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
        int code=iSysDictDataService.delete(ids);
        result.setBizData(code);
        return result;
    }
}
