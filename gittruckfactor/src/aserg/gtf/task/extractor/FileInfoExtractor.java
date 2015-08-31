package aserg.gtf.task.extractor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import aserg.gtf.dao.NewFileInfoDAO;
import aserg.gtf.model.NewFileInfo;
import aserg.gtf.task.AbstractTask;

public class FileInfoExtractor extends AbstractTask<List<NewFileInfo>>{
	private static final Logger LOGGER = Logger.getLogger(FileInfoExtractor.class);
	
	public FileInfoExtractor(String repositoryPath, String repositoryName) {
		super("filelist.log", repositoryPath, repositoryName);
	}
	
	public List<NewFileInfo> execute() throws IOException{
		List<NewFileInfo> files = new ArrayList<NewFileInfo>();
		try {
			LOGGER.info(repositoryName + ": Extracting file information...");
			BufferedReader br = new BufferedReader(new FileReader(repositoryPath
					+ fileName));
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				files.add(new NewFileInfo(repositoryName, sCurrentLine));
			}
			br.close();
		} catch (Exception e) {
			LOGGER.error("Error in project "+repositoryName,e);
		}
		return files;		
	}
	
	public void persist(List<NewFileInfo> files) throws IOException {
		NewFileInfoDAO fiDAO = new NewFileInfoDAO();		
		try{
			fiDAO.persistAll(files);
		}
		catch(Exception e){
			LOGGER.error("Error in fileInfo extraction \n",e);
		} 
		finally{
			fiDAO.clear();
		}
	}
	
}
