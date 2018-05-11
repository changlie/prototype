package com.yunjia.system.shiro.ext;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import com.yunjia.system.entity.User;

/**
 * 用于密码,md5加密。
 * <p>
 * User: Zhang Kaitao
 * <p>
 * Date: 14-1-28
 * <p>
 * Version: 1.0
 */
@Component
public class PasswordHelper {

	private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
	private String algorithmName = "md5";
	private int hashIterations = 2;

	public void setRandomNumberGenerator(RandomNumberGenerator randomNumberGenerator) {
		this.randomNumberGenerator = randomNumberGenerator;
	}

	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	public void setHashIterations(int hashIterations) {
		this.hashIterations = hashIterations;
	}

	/**
	 * 生成新的盐，加密用户密码
	 * @param user
	 */
	public void encryptPassword(User user) {
		String salt = randomNumberGenerator.nextBytes().toHex();
		String newPassword = new SimpleHash(algorithmName,
							user.getPassword(),
							ByteSource.Util.bytes(salt), 
							hashIterations).toHex();

		user.setSalt(salt);
		user.setPassword(newPassword);
	}
	
	/**
	 * 用指定的盐，加密用户密码
	 * @param user
	 * @param salt 指定的盐
	 */
	public void encryptPassword(User user, String salt) {
		String newPassword = new SimpleHash(algorithmName, 
							user.getPassword(),
							ByteSource.Util.bytes(salt), 
							hashIterations).toHex();
		
		user.setPassword(newPassword);
	}
	
	/**
	 * 密码匹配  
	 * @param user user对象
	 * @param pwd1 要匹配的密码
	 * @return
	 */
	public boolean match(User user, String pwd1) {
		String npwd = new SimpleHash(algorithmName, 
						pwd1,
						ByteSource.Util.bytes(user.getSalt()), 
						hashIterations).toHex();
		return npwd.equals(user.getPassword());
	}
	
	/**
	 * 密码匹配
	 * @param rawPwd 要匹配的密码
	 * @param pwd 已加密的密码
	 * @param salt 用于加密的盐
	 * @return
	 */
	public boolean match(String rawPwd, String pwd, String salt) {
		String npwd = new SimpleHash(algorithmName, 
						rawPwd,
						ByteSource.Util.bytes(salt), 
						hashIterations).toHex();
		return npwd.equals(pwd);
	}
}
