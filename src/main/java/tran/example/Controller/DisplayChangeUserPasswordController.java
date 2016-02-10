package tran.example.Controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class DisplayChangeUserPasswordController
 */
public class DisplayChangeUserPasswordController extends HttpServlet {
	private static final long serialVersionUID = 12L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DisplayChangeUserPasswordController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getParameter("action");
		
		if(action.equals("showChangePassword")) {
			HttpSession currentSession = request.getSession(false);
			if(currentSession != null) {
				String userName = currentSession.getAttribute("userName") != null ? (String) currentSession.getAttribute("userName") : "";
				if(!userName.equals("")) {
					/*
					request.setAttribute("userName", userName);
					request.getRequestDispatcher("/jsp/changePassword.jsp").forward(request, response);
					*/
					// if we use a forward the ChangeUserPasswordController will not be able to change the password because it will be forwarding with a get method.
					response.sendRedirect("/jsp/changePassword.jsp");
				}
				else {
					request.setAttribute("validationmessage", "You must log in before being able to change your password.");
					request.getRequestDispatcher("/LoginController?action=doLogin").forward(request, response);
				}
			}
			else {
				request.setAttribute("validationmessage", "You are not logged in so you cannot change your password");
				request.getRequestDispatcher("/LoginController?action=doLogin").forward(request, response);
			}
		}
		else {
			request.getRequestDispatcher("/").forward(request, response);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		
		if(action.equals("showChangePassword")) {
			HttpSession currentSession = request.getSession(false);
			if(currentSession != null) {
				String userName = currentSession.getAttribute("userName") != null ? (String) currentSession.getAttribute("userName") : "";
				if(!userName.equals("")) {
					if(request.getAttribute("validationMessage") != null) {
						request.setAttribute("validationMessage", (String)request.getAttribute("validationMessage"));
					}
					request.setAttribute("userName", userName);
					request.getRequestDispatcher("/jsp/changePassword.jsp").forward(request, response);
				}
				else {
					request.setAttribute("validationmessage", "You must log in before being able to change your password.");
					request.getRequestDispatcher("/LoginController?action=doLogin").forward(request, response);
				}
			}
			else {
				request.setAttribute("validationmessage", "You are not logged in so you cannot change your password");
				request.getRequestDispatcher("/LoginController?action=doLogin").forward(request, response);
			}
		}
		else {
			request.getRequestDispatcher("/").forward(request, response);
		}
	}
}
