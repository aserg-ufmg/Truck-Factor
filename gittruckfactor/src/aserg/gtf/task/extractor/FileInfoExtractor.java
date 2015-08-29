package aserg.gtf.task.extractor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import aserg.gtf.dao.LogCommitDAO;
import aserg.gtf.dao.NewFileInfoDAO;
import aserg.gtf.dao.ProjectInfoDAO;
import aserg.gtf.model.LogCommitInfo;
import aserg.gtf.model.NewFileInfo;
import aserg.gtf.model.ProjectInfo;
import aserg.gtf.model.authorship.RepositoryStatus;
import aserg.gtf.task.AbstractTask;

public class FileInfoExtractor extends AbstractTask<List<NewFileInfo>>{
	
	public FileInfoExtractor(String repositoryPath, String repositoryName) {
		super("filelist.log", repositoryPath, repositoryName);
	}
	
	
//	public static void main(String[] args) throws IOException {
////		FileInfoExtractor gitLogerExtractor = new FileInfoExtractor("C:/Users/Guilherme/Dropbox/docs/doutorado UFMG/pesquisas/github/dataset/_filesinfo/");
//		FileInfoExtractor gitLogerExtractor = new FileInfoExtractor("/Users/Guilherme/Dropbox/docs/doutorado UFMG/pesquisas/github/dataset/_filesinfo/");
//		System.out.println("BEGIN at "+ new Date() + "\n\n");
//		gitLogerExtractor.simpleExtract();
//		System.out.println("\n\nEND at "+ new Date());
//	}
	
	public List<NewFileInfo> execute() throws IOException{
		List<NewFileInfo> files = new ArrayList<NewFileInfo>();
		try {
			System.out.format(
					"%s (%s): Extracting file information...",
					repositoryName, new Date());
			BufferedReader br = new BufferedReader(new FileReader(repositoryPath
					+ fileName));
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				files.add(new NewFileInfo(repositoryName, sCurrentLine));
			}
			
			//System.out.println("Files added = " + files.size());
			br.close();
		} catch (Exception e) {
			System.err.println("Error in project "+repositoryName);
			System.err.println(e.toString());
		}
		return files;		
	}
	
	public void persist(List<NewFileInfo> files) throws IOException {
		NewFileInfoDAO fiDAO = new NewFileInfoDAO();		
		try{
			fiDAO.persistAll(files);
		}
		catch(Exception e){
			System.err.println("Error in fileInfo extraction \n"+e.toString());
		} 
		finally{
			fiDAO.clear();
		}
	}
	
}
