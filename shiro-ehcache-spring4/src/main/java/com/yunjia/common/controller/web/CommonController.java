package com.yunjia.common.controller.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.yunjia.basic.bean.PageBean;
import com.yunjia.basic.service.CommonService;
import com.yunjia.basic.service.OptionsService;
import com.yunjia.common.controller.base.ControllerSupport;
import com.yunjia.common.controller.base.ResponseBean;
import com.yunjia.util.LogUtil;

@RestController
@RequestMapping("/common")
public class CommonController extends ControllerSupport{
	@Autowired
	private CommonService commonService;
	@Autowired
	private OptionsService optionsService;
	
	@RequestMapping("/labels")
	public ResponseBean labels(String type) throws Exception {
		List<Map<String, Object>> labels = commonService.getLabels(type);
		return success(labels);
	}
	
	@RequestMapping("/metaInfo")
	public ResponseBean metaInfo(String type) throws Exception {
		JSONObject metaInfo = commonService.getMetaInfo(type);
		return success(metaInfo);
	}
	
	
	@RequestMapping("/optionSet")
	public ResponseBean queryOptions(String dataIds) throws Exception {
		String[] keys = dataIds.split(",");
		Map<String, Object> optionSet = new HashMap<String, Object>();
		for (String key : keys) {
			List<Map<String, Object>> options = optionsService.getOptions(key);
			optionSet.put(key, options);
		}
		return success(optionSet);
	}
	
	
	
	/*******  TODO 公共的增， 删， 改，查接口   **********/
	@RequestMapping("/query")
	public ResponseBean query(String type, String conditions, String ranks, int curPage, int pageSize) throws Exception {
		long start = System.currentTimeMillis();
		PageBean<Map<String, Object>> pageBean = commonService.query(type, conditions, ranks, curPage, pageSize);
		LogUtil.i("query "+type+" spend: " + (System.currentTimeMillis() - start) + "ms");
		return success().put("total", pageBean.getCount()).put("rows", pageBean.getList());
	}

	@RequestMapping("/add")
	public ResponseBean add(String type, String obj) throws Exception {
		long start = System.currentTimeMillis();
		commonService.add(type, obj);
		LogUtil.i("add "+type+" spend: " + (System.currentTimeMillis() - start) + "ms");
		return success();
	}
	
	@RequestMapping("/update")
	public ResponseBean update(String type, String obj, String key) throws Exception {
		long start = System.currentTimeMillis();
		commonService.update(type, obj, key);
		LogUtil.i("update "+type+" spend: " + (System.currentTimeMillis() - start) + "ms");
		return success();
	}

	@RequestMapping("/delete")
	public ResponseBean delete(String ids, String type, String key) throws Exception {
		long start = System.currentTimeMillis();
		LogUtil.i("delete=> "+type+" -- "+key+" -- "+ids);
		commonService.delete(type, key, ids);
		LogUtil.i("delete "+type+" spend: " + (System.currentTimeMillis() - start) + "ms");
		return success();
	}
	
	

}
