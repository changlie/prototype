package com.yunjia.common.controller.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yunjia.common.controller.base.ControllerSupport;
import com.yunjia.common.controller.base.ResponseBean;
import com.yunjia.system.entity.User;
import com.yunjia.system.service.SystemService;
import com.yunjia.system.service.UserService;

@RestController
public class LoginController extends ControllerSupport{
	@Autowired
	private SystemService systemService;
	@Autowired
	private UserService userService;

	@RequestMapping("/ajaxLogin")
	public ResponseBean ajaxLogin(String username, String password) throws Exception {
		Subject subject = SecurityUtils.getSubject();
		
		if(subject.isAuthenticated()) {//若已登录。
			return systemService.getMenuUserInfo();
		}
		
		//执行登录 操作
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		subject.login(token);
		
		User user = userService.findByUsername(username);
		userService.saveUserToSession(user);
		
		//登录成功，则返回系统菜单
		return systemService.getMenuUserInfo();
	}
	
	
	@RequestMapping("/loginInfo")
	public ResponseBean menuInfo() throws Exception {
		return systemService.getMenuUserInfo();
	}
	
	@RequestMapping("/isLogin")
	public ResponseBean isLogin() throws Exception {
		if(userService.isLogin()) {
			return success();
		}
		return failure();
	}
	
	@RequestMapping("/ajaxLogout")
	public ResponseBean ajaxLogout() throws Exception {
		SecurityUtils.getSubject().logout();
		return success();
	}
	
	@RequestMapping("/hintLogin")
	public ResponseBean toHintLogin() throws Exception {
		return hintLogin();
	}
	
}
