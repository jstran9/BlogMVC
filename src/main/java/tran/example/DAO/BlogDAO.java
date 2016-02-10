package tran.example.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tran.example.Model.Blog;

public class BlogDAO {

	private Connection dbConn;
	
	public BlogDAO(Connection dbConn) {
		// TODO Auto-generated constructor stub
		this.dbConn = dbConn;
	}
	
	// retrieve from the Blogs table.
	public List<Blog> getBlogs() throws SQLException  {
		List<Blog> blogs = new ArrayList<Blog>();
		if(dbConn != null) {

			String retrieveBlogs = "SELECT * FROM Blogs ORDER BY blog_dateModified DESC";
			Statement queries = dbConn.createStatement();

			ResultSet resultBlogs = queries.executeQuery(retrieveBlogs);

			while(resultBlogs.next()) {
				int blog_ID = resultBlogs.getInt("blog_ID");
				String blog_title = resultBlogs.getString("blog_title");
				String blog_Content = resultBlogs.getString("blog_Content").replaceAll("''", "'");
				String blog_author = resultBlogs.getString("blog_author");
				String blog_dateCreated = resultBlogs.getTimestamp("blog_dateCreated").toString();
				String blog_dateModified = resultBlogs.getTimestamp("blog_dateModified").toString();

				blogs.add(new Blog(blog_ID, blog_title, blog_Content, blog_author, blog_dateCreated, blog_dateModified));
			}
			queries.close();
		} 
		return blogs;
	}

	// add a Blog.
	public void addBlog(String title, String content, String author) throws SQLException {
		if(dbConn != null) {
			// originally the dateCreated and dateModified will be identical.
			String insertEntry = "INSERT into Blogs(blog_title, blog_Content, blog_author, blog_dateCreated, blog_dateModified) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement insertBlog = dbConn.prepareStatement(insertEntry);
			insertBlog.setString(1, title);
			insertBlog.setString(2, content.replaceAll("'", "''"));
			insertBlog.setString(3, author);
			insertBlog.setString(4, setDate());
			insertBlog.setString(5, setDate());
			insertBlog.executeUpdate();
			insertBlog.close();
		}
	}

	// remove the Blog based on the ID.
	public int removeBlog(int blogID, String userName) throws SQLException {
		int returnCode = 0;
		if(dbConn != null) {
			String deleteEntry = "DELETE FROM Blogs WHERE blog_ID = ? AND blog_author = ?";
			PreparedStatement deleteBlog = dbConn.prepareStatement(deleteEntry);
			deleteBlog.setInt(1, blogID);
			deleteBlog.setString(2, userName);
			returnCode = deleteBlog.executeUpdate();
		}
		return returnCode;
	}

	// update the Blog using the ID.
	public boolean updateBlog(int blogID, String newContent) throws SQLException  {
		return checkForChanges(blogID, newContent);

	}
	
	public boolean checkForChanges(int blogID, String newContent) throws SQLException {
		// uses the id of the post to check for changes and compares the new vs old content.
		Blog oldContent = new Blog();
		// grab the content.
		oldContent = getaBlog(blogID);
		String delimitedContent = newContent.replaceAll("'", "''");
		
		if(delimitedContent.equals(oldContent.getContent())) {
			return false;
		}
		else {
			if(dbConn != null) {				
				String updatePostQuery = "UPDATE Blogs SET blog_Content = ?, blog_dateModified = ? WHERE blog_ID = ?";
				PreparedStatement updateBlog = dbConn.prepareStatement(updatePostQuery);
				updateBlog.setString(1, delimitedContent);
				updateBlog.setString(2, setDate());
				updateBlog.setInt(3, blogID);
				
				updateBlog.executeUpdate();
				
				return true;
			}
			return false;
		}
	}

	// get a single Blog using the ID.
	public Blog getaBlog(int blogID) throws SQLException {
		Blog singlePost = new Blog();
		if(dbConn != null) {
			String retrieveBlogs = "SELECT * FROM Blogs WHERE blog_ID = ?";
			PreparedStatement queries = dbConn.prepareStatement(retrieveBlogs);
			queries.setInt(1, blogID);

			ResultSet resultBlogs = queries.executeQuery();

			// move the resultant cursor to the returned row.
			resultBlogs.next();
			
			int blog_ID = resultBlogs.getInt("blog_ID");
			String blog_title = resultBlogs.getString("blog_title");
			String blog_Content = resultBlogs.getString("blog_Content").replaceAll("''", "'");
			String blog_author = resultBlogs.getString("blog_author");
			String blog_dateCreated = resultBlogs.getTimestamp("blog_dateCreated").toString();
			String blog_dateModified = resultBlogs.getTimestamp("blog_dateModified").toString();

			singlePost.setPostID(blog_ID);
			singlePost.setTitle(blog_title);
			singlePost.setContent(blog_Content);
			singlePost.setAuthor(blog_author);
			singlePost.setDateCreated(blog_dateCreated);
			singlePost.setDateModified(blog_dateModified);
			
			queries.close();
		} 
		return singlePost;
	}

	// gets the ID of the newly created blog.
	public int getCreatedBlogID() throws SQLException {
		int createdBlogId = 0;
		
		if(dbConn != null) {
			String retrieveBlogs = "SELECT blog_ID FROM Blogs ORDER BY blog_ID DESC LIMIT 1";
			PreparedStatement queries = dbConn.prepareStatement(retrieveBlogs);

			ResultSet resultBlogs = queries.executeQuery();

			resultBlogs.next(); // move the cursor to the result from the query.
			createdBlogId = resultBlogs.getInt("blog_ID");
			queries.close();
		} 
		return createdBlogId;
	}

	// helper method to get the current time.
	public String setDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
	}
	
	// helper method to determine if the user can view certain options.
	public boolean isAuthorOfPost(String author, int blogID) throws SQLException {
		Blog getPost = getaBlog(blogID);
		if(getPost.getAuthor().equals(author))
			return true;
		else 
			return false;
	}
		
}
