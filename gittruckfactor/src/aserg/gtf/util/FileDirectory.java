package aserg.gtf.util;

import java.util.Map.Entry;

public class FileDirectory extends Directory{
	private String author;
	

	public FileDirectory(String name, String author) {
		super(name, null);
		this.author = author;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	@Override
	public String toJson(){
		String str = "{\"name\": \"" + this.getName()+ "\",\"author\" :"+ "\"" + author +"\"}";
		return str;
	}
}
