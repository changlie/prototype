package com.yunjia.common.controller.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RequestPreHandler {

	@ModelAttribute
	public void crossDomain(HttpServletRequest request, HttpServletResponse response){
		//System.out.println("sessionId: " + request.getSession().getId());
//		System.err.println("@RestControllerAdvice crossDomain --> "+request.getHeader("Origin"));
//        //
//        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");  
//        response.setHeader("Access-Control-Max-Age", "14200"); 
//        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");  
	}
}
