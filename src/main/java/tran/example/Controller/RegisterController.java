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
import javax.sql.DataSource;

import tran.example.DAO.UsersDAO;
import tran.example.Model.User;

/**
 * Servlet implementation class RegisterController
 */
public class RegisterController extends HttpServlet {
	private static final long serialVersionUID = 9L;
	
	private DataSource ds;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterController() {
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
		
		if(action.equals("showRegistration")) {
			request.setAttribute("validationmessage", "");
			request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
		}
		else {
			// user does not really want to go to the registration page.
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
		String validatePassword = request.getParameter("validatePassword");
		
		if(action.equals("checkRegistration")) {
			User user = new User(userName, password, validatePassword);
			if(user.validate()) {
				// now try to insert into the database.
				Connection userConn = null;
				try {
					userConn = ds.getConnection();
					UsersDAO userCreate = new UsersDAO(userConn);
					int userCreateCode = userCreate.createUser(userName, password);
					if(userCreateCode != 0) {
						request.setAttribute("userName", user.getUserName());
						request.setAttribute("validationmessage", "User account successful, now log in above!");
						request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
					}
					else {
						setRequestAttributes(request, user);
						request.setAttribute("validationmessage", "Unable to create this user, the user name already exists!");
						request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
					}
				}
				catch(SQLException e) {
					setRequestAttributes(request, user);
					request.setAttribute("validationmessage", "Unable to register your account, try again.");
					request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
				}
				finally {
					if(userConn != null) {
						try {
							userConn.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			else {
				setRequestAttributes(request, user);
				request.setAttribute("validationmessage", user.getMessage());
				request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
			}
		}
		else {
			// user does not really want to go to the registration page.
			request.getRequestDispatcher("/").forward(request, response);
		}
	}
	
	
	// a helper method to set some request attributes
	private void setRequestAttributes(HttpServletRequest request, User user) {
		request.setAttribute("userName", user.getUserName());
	}


}
