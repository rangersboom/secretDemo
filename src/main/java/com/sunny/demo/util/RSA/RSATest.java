package com.sunny.demo.util.RSA;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

@Slf4j
public class RSATest {

	@Test
	public void generateKeys() throws Exception {
		//===============生成公钥和私钥，公钥传给客户端，私钥服务端保留==================
		//生成RSA公钥和私钥，并Base64编码
		KeyPair keyPair = RSAUtil.getKeyPair();
		String publicKeyStr = RSAUtil.getPublicKey(keyPair);
		String privateKeyStr = RSAUtil.getPrivateKey(keyPair);
		log.info("RSA公钥Base64编码:" + publicKeyStr);
		log.info("RSA私钥Base64编码:" + privateKeyStr);
	}
 
	@Test
	public void testRSA(){
		try {
			//=================客户端=================
			String data = "{\"id\":\"111\",\"name\":\"sunny\"}";
			log.info("加密前明文："+data);
			//将Base64编码后的公钥转换成PublicKey对象
			PublicKey publicKey = RSAUtil.string2PublicKey(RSAUtil.PUBLIC_KEY);
			//用公钥加密
			byte[] publicEncrypt = RSAUtil.publicEncrypt(data.getBytes(), publicKey);
			//加密后的内容Base64编码
			String byte2Base64 = RSAUtil.byte2Base64(publicEncrypt);
			log.info("公钥加密并Base64编码的结果：" + byte2Base64);
			
			//##############	网络上传输的内容有Base64编码后的公钥 和 Base64编码后的公钥加密的内容     #################
			
			//===================服务端================
			//将Base64编码后的私钥转换成PrivateKey对象
			PrivateKey privateKey = RSAUtil.string2PrivateKey(RSAUtil.PRIVATE_KEY);
			//加密后的内容Base64解码
			byte[] base642Byte = RSAUtil.base642Byte(byte2Base64);
			//用私钥解密
			byte[] privateDecrypt = RSAUtil.privateDecrypt(base642Byte, privateKey);
			//解密后的明文
			log.info("解密后的明文: " + new String(privateDecrypt));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
}