package tran.example.Controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LogoutController
 */
public class LogoutController extends HttpServlet {
	private static final long serialVersionUID = 8L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogoutController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getParameter("action");
		if(action.equals("doLogout")) {
			HttpSession session = request.getSession(false);
			if(session != null){
				session.invalidate();
		    }
			request.getRequestDispatcher("/jsp/posts.jsp").forward(request, response);
		}
		else {
			request.setAttribute("infoMessage", "you chose not to log out!");
			request.getRequestDispatcher("/jsp/posts.jsp").forward(request, response);
		}

	}
}
