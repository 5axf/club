package com.sky.car.business.api.admin;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.sky.car.common.Result;
import com.sky.car.common.base.BaseController;
import com.sky.car.config.AppConfig;
import com.sky.car.util.FileUploadHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/admin/upload/")
@Api(tags = { "后台管理-文件上传接口" })
@CrossOrigin
public class UploadController extends BaseController {

	@Autowired
	AppConfig appConfig;

	@PostMapping("fileUpload1")
	@ApiOperation(value = "单文件上传1", notes = "", response = Result.class)
	public Result fileUpload1(@ApiParam(value = "上传的文件") @RequestParam("file") CommonsMultipartFile file) {
		File rFile = FileUploadHelper.uploadFile(file);
		if (rFile == null) {
			return Result.failResult();
		}
		Map<String, Object> map = new HashMap<>();
		map.put("fileName", rFile.getName());
		map.put("size", rFile.length());
		map.put("path", "");
		return Result.successResult(map);
	}
	
	@PostMapping("fileUpload2")
	@ApiOperation(value = "单文件上传2", notes = "", response = Result.class)
	public Result fileUpload2(@ApiParam(value = "上传的文件") CommonsMultipartFile file) {
		File rFile = FileUploadHelper.uploadFile(file);
		if (rFile == null) {
			return Result.failResult();
		}
		Map<String, Object> map = new HashMap<>();
		map.put("fileName", rFile.getName());
		map.put("size", rFile.length());
		map.put("path", "");
		return Result.successResult(map);
	}

	@PostMapping("fileUpload3")
	@ApiOperation(value = "单文件上传3", notes = "", response = Result.class)
	public Result fileUpload3(@ApiParam(value = "上传的文件") @RequestParam("file") MultipartFile file) {
		File rFile = FileUploadHelper.uploadFile(file);
		if (rFile == null) {
			return Result.failResult();
		}
		Map<String, Object> map = new HashMap<>();
		map.put("fileName", rFile.getName());
		map.put("size", rFile.length());
		map.put("path", "");
		return Result.successResult(map);
	}
	
	@PostMapping("fileUpload4")
	@ApiOperation(value = "单文件上传4", notes = "", response = Result.class)
	public Result fileUpload4(@ApiParam(value = "上传的文件") MultipartFile file){
		File rFile = FileUploadHelper.uploadFile(file);
		if (rFile == null) {
			return Result.failResult();
		}
		Map<String, Object> map = new HashMap<>();
		map.put("fileName", rFile.getName());
		map.put("size", rFile.length());
		map.put("path", "");
		return Result.successResult(map);
	}

	@PostMapping("fileUploadList")
	@ApiOperation(value = "多文件上传", notes = "", response = Result.class)
	public Result fileUploadList(HttpServletRequest request) {
		List<File> list = FileUploadHelper.uploadFileList(request);
		return Result.successResult(list);
	}

//	@PostMapping("fileUpload5")
//	@ApiOperation(value = "文件上传", notes = "", response = Result.class)
//	public Result fileUpload5(HttpServletRequest request, HttpServletResponse response)throws IOException{
//		System.out.println("进入get方法！--------------------------------------------------------------》》》》");
//		MultipartHttpServletRequest req =(MultipartHttpServletRequest)request;
//		MultipartFile multipartFile =  req.getFile("file");
//		String extName = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().indexOf(".")+1);
//		String fileName=new Date().getTime()+"."+extName;
//		String realPath = appConfig.uploadPath;
//		try {
//			File dir = new File(realPath);
//			if (!dir.exists()) {
//				dir.mkdir();
//			}
//			File file  =  new File(realPath,fileName);
//			multipartFile.transferTo(file);
//		} catch (IOException e) {
//			e.printStackTrace();
//			return Result.failResult();
//		} catch (IllegalStateException e) {
//			return Result.failResult();
//		}
//		return Result.successResult();
//	}

}
