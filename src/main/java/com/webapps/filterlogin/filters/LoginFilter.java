package com.webapps.filterlogin.filters;

import com.webapps.controller.managebeans.Login;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;

/**
 * Filter checks if LoginBean has loginIn property set to true. If it is not set
 * then request is being redirected to the login.xhml page.
 *
 * @author itcuties
 *
 */
public class LoginFilter implements Filter {

    /**
     * Checks if user is logged in. If not it redirects to the login.xhtml page.
     */
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//		// Get the loginBean from session attribute
//		LoginBean loginBean = (LoginBean)((HttpServletRequest)request).getSession().getAttribute("loginBean");
//		
//		// For the first application request there is no loginBean in the session so user needs to log in
//		// For other requests loginBean is present but we need to check if user has logged in successfully
//		if (loginBean == null || !loginBean.isLoggedIn()) {
//			String contextPath = ((HttpServletRequest)request).getContextPath();
//			((HttpServletResponse)response).sendRedirect(contextPath + "/login.xhtml");
//		}
//		
//		chain.doFilter(request, response);
//			
//	}
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        Object userManager = (session != null) ? session.getAttribute("LS") : null;

        if (userManager == null) {
            response.sendRedirect(request.getContextPath() + "/login.xhtml"); // No logged-in user found, so redirect to login page.
        } else {
            chain.doFilter(req, res); // Logged-in user found, so just continue request.
        }
    }

    public void init(FilterConfig config) throws ServletException {
        // Nothing to do here!
    }

    public void destroy() {
        // Nothing to do here!
    }

}
