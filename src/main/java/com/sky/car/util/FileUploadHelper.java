package com.sky.car.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.sky.car.config.AppConfig;

@Component
public class FileUploadHelper {
	
	protected static Log log = LogFactory.getLog(FileUploadHelper.class);
	
	@Autowired AppConfig appConfig;
	
	private static AppConfig myAppConfig;
	
	@PostConstruct
	public void initConfig() {
		myAppConfig = appConfig;
	}
	
	public static File uploadFile(CommonsMultipartFile file){
        long  startTime=System.currentTimeMillis();
        String upload_path = myAppConfig.uploadPath;
        String allowExtNames = myAppConfig.allowExtNames;
        String extName = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".")+1);
        String path=upload_path+new Date().getTime()+"."+extName;
        File rFile = null;
    	if(checkFilePath(file.getOriginalFilename(), allowExtNames)){
    		
    		//checkMkPath(getRootPath()+upload_path);//文件有效就检查目录
    		//rFile = new File(getRootPath()+path);
    		rFile = new File(path);
            try {
				file.transferTo(rFile);
			} catch (Exception e) {
				log.error(e);
			}
            long  endTime=System.currentTimeMillis();
            System.out.println("文件上传的运行时间："+(endTime-startTime)+"ms");
    	}
    	return rFile;
    }

	public static File uploadFile(MultipartFile file){
        long  startTime=System.currentTimeMillis();
        String upload_path = myAppConfig.uploadPath;
        String allowExtNames = myAppConfig.allowExtNames;
        String extName = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".")+1);
		System.out.println("文件扩展名===》："+extName);
		String path=upload_path+new Date().getTime()+"."+extName;
        File rFile = null;

    	if(checkFilePath(file.getOriginalFilename(), allowExtNames)){
    		
    		//checkMkPath(getRootPath()+upload_path);//文件有效就检查目录
    		//rFile = new File(getRootPath()+path);
			rFile = new File(path);
            try {
				file.transferTo(rFile);
			} catch (Exception e) {
				log.error(e);
			}
            long  endTime=System.currentTimeMillis();
			System.out.println("文件上传的运行时间："+(endTime-startTime)+"ms");
		}
		System.out.println("未执行上传方法");
    	return rFile;
    }
	    
	public static List<File> uploadFileList(HttpServletRequest request) {
		List<File> fileList = new ArrayList<File>();
		long startTime = System.currentTimeMillis();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			Iterator<String> iter = multiRequest.getFileNames();
			String upload_path = myAppConfig.uploadPath;
			String allowExtNames = myAppConfig.allowExtNames;
			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile(iter.next()
						.toString());
				if (file != null) {
					String extName = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".")+1);
					//String path = upload_path +new Date().getTime()+ file.getOriginalFilename();
					String path = upload_path +new Date().getTime()+ "."+extName;
					if (checkFilePath(file.getOriginalFilename(),allowExtNames)) {
						
						//checkMkPath(getRootPath()+upload_path);//文件有效就检查目录
						//File newFile = new File(getRootPath()+path);
						
						File newFile = new File(path);
						try {
							file.transferTo(newFile);
							fileList.add(newFile);
						} catch (Exception e) {
							log.error(e);
						}
					}
				}
			}
		}
		long endTime = System.currentTimeMillis();
		System.out.println("多文件上传运行时间：" + (endTime - startTime) + "ms");
		return fileList;
	}
    
    // 生成目录
    public static void checkMkPath(String path){
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
			try {
				Runtime.getRuntime().exec("chmod -R 777 " + path);
				// p.waitFor();
			} catch (Exception ex) {
				log.error(ex);
			}
		}
    }
    
	 public static boolean checkFilePath(String fileUrlPath, String allowExtNames) {
		 System.out.println("fileUrlPath===》："+fileUrlPath);
		 System.out.println("allowExtNames===》："+allowExtNames);
		 System.out.println("bbbbbbbbbb===》："+fileUrlPath.length());
//		 if(fileUrlPath.lastIndexOf(".")+1 < fileUrlPath.length()){
			String extName = fileUrlPath.substring(fileUrlPath.lastIndexOf(".")+1);
			if (((Utils.isEmpty(allowExtNames)) || (allowExtNames
					.indexOf(extName.toLowerCase()) > -1))
					&& (!Utils.isEmpty(extName))
					&& (!Utils.isEmpty(extName.toLowerCase()))) {
				return true;
			}
//		 }
		 return false;
	 }
	 
	 public static String getRootPath(){
		 String rootPath = "";
		 try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes()).getRequest();
			 rootPath=request.getSession().getServletContext().getRealPath("/");
	  	  	if (rootPath.lastIndexOf("/") != rootPath.length() - 1){
			    rootPath = rootPath + "/";
	  	  	}
		} catch (Exception e) {
			log.error(e);
		}
		 return "";
  	  	//return rootPath; 
	 }

}
