package com.lsfly.controller.system;

import com.github.pagehelper.PageInfo;

import com.lsfly.bas.model.system.GenTableTree;
import com.lsfly.bas.model.system.GenTableTreeExample;
import com.lsfly.bas.model.system.ext.GenTableTreeEdit;
import com.lsfly.bas.model.system.ext.GenTableTreeList;
import com.lsfly.bas.service.system.IGenTableTreeService;
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
 * GenTableTree的控制器业务处理页面
 */

@Controller
@RequestMapping(value = "genTableTree")
public class GenTableTreeController extends BaseController {
    public static final Logger logger = LoggerFactory.getLogger(GenTableTreeController.class);

    @Autowired
    private IGenTableTreeService iGenTableTreeService;
    /**
     * 获取全部列表，通常用于下拉框
     * @param request
     * @param genTableTree
     * @return
     */
    @RequestMapping(value = "/getLists.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getLists(HttpServletRequest request, GenTableTree genTableTree){
        Result result = new Result();
        GenTableTreeExample example = new GenTableTreeExample();
        GenTableTreeExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteNotEqualTo("1");
        List<GenTableTree> list=iGenTableTreeService.selectByExample(example);
        result.setBizData(list);
        return result;
    }

    /**
     * 获取分页列表，用于页面的列表查询
     * @param request
     * @param genTableTreeList
     * @return
     */
    @RequestMapping(value = "/list.do", method = RequestMethod.POST)
    @ResponseBody
    public PageInfo list(HttpServletRequest request,  GenTableTreeList genTableTreeList){
        Result result = new Result();
        PageInfo pageInfo=iGenTableTreeService.list(genTableTreeList);
        return pageInfo;
    }

    /**
     * 获取实体信息
     * @param request
     * @param genTableTreeEdit
     * @return
     */
    @RequestMapping(value = "/getInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getInfo(HttpServletRequest request,  GenTableTreeEdit genTableTreeEdit){
        Result result = new Result();
        if(ToolUtil.isEmpty(genTableTreeEdit.getId())){
            throw new InvalidParamException(MessageHelper.ID_ISNULL);  //抛出异常
        }
        GenTableTreeEdit genTableTree=iGenTableTreeService.getInfo(genTableTreeEdit.getId());
        result.setBizData(genTableTree);
        return result;
    }

    /**
     * 保存实体信息
     * @param request
     * @param genTableTreeEdit
     * @return
     */
    @RequestMapping(value = "/saveOrEdit.do", method = RequestMethod.POST)
    @ResponseBody
    public Result saveOrEdit(HttpServletRequest request,  GenTableTreeEdit genTableTreeEdit){
        Result result = new Result();
        if(genTableTreeEdit==null){
            throw new InvalidParamException(MessageHelper.MODEL_ISNULL);  //抛出异常
        }
        int code=iGenTableTreeService.saveOrEdit(genTableTreeEdit);
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
        int code=iGenTableTreeService.delete(ids);
        result.setBizData(code);
        return result;
    }
}
