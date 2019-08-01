package com.lsfly.controller.system;

import com.lsfly.bas.service.system.ISysDictService;
import com.lsfly.util.PropertiesUtil;
import com.lsfly.util.ServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;

@Controller
public class mainController extends BaseController {
    public static final Logger logger = LoggerFactory.getLogger(SysUserController.class);

    @Autowired
    private ISysDictService iSysDictService;
    /**
     * 跳转主页
     * @param request
     * @return
     */
    @RequestMapping("/mainPage.do")
    public ModelAndView toIndex(HttpServletRequest request, HttpSession session){
        //获取路径
        session.setAttribute("uploadHttpPath", PropertiesUtil.getValue("uploadHttpPath"));
//        SysUser user=(SysUser)request.getSession().getAttribute("sysuser");
//        logger.info("登录的用户"+user.getUserName());
        //iSysDictService.getLists();
        ModelAndView modelAndView=new ModelAndView("index");
        //System.out.println(new Gson().toJson(iSysDictService.getLists()));
        //modelAndView.addObject("sysDict",new Gson().toJson(iSysDictService.getLists()));
        return modelAndView;
    }

    //解析二级目录
    @RequestMapping("/{url}/{detail}")
    public ModelAndView allPage(HttpServletRequest request, @PathVariable String url,@PathVariable String detail,HttpSession session){
        String pageUrl="";
        String route=url+"/"+detail;
        try {
            pageUrl= java.net.URLDecoder.decode(route , "utf-8");   //解码
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
       ;

        session.setAttribute("uploadHttpPath", PropertiesUtil.getValue("uploadHttpPath"));
//        String ip= ServletUtils.getIpAddr();
//        String uploadHttpPath="http://"+ip+":8020/uploadfile";
//        session.setAttribute("uploadHttpPath", uploadHttpPath);

        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName(pageUrl);
        return modelAndView;
    }

    //三级目录
    @RequestMapping("/{url}/{detail}/{detail2}")
    public ModelAndView allPage(HttpServletRequest request, @PathVariable String url, @PathVariable String detail, @PathVariable String detail2){
        String pageUrl = "";
        String route = url +"/"+ detail+"/"+detail2;
        try {
            pageUrl = java.net.URLDecoder.decode(route , "utf-8");   //解码
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(pageUrl);
        return modelAndView;
    }

}
