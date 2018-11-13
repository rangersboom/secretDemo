package com.sunny.demo.util.AES;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

@Slf4j
public class AESTest {

	@Test
	public void generateKey() throws Exception {
		//生成AES秘钥，并Base64编码
		String base64Str = AESUtil.genKeyAES();
		log.info("AES秘钥Base64编码:" + base64Str);
	}

	@Test
	public void testAES(){
		try {
			//=================客户端=================
			String data = "{\"id\":\"111\",\"name\":\"sunny\"}";
			log.info("加密前明文："+data);
			//将Base64编码的字符串，转换成AES秘钥
			SecretKey aesKey = AESUtil.loadKeyAES(AESUtil.SECRET_KEY);
			//加密
			byte[] encryptAES = AESUtil.encryptAES(data.getBytes(), aesKey);
			//加密后的内容Base64编码
			String byte2Base64 = AESUtil.byte2Base64(encryptAES);
			log.info("加密并Base64编码的结果：" + byte2Base64);

			//##############	网络上传输的内容有Base64编码后的秘钥 和 Base64编码加密后的内容		#################

			//===================服务端================
			//将Base64编码的字符串，转换成AES秘钥
			SecretKey aesKey2 = AESUtil.loadKeyAES(AESUtil.SECRET_KEY);
			//加密后的内容Base64解码
			byte[] base642Byte = AESUtil.base642Byte(byte2Base64);
			//解密
			byte[] decryptAES = AESUtil.decryptAES(base642Byte, aesKey2);
			//解密后的明文
			log.info("解密后的明文: " + new String(decryptAES));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void decryptAESTest() throws Exception {
		String data = "BJ3iCmj8eFptuFhvFLDlnvTppGa2JVktjJoCqacmpyy6eJ93jJuy642jTbtWTky8uj+TZzeWZ9PF\n" +
				"7V1NnEC4WJ9v/8Ahq80/8ckPoHzHwXbRQD5cXs+HFBxFyGufkFk/LumKHLkyuo58r6sQFe46+g7V\n" +
				"5+8cKXmRlx6nS2/cmTgKwIfQZu2Tx5OUpsN2OwkPDlVAKrQGb0dKrxDSXOexAjbkBPBq1WthwCZ5\n" +
				"HULuZ9uAAIHHkRKyQJSxbBHRXan5SYXWF8CtOzkaHXwC13Gd9kcIi308BIIuENYnbUtuyOvTMoak\n" +
				"ftgfCoWXAGDAGHiaQ28QdXAo80yqh0vlT1Ry8fKoPAO811PF8vG8iwqR9pMILwFGG8h5dfz2xVtK\n" +
				"rJGK/b/InlQdxIhsstyBp8FHRExbdz7zHojOmkpNVhL2XM8=";
		//将Base64编码的字符串，转换成AES秘钥
		SecretKey aesKey2 = AESUtil.loadKeyAES(AESUtil.SECRET_KEY);
		//加密后的内容Base64解码
		byte[] base642Byte = AESUtil.base642Byte(data);
		//解密
		byte[] decryptAES = AESUtil.decryptAES(base642Byte, aesKey2);
		//解密后的明文
		log.info("解密后的明文: " + new String(decryptAES));
	}
 
}