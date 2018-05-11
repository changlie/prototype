package com.yunjia.util;

import java.security.Key;

import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;

public class EncryptUtil {
	
	static AesCipherService aesCipherService;
	
	static {
		aesCipherService = new AesCipherService();
		aesCipherService.setKeySize(128); //设置key长度
	}

	
	
	public static Key generateNewKey() {
		return aesCipherService.generateNewKey();
	}
	
	public static String encrypt(String origin, Key key) {
		return aesCipherService.encrypt(origin.getBytes(), key.getEncoded()).toHex();
	}
	
	public static String decrypt(String encryptText, Key key) {
		return new String(aesCipherService.decrypt(Hex.decode(encryptText), key.getEncoded()).getBytes());
	}

	protected static void testN() {
		//生成key  
		Key key = generateNewKey();
		System.out.println("key+"+key.getFormat()+", -> "+new String(key.getEncoded()));
		String origin = "hello";
		//加密  
		String encryptText =   encrypt(origin, key);
		System.out.println("encrptText-> "+encryptText);
		
		//解密  
		String DecryptText =  decrypt(encryptText, key);
		  
		System.out.println("DecryptText-> "+DecryptText);
	}
}
