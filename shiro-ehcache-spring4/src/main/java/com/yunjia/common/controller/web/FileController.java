package com.yunjia.common.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.yunjia.basic.bean.FileInfo;
import com.yunjia.basic.service.FileService;
import com.yunjia.common.controller.base.ControllerSupport;
import com.yunjia.common.controller.base.ResponseBean;

@RestController
@RequestMapping("/file")
public class FileController extends ControllerSupport{
	@Autowired
	private FileService fileService;
	
	@RequestMapping("/upload")
	public ResponseBean upload(MultipartFile file) throws Exception {
		FileInfo fileInfo = fileService.saveFile(file);
		return success(fileInfo);
	}
	
}
