package com.lsfly.util;


import com.lsfly.sys.BackMsg;

public class BackMsgUtil {
	
	public static BackMsg buildMsg(boolean isSuccess, String retMsg){
		BackMsg backMsg = new BackMsg();
		backMsg.setSuccess(isSuccess);
		backMsg.setMsg(retMsg);
		return backMsg;
	}

}
