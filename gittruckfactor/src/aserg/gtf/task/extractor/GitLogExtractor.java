package aserg.gtf.task.extractor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import aserg.gtf.dao.LogCommitDAO;
import aserg.gtf.dao.ProjectInfoDAO;
import aserg.gtf.model.LogCommitFileInfo;
import aserg.gtf.model.LogCommitInfo;
import aserg.gtf.model.ProjectInfo;
import aserg.gtf.task.AbstractTask;
import aserg.gtf.task.DOACalculator;
import aserg.gtf.util.CRLFLineReader;

public class GitLogExtractor extends AbstractTask<Map<String, LogCommitInfo>>{
	private static final Logger LOGGER = Logger.getLogger(GitLogExtractor.class);
	
	public GitLogExtractor(String repositoryPath, String repositoryName) {
		super("commitinfo.log", repositoryPath, repositoryName);
	}


	static String path = "";


	public Map<String, LogCommitInfo> execute() throws IOException{
		Map<String, LogCommitInfo> mapCommits = new HashMap<String, LogCommitInfo>();
		int countcfs = 0;
		try{	
			LOGGER.info("Extracting logCommits...  "+repositoryPath);
			BufferedReader br = new BufferedReader(new FileReader(
					repositoryPath + fileName));
			String sCurrentLine;
			String[] values;
			while ((sCurrentLine = br.readLine()) != null) {
				values = sCurrentLine.split(";");
				if (values.length<7)
					LOGGER.error("Problem in line  " + countcfs + ". Too much columns.");
				Date authorDate = !values[3].isEmpty() ? new Timestamp(Long.parseLong(values[3]) * 1000L) : null;
				Date commiterDate = !values[6].isEmpty() ? new Timestamp(Long.parseLong(values[6]) * 1000L) : null;
				String msg = (values.length == 8) ? values[7] : "";

				mapCommits.put(values[0],
						new LogCommitInfo(repositoryName,
								values[0], values[1], values[2],
								authorDate, values[4], values[5],
								commiterDate, msg));
				countcfs++;
			}
			insertFiles(repositoryName, mapCommits);
			br.close();
			
		}
		catch(Exception e ){
			String strError = String.format("Error in file %s, line %d%", repositoryName, countcfs);
			LOGGER.error(strError, e);
		}
				
		return mapCommits;
		
	}


	public void persist(Map<String, LogCommitInfo> map) throws IOException {
		LogCommitDAO lcDAO = new LogCommitDAO();
		try{
			lcDAO.persistAll(map.values());
		}
		catch(Exception e){
			LOGGER.error("Error in fileInfo extraction", e);
		} 
		finally{
			lcDAO.clear();
		}
		
	}
	
	
	private void insertFiles(String projectName, Map<String, LogCommitInfo> mapCommit) throws IOException{
		LOGGER.info(projectName+": Extracting logCommitFiles...");
		BufferedReader br = new BufferedReader(new FileReader(repositoryPath+"commitfileinfo.log"));
		String sCurrentLine;
		String[] values;

		while ((sCurrentLine = br.readLine()) != null) {
			values = sCurrentLine.split(";");
			String sha = values[0];
			LogCommitInfo commit = mapCommit.get(sha);
			commit.addCommitFile(new LogCommitFileInfo(commit, values[1], values[2], values[3]));
		}
		br.close();
	}



}
