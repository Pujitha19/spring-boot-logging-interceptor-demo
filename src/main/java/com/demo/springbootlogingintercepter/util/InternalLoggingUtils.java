package com.demo.springbootlogingintercepter.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.TeeOutputStream;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.mock.web.DelegatingServletOutputStream;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

@Component
@Slf4j
public class InternalLoggingUtils implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        MyCustomHttpRequestWrapper httpServletRequest = new MyCustomHttpRequestWrapper((HttpServletRequest)servletRequest);
        StringBuilder requestBuilder = new StringBuilder();
        requestBuilder.append("\n======================================== Internal Flow Started =================================================================================")
                        .append("\nRequest URI "+ httpServletRequest.getRequestURI())
                                .append("\nRequest Method "+ httpServletRequest.getMethod())
                                        .append("\nRequest RequestBody "+ new String(httpServletRequest.getByteArray()));


        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();

        if (headerNames != null) {
            requestBuilder.append("\nRequest Headers:: {\n");
            while (headerNames.hasMoreElements()) {
                String s=headerNames.nextElement();
                requestBuilder.append(s+"  : "+httpServletRequest.getHeader(s)+"\n");
            }
            requestBuilder.append("\n}\n");
        }
        log.info(requestBuilder.toString());


        MyCustomHttpResponseWrapper httpservletResponse = new MyCustomHttpResponseWrapper((HttpServletResponse)servletResponse);
        filterChain.doFilter(httpServletRequest,httpservletResponse);

        StringBuilder responseBuilder= new StringBuilder();
       responseBuilder
        .append("\nResponse Status "+ httpservletResponse.getStatus())
                .append("\nResponse Body "+ new String(httpservletResponse.getBaos().toByteArray()));
        Collection<String> headers=httpservletResponse.getHeaderNames();
        Iterator<String> iterator= headers.iterator();
        responseBuilder.append("\nResponse Headers:: {\n");
        while (iterator.hasNext()){
            String s=iterator.next();
            responseBuilder.append(s+"  : "+httpservletResponse.getHeader(s)+"\n");
        }
        responseBuilder.append("=================================================== Internal Flow Ends ===========================================================================");

        log.info(responseBuilder.toString());

    }

    @Override
    public void destroy() {

    }

    private class MyCustomHttpRequestWrapper extends HttpServletRequestWrapper {

        private byte[] byteArray;

        public MyCustomHttpRequestWrapper(HttpServletRequest request) {
            super(request);

            try{
                byteArray = IOUtils.toByteArray(request.getInputStream());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            DelegatingServletInputStream delegatingServletInputStream;
            return new DelegatingServletInputStream(new ByteArrayInputStream(byteArray));


        }

        public byte[] getByteArray() {
            return byteArray;
        }
    }

    private class MyCustomHttpResponseWrapper extends HttpServletResponseWrapper {
        private ByteArrayOutputStream baos = new ByteArrayOutputStream();
        private PrintStream printStream = new PrintStream(baos);

        public ByteArrayOutputStream getBaos() {
            return baos;
        }

        public MyCustomHttpResponseWrapper(HttpServletResponse servletResponse) {
            super(servletResponse);

        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return new DelegatingServletOutputStream( new TeeOutputStream(super.getOutputStream(), printStream));
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return new PrintWriter( new TeeOutputStream(super.getOutputStream(), printStream));
        }
    }
}

