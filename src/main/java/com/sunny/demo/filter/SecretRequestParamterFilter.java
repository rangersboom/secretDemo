package com.sunny.demo.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sunny.demo.comm.Result;
import com.sunny.demo.util.AES.AESUtil;
import com.sunny.demo.util.RSA.RSAUtil;
import com.sunny.demo.wapper.ParameterRequestWrapper;
import com.sunny.demo.wapper.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.PrivateKey;
import java.util.Iterator;

/**
 * @Author:Sunny
 * @Description: 参考
 * 修改request的parameter的几种方式       https://blog.csdn.net/xieyuooo/article/details/8447301
 * Iterator使用                          http://www.cnblogs.com/redcoatjk/articles/4863340.html
 * springboot配置过滤器                  https://blog.csdn.net/testcs_dn/article/details/80265196
 * @CreateDate: 21:53 2018/10/31
 * @Modified:
 * @Version:
 */
@Slf4j
@WebFilter(urlPatterns = "/param/*", filterName = "paramFilter")
@Order(value = 1)
public class SecretRequestParamterFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Result result;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        log.info("执行 RequestParamter secretFilter。。拦截请求URL====》" + request.getRequestURL());
        String sKey = request.getHeader("sKey");
        if (StringUtils.isEmpty(sKey)){
            result = new Result();
            result.setMsg("sKey is not exist");
            writeResponse(servletResponse, JSON.toJSONString(result));
            return ;
        }
        String requestBody = getRequestBody(request);

        ParameterRequestWrapper requestWrapper = new ParameterRequestWrapper(request);
        ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) servletResponse);
        SecretKey aesKeyObj = null;
        try {
            if (!StringUtils.isEmpty(requestBody)) {
                //第一步：获取RequestBody并进行解密
                //将Base64编码后的私钥转换成PrivateKey对象
                PrivateKey privateKey = RSAUtil.string2PrivateKey(RSAUtil.PRIVATE_KEY);
                //公钥加密AES秘钥后的内容(Base64编码)，进行Base64解码
                byte[] publicEncryptBytes = RSAUtil.base642Byte(sKey);
                //用私钥解密,得到aesKey
                byte[] aesKeyStrBytes = RSAUtil.privateDecrypt(publicEncryptBytes, privateKey);
                //解密后的aesKey
                String aesKeyStr2 = new String(aesKeyStrBytes);
                //将Base64编码后的AES秘钥转换成SecretKey对象
                aesKeyObj = AESUtil.loadKeyAES(aesKeyStr2);
                //AES秘钥加密后的内容(Base64编码)，进行Base64解码
                byte[] encryptAES2 = AESUtil.base642Byte(requestBody);
                //用AES秘钥解密实际的内容
                byte[] decryptAES = AESUtil.decryptAES(encryptAES2, aesKeyObj);
                //解密后的实际内容
                requestBody = new String(decryptAES);
                log.info("解密后的实际内容: " + requestBody);

                //第二步：封装解密后的RequestBody json数据到request的parameter中
                JSONObject jsonObject = JSONObject.parseObject(requestBody);
                Iterator<String> iterator = jsonObject.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String value = jsonObject.get(key).toString();
                    requestWrapper.addParameter(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        filterChain.doFilter(requestWrapper, responseWrapper);
        //第三步：对响应数据进行加密，封装到response中
        String responseBodyWapper = null;
        try {
            byte[] data = responseWrapper.getResponseData();
            String responseBody = new String(data, "utf-8");
            log.info("原始返回数据： " + responseBody);
            if (! StringUtils.isEmpty(responseBody)){
                //进行AES加密操作
                byte[] encryptAES = AESUtil.encryptAES(responseBody.getBytes(), aesKeyObj);
                //加密后的内容转换为Base64编码
                responseBodyWapper = AESUtil.byte2Base64(encryptAES);
                log.info("加密并Base64编码的结果：" + responseBodyWapper);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        writeResponse(servletResponse, responseBodyWapper);
    }

    @Override
    public void destroy() {

    }

    private void writeResponse(ServletResponse response, String responseString) throws IOException {
        PrintWriter out = response.getWriter();
        out.print(responseString);
        out.flush();
        out.close();
    }

    private String getRequestBody(HttpServletRequest req) {
        try {
            BufferedReader reader = req.getReader();
            StringBuffer sb = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();
            return json;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
