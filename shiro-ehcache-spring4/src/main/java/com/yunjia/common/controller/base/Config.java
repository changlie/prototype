package com.yunjia.common.controller.base;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.serializer.ValueFilter;

@Configuration
public class Config {

	/**
	 * 返回json字符串，若为null(空)的, 返回 ""(空串)
	 * @return
	 */
	@Bean
	public ValueFilter jsonValueFilter() {
		return new ValueFilter() {
			@Override
			public Object process(Object obj, String s, Object v) {
				if (v == null) {
					return "";
				}
				return v;
			}
		};
	}
	
	/**
	 * 权限过滤链配置。
	 * @return
	 */
	@Bean
	public String filtersChain() {
		StringBuilder chain = new StringBuilder();
		chain.append("/hintLogin=anon").append("\n");
		chain.append("/ajaxLogin=anon").append("\n");
		chain.append("/loginInfo=anon").append("\n");
		chain.append("/isLogin=anon").append("\n");
		chain.append("/test.js=anon").append("\n");
		chain.append("/api/**=anon").append("\n");
		chain.append("/websocket.html=anon").append("\n");
		chain.append("/WebsocketEndpoint/**=anon").append("\n");
		chain.append("/**=authc").append("\n");
		return chain.toString();
	}
	
}
