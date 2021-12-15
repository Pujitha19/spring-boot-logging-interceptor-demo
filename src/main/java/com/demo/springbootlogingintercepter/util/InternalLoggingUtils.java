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
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        MyCustomHttpRequestWrapper myCustomHttpRequestWrapper = new MyCustomHttpRequestWrapper((HttpServletRequest) servletRequest);
        StringBuilder requestBuilder = new StringBuilder();
        requestBuilder.append("\n======================================== Internal Flow Started =================================================================================")
                .append("\nRequest URI " + myCustomHttpRequestWrapper.getRequestURI())
                .append("\nRequest Method " + myCustomHttpRequestWrapper.getMethod())
                .append("\nRequest RequestBody " + new String(myCustomHttpRequestWrapper.getByteArray()));


        Enumeration<String> headerNames = myCustomHttpRequestWrapper.getHeaderNames();

        if (headerNames != null) {
            requestBuilder.append("\nRequest Headers:: {");
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                requestBuilder.append(headerName + "  : " + myCustomHttpRequestWrapper.getHeader(headerName) + ",");
            }
            requestBuilder.append("\n}\n");
        }
        log.info(requestBuilder.toString());


        MyCustomHttpResponseWrapper myCustomHttpResponseWrapper = new MyCustomHttpResponseWrapper((HttpServletResponse) servletResponse);
        filterChain.doFilter(myCustomHttpRequestWrapper, myCustomHttpResponseWrapper);

        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder
                .append("\nResponse Status " + myCustomHttpResponseWrapper.getStatus())
                .append("\nResponse Body " + new String(myCustomHttpResponseWrapper.getByteArrayOutputStream().toByteArray()));
        Collection<String> headers = myCustomHttpResponseWrapper.getHeaderNames();
        Iterator<String> iterator = headers.iterator();
        responseBuilder.append("\nResponse Headers:: {");
        while (iterator.hasNext()) {
            String headerName = iterator.next();
            responseBuilder.append(headerName + "  : " + myCustomHttpResponseWrapper.getHeader(headerName) + ",");
        }
        responseBuilder.append("=================================================== Internal Flow Ends ===========================================================================");

        log.info(responseBuilder.toString());

    }


    private class MyCustomHttpRequestWrapper extends HttpServletRequestWrapper {

        private byte[] byteArray;

        public MyCustomHttpRequestWrapper(HttpServletRequest request) {
            super(request);

            try {
                byteArray = IOUtils.toByteArray(request.getInputStream());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return new DelegatingServletInputStream(new ByteArrayInputStream(byteArray));
        }

        public byte[] getByteArray() {
            return byteArray;
        }
    }

    private class MyCustomHttpResponseWrapper extends HttpServletResponseWrapper {
        private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        private PrintStream printStream = new PrintStream(byteArrayOutputStream);

        public ByteArrayOutputStream getByteArrayOutputStream() {
            return byteArrayOutputStream;
        }

        public MyCustomHttpResponseWrapper(HttpServletResponse servletResponse) {
            super(servletResponse);

        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return new DelegatingServletOutputStream(new TeeOutputStream(super.getOutputStream(), printStream));
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return new PrintWriter(new TeeOutputStream(super.getOutputStream(), printStream));
        }
    }
}

