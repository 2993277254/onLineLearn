package com.lsfly.controller.system;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.lsfly.bas.model.system.*;
import com.lsfly.bas.model.system.ext.SysMenuModify;
import com.lsfly.bas.model.system.ext.SysUserEdit;
import com.lsfly.bas.model.system.ext.SysUserList;
import com.lsfly.bas.service.system.*;
import com.lsfly.commons.SystemLog;
import com.lsfly.shiro.CaptchaUsernamePasswordToken;
import com.lsfly.sys.Result;
import com.lsfly.exception.InvalidParamException;
import com.lsfly.util.*;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*  v1:初始建造，author:自动生成,date: 18-12-23 上午1:33
 */

@Controller
@RequestMapping(value = "sysUser")
public class SysUserController extends BaseController {
    public static final Logger logger = LoggerFactory.getLogger(SysUserController.class);

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private ISysMenuService iSysMenuService;

    @Autowired
    private ISysRoleKeyService iSysRoleKeyService;

    @Autowired
    private ISysDictService iSysDictService;


    @Autowired
    private ISysRoleService iSysRoleService;


    //跳转至后台
    @RequestMapping(value = "/selectAdminData.do", method = RequestMethod.POST)
    @ResponseBody
    public Result selectAdminData() {
        Result result = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        SysUser sysUser = CommUtil.getSysUserLoginInfo();
        //获取权限和菜单
        if (ToolUtil.isNotEmpty(sysUser.getUserName()) && sysUser.getUserName().equals(ReadProperties.getValue("superUserName"))) {
            //账号为超级管理员，拥有全部菜单
            SysMenuModify sysMenuModify=new SysMenuModify();
            List<Map<String, Object>> sysMenuList = iSysMenuService.selectMenuByRoId(sysMenuModify);
            map.put("menus", new Gson().toJson(sysMenuList));  //菜单
            //获取按钮权限
            //map.put("roles", "");  //菜单按钮权限
        } else {
            //获取菜单
            String type=getCurrentUser().getIdentity();
            SysRoleExample example = new SysRoleExample();
            SysRoleExample.Criteria criteria = example.createCriteria();
            criteria.andIdentityEqualTo(type);//通过角色的身份来查询角色权限表的数据
            criteria.andIsDeleteNotEqualTo("1");
            List<SysRole> list=iSysRoleService.selectByExample(example);
            String RoId="";
            if (list!=null&&list.size()>0){
                RoId=list.get(0).getId();
                SysMenuModify sysMenuModify=new SysMenuModify();
                sysMenuModify.setRoId(RoId);
                List<Map<String, Object>> sysMenuList = iSysMenuService.selectMenuByRoId(sysMenuModify);
                map.put("menus", new Gson().toJson(sysMenuList));  //菜单
            }else {//该角色无菜单
                map.put("menus", new Gson().toJson(new ArrayList<>()));
            }
            //map.put("roles", new Gson().toJson(new ArrayList<>()));  //菜单按钮权限
        }
        result.setBizData(map);
        return result;
    }

