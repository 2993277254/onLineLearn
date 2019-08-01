package com.lsfly.util;

import cc.eguid.FFmpegCommandManager.FFmpegManager;
import cc.eguid.FFmpegCommandManager.FFmpegManagerImpl;
import cc.eguid.FFmpegCommandManager.entity.TaskEntity;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FFmpegUtil {

    private static final Logger logger = LoggerFactory.getLogger(FFmpegUtil.class);
    private static final int VIDEO_HLS_TIME=10;
    private static FFmpegManager manager = new FFmpegManagerImpl();
    private static String rootPath = "";//D:/soft/ffmpeg/bin
    private static String mencoderPath = "";//D:/soft/ffmpeg/bin
    static {
//        rootPath ="f:/ffmpeg/bin";
//        mencoderPath = "f:/ffmpeg/bin/mencoder.exe";
        rootPath= ServletUtils.getRealPath()+"ffmpeg\\bin";
        mencoderPath=ServletUtils.getRealPath()+"ffmpeg\\bin\\mencoder.exe";
        logger.info("rootPath::"+rootPath);
        logger.info("mencoderPath::"+mencoderPath);
    }

    public static FFmpegManager getFFmpegManager(){
        return manager;
    }

    /**
     * 获取视频某个时间点的图片
     * timeStr 为秒 第几秒
     */
    public static String catchImage(String filePath,String picName, String size){
        String tempDir = filePath.substring(0,filePath.lastIndexOf("/")+1);
        String name = picName+"_postpath.jpg";
        String output = tempDir + name;
        try {
            List<String> command = new ArrayList<String>();
            command.add("cmd.exe");
            command.add("/c");
            command.add(rootPath+"/ffmpeg");
            command.add("-i");
            command.add(filePath);
            command.add("-y");
            command.add("-f");
            command.add("image2");
            command.add("-r");
            command.add("1");
            command.add("-ss");
            command.add("1");
            command.add("-t");
            command.add("1");
            command.add("-s");
            command.add(size);
            command.add(output);
            System.out.println("截图命令==="+command);
            //开始执行，并不等待返回
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(command);
            Process p = pb.start();
            InputStream stderr = p.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null){
                sb.append(line);
                continue;
            }
            int exitVal = p.waitFor();
            p.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        output=output.replace(PropertiesUtil.getValue("uploadPath"),"");
        return output;
    }
    private static void convertToflvHD(String filePath,String dest){
        try {
            List<String> command = new ArrayList<String>();
            command.add("cmd.exe");
            command.add("/C");
            command.add(rootPath+"/ffmpeg");
            command.add("-i");              //输入文件路径
            command.add(filePath);
            command.add("-y");              //是否覆盖
            command.add("-ab");             //音频数据流量，32 64 96 128
            command.add("32");
            command.add("-ar");             //声音采样频率 22050
            command.add("22050");
            command.add("-acodec");         //音频编码AAC
            command.add("aac");
            command.add("-s");              //指定分辨率，标清，高清；
//          command.add("640x480");         //标清：480p
            command.add("1280x720");        //高清：720p
            command.add("-qscale");         //以数值质量为基础的VBR，范围：0.01-255，越小质量越好
            command.add("10");
            command.add("-r");              //帧速率，数值： 15 29.97
            command.add("15");
            command.add(dest);              //目标存储路径
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(command);
            pb.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 获得视频原有的分辨率
     * @return
     */
    public static String getVideoResolution(StringBuffer sb ){
        Pattern pat = Pattern.compile("([0-9]{2,4}x[0-9]{2,4})");
        Matcher matcher = pat.matcher(sb.toString());
        boolean find = matcher.find();
        if(find){
            String res = matcher.group(1);
            return res;
        }
        return "";
    }
    /**
     * 返回视频的长度：毫秒
     * @return
     */
    public static long getVideoTime(StringBuffer sb){
        Pattern pat = Pattern.compile("Duration: ([0-9:.]*),");
        Matcher matcher = pat.matcher(sb.toString());
        boolean find = matcher.find();
        if(find){
            String res = matcher.group(1);
            //处理
            String[] arr = res.split("[.]");
            String t = "";
            String l = "0";
            if(arr.length > 0){
                t = arr[0];
                if(arr.length > 1)l = arr[1];
            }
            try {
                long tempL = Long.parseLong(l);
                tempL = tempL * (l.length() ==1 ? 100 : (l.length() == 2 ? 10 : 1));
                String[] tempT = t.split(":");
                long tempH = Long.parseLong(tempT[0]);
                long tempM = Long.parseLong(tempT[1]);
                long tempS = Long.parseLong(tempT[2]);
                tempH = tempH * 60 * 60 * 1000;
                tempM = tempM * 60 * 1000;
                tempS = tempS * 1000;

                return (tempH + tempM + tempS + tempL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0L;
    }
    /**
     * 转化视频文件为Flv格式文件；
     * @param filePath
     * @return
     */
    public static JSONObject convertToFlv(String filePath){
        JSONObject result = new JSONObject();
        String tempFolder = filePath.substring(0,filePath.lastIndexOf("/"));
        String fileName = filePath.substring(filePath.lastIndexOf("/"),filePath.lastIndexOf("."));
        File folder = new File(tempFolder);
        if(!folder.isDirectory()){
            folder.mkdirs();
        }
        try {
            List<String> command = new ArrayList<String>();
            String dest = tempFolder+fileName+".flv";
            String destHD = tempFolder+fileName+"-HD.flv";
            System.out.println(filePath+"\r\n"+dest);
            command.add("cmd.exe");
            command.add("/C");
            command.add(rootPath+"/ffmpeg");
            command.add("-i");              //输入文件路径
            command.add(filePath);
            command.add("-y");              //是否覆盖
//          command.add("-ab");             //音频数据流量，32 64 96 128
//          command.add("32K");
//          command.add("-ar");             //声音采样频率 22050
//          command.add("22050");
//          command.add("-acodec");         //音频编码AAC
//          command.add("aac");
            command.add("-s");              //指定分辨率，标清，高清；
            command.add("640x480");         //标清：480p
//          command.add("1280x720");        //高清：720p
//          command.add("-qscale");         //以数值质量为基础的VBR，范围：0.01-255，越小质量越好
//          command.add("10");
            command.add("-r");              //帧速率，数值： 15 29.97
            command.add("15");
            command.add(dest);              //目标存储路径
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(command);
            Process p = pb.start();
            InputStream stderr = p.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null){
                sb.append(line);
                System.out.println(line);
                continue;
            }
            int exitVal = p.waitFor();
            p.destroy();
            //获得时长/速率/其他meta信息
            convertToflvHD(filePath,destHD);
            long time = getVideoTime(sb);
            String solution = getVideoResolution(sb);
            //截取第一秒的图片做封面
            catchImage(dest, fileName, solution);
            result.element("destPath",dest);    //普情FLV存放地址
            result.element("duration",time);    //时长
            result.element("destPathHD",destHD);//高清FLV存放地址
            result.element("solution",solution);//分辨率
            result.element("imagePath",tempFolder+fileName+"-1-"+solution+".jpg");
            //转化完成后进行增加索引
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * avi转成MP4
     * @param destAviPath
     * @param destMp4Path
     * @param fileName
     * @return
     */
    public static Boolean convertToMp4ByManager(String destAviPath,String destMp4Path,String fileName){
        List<String> command = new ArrayList<String>();
        command.add(rootPath+"/ffmpeg");
        command.add("-i");              //输入文件路径
        command.add(destAviPath);
        command.add("-c:v");
        command.add("libx264");
//        command.add("-c:a");
//        command.add("aac");
        command.add("-strict");
        command.add("-2");
//        command.add("-hls_time");
//        command.add(VIDEO_HLS_TIME+""); //每个切片的时间
//        command.add("-hls_list_size");
//        command.add("0");
//        command.add("-f");
//        command.add("hls");
        command.add(destMp4Path);
        TaskEntity info=manager.query(fileName);
        if(info!=null){
            manager.stop(fileName);
        }
        String id=manager.start(fileName,String.join(" ",command),true);
        if(StringUtils.isNotEmpty(id)){
            return  true;
        }else{
            return  false;
        }
    }
    //根据当前视频的分辨率获得高清分辨率信息
    public static String getHDSolution(String solution){
        String rs = "1280x720";
        try {
            if(solution.indexOf("x") > -1){
                String[] ars = solution.split("x");
                String width = ars[1];
                int iwidth = Integer.valueOf(width);
                if(iwidth < 720){
                    rs = solution;
                }
            }
        } catch (Exception e) {}
        return rs;
    }

    /**
     * 新版-视频转化逻辑流程
     * 1. 至转化高清
     * @param filePath
     * @return
     */
    public static JSONObject callFFmpegNew(String filePath){
        Date d1 = new Date();
        long d1l = d1.getTime();
        filePath = filePath.replaceAll("\\\\", "/");
        JSONObject jo = new JSONObject();
        String folder = filePath.substring(0,filePath.lastIndexOf("/"));//文件的相对文件夹路径,例如：/attachment/video/
        String fileName = filePath.substring(filePath.lastIndexOf("/")+1,filePath.lastIndexOf("."));//文件的文件名,例如：sss.mp4
        String hdDestFolder = folder+"/"+fileName+"_HD";//高清存放路径
        String hdDestM3u8Path = hdDestFolder+"/"+fileName+".m3u8";
        String hdResPath = folder+"/"+fileName+"_HD"+"/"+fileName+".m3u8";

        File destFolderFile = new File(hdDestFolder);
        if(!destFolderFile.isDirectory()){
            destFolderFile.mkdirs();
        }

        BufferedReader br = null;
        String solution = "1280x720";
        Long duration = 0L;
        try {
            String[] command = new String[18];
            command[0] = "cmd.exe";
            command[1] = "/C";
            command[2] = rootPath+"/ffmpeg";
            command[3] = "-i";              //输入文件路径
            command[4] = filePath;
            command[5] = "-c:v";            //
            command[6] = "libx264";
            command[7] = "-c:a";
            command[8] = "aac";
            command[9] = "-strict";
            command[10] = "-2";
            command[11] = "-hls_time";
            command[12] = VIDEO_HLS_TIME+"";
            command[13] = "-hls_list_size";
            command[14] = "0";
            command[15] = "-f";
            command[16] = "hls";
            command[17] = hdDestM3u8Path;
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(command);
            Process p = pb.start();
            InputStream stderr = p.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            br = new BufferedReader(isr);
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null){
                sb.append(line);
                continue;
            }
            //获得视频的分辨率，默认分辨率1280x720
            solution = getVideoResolution(sb);
            duration = getVideoTime(sb);
            int exitVal = p.waitFor();
            p.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //通过输出流获得需要的信息
        //截取第一秒的图片做封面
        String name = catchImage(filePath, fileName, solution);
        String postPath = folder+"/"+fileName+"_postpath.jpg";
        if(!new File(postPath).exists()){
            postPath = folder + "/" +name;
        }
        jo.element("solution",solution);
        jo.element("postpath",postPath);
        jo.element("success",true);
        jo.element("duration",duration);
        jo.element("hdPath",hdResPath);
        jo.element("sdPath","");

        Date d2 = new Date();
        long d2l = d2.getTime();
        long diff = d2l - d1l;
       // log.debug("\r\n 视频转化转化结束，转化结果为：\r\n 文件路径:"+filePath+" \r\n 耗时: "+(diff/1000)+"秒 \r\n 返回信息:"+jo.toString());
        return jo;
    }

    /**
     * 对文件进行分片,默认处理方法：在目标文件目录下建立一个同名文件夹，然后将对应的分片数据放入文件夹内
     * @param filePath
     * @return
     */
    public static JSONObject callFFmpeg(String filePath){
        /***
         * 目标：两种分辨率格式，最终都为m3u8,时长固定,切换源，名字一样？
         * 1.将源文件转化为两个格式的flv文件，分别存储，
         * ID_HD-->id.flv-->id.m3u8
         * ID_SD-->id.flv-->id.m3u8
         * 2.将两个分辨率的flv转化为m3u8格式
         * 3.将两个m3u8格式的地址回传回去
         */
       // log.info("开始转化视频");
        filePath = filePath.replaceAll("\\\\", "/");
        JSONObject jo = new JSONObject();
//      String basePath = "";//FileUtil.getConfigRealPath();//tomcat 路径,例如：i:/tomcat1/resource/
        String folder = filePath.substring(0,filePath.lastIndexOf("/"));//文件的相对文件夹路径,例如：/attachment/video/
        String fileName = filePath.substring(filePath.lastIndexOf("/")+1,filePath.lastIndexOf("."));//文件的文件名,例如：sss.mp4
//      String sourcePath = basePath+filePath;//文件的绝对路径，源文件
        String hdDestFolder = folder+"/"+fileName+"_HD";//高清存放路径
        String hdDestFlvPath = hdDestFolder+"/"+fileName+".flv";
        String hdDestM3u8Path = hdDestFolder+"/"+fileName+".m3u8";
        String hdResPath = folder+"/"+fileName+"_HD"+"/"+fileName+".m3u8";
        String sdDestFolder = folder+"/"+fileName+"_SD";//普清存放路径
        String sdDestFlvPath = sdDestFolder+"/"+fileName+".flv";
        String sdDestM3u8Path = sdDestFolder+"/"+fileName+".m3u8";
        String sdResPath = folder+"/"+fileName+"_SD"+"/"+fileName+".m3u8";



        File destFolderFile = new File(hdDestFolder);
        if(!destFolderFile.isDirectory()){
            destFolderFile.mkdirs();
        }
        File destFolderFile2 = new File(sdDestFolder);
        if(!destFolderFile2.isDirectory()){
            destFolderFile2.mkdirs();
        }
        //1. 转化源文件为普清FLV格式

        BufferedReader br = null;
        String solution = "";
        Long duration = 0L;
        try {
            String[] command = new String[13];
            command[0] = "cmd.exe";
            command[1] = "/C";
            command[2] = rootPath+"/ffmpeg";
            command[3] = "-i";              //输入文件路径
            command[4] = filePath;
            command[5] = "-y";              //是否覆盖
//          command[6] = "-ab";             //音频数据流量，32 64 96 128
//          command[7] = "32K";
            command[6] = "-ar";             //声音采样频率 22050
            command[7] = "22050";
//          command[10] = "-acodec";            //音频编码AAC
//          command[11] = "aac";
            command[8] = "-s";              //指定分辨率，标清，高清；
            command[9] = "640x480";         //标清：480p
//          command[14] = "1280x720";       //高清：720p
//          command[14] = "-qscale";            //以数值质量为基础的VBR，范围：0.01-255，越小质量越好
//          command[15] = "10";
            command[10] = "-r";             //帧速率，数值： 15 29.97
            command[11] = "15";
            command[12] = sdDestFlvPath;//目标存储路径
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(command);
            Process p = pb.start();
            InputStream stderr = p.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            br = new BufferedReader(isr);
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null){
                sb.append(line);
                continue;
            }
            System.out.println(sb.toString());
            int exitVal = p.waitFor();
            solution = getVideoResolution(sb);
            duration = getVideoTime(sb);
            br.close();
            br = null;
            isr.close();
            p.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //2. 转化源文件为高清FLV格式
        //根据solution获得HD Solution,edit by lixun ,鉴于视频转化低分辨率后失真严重，高清不在转化其他分辨率。
//      String hdSolution = getHDSolution(solution);
        String hdSolution = solution;
        try {
            String[] command = new String[13];
            command[0] = "cmd.exe";
            command[1] = "/C";
            command[2] = rootPath+"/ffmpeg";
            command[3] = "-i";              //输入文件路径
            command[4] = filePath;
            command[5] = "-y";              //是否覆盖
//          command[6] = "-ab";             //音频数据流量，32 64 96 128
//          command[7] = "32K";
            command[6] = "-ar";             //声音采样频率 22050
            command[7] = "22050";
//          command[10] = "-acodec";            //音频编码AAC
//          command[11] = "aac";
            command[8] = "-s";              //指定分辨率，标清，高清；
//          command[13] = "640x480";            //标清：480p
//          command[7] = "1280x720";        //高清：720p
            command[9] = hdSolution;
//          command[15] = "-qscale";            //以数值质量为基础的VBR，范围：0.01-255，越小质量越好
//          command[16] = "10";
            command[10] = "-r";             //帧速率，数值： 15 29.97
            command[11] = "15";
            command[12] = hdDestFlvPath;                //目标存储路径
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(command);
            Process p = pb.start();
            InputStream stderr = p.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            br = new BufferedReader(isr);
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null){
                sb.append(line);
                continue;
            }
            System.out.println(sb.toString());
            int exitVal = p.waitFor();
            br.close();
            br = null;
            isr.close();
            p.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //3. 转化普清FLV为m3u8格式
        try {
            String[] command = new String[18];
            command[0] = "cmd.exe";
            command[1] = "/C";
            command[2] = rootPath+"/ffmpeg";
            command[3] = "-i";              //输入文件路径
            command[4] = sdDestFlvPath;
            command[5] = "-c:v";            //
            command[6] = "libx264";
            command[7] = "-c:a";
            command[8] = "aac";
            command[9] = "-strict";
            command[10] = "-2";
            command[11] = "-hls_time";
            command[12] = VIDEO_HLS_TIME+"";
            command[13] = "-hls_list_size";
            command[14] = "0";
            command[15] = "-f";
            command[16] = "hls";
            command[17] = sdDestM3u8Path;

            ProcessBuilder pb = new ProcessBuilder();
            pb.command(command);
            Process p = pb.start();
            InputStream stderr = p.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            br = new BufferedReader(isr);
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null){
                sb.append(line);
                continue;
            }
            System.out.println(sb.toString());
            int exitVal = p.waitFor();
            p.destroy();
            isr.close();
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        //4. 转化高清FLV为m3u8格式
        try {
            String[] command = new String[18];
            command[0] = "cmd.exe";
            command[1] = "/C";
            command[2] = rootPath+"/ffmpeg";
            command[3] = "-i";              //输入文件路径
            command[4] = hdDestFlvPath;
            command[5] = "-c:v";            //
            command[6] = "libx264";
            command[7] = "-c:a";
            command[8] = "aac";
            command[9] = "-strict";
            command[10] = "-2";
            command[11] = "-hls_time";
            command[12] = VIDEO_HLS_TIME+"";
            command[13] = "-hls_list_size";
            command[14] = "0";
            command[15] = "-f";
            command[16] = "hls";
            command[17] = hdDestM3u8Path;
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(command);
            Process p = pb.start();
            InputStream stderr = p.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            br = new BufferedReader(isr);
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null){
                sb.append(line);
                continue;
            }
            System.out.println(sb.toString());
            int exitVal = p.waitFor();
            p.destroy();
            isr.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            jo.element("success",false);
        }

        //通过输出流获得需要的信息
        //截取第一秒的图片做封面
        String name = catchImage(filePath, fileName, solution);
        String postPath = folder+"/"+fileName+"_postpath.jpg";
        if(!new File(postPath).exists()){
            postPath = folder + "/" +name;
        }
        jo.element("solution",solution);
        jo.element("postpath",postPath);
        jo.element("success",true);
        jo.element("duration",duration);
//        jo.element("destpath",resPath);
        jo.element("hdPath",hdResPath);
        jo.element("sdPath",sdResPath);
        System.out.println("视频转化转化结束，转化结果为：\r\n"+jo.toString());
        return jo;
    }

    /**
     * /转化源文件为原始分辨率的FLV格式
     * @param filePath
     */
    public static Boolean convertToOrgFlv(String filePath,String destFlvPath){
        Boolean result=false;
        //获取原始分辨率
        String solution = getOrgVideoSolution(filePath);
        //获取原始视频的时长
        long duration=getOrgVideoTime(filePath);
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        try {
            List<String> command = new ArrayList<String>();
            command.add("cmd.exe");
            command.add("/C");
            command.add(rootPath+"/ffmpeg");
            command.add("-i");              //输入文件路径
            command.add(filePath);
            command.add("-y");              //是否覆盖
//          command.add("-ab");             //音频数据流量，32 64 96 128
//          command.add("32K");
            command.add("-ar");             //声音采样频率 22050
            command.add("22050");
//          command.add("-acodec");            //音频编码AAC
//          command.add("aac");
            command.add("-s");              //指定分辨率，标清，高清；
//          command.add("640x480");            //标清：480p
//          command.add("1280x720");        //高清：720p
            command.add(solution);         //获取原始分辨率
//          command.add("-qscale");            //以数值质量为基础的VBR，范围：0.01-255，越小质量越好
//          command.add("10");
            command.add("-r");             //帧速率，数值： 15 29.97
            command.add("15");
            command.add(destFlvPath);                //目标存储路径
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(command);
            Process p = pb.start();
            InputStream stderr = p.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null){
                sb.append(line);
                continue;
            }
            System.out.println(sb.toString());
            int exitVal = p.waitFor();
            br.close();
            isr.close();
            p.destroy();
            long lastTime=getLastConvertTime(sb);
            if(lastTime==duration){
                result=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * /转化源文件为普清FLV格式（分辨率为640x480）
     * @param filePath
     */
    public static Boolean convertToSDFlv(String filePath,String sdDestFlvPath){
        Boolean result=false;
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        try {
            List<String> command = new ArrayList<String>();
            command.add("cmd.exe");
            command.add("/C");
            command.add(rootPath+"/ffmpeg");
            command.add("-i");              //输入文件路径
            command.add(filePath);
            command.add("-y");              //是否覆盖
            command.add("-report");         //开启日志
//          command.add("-ab");             //音频数据流量，32 64 96 128
//          command.add("32K");
            command.add("-ar");             //声音采样频率 22050
            command.add("22050");
//          command.add("-acodec");            //音频编码AAC
//          command.add("aac");
            command.add("-s");              //指定分辨率，标清，高清；
            command.add("640x480");         //标清：480p
//          command.add("1280x720");       //高清：720p
//          command.add("-qscale");            //以数值质量为基础的VBR，范围：0.01-255，越小质量越好
//          command.add("10");
            command.add("-r");             //帧速率，数值： 15 29.97
            command.add("15");
            command.add(sdDestFlvPath);//目标存储路径
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(command);
            Process p = pb.start();
            InputStream stderr = p.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null){
                sb.append(line);
                continue;
            }
            System.out.println(sb.toString());
            int exitVal = p.waitFor();
            br.close();
            isr.close();
            p.destroy();
            result=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  result;
    }


    /**
     * 转化源文件为高清FLV格式（分辨率为1280x720）
     * @param filePath
     * @param hdDestFlvPath
     * @return
     */
    public static Boolean convertToHDFlv(String filePath,String hdDestFlvPath){
        Boolean result=false;
        //根据solution获得HD Solution,edit by lixun ,鉴于视频转化低分辨率后失真严重，高清不在转化其他分辨率。
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        try {
            List<String> command = new ArrayList<String>();
            command.add("cmd.exe");
            command.add("/C");
            command.add(rootPath+"/ffmpeg");
            command.add("-i");              //输入文件路径
            command.add(filePath);
            command.add("-y");              //是否覆盖
            command.add("-report");         //开启日志
//          command.add("-ab");             //音频数据流量，32 64 96 128
//          command.add("32K");
            command.add("-ar");             //声音采样频率 22050
            command.add("22050");
//          command.add("-acodec");            //音频编码AAC
//          command.add("aac");
            command.add("-s");              //指定分辨率，标清，高清；
//          command.add("640x480");            //标清：480p
            command.add("1280x720");        //高清：720p
//            command.add(hdSolution);
//          command.add("-qscale");            //以数值质量为基础的VBR，范围：0.01-255，越小质量越好
//          command.add("10");
            command.add("-r");             //帧速率，数值： 15 29.97
            command.add("15");
            command.add(hdDestFlvPath);                //目标存储路径
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(command);
            Process p = pb.start();
            InputStream stderr = p.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null){
                sb.append(line);
                continue;
            }
            System.out.println(sb.toString());
            int exitVal = p.waitFor();
            br.close();
            isr.close();
            p.destroy();
            result=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 通过FFmpegManager
     * @param destFlvPath
     * @param destM3u8Path
     * @return
     */
    public static Boolean convertM3u8ByManager(String destFlvPath,String destM3u8Path,String fileName){
        List<String> command = new ArrayList<String>();
        command.add(rootPath+"/ffmpeg");
        command.add("-i");              //输入文件路径
        command.add(destFlvPath);
        command.add("-c:v");
        command.add("libx264");
        command.add("-c:a");
        command.add("aac");
        command.add("-strict");
        command.add("-2");
        command.add("-hls_time");
        command.add(VIDEO_HLS_TIME+""); //每个切片的时间
        command.add("-hls_list_size");
        command.add("0");
        command.add("-f");
        command.add("hls");
        command.add(destM3u8Path);
        TaskEntity info=manager.query(fileName);
        if(info!=null){
            manager.stop(fileName);
        }
        String id=manager.start(fileName,String.join(" ",command),true);
        if(StringUtils.isNotEmpty(id)){
            return  true;
        }else{
            return  false;
        }
    }

    /**
     * 通过FFmpegManager
     * @param destRtspPath
     * @param destRtmpPath
     * @return
     */
    public static Boolean rtsp2rtmp(String destRtspPath,String destRtmpPath,String fileName){
        //-i rtsp://admin:admin123@192.168.18.204:554/h264/ch1/main/av_stream -vcodec copy -acodec copy -f flv -y rtmp://47.106.164.158:1935/hls/12345678899
        List<String> command = new ArrayList<String>();
        command.add(rootPath+"/ffmpeg");
        command.add("-i");              //输入文件路径
        command.add(destRtspPath);
        command.add("-vcodec");
        command.add("copy");
        command.add("-acodec");
        command.add("copy");
        command.add("-f");
        command.add("flv");
        command.add("-y");
        command.add(destRtmpPath);
        TaskEntity info=manager.query(fileName);
        if(info!=null){
            manager.stop(fileName);
        }
        String id=manager.start(fileName,String.join(" ",command),true);
        if(StringUtils.isNotEmpty(id)){
            return  true;
        }else{
            return  false;
        }
    }

    /**
     *  转化原始分辨率FLV为m3u8格式
     * @param destFlvPath
     * @param destM3u8Path
     */
    public static StringBuffer convertToOrgm3u8(String destFlvPath,String destM3u8Path){
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        try {
            List<String> command = new ArrayList<String>();
            command.add("cmd.exe");
            command.add("/C");
            command.add(rootPath+"/ffmpeg");
            command.add("-i");              //输入文件路径
            command.add(destFlvPath);
//        command.add("-c:v");            //
//        command.add("libx264");
//        command.add("-c:a");
//        command.add("aac");
//        command.add("-strict");
//        command.add("-2");
            command.add("-c");
            command.add("copy");
            command.add("-hls_time");
            command.add(VIDEO_HLS_TIME+""); //每个切片的时间
            command.add("-hls_list_size");
            command.add("0");
            command.add("-f");
            command.add("hls");
            command.add(destM3u8Path);

            ProcessBuilder pb = new ProcessBuilder();
            pb.command(command);
            Process p = pb.start();
            InputStream stderr = p.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null){
                sb.append(line);
                continue;
            }
            System.out.println(sb.toString());
            int exitVal = p.waitFor();
            p.destroy();
            isr.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }

    /**
     *  转化普清FLV为m3u8格式
     * @param sdDestFlvPath
     * @param sdDestM3u8Path
     */
    public static StringBuffer convertToSDm3u8(String sdDestFlvPath,String sdDestM3u8Path){
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            List<String> command = new ArrayList<String>();
            command.add("cmd.exe");
            command.add("/C");
            command.add(rootPath+"/ffmpeg");
            command.add("-i");              //输入文件路径
            command.add(sdDestFlvPath);
            command.add("-c:v");            //
            command.add("libx264");
            command.add("-c:a");
            command.add("aac");
            command.add("-strict");
            command.add("-2");
            command.add("-hls_time");
            command.add(VIDEO_HLS_TIME+"");
            command.add("-hls_list_size");
            command.add("0");
            command.add("-f");
            command.add("hls");
            command.add(sdDestM3u8Path);

            ProcessBuilder pb = new ProcessBuilder();
            pb.command(command);
            Process p = pb.start();
            InputStream stderr = p.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null){
                sb.append(line);
                continue;
            }
            System.out.println(sb.toString());
            int exitVal = p.waitFor();
            p.destroy();
            isr.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }

    /**
     * 转化高清FLV为m3u8格式
     * @param hdDestFlvPath
     * @param hdDestM3u8Path
     */
    public static StringBuffer convertToHDm3u8(String hdDestFlvPath,String hdDestM3u8Path){
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            List<String> command = new ArrayList<String>();
            command.add("cmd.exe");
            command.add("/C");
            command.add(rootPath+"/ffmpeg");
            command.add("-i");              //输入文件路径
            command.add(hdDestFlvPath);
            command.add("-c:v");            //
            command.add("libx264");
            command.add("-c:a");
            command.add("aac");
            command.add("-strict");
            command.add("-2");
            command.add("-hls_time");
            command.add(VIDEO_HLS_TIME+"");
            command.add("-hls_list_size");
            command.add("0");
            command.add("-f");
            command.add("hls");
            command.add(hdDestM3u8Path);
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(command);
            Process p = pb.start();
            InputStream stderr = p.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null){
                sb.append(line);
                continue;
            }
            System.out.println(sb.toString());
            int exitVal = p.waitFor();
            p.destroy();
            isr.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }

    /**
     * 获取原始视频分辨率信息
     * @param filePath
     */
    public static String getOrgVideoSolution(String filePath) {
        filePath = filePath.replaceAll("\\\\", "/");
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        try {
            List<String> command = new ArrayList<String>();
            command.add("cmd.exe");
            command.add("/C");
            command.add(rootPath + "/ffmpeg");
            command.add("-i");              //输入文件路径
            command.add(filePath);
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(command);
            Process p = pb.start();
            InputStream stderr = p.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null){
                sb.append(line);
                continue;
            }
            System.out.println(sb.toString());
            int exitVal = p.waitFor();
            p.destroy();
            isr.close();
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return getVideoResolution(sb);
    }

    /**
     * 获取原始视频的时长
     * @param filePath
     * @return
     */
    public static long getOrgVideoTime(String filePath) {
        filePath = filePath.replaceAll("\\\\", "/");
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        try {
            List<String> command = new ArrayList<String>();
            command.add("cmd.exe");
            command.add("/C");
            command.add(rootPath + "/ffmpeg");
            command.add("-i");              //输入文件路径
            command.add(filePath);
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(command);
            Process p = pb.start();
            InputStream stderr = p.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null){
                sb.append(line);
                continue;
            }
            System.out.println(sb.toString());
            int exitVal = p.waitFor();
            p.destroy();
            isr.close();
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return getVideoTime(sb);
    }
    /**
     * 对文件进行分片,转成高清格式
     * 目标：三种种分辨率格式，最终都为m3u8,时长固定,切换源，名字一样？
     * 1.将源文件转化为两个格式的flv文件，分别存储，
     * ID_ORG-->id.flv-->id.m3u8  //原始分辨率
     * ID_HD-->id.flv-->id.m3u8
     * ID_SD-->id.flv-->id.m3u8
     * 2.将两个分辨率的flv转化为m3u8格式
     * 3.将两个m3u8格式的地址回传回去
     * @param filePath
     * @return
     */
    public static Map<String,Object> convertFile(String filePath,String filename){
        if(!checkfile(filePath)){
            logger.info(filePath + "文件不存在");
            return null;
        }
        //首先判断是否ffmpeg支持的格式,不是的话自动转化为avi格式先
        if((checkContentType(filePath)==1)){
            logger.info("ffmpeg不支持的格式，开始转化为avi格式先");
            filePath=processAVI(filePath);
        }
        logger.info("ffmpeg开始转化视频");
        filePath = filePath.replaceAll("\\\\", "/");
//      String basePath = "";//FileUtil.getConfigRealPath();//tomcat 路径,例如：i:/tomcat1/resource/
        String folder = filePath.substring(0,filePath.lastIndexOf("/"));//文件的相对文件夹路径,例如：/attachment/video/
        String fileName = filePath.substring(filePath.lastIndexOf("/")+1,filePath.lastIndexOf("."));//文件的文件名,例如：sss.mp4
//      String sourcePath = basePath+filePath;//文件的绝对路径，源文件
        String destFolder = folder;//原始分辨率存放路径
        String destM3u8Path = destFolder+"/"+fileName+".m3u8";

        File destFolderFile= new File(destFolder);
        if(!destFolderFile.isDirectory()){
            destFolderFile.mkdirs();
        }
        Boolean result=false;
        String solution="";
        long duration=0L;
        solution=getOrgVideoSolution(filePath);
        logger.info("获取原始视频的分辨率："+solution);
        //获取原始视频的时长
        duration=getOrgVideoTime(filePath);
        logger.info("获取原始视频的时长："+duration);
        //转化普清FLV为m3u8格式
        result=convertM3u8ByManager(filePath,destM3u8Path,filename);

        //通过输出流获得需要的信息
        //截取第一秒的图片做封面
        String name = catchImage(filePath, fileName, solution);
        String postPath = destFolder+"/"+fileName+".jpg";
        if(!new File(postPath).exists()){
            postPath = folder + "/" +name;
        }
        Map<String,Object> jo = new HashMap<String,Object>();
        jo.put("solution",solution);
        jo.put("postpath",postPath);
        jo.put("success",result);
        jo.put("duration",duration);
        String root= PropertiesUtil.getValue("uploadPath");
        jo.put("path",destM3u8Path.replaceFirst(root,""));
        logger.info("视频转化转化结束，转化结果为：\r\n"+jo.toString());
        return jo;
    }

    /**
     * 对文件进行分片,转成高清格式
     * @param httpPath 支持本地路径或者http路径
     * @param path 文件的相对路径
     * @param filePath 需要生成的m3u8的路径
     * @param filename
     * @return
     */
    public static Map<String,Object> convertFile(String httpPath,String path,String filePath,String filename){
        //首先判断是否ffmpeg支持的格式,不是的话自动转化为avi格式先
        if((checkContentType(httpPath)==1)){
            logger.info("ffmpeg不支持的格式，开始转化为avi格式先");
            httpPath=processAVI(httpPath,filePath);
        }
        logger.info("ffmpeg开始转化视频");
        filePath = filePath.replaceAll("\\\\", "/");
        String folder = filePath.substring(0,filePath.lastIndexOf("/"));//文件的相对文件夹路径,例如：/attachment/video/
        File destFolderFile= new File(folder);
        if(!destFolderFile.isDirectory()){
            destFolderFile.mkdirs();
        }
        Boolean result=false;
        String solution="";
        long duration=0L;
        solution=getOrgVideoSolution(httpPath);
        logger.info("获取原始视频的分辨率："+solution);
        //获取原始视频的时长
        duration=getOrgVideoTime(httpPath);
        logger.info("获取原始视频的时长："+duration);
        //转化普清FLV为m3u8格式
        result=convertM3u8ByManager(httpPath,filePath,filename);

        //通过输出流获得需要的信息
        //截取第一秒的图片做封面
        //图片的相对路径
        String name = catchImage(PropertiesUtil.getValue("uploadPath")+path, filename, solution);
        System.out.println("截图的路径==="+name);
        Map<String,Object> jo = new HashMap<String,Object>();
        jo.put("pictureUrl",name);
        jo.put("solution",solution);
        jo.put("success",result);
        jo.put("duration",duration);
        String root=PropertiesUtil.getValue("uploadPath");
        jo.put("path",filePath.replaceFirst(root,""));
        logger.info("视频转化转化结束，转化结果为：\r\n"+jo.toString());
        return jo;
    }

    /**
     *
     * @param httpPath
     * @param filePath 生成的MP4路径
     * @param filename
     * @return
     */
    public static Map<String,Object> convertFileToMp4(String httpPath,String filePath,String filename){
        //首先判断是否ffmpeg支持的格式,不是的话自动转化为avi格式先
        if((checkContentType(httpPath)==1)){
            logger.info("ffmpeg不支持的格式，开始转化为avi格式先");
            httpPath=processAVI(httpPath,filePath);
        }
        logger.info("ffmpeg开始转化视频");
        filePath = filePath.replaceAll("\\\\", "/");
        String folder = filePath.substring(0,filePath.lastIndexOf("/"));//文件的相对文件夹路径,例如：/attachment/video/
        File destFolderFile= new File(folder);
        if(!destFolderFile.isDirectory()){
            destFolderFile.mkdirs();
        }
        Boolean result=false;
        String solution="";
        long duration=0L;
        solution=getOrgVideoSolution(httpPath);
        logger.info("获取原始视频的分辨率："+solution);
        //获取原始视频的时长
        duration=getOrgVideoTime(httpPath);
        logger.info("获取原始视频的时长："+duration);
        //转化为mp4格式
        result=convertToMp4ByManager(httpPath,filePath,filename);

        //通过输出流获得需要的信息
        Map<String,Object> jo = new HashMap<String,Object>();
        jo.put("solution",solution);
        jo.put("success",result);
        jo.put("duration",duration);
        String root=PropertiesUtil.getValue("uploadPath");
        jo.put("path",filePath.replaceFirst(root,""));
        logger.info("视频转化转化结束，转化结果为：\r\n"+jo.toString());
        return jo;
    }

    /**
     * 读取最后转化的时间，来判断是否成功转化
     * @param sb
     */
    private static long getLastConvertTime(StringBuffer sb) {
        long time=0L;
        if(sb!=null&&sb.length()>0){
            //格式 time=00:01:41.14
            Pattern pat = Pattern.compile("time=(\\d{2}:\\d{2}:\\d{2}.\\d{2})");
            Matcher matcher = pat.matcher(sb.toString());
            String res="";
            while (matcher.find()){
                res = matcher.group(1);
            }
            //处理
            if(StringUtils.isNotEmpty(res)){
                //处理
                String[] arr = res.split("[.]");
                String t = "";
                String l = "0";
                if(arr.length > 0){
                    t = arr[0];
                    if(arr.length > 1)l = arr[1];
                }
                try {
                    long tempL = Long.parseLong(l);
                    tempL = tempL * (l.length() ==1 ? 100 : (l.length() == 2 ? 10 : 1));
                    String[] tempT = t.split(":");
                    long tempH = Long.parseLong(tempT[0]);
                    long tempM = Long.parseLong(tempT[1]);
                    long tempS = Long.parseLong(tempT[2]);
                    tempH = tempH * 60 * 60 * 1000;
                    tempM = tempM * 60 * 1000;
                    tempS = tempS * 1000;

                    return (tempH + tempM + tempS + tempL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return time;
    }

    /**
     能支持的格式
     ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
     不能支持的格式
     对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
     */
    private static int checkContentType(String filePath) {
        String type = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length()).toLowerCase();
        // ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
        if (type.equals("avi")) {
            return 0;
        } else if (type.equals("mpg")) {
            return 0;
        } else if (type.equals("wmv")) {
            return 0;
        } else if (type.equals("3gp")) {
            return 0;
        } else if (type.equals("mov")) {
            return 0;
        } else if (type.equals("mp4")) {
            return 0;
        } else if (type.equals("asf")) {
            return 0;
        } else if (type.equals("asx")) {
            return 0;
        } else if (type.equals("flv")) {
            return 0;
        }
        // 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
        // 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
        else if (type.equals("wmv9")) {
            return 1;
        } else if (type.equals("rm")) {
            return 1;
        } else if (type.equals("rmvb")) {
            return 1;
        }
        return 9;
    }

    // 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等), 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
    private static String processAVI(String filePath) {
        filePath = filePath.replaceAll("\\\\", "/");
//      String basePath = "";//FileUtil.getConfigRealPath();//tomcat 路径,例如：i:/tomcat1/resource/
        String folder = filePath.substring(0,filePath.lastIndexOf("/"));//文件的相对文件夹路径,例如：/attachment/video/
        String fileName = filePath.substring(filePath.lastIndexOf("/")+1,filePath.lastIndexOf("."));//文件的文件名,例如：sss.mp4
        String newFilePath=folder+"/"+fileName+".avi";
        List<String> commend = new ArrayList<String>();
        commend.add(mencoderPath);
        commend.add(filePath);
        commend.add("-oac");
        commend.add("lavc");
        commend.add("-lavcopts");
        commend.add("acodec=mp3:abitrate=64");
        commend.add("-ovc");
        commend.add("xvid");
        commend.add("-xvidencopts");
        commend.add("bitrate=600");
        commend.add("-of");
        commend.add("avi");
        commend.add("-o");
        commend.add(newFilePath);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            Process p=builder.start();
            doWaitFor(p);
            p.destroy();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        return newFilePath;
    }

    // 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等), 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
    private static String processAVI(String httpPath,String filePath) {
        if(FileUtils.downFileRequest(httpPath,filePath)){
            return processAVI(filePath);
        }
        return "";
    }
    /**
     * 检查文件是否存在
     * @param path
     * @return
     */
    private static boolean checkfile(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            return false;
        } else {
            return true;
        }
    }


    public static int doWaitFor(Process p) {
        InputStream in = null;
        InputStream err = null;
        int exitValue = -1; // returned to caller when p is finished
        try {
            in = p.getInputStream();
            err = p.getErrorStream();
            boolean finished = false; // Set to true when p is finished

            while (!finished) {
                try {
                    while (in.available() > 0) {
                        Character c = new Character((char) in.read());
                        System.out.print(c);
                    }
                    while (err.available() > 0) {
                        Character c = new Character((char) err.read());
                        System.out.print(c);
                    }

                    exitValue = p.exitValue();
                    finished = true;

                } catch (IllegalThreadStateException e) {
                    Thread.currentThread().sleep(500);
                }
            }
        } catch (Exception e) {
            System.err.println("doWaitFor();: unexpected exception - " + e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            if (err != null) {
                try {
                    err.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return exitValue;
    }

    /**
     *  在线视频
     * @param destFlvPath
     * @param destM3u8Path
     */
    public static StringBuffer convertOnlineTom3u8(String destFlvPath,String destM3u8Path){
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        try {
            List<String> command = new ArrayList<String>();
            command.add("cmd.exe");
            command.add("/C");
            command.add(rootPath+"/ffmpeg");
            command.add("-i");              //输入文件路径
            command.add(destFlvPath);
            command.add("-c:v");            //
            command.add("libx264");
            command.add("-c:a");
            command.add("aac");
            command.add("-strict");
            command.add("-2");
            command.add("-hls_time");
            command.add("10"); //每个切片的时间
            command.add("-hls_list_size");
            command.add("5");
//            command.add("-hls_wrap");
//            command.add("10");
            command.add("-f");
            command.add("flv");
            command.add(destM3u8Path);

            ProcessBuilder pb = new ProcessBuilder();
            pb.command(command);
            Process p = pb.start();
            InputStream stderr = p.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null){
                sb.append(line);
                continue;
            }
            System.out.println(sb.toString());
            int exitVal = p.waitFor();
//            doWaitFor(p);
            p.destroy();
            isr.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }

    /**
     * 在线直播视频
     * @param destFlvPath
     * @param destM3u8Path
     * @return
     */
    public static void convertLive(String destFlvPath,String destM3u8Path,String id){
        BufferedReader br = null;
        List<String> command = new ArrayList<String>();
        command.add(rootPath+"/ffmpeg");
        command.add("-i");              //输入文件路径
        command.add(destFlvPath);
        command.add("-c:v");            //
        command.add("libx264");
        command.add("-c:a");
        command.add("aac");
        command.add("-strict");
        command.add("-2");
//        command.add("-hls_time");
//        command.add("10"); //每个切片的时间
//        command.add("-hls_list_size");
//        command.add("5");
//            command.add("-hls_wrap");
//            command.add("10");
        command.add("-f");
        command.add("flv");
        command.add(destM3u8Path);
        System.out.println(String.join(" ",command));
        //执行任务，id就是appName，如果执行失败返回为null
        //通过id查询
        TaskEntity info=manager.query(id);
        if(info!=null){
            manager.stop(id);
        }
        manager.start(id,String.join(" ",command),true);
    }


    /**
     * 开启在线直播
     * @param destFlvPath 源播放地址，m3u8地址，比如：http://sxws.chinashadt.com:1936/live/tv5.stream_360p/playlist.m3u8
     * @param id 网络直播资源表id，主键id
     * @param ip 流媒体ip
     * @param port 流媒体端口
     * @return 返回播放路径
     */
    public static String openLive(String destFlvPath,String id,String ip, String port){
        id=id.replaceAll("-","");
        if(StringUtils.isNotEmpty(id)){
            String path="rtmp://"+ip+":"+port+"/hls/"+id.replaceAll("-","");
            convertLive(destFlvPath,path,id);
            return path;
        }
        return "";
    }
    /**
     * 开启在线摄像头
     * @param destFlvPath 源播放地址，m3u8地址，比如：http://sxws.chinashadt.com:1936/live/tv5.stream_360p/playlist.m3u8
     * @param id 网络直播资源表id，主键id
     * @param ip 流媒体ip
     * @param port 流媒体端口
     * @return 返回播放路径
     */
    public static String startLive(String destFlvPath,String id,String ip, String port){
        id=id.replaceAll("-","");
        if(StringUtils.isNotEmpty(id)){
            String path="rtmp://"+ip+":"+port+"/hls/"+id.replaceAll("-","");
            rtsp2rtmp(destFlvPath,path,id);
            return path;
        }
        return "";
    }

    /**
     * 关闭直播
     * @param id 主键id
     */
    public static void closeLive(String id){
        id=id.replaceAll("-","");
        //通过id查询
        TaskEntity info=manager.query(id);
        if(info!=null){
            System.out.println(info);
            manager.stop(id);
        }
    }

    /**
     * 根据id获取直播状态，true表示正在直播中，false表示关闭中
     * @param id
     * @return 返回true或者false
     */
    public static Boolean getLiveStatus(String id){
        id=id.replaceAll("-","");
        //通过id查询
        TaskEntity info=manager.query(id);
        if(info!=null){
            return  true;
        }
        return  false;
    }

    /**
     * 关闭进程
     * @param id 主键id
     */
    public static void closeProcess(String id){
        //通过id查询
        TaskEntity info=manager.query(id);
        if(info!=null){
            System.out.println(info);
            manager.stop(id);
        }
    }



    public static void main(String[] args) {
        String file = "D:/zhjs/test/test.avi";
        //convertFile(file,"D:/zhjs/aa/aa1.m3u8","aa1");
        file = "http://sxws.chinashadt.com:1936/live/tv5.stream_360p/playlist.m3u8";
        //直播
        //System.out.println(openLive(file, "12345","192.168.1.122","1935"));
//        String path="http://localhost:8080/uploadfile/video/20181119/C5347ED6261941F0A87175866143F457.avi";
//        System.out.println(convertFileToMp4(file,"d:/zhjs","dd"));
        String f="F:\\ls_down\\视频\\1.avi";
        String f2="F:\\ls_down\\视频\\";
       convertM3u8ByManager(f,f2,"11.m3u8");

    }

}
