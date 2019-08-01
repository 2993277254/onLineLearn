package com.lsfly.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class CaptchaUsernamePasswordToken extends UsernamePasswordToken {

	private static final long serialVersionUID = 4560440260899490595L;
	private String captcha;
	private boolean needPassword; //true  需要验证密码  false 不需要验证密码
	
	public CaptchaUsernamePasswordToken(String username, String password) {
		super(username, password);
	}
	
	public CaptchaUsernamePasswordToken(String username, String password, boolean needPassword) {
		super(username, password);
		this.needPassword = needPassword;
	}
	
	public CaptchaUsernamePasswordToken(String username, String password, boolean rememberMe, String host,
			String captcha) {
		super(username, password, rememberMe, host);
		this.captcha = captcha;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public boolean getNeedPassword() {
		return needPassword;
	}

	public void setNeedPassword(boolean needPassword) {
		this.needPassword = needPassword;
	}
}
