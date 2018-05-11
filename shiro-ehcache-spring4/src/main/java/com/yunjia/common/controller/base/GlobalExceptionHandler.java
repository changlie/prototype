package com.yunjia.common.controller.base;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;

import com.yunjia.exception.DataAccessException;
import com.yunjia.exception.LoginTimeOutException;
import com.yunjia.exception.UpdateException;
/**
 * 全局异常处理
 * @author changlie
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ControllerSupport{
	
	private boolean isFromApp(NativeWebRequest request) {
		String contextPath = request.getContextPath();
		HttpServletRequest req = request.getNativeRequest(HttpServletRequest.class);
		String uri = req.getRequestURI();
		return uri.startsWith(contextPath+"/api");
	}
	private Object afterExceptionReturn(NativeWebRequest request, String errorMsg) {
		if(isFromApp(request)) {
			
			Object newtoken = request.getAttribute("newtoken", RequestAttributes.SCOPE_REQUEST);
			Result result = new Result();
			result.setNewtoken(newtoken+"");
			
			return result.failure(errorMsg);
		}
		return new ResponseBean().failure(errorMsg);
	}
	
	
	@ExceptionHandler(Exception.class)
	public Object processException(NativeWebRequest request, Exception e) {
		e.printStackTrace();
		
		String errorMsg = "未知异常！";
		return afterExceptionReturn(request, errorMsg);
	}
	
	@ExceptionHandler(DataAccessException.class)
	public Object processDataAccessException(NativeWebRequest request, DataAccessException e) {
		e.printStackTrace();
		
		String errorMsg = "数据访问失败！";
		return afterExceptionReturn(request, errorMsg);
	}
	
	//数据更新异常。
	@ExceptionHandler(UpdateException.class)
	public Object processUpdateException(NativeWebRequest request, UpdateException e) {
		e.printStackTrace();
		
		String errorMsg = e.getMessage();
		return afterExceptionReturn(request, errorMsg);
	}

	
	
	
	@ExceptionHandler(AuthenticationException.class)
	public Object processAuthenticationException(NativeWebRequest request, AuthenticationException e) {
		e.printStackTrace();

		String errorMsg = "登录认证失败！";
		return afterExceptionReturn(request, errorMsg);
	}
	
	@ExceptionHandler({UnauthorizedException.class})
    public Object processUnauthenticatedException(NativeWebRequest request, UnauthorizedException e) {
		e.printStackTrace();

		String errorMsg = "权限认证失败！";
		return afterExceptionReturn(request, errorMsg);
    }
	
	/**
	 * app端身份验证失效！
	 * @param request
	 * @param e
	 * @return
	 */
	@ExceptionHandler(LoginTimeOutException.class)
	public Result processLoginTimeOutException(NativeWebRequest request, LoginTimeOutException e) {
		e.printStackTrace();
		Result result = new Result();
//		result.setNewtoken(UserApiService.default_token);
		return result.loseLogin(e.getMessage());
	}
	
}
