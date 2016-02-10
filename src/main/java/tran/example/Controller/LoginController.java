package tran.example.Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import tran.example.DAO.UsersDAO;
import tran.example.Model.HashCookies;

/**
 * Servlet implementation class LoginController
 */
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private DataSource ds;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginController() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void init(ServletConfig config) throws ServletException {
    	// TODO Auto-generated method stub
    	try {
			InitialContext cxt = new InitialContext();
			ds = (DataSource) cxt.lookup("java:/comp/env/jdbc/MySQLDS");
		}
		catch(NamingException e) {
			e.printStackTrace();
		}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getParameter("action");
		// display the user page
		if(action.equals("doLogin")) {
			/*
			String userName = "";
			String password = "";
			*/
			String validationmessage = "";
			/*
			request.setAttribute("userName", userName);
			request.setAttribute("password", password);
			*/
			request.setAttribute("validationmessage", validationmessage);
			//request.getRequestDispatcher("/" + response.encodeURL(request.getContextPath() + "/jsp/login.jsp")).forward(request, response);
			request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
		}
		else {
			//request.getRequestDispatcher(response.encodeURL(request.getContextPath() + "/")).forward(request, response);
			request.getRequestDispatcher("/").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getParameter("action");
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		
		Connection db = null;
		try {
			db = ds.getConnection();
			// display the user page
			if(action.equals("doLogin")) {
				// query DB and check if valid user login.
				UsersDAO userCheck = new UsersDAO(db);
				if(userCheck.checkUserPW(userName, password)) { 
					// create the session and cookie to represent a logged in user.
					HttpSession session = request.getSession();
					
					int sessionLength = 24*60*60; // 1 day long.
					
					session.setAttribute("userName", userName);
					session.setMaxInactiveInterval(sessionLength);
					
					HashCookies hashCookies = new HashCookies();
					String hashedCookieValue = hashCookies.createCookieHash(userName);
					
					Cookie cookie = new Cookie(userName, hashedCookieValue);
					cookie.setMaxAge(sessionLength);
					
					response.addCookie(cookie);
					
					// forward it to a posts page.
					request.getRequestDispatcher("/GetPostsController?action=viewPosts").forward(request, response);
				}
				else {
					failedLogin(request, response, userName);
				}
			}
			else {
				failedLogin(request, response, userName);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(db != null) {
					db.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * helper function for when the user fails to login or attempts to change the parameters when logging in.
	 */
	private void failedLogin(HttpServletRequest request, HttpServletResponse response, String userName) {
		request.setAttribute("validationmessage", "not able to login, try again");
		request.setAttribute("userName", userName);
		try {
			//request.getRequestDispatcher(response.encodeURL(request.getContextPath() + "/jsp/login.jsp")).forward(request, response);
			request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
		}
		catch(ServletException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

}
