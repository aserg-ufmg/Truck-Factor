package aserg.gtf.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aserg.gtf.dao.authorship.FileDAO;
import aserg.gtf.dao.authorship.RepositoryDAO;
import aserg.gtf.model.authorship.FileAuthors;

public class FormatDoaDistribution {
	
	
	
	public static void main(String[] args) {
		RepositoryDAO repDAO = new RepositoryDAO();
		List<FileAuthors> fileAuthors = repDAO.getFilesAuthorList("activeadmin/activeadmin");
		Directory mainDirectory = new Directory("Main");
		for (FileAuthors fileAuthor : fileAuthors) {
			String names[] = fileAuthor.getFileName().split("/");
			insert(mainDirectory, names, fileAuthor.getMainAuthor());
		}
		System.out.println(mainDirectory.toJson());
	}

	private static void insert(Directory mainDirectory, String[] names, String author) {
		Directory insertDirectory = mainDirectory;
		for(int i=0;i<(names.length-1); i++ ){
			if (!insertDirectory.getDirectoryMap().containsKey(names[i]))
				insertDirectory.getDirectoryMap().put(names[i], new Directory(names[i]));
			insertDirectory = insertDirectory.getDirectoryMap().get(names[i]);
		}
		String fileName = names[names.length-1];
		insertDirectory.getDirectoryMap().put(fileName, new FileDirectory(fileName, author));
		
	}
}
