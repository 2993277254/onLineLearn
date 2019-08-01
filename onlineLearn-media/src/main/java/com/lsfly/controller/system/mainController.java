package com.lsfly.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class mainController extends BaseController {


	@RequestMapping(value = "/login",method = RequestMethod.GET)
	public String login(){
		return "login";
	}

	@RequestMapping("/m.do")
	public void mainPage1(){
		System.out.println("xxxxxxxxxxxxxxxxxxxxxx");
        //return "index";
	}
}
