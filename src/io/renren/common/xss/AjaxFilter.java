package io.renren.common.xss;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class AjaxFilter implements javax.servlet.Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
    @Override
    public void destroy() {

    }

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		    HttpServletResponse httpServletResponse=(HttpServletResponse)arg1;
		    httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
	        httpServletResponse.setHeader("Access-Control-Allow-Headers", "Authentication");
	        arg2.doFilter(arg0,httpServletResponse);
		
	}
}