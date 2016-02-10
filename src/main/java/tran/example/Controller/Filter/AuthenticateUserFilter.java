package tran.example.Controller.Filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tran.example.Model.HashCookies;

/**
 * Servlet Filter implementation class AuthenticateUserFilter
 */
public class AuthenticateUserFilter implements Filter {

    /**
     * Default constructor. 
     */
    public AuthenticateUserFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HashCookies validateCookie = new HashCookies();
		
		HttpSession session = req.getSession(false);
		if(session == null) {
			req.getRequestDispatcher("/LoginController?action=doLogin").forward(req, res);
		}
		else {		
			String userName = (String)session.getAttribute("userName");
			if(userName != null) {
				String hashedCookieValue = "";
				Cookie[] sessionCookies = req.getCookies();
				for(Cookie userCookie : sessionCookies) {
					if(userCookie.getName().equals(userName)) {
						hashedCookieValue = userCookie.getValue();
						break;
					}
				}
				if(hashedCookieValue.equals("")) {
					redirectUser(req, res, session);
					return ;
				}
				else {
					if(validateCookie.validateCookieHash(userName, hashedCookieValue)) {
						// pass the request along the filter chain
						String requestURI = req.getRequestURI();
						if(requestURI.endsWith(("/LoginController")) || requestURI.endsWith("login.jsp")) {
							req.getRequestDispatcher("/GetPostsController?action=viewPosts").forward(req, res);
							return;
						}
						else {
							chain.doFilter(req, res);
						}
					}
					else {
						redirectUser(req, res, session);
						return ;
					}
				}
			}
			else {
				redirectUser(req, res, session);
				return ;
			}
		}
	}
	
	// will be deleted later
	private void deleteSession(HttpSession session) {
		if(session != null) {
			session.setMaxInactiveInterval(0);
			session.invalidate();
			return ;
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}
	
	// helper function to redirect a user that is not properly authenticated
	private void redirectUser(HttpServletRequest req, HttpServletResponse res, HttpSession session) {
		try {
			if(session != null) {
				// we do this because we are going to ask the user to log back in.
				session.invalidate();
			}
			req.setAttribute("validationmessage", "You must be logged in to perform this action or view this page.");
			req.setAttribute("userName", "");
			req.setAttribute("password", "");
			req.getRequestDispatcher("/LoginController?action=doLogin").forward(req, res);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		catch(ServletException e) {
			e.printStackTrace();
		}
	}

}
