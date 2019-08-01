package com.lsfly.controller.system;


import com.lsfly.bas.model.system.GenTable;
import com.lsfly.bas.model.system.GenTableExample;
import com.lsfly.bas.model.system.GenTableTree;
import com.lsfly.bas.model.system.ext.GenTableColumnEdit;
import com.lsfly.bas.service.system.IGenTableColumnService;
import com.lsfly.bas.service.system.IGenTableService;
import com.lsfly.bas.service.system.IGenTableTreeService;
import com.lsfly.commons.GenViewAndControl;
import com.lsfly.commons.GeneratorConfig;
import com.lsfly.exception.InvalidParamException;
import com.lsfly.sys.Result;
import com.lsfly.util.MessageHelper;
import com.lsfly.util.ReadProperties;
import com.lsfly.util.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author huoquan
 * @date 2018/8/25.
 */
@Controller
@RequestMapping("genTable")
public class GenTableController extends BaseController {

    @Autowired
    private IGenTableService iGenTableService;
    @Autowired
    private IGenTableColumnService iGenTableColumnService;
    @Autowired
    private IGenTableTreeService iGenTableTreeService;

    /**
     * 查询所有列表
     * @return
     */
    @RequestMapping(value = "/genTableList.do", method = RequestMethod.POST)
    @ResponseBody
    public Result genTableList(String tableName){
        Result result = new Result();
        GenTableExample example = new GenTableExample();
        GenTableExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(tableName);
        List<GenTable> list=iGenTableService.selectByExample(example);
        if(list!=null&&list.size()>0){
            result.setBizData(list.get(0));
        }else{
            result.setBizData("");
        }
        return result;
    };

    /**
     * 查询所有列表
     * @return
     */
    @RequestMapping(value = "/findTableList.do", method = RequestMethod.POST)
    @ResponseBody
    public Result findTableList(){
        Result result = new Result();
        result.setBizData(iGenTableService.findTableList(ReadProperties.getValue("jdbc.type")));
        return result;
    };

    /**
     * 获取数据表字段
     * @return
     */
    @RequestMapping(value = "/findTableColumnList.do", method = RequestMethod.POST)
    @ResponseBody
    public Result findTableColumnList(String tableName){
        Result result = new Result();
        List<String> baseColumList=new ArrayList<String>();
        if(ToolUtil.isNotEmpty(GenViewAndControl.field_status)){
            baseColumList.add(GenViewAndControl.field_status);
        }
        if(ToolUtil.isNotEmpty(GenViewAndControl.field_version)){
            baseColumList.add(GenViewAndControl.field_version);
        }
        if(ToolUtil.isNotEmpty(GenViewAndControl.field_createBy)){
            baseColumList.add(GenViewAndControl.field_createBy);
        }
        if(ToolUtil.isNotEmpty(GenViewAndControl.field_createTime)){
            baseColumList.add(GenViewAndControl.field_createTime);
        }
        if(ToolUtil.isNotEmpty(GenViewAndControl.field_updateBy)){
            baseColumList.add(GenViewAndControl.field_updateBy);
        }
        if(ToolUtil.isNotEmpty(GenViewAndControl.field_updateTime)){
            baseColumList.add(GenViewAndControl.field_updateTime);
        }
        result.setBizData(iGenTableService.findTableColumnList(ReadProperties.getValue("jdbc.type"),tableName,baseColumList));
        return result;
    };

    /**
     * 获取多个表的字段
     * @return
     */
    @RequestMapping(value = "/findTableColumnLists.do", method = RequestMethod.POST)
    @ResponseBody
    public Result findTableColumnLists(@RequestParam("tables[]") String[] tables){
        Result result = new Result();
        if(tables!=null&&tables.length==0){
            throw new InvalidParamException("table不能为空");  //抛出异常
        }
        result.setBizData(iGenTableService.findTableColumnLists(ReadProperties.getValue("jdbc.type"), Arrays.asList(tables)));
        return result;
    };

    /**
     * 保存实体信息
     * @param request
     * @param genTableColumnEdit
     * @return
     */
    @RequestMapping(value = "/saveOrEdit.do", method = RequestMethod.POST)
    @ResponseBody
    public Result saveOrEdit(HttpServletRequest request,@RequestBody GenTableColumnEdit genTableColumnEdit){
        Result result = new Result();
        if(genTableColumnEdit==null){
            throw new InvalidParamException(MessageHelper.MODEL_ISNULL);  //抛出异常
        }
        int code=iGenTableColumnService.saveOrEdit(genTableColumnEdit);
        result.setBizData(code);
        return result;
    }

    /**
     * 保存实体信息
     * @param request
     * @param genTableColumnEdit
     * @return
     */
    @RequestMapping(value = "/autoCreate.do", method = RequestMethod.POST)
    @ResponseBody
    public Result autoCreate(HttpServletRequest request,@RequestBody GenTableColumnEdit genTableColumnEdit){
        Result result = new Result();
        if(genTableColumnEdit==null){
            throw new InvalidParamException(MessageHelper.MODEL_ISNULL);  //抛出异常
        }
        int code=iGenTableColumnService.saveOrEdit(genTableColumnEdit);
        if(code==1){
            genTableColumnEdit.setPackage(true);
            //自动一键生成代码
            GenTable genTable=genTableColumnEdit.getGenTable();
            if(genTable!=null&&ToolUtil.isNotEmpty(genTable.getIsLeftZtree())
                    &&genTable.getIsLeftZtree().equals("1")&&ToolUtil.isNotEmpty(genTable.getLeftZtreeId())){
                //生成左侧树
                GenTableTree genTableTree=iGenTableTreeService.getByPrimaryKey(genTable.getLeftZtreeId());
                genTableColumnEdit.setGenTableTree(genTableTree);
            }
            GeneratorConfig.createGen(genTableColumnEdit);
        }
        result.setBizData(code);
        return result;
    }
}
