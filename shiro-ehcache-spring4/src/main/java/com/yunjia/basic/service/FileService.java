package com.yunjia.basic.service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.yunjia.basic.bean.FileInfo;
import com.yunjia.basic.dao.FileDao;
import com.yunjia.util.DateUtil;
import com.yunjia.util.StrUtil;

@Service
public class FileService {
	@Value("${fileUrl}")
	private String fileUrl;
	@Value("${fileDiskDir}")
	private String fileDiskDir;
	@Autowired
	private FileDao fileDao;
	
	/**
	 * 保存上传图片
	 * @param originfile
	 * @return
	 * @throws Exception
	 */
	public FileInfo saveFile(MultipartFile originfile) throws Exception {
		String originfileName = originfile.getOriginalFilename();
		StringBuilder diskPath = new StringBuilder(50);
		//拼接根路径
		diskPath.append("/").append(DateUtil.formatSlantingDate(new Date())).append("/");
		
		File dir = new File(fileDiskDir+diskPath);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		//拼接文件名
		diskPath.append(UUID.randomUUID().toString().replace("-", ""))
				.append(originfileName.substring(originfileName.indexOf(".")));
		
		File destFile = new File(fileDiskDir+diskPath.toString());
		originfile.transferTo(destFile);
		
		long id = fileDao.saveFileInfo(originfileName, diskPath.toString());
		
		FileInfo fileInfo = new FileInfo();
		fileInfo.setId(id);
		fileInfo.setName(originfileName);
		fileInfo.setPath(fileUrl+diskPath);
		return fileInfo;
	}
	
	/**
	 * 获取多个文件信息
	 * @param fileIds e.g. "1,2,11,8"
	 * @return
	 */
	public List<FileInfo> getFiles(String fileIds) {
		return fileDao.getFileInfos(fileIds);
	}
	
	/**
	 * 获取单个文件信息
	 * @param fileId
	 * @return
	 */
	public FileInfo getFileInfo(Integer fileId) {
		return fileDao.getFileInfo(fileId);
	}
	
	/**
	 * 删除多个上传文件
	 * @param fileIds
	 */
	public void deleteFiles(String fileIds) {
		List<Integer> ids = StrUtil.getIntegerList(fileIds);
		for (Integer id : ids) {
			deleteFile(id);
		}
	}

	/**
	 * 删除单个上传文件
	 * @param fileId
	 */
	public void deleteFile(Integer fileId) {
		FileInfo fileInfo = fileDao.getFileInfo(fileId);
		File file = new File(fileDiskDir+fileInfo.getPath());
		if(file.exists()) {
			file.delete();
		}
		
		fileDao.deleteFileInfo(fileId);
	}
	
}
