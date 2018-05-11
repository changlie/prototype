package com.yunjia.basic.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.yunjia.basic.dao.CommonDao;
import com.yunjia.system.service.OrganizationService;

@Service
public class OptionsService{
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private OrganizationService organizationService;
	
	public List<Map<String, Object>> getOptions(String dataId){
		List<Map<String, Object>> ret = null;
		Assert.hasLength(dataId, "dataId 不能为空 ， 而不能为空格！");
		
		if("departments".equals(dataId)) {
			ret = organizationService.getCascadelist();
			return ret;
		}
		
		if(existOptionsSql(dataId)) {
			String optionsSql = getOptionsSql(dataId);
			ret = commonDao.getOptionsBySql(optionsSql);
		}else {
			ret = commonDao.getOptions(dataId);
		}
		return ret;
	}
	
	
	private boolean existOptionsSql(String dataId) {
		return commonDao.existSql(dataId, "options");
	}
	private String getOptionsSql(String dataId) {
		return commonDao.getSql(dataId, "options");
	}

	
}
