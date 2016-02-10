package tran.example.Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Blog {

	private String title;

	private String content;

	private int postID;

	private String author;

	private String dateCreated;

	private String dateModified;

	public Blog() {}

	// constructor getting the posts.
	public Blog(int postID, String title, String content, String author, String dateCreated, String dateModified) {
		this.postID = postID;
		this.title = title;
		this.content = content;
		this.author = author;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
	}

	// constructor for inserting from a form.
	public Blog(String title, String content, String author) {
		this.title = title;
		this.content = content;
		this.author = author;
		this.dateCreated = setDateCreated();
		this.dateModified = setDateCreated();
	}

	// constructor for testing the MySQL connection.
	public Blog(String title, String content, int postID, String author) {
		this.title = title;
		this.content = content;
		this.postID = postID;
		this.author = author;
		this.dateCreated = setDateCreated();
		this.dateModified = setDateCreated();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getPostID() {
		return postID;
	}

	public void setPostID(int postID) {
		this.postID = postID;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String setDateCreated() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
	}

	public String getDateModified() {
		return dateModified;
	}
	
	public void setDateModified(String dateModified) {
		this.dateModified = dateModified;
	}

	public void setDateModified() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        this.dateModified = dateFormat.format(date);
	}

	// this method is helpful if a user has modified a blog post.
	public String modifiedBlogPost() {
		if(this.dateModified.equals(this.dateCreated))
			return "";
		else
			return "This was last modified on: " + this.dateModified;
	}

	// convert the date to a format of Month Day, Year | time
	public String printDateInFormat(String thedate) {
		// storage as : 'year-month-day hour:minutes:seconds' for mysql
		
		String base[] = thedate.split(" ");
		
		String[] date = base[0].split("-");
		
		String[] time = base[1].split(":");
		
		boolean suffix = true;
		
		int hour = 0;
		int minutes = 0;
		
		HashMap<String, String> monthMapping = new HashMap<String, String>();
		
		// map the months.
		monthMapping.put("01", "Jan");
		monthMapping.put("02", "Feb");
		monthMapping.put("03", "Mar");
		monthMapping.put("04", "Apr");
		monthMapping.put("05", "May");
		monthMapping.put("06", "Jun");
		monthMapping.put("07", "Jul");
		monthMapping.put("08", "Aug");
		monthMapping.put("09", "Sept");
		monthMapping.put("10", "Oct");
		monthMapping.put("11", "Nov");
		monthMapping.put("12", "Dec");		
		
		StringBuilder output = new StringBuilder();
		
		output.append(monthMapping.get(date[1]) + " ");
		output.append(date[2] + ", ");
		output.append(date[0] + "|");
		
		// add the hour part to the string.
		if(Integer.parseInt(time[0]) == 0) {
			hour = 12;
			output.append(hour + ":");
		}
		else if(Integer.parseInt(time[0]) > 0 && Integer.parseInt(time[0]) <= 11) {
			hour = Integer.parseInt(time[0]);
			output.append(Integer.parseInt(time[0]) + ":");
		}
		else if (Integer.parseInt(time[0]) == 12){
			hour = Integer.parseInt(time[0]);
			output.append(hour + ":");
			suffix = false;
		}
		else { // pm case.
			hour = Integer.parseInt(time[0]) - 12;
			output.append(hour + ":");
			suffix = false; // PM suffix.
		}
		
		// minutes
		minutes = Integer.parseInt(time[1]);
		if(minutes >= 0 && minutes < 10)
			output.append("0");
		output.append(minutes);
		
		if(suffix)
			output.append(" AM");
		else
			output.append(" PM");

		return output.toString();
	}

}