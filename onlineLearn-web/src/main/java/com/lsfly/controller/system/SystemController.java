package com.lsfly.controller.system;


import com.lsfly.exception.InvalidParamException;
import com.lsfly.sys.Result;
import com.lsfly.util.DateUtils;
import com.lsfly.util.FileUtils;
import com.lsfly.util.ReadProperties;
import com.lsfly.util.ToolUtil;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;


/**
 * Created by huoquan on 18-8-24 下午6:13
 * system的系统共有方法，其他人请勿修改
 */

@Controller
@RequestMapping(value = "system")
public class SystemController extends BaseController{
    public static final Logger logger = LoggerFactory.getLogger(SystemController.class);



    /**
     * 公用的上传方法
     * @param request
     * @param file
     * @param module
     * @param type 空默认是照片，1：视频，2：音频
     * @return
     */
    @RequestMapping(value = "/uploadFile.do", method = RequestMethod.POST)
    @ResponseBody
    public Result uploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile file,String module,String type) {
        Result result = new Result();
        String fileName = file.getOriginalFilename(); //文件名称
        long size=file.getSize();
        String ext= FileUtils.getFileExtension(fileName); //获取后缀
        if (ToolUtil.isEmpty(module)){
            module="system";
        }
        String path= "/"+module+"/"+ DateUtils.dateToString(new Date(),"yyyyMMdd")+"/"
                + ToolUtil.getUUID().replaceAll("-","")+"."+ext;
        File uploadFile=null;
        try {
            uploadFile=new File(ReadProperties.getValue("uploadPath")+path);
            if(FileUtils.createFile(ReadProperties.getValue("uploadPath")+path)){
                file.transferTo(uploadFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidParamException("上传异常");  //抛出异常
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("fileName",fileName);
        map.put("path",path);
        map.put("size",size);
        map.put("ext",ext);
        if(ToolUtil.isNotEmpty(type)&&("1".equals(type)||"2".equals(type))){
                ///视频或者音频
            String duration="";//视频或者音频时长
            Encoder encoder = new Encoder();
            try {
                MultimediaInfo multimediaInfo = encoder.getInfo(uploadFile);//获取视频或者音频异常
                long ls=multimediaInfo.getDuration()/1000;
                map.put("lstime",ls);//秒数
                int hour = (int) (ls/3600);
                int minute = (int) (ls%3600)/60;
                int second = (int) (ls-hour*3600-minute*60);
                duration = hour+"时"+minute+"分"+second+"秒";
            }catch (Exception e){
                e.printStackTrace();
                throw new InvalidParamException("解析视频/音频异常");  //抛出异常
            }
            map.put("time",duration);
            if (type.equals("1")){
                map.put("uid",ToolUtil.getUUID());
            }
        }
        result.setBizData(map);
        return result;
    }


    /**
     * 分片上传
     * @param request
     * @param file
     * @param module
     * @param type
     * @param chunks 分块数
     * @param chunk 当前分块序号
     * @param temFileName 临时文件的名称
     * @return
     */
    @RequestMapping(value = "/uploadFileByChunks.do", method = RequestMethod.POST)
    @ResponseBody
    public Result uploadFileByChunks(HttpServletRequest request,
                                    @RequestParam("file") MultipartFile file,
                                    String module,
                                    String type,
                                    Integer chunks,
                                    Integer chunk,
                                    String temFileName) {
        Result result = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        String uploadDesc= ReadProperties.getValue("uploadPath");
        String fileName = file.getOriginalFilename(); //文件名称
        long size=file.getSize();
        String ext=FileUtils.getFileExtension(fileName); //获取后缀
        if (ToolUtil.isEmpty(module)){
            module="system";
        }
        //判断文件是否分块
        if (chunk == null){
            chunk=0;
        }
        if (chunks == null){
            chunks=1;
        }
        // 将文件分块保存到临时文件夹里，便于之后的合并文件
        String savePath= "/"+module+"/" +temFileName+"/";
        String fileFullName= chunk+"";
        try {
            // 将文件分块保存到临时文件夹里，便于之后的合并文件
            FileUtils.saveFile(uploadDesc+savePath,  fileFullName, file);
        } catch (Exception e) {
            logger.error("上传异常",e);
            throw new InvalidParamException("上传异常");  //抛出异常
        }
        map.put("path",savePath+fileFullName);
        map.put("fileName",fileName);
        map.put("size",size);
        map.put("ext",ext);
        result.setBizData(map);
        return result;
    }

    /**
     *  合并分块操作
     * @param request
     * @param module 文件所在的模块
     * @param temFileName 文件临时名称
     * @param fileName 文件名称，包括后缀
     * @param fileSize 文件体积（字节）
     * @param type 空默认是照片，1：视频，2：音频
     * @return
     */
    @RequestMapping(value = "/mergeChunks.do", method = RequestMethod.POST)
    @ResponseBody
    public Result mergeChunks(HttpServletRequest request,
                              String module,
                              String temFileName,
                              String fileName,
                              String fileSize,
                              String type){
        Result result = new Result();
        String ext=FileUtils.getFileExtension(fileName); //获取后缀
        String uploadDesc= ReadProperties.getValue("uploadPath");
        String savePath= "/"+module+"/" +temFileName;
        //读取目录里的所有文件
        File f = new File(uploadDesc+savePath);
        File[] fileArray = f.listFiles(new FileFilter(){
            //排除目录只要文件
             @Override
             public boolean accept(File pathname) {
                 // TODO Auto-generated method stub
                 if(pathname.isDirectory()){
                     return false;
                 }
                 return true;
             }
        });
        if(fileArray==null||fileArray.length==0){
            throw new InvalidParamException("上传失败，分块文件为空");  //抛出异常
        }
        //转成集合，便于排序
        List<File> fileList = new ArrayList<File>(Arrays.asList(fileArray));
        Collections.sort(fileList,new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                // TODO Auto-generated method stub
                if(Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName())){
                    return -1;
                }
                return 1;
            }
        });
        //不分块的话，直接保存
        String path= "/"+module+"/"+ DateUtils.dateToString(new Date(),"yyyyMMdd")+"/";
        String fileFullName=  ToolUtil.getUUID().replaceAll("-","")+"."+ext;
        File outputFile = new File(uploadDesc+path+fileFullName);
        try{
            FileUtils.createFile(uploadDesc+path+fileFullName);
            //创建文件
            outputFile.createNewFile();
            //输出流
            FileChannel outChnnel = new FileOutputStream(outputFile).getChannel();
            //合并
            FileChannel inChannel;
            for(File file : fileList){
                inChannel = new FileInputStream(file).getChannel();
                inChannel.transferTo(0, inChannel.size(), outChnnel);
                inChannel.close();
            }
            outChnnel.close();
            // 删除临时目录中的分片文件
            FileUtils.deleteDirectory(uploadDesc+savePath);
            logger.info("大文件上传合并成功");
        }catch(Exception e){
            logger.error("上传异常",e);
            throw new InvalidParamException("上传异常");  //抛出异常
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if(ToolUtil.isNotEmpty(type)&&("1".equals(type)||"2".equals(type))){
            ///视频或者音频
            String duration="";//视频或者音频时长
            Encoder encoder = new Encoder();
            try {
                MultimediaInfo multimediaInfo = encoder.getInfo(new File(uploadDesc+path+fileFullName));//获取视频或者音频异常
                long ls=multimediaInfo.getDuration()/1000;
                int hour = (int) (ls/3600);
                int minute = (int) (ls%3600)/60;
                int second = (int) (ls-hour*3600-minute*60);
                duration = hour+"时"+minute+"分"+second+"秒";
            }catch (Exception e){
                e.printStackTrace();
                throw new InvalidParamException("解析视频/音频异常");  //抛出异常
            }
            map.put("time",duration);
        }
        map.put("path",path+fileFullName);
        map.put("fileName",fileName);
        map.put("size",fileSize);
        map.put("ext",ext);
        result.setBizData(map);
        return result;
    }


    /**
     * 上传视频并且解码视频
     * @param request
     * @param file
     * @param module
     * @return
     */
    @RequestMapping(value = "/convertFile.do", method = RequestMethod.POST)
    @ResponseBody
    public Result convertFile(HttpServletRequest request, @RequestParam("file") MultipartFile file, String ip,String module) {
        Result result = new Result();
        String fileName = file.getOriginalFilename(); //文件名称
        long size=file.getSize();
        String ext=FileUtils.getFileExtension(fileName); //获取后缀
        if (ToolUtil.isEmpty(module)){
            module="system";
        }
        String path= "/"+module+"/"+ DateUtils.dateToString(new Date(),"yyyyMMdd")+"/"
                + ToolUtil.getUUID().replaceAll("-","")+"."+ext;
        File uploadFile=null;
        String descFileName=ReadProperties.getValue("uploadPath")+path;
        try {
            uploadFile=new File(descFileName);
            if(FileUtils.createFile(descFileName)){
                file.transferTo(uploadFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidParamException("上传异常");  //抛出异常
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("fileName",fileName);
        map.put("size",size);
        map.put("ext",ext);
        if(uploadFile!=null){
            //调用远程文件的转码方法
            /*String dataPath=MediaApiUtil.convertFile(ip,path);
            map.put("path",dataPath);*/
        }
        result.setBizData(map);
        return result;
    }
}
