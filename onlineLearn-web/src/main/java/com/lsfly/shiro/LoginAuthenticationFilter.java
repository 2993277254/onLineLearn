package com.lsfly.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


public class LoginAuthenticationFilter extends FormAuthenticationFilter {
	
	private static final Logger log = LoggerFactory
			.getLogger(LoginAuthenticationFilter.class);
	
	public static final String DEFAULT_CAPTCHA_PARAM = "captcha";

	private String captchaParam = DEFAULT_CAPTCHA_PARAM;

	public String getCaptchaParam() {
		return captchaParam;
	}

	public void setCaptchaParam(String captchaParam) {
		this.captchaParam = captchaParam;
	}

	protected String getCaptcha(ServletRequest request) {
		return WebUtils.getCleanParam(request, getCaptchaParam());
	}

	// 创建 Token
	@Override
	protected CaptchaUsernamePasswordToken createToken(ServletRequest request, ServletResponse response) {
		String username = getUsername(request);
		String password = getPassword(request);
		String captcha = getCaptcha(request);
		boolean rememberMe = isRememberMe(request);
		String host = getHost(request);
		
		//加密密码
		
		return new CaptchaUsernamePasswordToken(username, password, rememberMe, host, captcha);
	}

	// 验证码校验
	protected void doCaptchaValidate(HttpServletRequest request, CaptchaUsernamePasswordToken token) {
		if (captchaParam != null && !captchaParam.equalsIgnoreCase(token.getCaptcha())) {
			request.setAttribute("msg", "验证码错误");
			throw new IncorrectCaptchaException("验证码错误！");
		}
	}

	// 登录
	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
			CaptchaUsernamePasswordToken token = createToken(request, response);
	        try {
	            Subject subject = getSubject(request, response);
	            subject.login(token);
	            return onLoginSuccess(token, subject, request, response); //进入之前的请求页面
	        } catch (AuthenticationException e) {
	        	request.setAttribute("oolLoginFail", "登入失败,账号或密码错误");
	            return onLoginFailure(token, e, request, response);
	        }
	}
	
	
	/**
	 * 配置的登录成功路径
	 */
//	public String getSuccessUrl() {
//		//暂时不用特殊处理
//		return super.getSuccessUrl();
//	}
	
    /**
     * 当登录成功  
     * 如果是ajax的方式，返回结果要特殊处理。 同时转发页面也是ajax处理
     * 如果是表单形式，跳转到上次的页面
     * @param token
     * @param subject
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected boolean onLoginSuccess(CaptchaUsernamePasswordToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		//System.out.println("===============上一次页面"+WebUtils.getSavedRequest(request).getRequestURI());
        if (!"XMLHttpRequest".equalsIgnoreCase(httpServletRequest.getHeader("X-Requested-With"))) {
        	// 不是ajax请求
            issueSuccessRedirect(request, response);
        } else {

            httpServletResponse.setCharacterEncoding("UTF-8");
            PrintWriter out = httpServletResponse.getWriter();
            out.println("{\"success\":true,\"msg\":\"登入成功\"}");
			//out.print(WebUtils.getSavedRequest(request).getRequestURI());
            out.flush();
            out.close();
        }
        return false; //不返回登入接口     //return true;回到登入方法
    }
    
    
    /**
	 * 主要是处理登入失败的方法
	 */
//	protected boolean onLoginFailure(CaptchaUsernamePasswordToken token,
//			AuthenticationException e, ServletRequest request,
//			ServletResponse response) {
//		if (!"XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request)
//				.getHeader("X-Requested-With"))) {// 不是ajax请求
//			setFailureAttribute(request, e);
//			return true;
//		}
//		try {
//			response.setCharacterEncoding("UTF-8");
//			PrintWriter out = response.getWriter();
//			String message = e.getClass().getSimpleName();
//			if ("IncorrectCredentialsException".equals(message)) {
//				out.println("{success:false,message:'密码错误'}");
//			} else if ("UnknownAccountException".equals(message)) {
//				out.println("{success:false,message:'账号不存在'}");
//			} else if ("LockedAccountException".equals(message)) {
//				out.println("{success:false,message:'账号被锁定'}");
//			} else {
//				out.println("{success:false,message:'未知错误'}");
//			}
//			out.flush();
//			out.close();
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		return false;
//	}
    
    
    /**
	 * 所有请求都会经过的方法。
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		if (isLoginRequest(request, response)) {
			if (isLoginSubmission(request, response)) {
				if (log.isTraceEnabled()) {
					log.trace("Login submission detected.  Attempting to execute login.");
				}
				if ("XMLHttpRequest"
						.equalsIgnoreCase(((HttpServletRequest) request)
								.getHeader("X-Requested-With"))) {
					//这里也可以验证登入
//					String vcode = request.getParameter("vcode");

				}
				return executeLogin(request, response);
			} else {
				return true;
			}
		} else {
			if (log.isTraceEnabled()) {
				log.trace("需要授权的路径： [" + getLoginUrl() + "]");
			}
			if (!"XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"))) {
				saveRequestAndRedirectToLogin(request, response);
			} else {

				response.setContentType("text/html; charset=utf-8");
				//response.setCharacterEncoding("UTF-8");
				PrintWriter out = response.getWriter();
				out.println("{\"rtnCode\":100,\"msg\":\"登入授权验证失败,请重新登录\",\"login_status\":\"authc_fail\"}");
				out.flush();
				out.close();
			}
			return false;
		}
	}

}
