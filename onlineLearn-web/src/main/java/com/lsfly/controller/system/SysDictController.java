package com.lsfly.controller.system;

import com.github.pagehelper.PageInfo;

import com.google.gson.Gson;
import com.lsfly.bas.model.system.SysDict;
import com.lsfly.bas.model.system.SysDictExample;
import com.lsfly.bas.model.system.ext.SysDictDataList;
import com.lsfly.bas.model.system.ext.SysDictEdit;
import com.lsfly.bas.model.system.ext.SysDictList;
import com.lsfly.bas.service.system.ISysDictDataService;
import com.lsfly.bas.service.system.ISysDictService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 自动生成 on 18-8-24 下午6:13
 * SysDict的控制器业务处理页面
 */

@Controller
@RequestMapping(value = "sysDict")
public class SysDictController extends BaseController {
    public static final Logger logger = LoggerFactory.getLogger(SysDictController.class);

    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    private ISysDictDataService iSysDictDataService;
    /**
     * 获取全部列表，通常用于下拉框
     * @param request
     * @param sysDict
     * @return
     */
    @RequestMapping(value = "/getLists.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getLists(HttpServletRequest request, SysDict sysDict){
        Result result = new Result();
        SysDictExample example = new SysDictExample();
        SysDictExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteNotEqualTo("1");
        example.setOrderByClause("dict_type asc");
        List<SysDict> list=iSysDictService.selectByExample(example);
        result.setBizData(list);
        return result;
    }


    //获取字典数据
    @RequestMapping(value = "/getGsonLists.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getGsonLists(SysDict sysDict){
        Result result = new Result();
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("sysDict",new Gson().toJson(iSysDictService.getLists()));  //获取数据字典
//        SysDictDataList sysDictDataList=new SysDictDataList();
//        sysDict.setDictType("course_type_");
//        sysDictDataList.setSysDict(sysDict);
//        map.put("courseType",new Gson().toJson(iSysDictDataService.list2(sysDictDataList)));
        result.setBizData(map);
        return result;
    }
    /**
     * 获取分页列表，用于页面的列表查询
     * @param request
     * @param sysDictList
     * @return
     */
    @RequestMapping(value = "/list.do", method = RequestMethod.POST)
    @ResponseBody
    public PageInfo list(HttpServletRequest request,  SysDictList sysDictList){
        Result result = new Result();
        PageInfo pageInfo=iSysDictService.list(sysDictList);
        return pageInfo;
    }

    /**
     * 获取实体信息
     * @param request
     * @param sysDictList
     * @return
     */
    @RequestMapping(value = "/getInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getInfo(HttpServletRequest request,  SysDictList sysDictList){
        Result result = new Result();
        if(ToolUtil.isEmpty(sysDictList.getId())){
            throw new InvalidParamException(MessageHelper.ID_ISNULL);  //抛出异常
        }
        SysDict sysDict=iSysDictService.getByPrimaryKey(sysDictList.getId());
        result.setBizData(sysDict);
        return result;
    }

    /**
     * 保存实体信息
     * @param request
     * @param sysDictEdit
     * @return
     */
    @RequestMapping(value = "/saveOrEdit.do", method = RequestMethod.POST)
    @ResponseBody
    public Result saveOrEdit(HttpServletRequest request,  SysDictEdit sysDictEdit){
        Result result = new Result();
        if(sysDictEdit==null){
            throw new InvalidParamException(MessageHelper.MODEL_ISNULL);  //抛出异常
        }
        int code=iSysDictService.saveOrEdit(sysDictEdit);
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
        int code=iSysDictService.delete(ids);
        result.setBizData(code);
        return result;
    }
}