    /**
     * 登录页面生成验证码
     */
    @RequestMapping(value = "/getVerify.do")

    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
        response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        RandomValidateCode randomValidateCode = new RandomValidateCode();
        try {
            randomValidateCode.getRandcode(request, response);//输出验证码图片方法
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
//    @ResponseBody
//    public Result loginSys(HttpServletRequest request){
//        Result result = new Result();
//        String errorClassName = (String) request.getAttribute("oolLoginFail");
//        if (ToolUtil.isNotEmpty(errorClassName)) {
//            result.setRtnCode("1");
//            result.setMsg(errorClassName);
//            return result;
//        }
//
//        return result;
//    }

    @RequestMapping(value = "/clearAndLogin.do", method = RequestMethod.POST)
    @ResponseBody
    @SystemLog(moduleName = "访问系统", description = "登录成功")
    public Result login(HttpServletRequest request, HttpSession session, SysUser model, @Param("loginImgVerify") String loginImgVerify, @Param("type") String type) {
        Result result = new Result();
        //type为空是有验证码登录，不为空表示注册后跳转登录
        if (ToolUtil.isEmpty(type)) {
            //从session中获取随机数,RANDOMVALIDATECODEKEY对应验证码产生的随机数
            String random = (String) session.getAttribute("RANDOMVALIDATECODEKEY");
            if (random.equals(loginImgVerify)) {
                //return login(model,false );
                JSONObject jsonObject = JSONObject.fromObject(login(request,model, false));

                result.setMsg(jsonObject.get("msg").toString());
                result.setRtnCode(jsonObject.get("rtnCode").toString());
                //result.setBizData(jsonObject.get("bizData"));
                return result;

            } else {
                result.setRtnCode("1");
                result.setMsg("验证码错误，请重新输入");
            }
        } else {
            JSONObject jsonObject = JSONObject.fromObject(login(request,model, false));
            result.setMsg(jsonObject.get("msg").toString());
            result.setRtnCode(jsonObject.get("rtnCode").toString());
            //result.setBizData(jsonObject.get("bizData"));
            return result;
        }
        return result;
    }

    public Result login(HttpServletRequest request,SysUser model, boolean needPassword) {
        Result result = new Result();
        Subject subject = SecurityUtils.getSubject();
        String psw = "";
        if (CommUtil.getSysUserLoginInfo() != null
                && !CommUtil.getSysUserLoginInfo().getUserName().equals(model.getUserName())
                && !CommUtil.getSysUserLoginInfo().getPassWord().equals(psw)
        ) {
            subject.logout();
        }
        CaptchaUsernamePasswordToken token = new CaptchaUsernamePasswordToken(model.getUserName(), model.getPassWord(), needPassword);
        try {
            //验证角色和权限
            subject.login(token);
        } catch (UnknownAccountException e) {
            result.setMsg(e.getMessage());
            result.setRtnCode("1");
            return result;
        } catch (DisabledAccountException e) {
            result.setMsg(e.getMessage());
            result.setRtnCode("1");
            return result;
        } catch (IncorrectCredentialsException e) {
            result.setMsg(e.getMessage());
            result.setRtnCode("1");
            return result;
        } catch (ExcessiveAttemptsException e) {
            result.setMsg(e.getMessage());
            result.setRtnCode("1");
            return result;
        }

        return result;
    }

    /**
     * 获取全部列表，通常用于下拉框
     *
     * @param request
     * @param sysUser
     * @return
     */
    @RequestMapping(value = "/getLists.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getLists(HttpServletRequest request, SysUser sysUser) {
        Result result = new Result();
        SysUserExample example = new SysUserExample();
        SysUserExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteNotEqualTo("1");
        List<SysUser> list = iSysUserService.selectByExample(example);
        result.setBizData(list);
        return result;
    }

    /**
     * 获取分页列表，用于页面的列表查询
     *
     * @param request
     * @param sysUserList
     * @return
     */
    @RequestMapping(value = "/list.do", method = RequestMethod.POST)
    @ResponseBody
    public PageInfo list(HttpServletRequest request, SysUserList sysUserList) {
        Result result = new Result();

        PageInfo pageInfo = iSysUserService.list(sysUserList);
        return pageInfo;
    }


    @RequestMapping(value = "/save.do", method = RequestMethod.POST)
    @ResponseBody
    public Result save(HttpSession session, SysUserEdit model, @Param("registerImgVerify") String registerImgVerify) {
        Result result = new Result();
        if (ToolUtil.isNotEmpty(registerImgVerify)) {
            //验证码不为空
            String random = (String) session.getAttribute("RANDOMVALIDATECODEKEY");
            if (random.equals(registerImgVerify)) {//验证码成功
                result=saveOrEdit(model);
            } else {
                result.setRtnCode("1");
                result.setMsg("验证码错误，请重新输入");
            }
        } else {
            result=saveOrEdit(model);
        }
        return result;
    }

    @SystemLog(description = "保存实体")
    public Result saveOrEdit(SysUserEdit sysUserEdit) {
        Result result = new Result();
        if (sysUserEdit == null) {
            throw new InvalidParamException(MessageHelper.MODEL_ISNULL);  //抛出异常
        }
        int code = iSysUserService.saveOrEdit(sysUserEdit);
        result.setBizData(code);
        return result;
    }

    /**
     * 保存实体信息
     *
     * @param request
     * @param sysUserEdit
     * @return
     */
    @SystemLog(description = "保存实体")
    @RequestMapping(value = "/saveOrEdit2.do", method = RequestMethod.POST)
    @ResponseBody
    public Result saveOrEdit2(HttpServletRequest request, SysUserEdit sysUserEdit) {
        Result result = new Result();
        if (sysUserEdit == null) {
            throw new InvalidParamException(MessageHelper.MODEL_ISNULL);  //抛出异常
        }
        result.setBizData(iSysUserService.saveOrEdit2(sysUserEdit));

        return result;
    }

    /**
     * 获取实体信息
     *
     * @param request
     * @param sysUserEdit
     * @return
     */
    @RequestMapping(value = "/getInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getInfo(HttpServletRequest request, SysUserEdit sysUserEdit) {
        Result result = new Result();
        if (ToolUtil.isEmpty(sysUserEdit.getUid())) {
            throw new InvalidParamException(MessageHelper.ID_ISNULL);  //抛出异常
        }
        SysUserEdit sysUser = iSysUserService.getInfo(sysUserEdit.getUid());
        result.setBizData(sysUser);
        return result;
    }

    /**
     * 删除实体信息
     *
     * @param request
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete.do", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(HttpServletRequest request, @RequestParam("ids[]") String[] ids) {
        Result result = new Result();
        if (ids != null && ids.length == 0) {
            throw new InvalidParamException(MessageHelper.ID_ISNULL);  //抛出异常
        }
        int code = iSysUserService.delete(ids);
        result.setBizData(code);
        return result;
    }

}
