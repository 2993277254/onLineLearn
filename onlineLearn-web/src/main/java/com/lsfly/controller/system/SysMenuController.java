package com.lsfly.controller.system;

import com.lsfly.bas.model.system.SysKey;
import com.lsfly.bas.model.system.ext.SysMenuModify;
import com.lsfly.bas.service.system.ISysKeyService;
import com.lsfly.commons.SystemLog;
import com.lsfly.exception.InvalidParamException;
import com.lsfly.sys.Result;
import com.lsfly.bas.model.system.SysMenu;

import com.lsfly.bas.service.system.ISysMenuService;
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
import java.util.Arrays;
import java.util.List;

/**
 * Created by on 18-8-24 下午6:13
 * SysMenu的控制器业务处理页面
 */

@Controller
@RequestMapping(value = "sysMenu")
public class SysMenuController extends BaseController{
    public static final Logger logger = LoggerFactory.getLogger(SysMenuController.class);

    @Autowired
    private ISysMenuService iSysMenuService;
    @Autowired
    private ISysKeyService iSysKeyService;

    /**
     * 查询菜单
     * @param request
     * @param sysmenu
     * @return
     */
    @RequestMapping(value = "/list.do", method = RequestMethod.POST)
    @ResponseBody
    public Result list(HttpServletRequest request,  SysMenu sysmenu){
        Result result = new Result();  //返回实体
        List<SysMenu> sysmenuList=iSysMenuService.list(sysmenu);
        result.setBizData(sysmenuList);
        return result;
    }

    /**
     * 新增/修改菜单
     * @param request
     * @param sysMenu
     * @return
     */
    @RequestMapping(value = "/saveOrEdit.do", method = RequestMethod.POST)
    @ResponseBody
    @SystemLog(moduleName="菜单管理",description="新增/修改操作")
    public Result saveOrEdit(HttpServletRequest request,  SysMenu sysMenu){
        Result result = new Result();  //返回实体
        SysMenu entity=iSysMenuService.add(sysMenu);
        result.setBizData(entity);
        return result;
    }

    /**
     * 根据id获取菜单实体
     * @param request
     * @param sysMenu
     * @return
     */
    @RequestMapping(value = "/getInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getInfo(HttpServletRequest request, SysMenu sysMenu){
        if(sysMenu==null|| ToolUtil.isEmpty(sysMenu.getId())){
            throw new InvalidParamException(MessageHelper.ID_ISNULL);  //抛出异常,id不能为空
        }
        Result result = new Result();  //返回实体
        SysMenuModify model=iSysMenuService.info(sysMenu);
        result.setBizData(model);
        return result;
    }

    /**
     * 删除菜单
     * @param request
     * @param sysMenu
     * @return
     */
    @RequestMapping(value = "/delete.do", method = RequestMethod.POST)
    @ResponseBody
    @SystemLog(moduleName="菜单管理",description="删除操作")
    public Result delete(HttpServletRequest request,  SysMenu sysMenu){
        if(sysMenu==null|| ToolUtil.isEmpty(sysMenu.getId())){
            throw new InvalidParamException(MessageHelper.ID_ISNULL);  //抛出异常,id不能为空
        }
        Result result = new Result();  //返回实体
        int code=iSysMenuService.delete(sysMenu);
        if(code==0){
            throw new InvalidParamException(MessageHelper.DELFAIL);  //抛出异常，删除失败
        }
        return result;
    }

    /**
     * 菜单排序
     * @param request
     * @param ids
     * @return
     */
    @RequestMapping(value = "/sort.do", method = RequestMethod.POST)
    @ResponseBody
    @SystemLog(moduleName="菜单管理",description="排序操作")
    public Result sort(HttpServletRequest request, @RequestParam("ids[]") String[] ids,String parentId){
        if(ids==null|| ids.length==0){
            throw new InvalidParamException(MessageHelper.ID_ISNULL);  //抛出异常
        }
        Result result = new Result();  //返回实体
        int code=iSysMenuService.sort(Arrays.asList(ids),parentId);
        result.setBizData(code);
        return result;
    }

    /**
     *根据菜单id获取按钮权限列表
     * @param request
     * @param syskey
     * @return
     */
    @RequestMapping(value = "/searchBtnList.do", method = RequestMethod.POST)
    @ResponseBody
    public Result searchBtnList(HttpServletRequest request,  SysKey syskey){
        if(syskey==null|| ToolUtil.isEmpty(syskey.getMenuId())){
            throw new InvalidParamException("菜单实体id不能为空");  //抛出异常
        }
        Result result = new Result();  //返回实体
        List<SysKey> model=iSysKeyService.searchBtnList(syskey.getMenuId());
        result.setBizData(model);
        return result;
    }

    /**
     * 根据id获取按钮实体
     * @param request
     * @param syskey
     * @return
     */
    @RequestMapping(value = "/getBthInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getBthInfo(HttpServletRequest request,  SysKey syskey){
        if(syskey==null|| ToolUtil.isEmpty(syskey.getId())){
            throw new InvalidParamException("按钮实体id不能为空");  //抛出异常
        }
        Result result = new Result();  //返回实体
        SysKey model=iSysKeyService.getByPrimaryKey(syskey.getId());
        result.setBizData(model);
        return result;
    }

    /**
     * 保存按钮实体
     * @param request
     * @param syskey
     * @return
     */
    @RequestMapping(value = "/saveBthInfo.do", method = RequestMethod.POST)
    @ResponseBody
    @SystemLog(moduleName="菜单管理",description="按钮保存操作")
    public Result saveBthInfo(HttpServletRequest request,  SysKey syskey){
        if(syskey==null){
            throw new InvalidParamException("按钮实体不能为空");  //抛出异常
        }
        Result result = new Result();  //返回实体
        int model=iSysKeyService.saveBthInfo(syskey);
        result.setBizData(model);
        return result;
    }

    /**
     * 删除按钮权限
     * @param request
     * @param ids
     * @return
     */
    @RequestMapping(value = "/menuBthDel.do", method = RequestMethod.POST)
    @ResponseBody
    @SystemLog(moduleName="菜单管理",description="按钮删除操作")
    public Result menuBthDel(HttpServletRequest request,@RequestParam("ids[]") String[] ids){
        if(ids!=null&&ids.length==0){
            throw new InvalidParamException(MessageHelper.ID_ISNULL);  //抛出异常
        }
        Result result = new Result();  //返回实体
        int code=iSysKeyService.menuBthDel(ids);
        result.setBizData(code);
        return result;
    }
}
