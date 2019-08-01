package com.lsfly.controller.system;

import com.github.pagehelper.PageInfo;
import com.lsfly.bas.model.system.*;
import com.lsfly.bas.service.system.ISysRoleKeyService;
import com.lsfly.exception.InvalidParamException;
import com.lsfly.sys.Result;
import com.lsfly.bas.model.system.ext.SysRoleEdit;
import com.lsfly.bas.model.system.ext.SysRoleList;
import com.lsfly.bas.service.system.ISysRoleService;
import com.lsfly.util.MessageHelper;
import com.lsfly.util.ToolUtil;
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
 * SysRole的控制器业务处理页面
 */

@Controller
@RequestMapping(value = "sysRole")
public class SysRoleController extends BaseController{
    public static final Logger logger = LoggerFactory.getLogger(SysRoleController.class);

    @Autowired
    private ISysRoleService iSysRoleService;

    @Autowired
    private ISysRoleKeyService iSysRoleKeyService;

    /**
     * 获取全部列表，通常用于下拉框
     * @param request
     * @param sysRole
     * @return
     */
    @RequestMapping(value = "/getLists.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getLists(HttpServletRequest request,SysRole sysRole){
        Result result = new Result();
        SysRoleExample example = new SysRoleExample();
        SysRoleExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteNotEqualTo("1");
        List<SysRole> list=iSysRoleService.selectByExample(example);
        result.setBizData(list);
        return result;
    }

    /**
     * 获取分页列表，用于页面的列表查询
     * @param request
     * @param sysRoleList
     * @return
     */
    @RequestMapping(value = "/list.do", method = RequestMethod.POST)
    @ResponseBody
    public PageInfo list(HttpServletRequest request,  SysRoleList sysRoleList){
        Result result = new Result();
        PageInfo pageInfo=iSysRoleService.list(sysRoleList);
        return pageInfo;
    }

    /**
     * 获取实体信息
     * @param request
     * @param sysRoleEdit
     * @return
     */
    @RequestMapping(value = "/getInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getInfo(HttpServletRequest request,  SysRoleEdit sysRoleEdit){
        Result result = new Result();
        if(ToolUtil.isEmpty(sysRoleEdit.getId())){
            throw new InvalidParamException(MessageHelper.ID_ISNULL);  //抛出异常
        }
        SysRoleEdit sysRole=iSysRoleService.getInfo(sysRoleEdit.getId());
        result.setBizData(sysRole);
        return result;
    }

    /**
     * 获取角色权限
     * @param id
     * @return
     */
    @RequestMapping(value = "/getKeyId.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getKeyId(String id){
        Result result = new Result();
        SysRoleKeyExample example = new SysRoleKeyExample();
        SysRoleKeyExample.Criteria criteria = example.createCriteria();
        criteria.andRoleIdEqualTo(id);
        List<SysRoleKey> list =iSysRoleKeyService.selectByExample(example);
        result.setBizData(list);
        return result;
    }
    /**
     * 保存实体信息
     * @param request
     * @param sysRoleEdit
     * @return
     */
    @RequestMapping(value = "/saveOrEdit.do", method = RequestMethod.POST)
    @ResponseBody
    public Result saveOrEdit(HttpServletRequest request,@RequestBody SysRoleEdit sysRoleEdit){
        Result result = new Result();
        if(sysRoleEdit==null){
            throw new InvalidParamException(MessageHelper.MODEL_ISNULL);  //抛出异常
        }
        int code=iSysRoleService.saveOrEdit(sysRoleEdit);
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
        int code=iSysRoleService.delete(ids);
        result.setBizData(code);
        return result;
    }


    /**
     * 查询菜单
     * @param request
     * @param sysmenu
     * @return
     */
    @RequestMapping(value = "/selectMenu.do",method = RequestMethod.POST)
    @ResponseBody
    public Result selectMenu(HttpServletRequest request,  SysMenu sysmenu){
        Result result = new Result();  //返回实体
        List<Map<String, Object>> data=iSysRoleService.selectMenu(request,sysmenu);
        result.setBizData(data);
        return result;
    }
}
