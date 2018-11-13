package com.sunny.demo.util.RSA;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtil {
	public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn1wjC4OD3CiugyC1qUqYJFE/JJKZeM9i\n" +
			"6ot76Hp+YpUM2AdCRjRnYQqLx97/8hWxW6fFSYtgAu8il8k4jMSeg2nmMrmBamBdc/VQ+zj2ia+8\n" +
			"W6IGGlo8w23qdA0KXQ417kn1LETRvLMreHWWgLCBZ7PFim5VZaz+sFycml7u6fLam8oRt5J+2RM2\n" +
			"Vup4kVkXCO2WmXykbwByeluGoP3GRcft4EemQb9XXGqKk2RFHwYyAY0hkP8vuO1bsuHyYsyFlEPo\n" +
			"UHRY7R1TTdId47iPjLyy8jOW9pRif3ZUhGCyUXj+Eoemzpvy27gQRVjtSDmqVD/jzthq8zFafBiC\n" +
			"awIQhQIDAQAB";
	public static final String PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCfXCMLg4PcKK6DILWpSpgkUT8k\n" +
			"kpl4z2Lqi3voen5ilQzYB0JGNGdhCovH3v/yFbFbp8VJi2AC7yKXyTiMxJ6DaeYyuYFqYF1z9VD7\n" +
			"OPaJr7xbogYaWjzDbep0DQpdDjXuSfUsRNG8syt4dZaAsIFns8WKblVlrP6wXJyaXu7p8tqbyhG3\n" +
			"kn7ZEzZW6niRWRcI7ZaZfKRvAHJ6W4ag/cZFx+3gR6ZBv1dcaoqTZEUfBjIBjSGQ/y+47Vuy4fJi\n" +
			"zIWUQ+hQdFjtHVNN0h3juI+MvLLyM5b2lGJ/dlSEYLJReP4Sh6bOm/LbuBBFWO1IOapUP+PO2Grz\n" +
			"MVp8GIJrAhCFAgMBAAECggEAbO+PBU/h0537raeuhwt29VyUzWtpfixGcg7npQQXYUyCeyp7I4Kv\n" +
			"KKcM5pSKYfJySzVkp+GewGK4QwpKne/KbAr4UbR9wzocqCgKM7G81mJLYFhh06Hb57w7iRlg/Du6\n" +
			"oinsb1acCWJxVOcM+uutijZZ00eZGOgpMmYcp2o9564Hi4Z58YxbJXfuGcHSzENq4jluytv18B+M\n" +
			"TRhlLqslXgfFpZYUnokkNl0mLNkPGceDrsRT/buu1N/u5dANEBfIvWMad/AzHmiFKVKNOO1q5jHM\n" +
			"VhuPKwbcp8S6ZvU51zz0ZAW7RT2cKPWcChjt/ugmXDGS+JU+GL2VPXDCHq+0AQKBgQDOjuzxqEL8\n" +
			"UGzQBTj2XoU6LYkyiiS/xenKqtzlqcqzwVBa49ALR0mY7/iieB9SBbpbXnaORYJ7UMZcTMpvsAFR\n" +
			"rzJW504udMCs02ftUU5S9ceO0jJKV3H2ATwgkowqc95qtnfKGSjiXYU/AQlmiFCCyXRhMgKBdyjc\n" +
			"BPBvLClmnwKBgQDFgRSuu+RId1o4I1bsLNlmJoWS2MpbctcKqftqbw97ym0hWwddPNmRcY6ikE1i\n" +
			"WeYAYk+cbQsUIR6QjfXwmEB0gVgDuYuWo1u3MvcLNBdhai0sICSPgnudO3oB9TcWDqmA4q2sAY8I\n" +
			"l7JsVNSxCUrz855CAYr+buRkKZdBPLuqWwKBgQChsIXPwPv2kGotB/mH+YNmgCCmfDa6G42gBNH3\n" +
			"wUqYHVnuW+gN/Bajbi192HQD+9HeFfrN8aFrMFamB8JrXSYbL/5CwruTZ/cvEimHPp8GF7jHlsih\n" +
			"IwRlIqBTiwPHc34GoKDyhXFYiQZ1xDT8mvYS+ulr7/7AI2vLlt4y2CnsVwKBgHgZYiZPAyY9zZLN\n" +
			"ipHbQ+emDRua0JKDZtiJPOUf295+3ZWm8Pd1hSe4Ue/wp93ipMYfAiIw/MekzB1C9gbzzl4KOScq\n" +
			"sQNNGvxv9WHOr1kBY66tbQ4HXnviKvL91h2HlPNW5BzwZbXn6QuPGjb9W6K0xcuFfKvzatato9h7\n" +
			"wfJ5AoGBALbN7HxTgCre7CQT9xF1WBxx6UVdnivakMc4plZmcq9XCta9z0CiHArtCYYsKYH+TQr1\n" +
			"+rbOGX3DvnE1YpwYg9k4ES2j8TnOavnTnHL1aZRuBsklSjFeeccvYTG+HFX/c78UYXvw/ry1Rlva\n" +
			"r+d9mMUj7e0XQMr8cGsdxowMt5bj";

	//生成秘钥对
	public static KeyPair getKeyPair() throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		return keyPair;
	}
	
	//获取公钥(Base64编码)
	public static String getPublicKey(KeyPair keyPair){
		PublicKey publicKey = keyPair.getPublic();
		byte[] bytes = publicKey.getEncoded();
		return byte2Base64(bytes);
	}
	
	//获取私钥(Base64编码)
	public static String getPrivateKey(KeyPair keyPair){
		PrivateKey privateKey = keyPair.getPrivate();
		byte[] bytes = privateKey.getEncoded();
		return byte2Base64(bytes);
	}
	
	//将Base64编码后的公钥转换成PublicKey对象
	public static PublicKey string2PublicKey(String pubStr) throws Exception{
		byte[] keyBytes = base642Byte(pubStr);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}
	
	//将Base64编码后的私钥转换成PrivateKey对象
	public static PrivateKey string2PrivateKey(String priStr) throws Exception{
		byte[] keyBytes = base642Byte(priStr);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}
	
	//公钥加密
	public static byte[] publicEncrypt(byte[] content, PublicKey publicKey) throws Exception{
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] bytes = cipher.doFinal(content);
		return bytes;
	}
	
	//私钥解密
	public static byte[] privateDecrypt(byte[] content, PrivateKey privateKey) throws Exception{
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] bytes = cipher.doFinal(content);
		return bytes;
	}
	
	//字节数组转Base64编码
	public static String byte2Base64(byte[] bytes){
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(bytes);
	}
	
	//Base64编码转字节数组
	public static byte[] base642Byte(String base64Key) throws IOException{
		BASE64Decoder decoder = new BASE64Decoder();
		return decoder.decodeBuffer(base64Key);
	}
}