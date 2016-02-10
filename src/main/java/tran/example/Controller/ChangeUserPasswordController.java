package tran.example.Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import tran.example.DAO.UsersDAO;
import tran.example.Model.User;

/**
 * Servlet implementation class ChangeUserPassword
 */
public class ChangeUserPasswordController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private DataSource ds;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangeUserPasswordController() {
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getParameter("action");
		
		if(action.equals("doChangePassword")) {
			String password = request.getParameter("password");
			String verifyPassword = request.getParameter("passwordVerify");
			User verifyPasswords = new User(password, verifyPassword);
			if(verifyPasswords.validatePasswords()) {
				// get the user of the current session.
				HttpSession currentSession = request.getSession(false);
				String userName = "";
				if(currentSession != null) {
					userName = currentSession.getAttribute("userName") != null ? (String) currentSession.getAttribute("userName") : "";
					
					Connection dbConn = null;
					try {
						dbConn = ds.getConnection();
						UsersDAO modifyUserPassword = new UsersDAO(dbConn);
						
						int successValue = modifyUserPassword.modifyPassword(userName, password);
						if(successValue > 0) {
							currentSession.invalidate();
							// if you forward directly to the login page it will go to the post method of the login which it should not do.
							// this would essentially skip the step where a form is displayed to the user to enter the credentials.
							// so instead just forward to the view posts page where the user WILL NOT BE LOGGED IN.
							request.setAttribute("infoMessage", "Password change succesful, You must log back in!");
							request.getRequestDispatcher("/GetPostsController?action=viewPosts").forward(request, response);
						}
						else if(successValue == 0) {
							// since nothing is returned let the user know 
							request.setAttribute("validationMessage", "This is the same as your previous password so it will not be changed.");
							request.getRequestDispatcher("/DisplayChangeUserPasswordController?action=showChangePassword").forward(request, response);
						}
					}
					catch(SQLException e) {
						request.setAttribute("infoMessage", "an error occured while attempting to change the password, try again later.");
						request.getRequestDispatcher("/GetPostsController?action=viewPosts").forward(request, response);
					}
					finally {
						if(dbConn != null) {
							try {
								dbConn.close();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
			else {
				request.setAttribute("validationMessage", verifyPasswords.getMessage());
				request.getRequestDispatcher("/DisplayChangeUserPasswordController?action=showChangePassword").forward(request, response);
			}
		}
		else {
			request.getRequestDispatcher("/").forward(request, response);
		}
	}

}
