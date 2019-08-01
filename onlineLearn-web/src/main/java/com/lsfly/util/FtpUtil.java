package com.lsfly.util;


import com.lsfly.exception.InvalidParamException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.SocketException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/10/26.
 */
public class FtpUtil {


    /**
     * add  by huoquan
     * 上传至ftp
     * @param bytes
     * @param url  /XX/XXX格式
     * @param filename 文件名称
     * @return  成功后返回一个url路径
     * @throws IOException
     */
    public static String uploadToFtp(byte[] bytes,String url,String filename) throws InvalidParamException {
        String urlPath="";
        String fileNewName="";
        InputStream inputStream=null;
        if(ToolUtil.isEmpty(url)&&ToolUtil.isEmpty(filename))
            throw new InvalidParamException("文件为空,上传失败");
        FtpUtil filesUpAndDownFtp=new FtpUtil();
        inputStream=new ByteArrayInputStream(bytes);
        String suffix = "."+filename.substring(filename.lastIndexOf(".") + 1);//后缀
        FTPClient client=new FTPClient();
        boolean islogin=filesUpAndDownFtp.loginFTP(client);
        if(islogin){
            fileNewName=filesUpAndDownFtp.getCurrentDate()+filesUpAndDownFtp.getRandomNum()+suffix;
            boolean isupload=filesUpAndDownFtp.uploadFile(client, PropertiesUtil.getValue("ftpRootPath")+url, fileNewName, inputStream);
            if(isupload)  //上传成功
                urlPath=PropertiesUtil.getValue("ftpRootPath")+url+"/"+fileNewName;
            else
                throw new InvalidParamException("上传失败");
            filesUpAndDownFtp.logout(client);// 退出/断开FTP服务器链接
        }else{
            //登陆不成功
            throw new InvalidParamException("ftp服务器无法登陆，上传失败");
        }
        if(inputStream!=null)
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return urlPath;
    }

    //登陆
    public static boolean loginFTP(FTPClient client) {
        String ftpIp = PropertiesUtil.getIp();
        String ftpPort = PropertiesUtil.getPort();
        String ftpUser = PropertiesUtil.getUser();
        String ftpPwd = PropertiesUtil.getPwd();
        boolean b = false;

        try {
            client.connect(ftpIp, Integer.parseInt(ftpPort));
        } catch (NumberFormatException e) {
            System.out.println("无法连接到ftp");
            return false;
        } catch (SocketException e) {
            System.out.println("无法连接到ftp");
            return false;
        } catch (IOException e) {
            System.out.println("无法连接到ftp");
            return false;
        }
        client.setControlEncoding("uft-8");
        try {
            b = client.login(ftpUser, ftpPwd);
        } catch (IOException e) {
            System.out.println("登录ftp出错");
            logout(client);// 退出/断开FTP服务器链接
            return false;
        }
        return b;

    }

    //登出
    public static boolean logout(FTPClient client) {
        boolean b = false;

        try {
            b = client.logout();// 退出登录
            client.disconnect();// 断开连接
        } catch (IOException e) {
            return false;
        }
        return b;

    }

    //上传
    public static boolean uploadFile(FTPClient client, String remotePath,
                              String fileNewName, InputStream inputStream) {
        boolean b = false;
        try {
            client.setFileType(FTPClient.BINARY_FILE_TYPE);
            client.enterLocalPassiveMode();
            if (remotePath != null && !"".equals(remotePath.trim())) {
                String[] pathes = remotePath.split("/");
                for (String onepath : pathes) {
                    if (onepath == null || "".equals(onepath.trim())) {
                        continue;
                    }

                    onepath = new String(onepath.getBytes("utf-8"),
                            "iso-8859-1");
                    System.out.println("onepath=" + onepath);
                    if (!client.changeWorkingDirectory(onepath)) {
                        client.makeDirectory(onepath);// 创建FTP服务器目录
                        client.changeWorkingDirectory(onepath);// 改变FTP服务器目录
                    } else {
                        System.out.println("文件单路径");
                    }
                }
            }
            b = client.storeFile(new String(fileNewName.getBytes("utf-8"),
                    "iso-8859-1"), inputStream);
        } catch (UnsupportedEncodingException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return b;
    }

    //获取随机数
    public static Integer getRandomNum(){
        int randomNum=(int)(Math.random()*900)+100;
        return randomNum;
    }

    //获取当前时间，精确到mmss
    public static String getCurrentDate(){
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentDate = formatter.format(currentTime);
        return currentDate;
    }

    //下载

    /**
     * 从ftp下载文件
     * @param response
     * @param path   数据库的相对路径  例： XXX/XXX/xxx.JPG
     * @param title  文件的中文名字
     * @throws IOException
     */
    public static void downFileByFtp(HttpServletResponse response, String path, String title){
        FTPFile[] fs;
        InputStream is = null;
        //登陆ftp
        FTPClient ftpClient=new FTPClient();
        if(ToolUtil.isEmpty(path))
            throw new InvalidParamException("ftp服务器无法登陆，上传失败");
        try {
            //下载提示框
            //设置文件下载头
            loginFTP(ftpClient);
            title = URLEncoder.encode(title,"UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + title);
            //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType("multipart/form-data");
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            // 设置被动模式
            ftpClient.enterLocalPassiveMode();
            // 设置以二进制流的方式传输
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            // 设置编辑格式
            ftpClient.setControlEncoding("utf-8");
            is = ftpClient.retrieveFileStream(new String(
                    (path).getBytes("utf-8"),
                    "iso-8859-1"));
            int len = 0;
            while((len = is.read()) != -1){
                out.write(len);
                out.flush();
            }
            if(is!=null)
                is.close();
            logout(ftpClient);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取图片的base64编码
     * @param path
     * @return
     */
    public static String getImageBase64(String path){
        InputStream inStream = null;
        byte[] in_b=null;
        FTPClient ftpClient=new FTPClient();
        //ImageIO.read(inStream);
        try{
            loginFTP(ftpClient);
            // 设置被动模式
            ftpClient.enterLocalPassiveMode();
            // 设置以二进制流的方式传输
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            // 设置编辑格式
            ftpClient.setControlEncoding("utf-8");
            inStream = ftpClient.retrieveFileStream(new String(
                    (path).getBytes("utf-8"),
                    "iso-8859-1"));
            if(inStream==null)
                return "";
            in_b = input2byte(inStream); //in_b为转换之后的结果
        }catch(Exception e){
            e.printStackTrace();
        }finally {
                try {
                    if(inStream!=null)
                        inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            logout(ftpClient);
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return "data:image/png;base64,"+encoder.encode(in_b);// 返回Base64编码过的字节数组字符串
    }

    /**
     * byte[]和InputStream的相互转换
     * @param inStream
     * @return
     * @throws IOException
     */
    public static final byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }
}
