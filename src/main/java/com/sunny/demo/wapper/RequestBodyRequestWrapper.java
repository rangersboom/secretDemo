package com.sunny.demo.wapper;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

public class RequestBodyRequestWrapper extends HttpServletRequestWrapper {
    /**
     * 请求报文
     */
    private String requestBody = null;
    HttpServletRequest req = null;

    public RequestBodyRequestWrapper(HttpServletRequest request) {
        super(request);
        this.req = request;
    }

    public RequestBodyRequestWrapper(HttpServletRequest request, String requestBody) {
        super(request);
        this.requestBody = requestBody;
        this.req = request;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletRequestWrapper#getReader()
     */
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new StringReader(requestBody));
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletRequestWrapper#getInputStream()
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            private InputStream in = new ByteArrayInputStream(
                    requestBody.getBytes(req.getCharacterEncoding()));

            @Override
            public int read() throws IOException {
                return in.read();
            }
        };
    }
}
