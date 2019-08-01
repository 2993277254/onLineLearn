package com.lsfly.util;

import com.lsfly.bas.model.system.SysUser;
import com.lsfly.bas.model.system.ext.SysUserList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/27.
 */
public class CommUtil {

    /**
     * 后台登入信息 admin_login
     */
    public static SysUserList getSysUserLoginInfo(){
        if(null == getSession())
            return null;
        else
            return (SysUserList)getSession().getAttribute("ollSysUser");


    }

    public static SysUser clearSysUserLoginInfo(){
        if(null == getSession())
            return null;
        else
            return (SysUser)getSession().removeAttribute("ollSysUser");
    }

    /**
     * 得到shiro Session
     * @return Session
     */
    public static Session getSession(){
        Subject subject = SecurityUtils.getSubject();
        return subject.getSession(false);
    }

    public  static void main(String[] args){
        String data="[{\n" +
                "\t\"alarmTerid\": \"-1000\",\n" +
                "\t\"callsipReg\": 0,\n" +
                "\t\"createTime\": \"2018-11-14 15:45:05\",\n" +
                "\t\"elecfanStatus\": 0,\n" +
                "\t\"entrguardStatus\": 0,\n" +
                "\t\"id\": 26,\n" +
                "\t\"ip\": \"192.168.18.268\",\n" +
                "\t\"isDelete\": 0,\n" +
                "\t\"lightStatus\": 0,\n" +
                "\t\"model\": \"BL-JS500-ZB21\",\n" +
                "\t\"name\": \"fcz测试主机\",\n" +
                "\t\"netStatus\": 0,\n" +
                "\t\"number\": \"10069\",\n" +
                "\t\"orgId\": \"46933925-E2AF-42DE-A0E7-F531369404D5\",\n" +
                "\t\"password\": \"123456abc\",\n" +
                "\t\"sipIp\": \"0.0.0.0\",\n" +
                "\t\"sn\": \"02HST18411656\",\n" +
                "\t\"status\": 0,\n" +
                "\t\"superId\": \"-1000\",\n" +
                "\t\"terPostion\": \"中国广州1\",\n" +
                "\t\"timeStamp\": 0,\n" +
                "\t\"uuid\": \"e0aa2368-cf33-46ba-ae17-b18e89079614\",\n" +
                "\t\"version\": 0\n" +
                "}]";
        post("http://localhost:8080/api/uploadTerminal.do",data);
    }

    public static String post(String url, String data) {
        String result = "";
        HttpPost httpRequst = new HttpPost(url);//创建HttpPost对象

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("param", data));
        //Log.i("params","key"+params);

        try {
            httpRequst.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            httpRequst.setHeader("Accept", "application/json"); //对参数进行编码，防止中文乱码,解决平台接收乱码问题
            httpRequst.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequst);
            //Log.i("444","StatusCode"+httpResponse.getStatusLine().getStatusCode());
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);//取出应答字符串
            }
        } catch (UnsupportedEncodingException e) {
           // MyLog.error(Author.COMMON, HttpConnect.class, "POST", e.toString());
        } catch (ClientProtocolException e) {
           // MyLog.error(Author.COMMON, HttpConnect.class, "POST", e.toString());
        } catch (IOException e) {
           // Log.i("111","e.toString()"+e.toString());
           // MyLog.error(Author.COMMON, HttpConnect.class, "POST", e.toString());
        } catch (Throwable throwable) {
           // MyLog.error(Author.COMMON, HttpConnect.class, "POST", throwable.toString());
        }
        return result;
    }

}
