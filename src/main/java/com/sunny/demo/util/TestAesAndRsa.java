package com.sunny.demo.util;

import com.sunny.demo.util.AES.AESUtil;
import com.sunny.demo.util.RSA.RSAUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @Description 參考文章 https://blog.csdn.net/qq_32523587/article/details/79146977
 */
@Slf4j
public class TestAesAndRsa {

	@Test
	public void generateKeys() throws Exception {
		//生成RSA公钥和私钥，并Base64编码
		KeyPair keyPair = RSAUtil.getKeyPair();
		String publicKeyBase64 = RSAUtil.getPublicKey(keyPair);
		String privateKeyBase64 = RSAUtil.getPrivateKey(keyPair);
		log.info("RSA公钥Base64编码:" + publicKeyBase64);
		log.info("RSA私钥Base64编码:" + privateKeyBase64);
	}

	@Test
	public void generateKey() throws Exception {
		//生成AES秘钥，并Base64编码
		String aesKey = AESUtil.genKeyAES();
		log.info("AES秘钥Base64编码:" + aesKey);
	}

	//测试RSA与AES的结合。
	//	客户端用公钥加密AES秘钥，AES秘钥加密实际内容；
	//	服务端用私钥解密AES秘钥，AES秘钥解密实际内容
	@Test
	public void testAesAndRsa() throws Exception {
		//=================客户端=================
		String data = "{\"id\":\"1\",\"name\":\"用户\"}";
		log.info("加密前明文："+data);
		//将Base64编码后的公钥转换成PublicKey对象
		PublicKey publicKey = RSAUtil.string2PublicKey(RSAUtil.PUBLIC_KEY);
		//生成AES秘钥，并Base64编码
		log.info("AES秘钥Base64编码:" + AESUtil.SECRET_KEY);
		//用公钥加密AES秘钥
		byte[] publicEncrypt = RSAUtil.publicEncrypt(AESUtil.SECRET_KEY.getBytes(), publicKey);
		//公钥加密AES秘钥后的Base64编码
		String publicEncryptAESKeyBase64 = RSAUtil.byte2Base64(publicEncrypt);
		log.info("公钥加密AES秘钥并Base64编码的结果：" + publicEncryptAESKeyBase64);
		
		//将Base64编码后的AES秘钥转换成SecretKey对象
		SecretKey aesKey = AESUtil.loadKeyAES(AESUtil.SECRET_KEY);
		//用AES秘钥加密实际的内容
		byte[] encryptAES = AESUtil.encryptAES(data.getBytes(), aesKey);
		//AES秘钥加密后的内容Base64编码
		String encryptAESBase64 = AESUtil.byte2Base64(encryptAES);
		log.info("AES秘钥加密实际的内容并Base64编码的结果：" + encryptAESBase64);
		
		//##############	网络上传输的内容有Base64编码后的公钥加密AES秘钥的结果 和 Base64编码后的AES秘钥加密实际内容的结果   #################
		//##############	即publicEncryptAESKeyBase64和encryptAESBase64	###################
		
		//===================服务端================
		//将Base64编码后的私钥转换成PrivateKey对象
		PrivateKey privateKey = RSAUtil.string2PrivateKey(RSAUtil.PRIVATE_KEY);
		//公钥加密AES秘钥后的内容(Base64编码)，进行Base64解码
		byte[] publicEncryptBytes = RSAUtil.base642Byte(publicEncryptAESKeyBase64);
		//用私钥解密,得到aesKey
		byte[] aesKeyStrBytes = RSAUtil.privateDecrypt(publicEncryptBytes, privateKey);
		//解密后的aesKey
		String aesKeyStr2 = new String(aesKeyStrBytes);
		log.info("解密后的aesKey(Base64编码): " + aesKeyStr2);
		
		//将Base64编码后的AES秘钥转换成SecretKey对象
		SecretKey aesKey2 = AESUtil.loadKeyAES(aesKeyStr2);
		//AES秘钥加密后的内容(Base64编码)，进行Base64解码
		byte[] encryptAES2 = AESUtil.base642Byte(encryptAESBase64);
		//用AES秘钥解密实际的内容
		byte[] decryptAES = AESUtil.decryptAES(encryptAES2, aesKey2);
		//解密后的实际内容
		log.info("解密后的实际内容: " + new String(decryptAES));
		
	}
 
}