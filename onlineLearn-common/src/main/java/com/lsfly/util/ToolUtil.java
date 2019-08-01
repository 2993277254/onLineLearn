
package com.lsfly.util;


import com.google.gson.Gson;

import com.lsfly.sys.BackMsg;
import com.lsfly.sys.MsgResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToolUtil {

	private static final char SEPARATOR = '_';
	//======================= 系统全局常量或参数 ========================
	
	//全局变量，配置网站根目录
	public static String ROOT_PATH = System.getProperty("zbwz"); //webAppRootKey


	
	
	//================= 系统方法 =====================
	/**
	 * 返回当前用户的id
	 * 可以获得当前登录者的ID
	 * @return
	 */
	public static String getCurrentUserId() {
		System.out.println(getSession());
		if(getSession()!=null&&getSession().getAttribute("ollUserId")!=null)
			return getSession().getAttribute("ollUserId").toString();
		else
			return null;
	}

	public static String getCurrentUserName() {
		if(null == getSession())
			return null;
		else
			return (String)getSession().getAttribute("userName");
	}
	
	public final static String encryptPassword(String userCode, String psw){
		return strToMd5(userCode+psw);
	}
	
	public static String GetCurrentTime(){
		SimpleDateFormat sdf  = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
	
	public static String GetCurrentTime(String format){
		SimpleDateFormat sdf  = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}
	
	//前一天
    public static String beforeDate(){
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
    	
    	Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date date = calendar.getTime();
		return sdf.format(date);
    }

	/**
	 * 得到shiro Session
	 * @return Session
	 */
	public static Session getSession(){
		Subject subject = SecurityUtils.getSubject();
		return subject.getSession(false);
	}
	/**
	 * obj to map
	 * @param obj
	 * @return
	 */
	public static Map<String,Object> obj2Map(Object obj){
		Map<String,Object> map = new HashMap<String, Object>();
		Method[] methods= obj.getClass().getMethods();
		for(int i=0;i<methods.length;i++){
			String name = methods[i].getName();
			Object value = null;
			if(name.startsWith("get")){
				try {
					if(obj==null){
						continue;
					}
					value = methods[i].invoke(obj);
					if(value!=null){
						map.put(name.substring(3).toLowerCase(), value);
					}
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}
	
	//================= 工具类型 =====================
	
	//判断字符串是否不为空
	public static boolean isNotEmpty(String str){
		return str != null && str.length() > 0;
	}
	//判断字符串是否为空
	public static boolean isEmpty(String str){
		return str == null || str.length() == 0;
	}
	//判断字符串是否为数字
	public static boolean isNum(String str){
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}
	//判断字符串是否为正确的座机或手机号码 
	public static boolean isMobileOrPhone(String mobiles){  
		if(isMobile(mobiles) || isPhone(mobiles)){
			return true;
		}
		return false;
	}
	
	//手机号验证 验证通过返回true
	public static boolean isMobile(String str) { 
		Pattern p = null;
		Matcher m = null;
		boolean b = false; 
		p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches(); 
		return b;
	}
	// 电话号码验证 验证通过返回true
	public static boolean isPhone(String str) { 
		Pattern p1 = null,p2 = null;
		Matcher m = null;
		boolean b = false;  
		p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
		if(str.length() >9)
		{	m = p1.matcher(str);
 		    b = m.matches();  
		}else{
			m = p2.matcher(str);
 			b = m.matches(); 
		}  
		return b;
	}
	
	//判断字符串是否为正确的电子邮箱
	public static boolean isEmail(String email){  
        if (null==email || "".equals(email)) return false;    
        Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配  
//	   	Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配  
        Matcher m = p.matcher(email);  
        return m.matches();  
    } 
	
	/**
	 * 对字符串进行MD5加密
	 * @param str 需要加密的字符串
	 * @return 加密后的字符串
	 */
	public static String strToMd5(String str){
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte[] byteDigest = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < byteDigest.length; offset++) {
				i = byteDigest[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			// 32位加密
			return buf.toString();
			// 16位的加密
			// return buf.toString().substring(8, 24);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．
	 * (mkdir如果父目录不存在则会抛出异常)
	 * @param destPath 存放目录
	 */
	public static void mkdirs(String destPath) {
		File file = new File(destPath);
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();
		}
	}
	
	/**
	 * 返回32位UUID字符串
	 * @return 32位UUID字符串
	 */
	public static String getUUID(){
		//return UUID.randomUUID().toString().replace("-","").toUpperCase();
		return UUID.randomUUID().toString().toUpperCase();
	}

	public static String getEmptyUUID(){
		//return UUID.randomUUID().toString().replace("-","").toUpperCase();
		return "00000000-0000-0000-0000-000000000000";
	}
	
//	public static String SerialNo(){
//		int uuid = Math.abs(UUID.randomUUID().toString().hashCode());
//		String id = String.valueOf(uuid);
//		if(id.length() > 9){
//			int index = 9-id.length();
//			for(int i=0;i<index;i++){
//				id += "0";
//			}
//		}
//   	 	DateFormat df = new SimpleDateFormat("yyMM"); //yyMMdd
//		String dateTime = df.format(new Date());
//		return dateTime+id;
//	}
	
	
	/**
	 * 发送短信方法  企信通的说明
	 * @param content	短信内容
	 * @param mobileList	短信接收人列表	格式：13900008888,13585519197,...
	 * 说明:
	 * @return	发送状态

	 */
	@SuppressWarnings("deprecation")
	public static String sendSMS(String content,String mobileList){

		//互易推
		try {
			String userName = "wlxx";
			String password = "wlxx123";
			StringBuffer url = new StringBuffer();
			url.append("http://120.76.103.224:8088/SendMess.aspx?user="+userName+"&pwd="+password+"&extend=110&channel="+java.net.URLEncoder.encode("qxtong", "gbk"));
			url.append("&phonelist="+mobileList);
			url.append("&cont="+java.net.URLEncoder.encode(content, "gbk")+"");
		    URL DxUrl = new URL(url.toString());   
		   
		    BufferedReader in = new BufferedReader(   
	                new InputStreamReader(DxUrl.openStream()));   

		    String inputLine;
		    while ((inputLine = in.readLine()) != null)   
		        System.out.println(inputLine);   
		    in.close();   
		    
		    return inputLine;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	/**
	 * 将图片的Base64字符串，反转为字节数组，最后写入指定图片文件中
	 * @param imgBase64		图片文件
	 * @param filePath		保存路径
	 * @param fileName		文件名称
	 * @throws Exception	
	 */
	public static BackMsg writeImgBase64ToFile(String imgBase64, String imageType, String fileName) throws Exception{
		BackMsg backMsg= BackMsgUtil.buildMsg(false, MessageHelper.ADDSUCC);
		
		if(isEmpty(imgBase64)) return backMsg;
		
		String filePath = "";
		if("productImg".toLowerCase().equals( imageType.toLowerCase()) ){
			filePath += "productImg/snap";
		}else{
			filePath += imageType+"/snap";
		}
		
		//判断是否带有协议前缀：data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAA
//		if(imgBase64.startsWith("data")){
//			imgBase64 = imgBase64.substring(imgBase64.indexOf(",") + 1);
//		}
		imgBase64 = imgBase64.split(",")[1];
		File file =new File(ROOT_PATH+filePath);
		//如果文件夹不存在则创建
		if  (!file.exists()  && !file.isDirectory())
		{
		    file.mkdirs();
		}
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			//Base64解码
            byte[] b = decoder.decodeBuffer(imgBase64);
            for(int i=0;i<b.length;++i)
            {
                if(b[i]<0) 
                {//调整异常数据
                    b[i]+=256;
                }
            }
            //生成jpeg图片  
			OutputStream out = new FileOutputStream(ROOT_PATH+filePath+"/"+fileName);
			out.write(b);
			out.flush();
			out.close();
			
			backMsg.setSuccess(true);
			backMsg.setData(filePath+"/"+fileName);
			return backMsg;
		} catch (Exception e) {
			e.printStackTrace();
			return backMsg;
		}
	}
	
	
	
	//---------------------------私有方法-----------------------------------------------
	/**
	 * 得到原生HttpServletRequest
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest getHttpServletRequest(){
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();  
	    HttpServletRequest request = ((ServletRequestAttributes)ra).getRequest();  
	    return request;
	}
	
	public static String getWebUrl(){
		String path = getHttpServletRequest().getContextPath();
		String basePath = getHttpServletRequest().getScheme()+"://"+getHttpServletRequest().getServerName()+":"+getHttpServletRequest().getServerPort()+path+"/";
		return basePath;
	}
	
	/**
	 * 得到原生HttpSession
	 * @return HttpSession
	 */
	private static HttpSession getHttpSession(){
		return getHttpServletRequest().getSession(false);
	}

	/**
	 * 分页方法
	 *
	 * @param list
	 *            源数据
	 * @param currentPage
	 *            当前页
	 * @param maxNum
	 *            每页显示几条
	 * @param pageNum
	 *            总页数
	 * @return
	 */
	public static List getPageList(List list, int currentPage, int maxNum,
								   int pageNum) {

		int fromIndex = 0; // 从哪里开始截取
		int toIndex = 0; // 截取几个
		if (list == null || list.size() == 0)
			return null;
		// 当前页小于或等于总页数时执行
		if (currentPage <= pageNum && currentPage != 0) {
			fromIndex = (currentPage - 1) * maxNum;

			if (currentPage == pageNum) {
				toIndex = list.size();

			} else {
				toIndex = currentPage * maxNum;
			}

		}
		return list.subList(fromIndex, toIndex);
	}

	/**
	 * 获取返回信息
	 * @param list
	 * @param msg
	 */
	public static String getReturnMsg(List<MsgResult> list, String msg){
		String str="";
		try{
			String listStr=new Gson().toJson(list);
			str+=listStr;
		}catch (Exception e){
			e.printStackTrace();
		}
		if(isNotEmpty(msg))
			str+=msg;
		return str;
	}

	/**
	 * 获取系统参数
	 * @param str
	 * @return
	 */
	public  static String  getParameterByRequest(HttpServletRequest request,String str){
		String param="";
		try{
			param=new String(request.getParameter(str).getBytes("ISO-8859-1"), "utf-8");
		}catch(Exception e){
/*			e.printStackTrace();*/
		}
		return param;
	}

	/**
	 * 驼峰命名法工具
	 * @return
	 * 		toCamelCase("hello_world") == "helloWorld"
	 * 		toCapitalizeCamelCase("hello_world") == "HelloWorld"
	 * 		toUnderScoreCase("helloWorld") = "hello_world"
	 */
	public static String toCamelCase(String s) {
		if (s == null) {
			return null;
		}

		s = s.toLowerCase();

		StringBuilder sb = new StringBuilder(s.length());
		boolean upperCase = false;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if (c == SEPARATOR) {
				upperCase = true;
			} else if (upperCase) {
				sb.append(Character.toUpperCase(c));
				upperCase = false;
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	/**
	 * 驼峰命名法工具
	 * @return
	 * 		toCamelCase("hello_world") == "helloWorld"
	 * 		toCapitalizeCamelCase("hello_world") == "HelloWorld"
	 * 		toUnderScoreCase("helloWorld") = "hello_world"
	 */
	public static String toCapitalizeCamelCase(String s) {
		if (s == null) {
			return null;
		}
		s = toCamelCase(s);
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	/**
	 * 驼峰命名法工具
	 * @return
	 * 		toCamelCase("hello_world") == "helloWorld"
	 * 		toCapitalizeCamelCase("hello_world") == "HelloWorld"
	 * 		toUnderScoreCase("helloWorld") = "hello_world"
	 */
	public static String toUnderScoreCase(String s) {
		if (s == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		boolean upperCase = false;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			boolean nextUpperCase = true;

			if (i < (s.length() - 1)) {
				nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
			}

			if ((i > 0) && Character.isUpperCase(c)) {
				if (!upperCase || !nextUpperCase) {
					sb.append(SEPARATOR);
				}
				upperCase = true;
			} else {
				upperCase = false;
			}

			sb.append(Character.toLowerCase(c));
		}

		return sb.toString();
	}

	/**
	 *  下划线的字符串，获取别名，比如sys_user_aa==》sua
	 * @param tableName
	 * @return
	 */
	public static String getAliasNameByUnderline(String tableName) {
		String aiasName="";
		if(StringUtils.isNotEmpty(tableName)){
			try{
				String[] str=tableName.split("_");
				if(str.length>0){
					if(str.length==1){
						aiasName=str[0];
					}else{
						for (String s:str) {
							aiasName+=s.charAt(0);
						}
					}
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return  aiasName.toLowerCase();
	}

	//获取时间时间戳
	public  static Long getTimeMillis(){
		return System.currentTimeMillis();
	}

	public static void main(String[] args) {
		System.out.println(strToMd5("123456"));
	}
}
