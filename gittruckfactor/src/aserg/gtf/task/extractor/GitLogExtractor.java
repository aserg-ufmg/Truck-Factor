package aserg.gtf.task.extractor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import aserg.gtf.dao.LogCommitDAO;
import aserg.gtf.model.LogCommitFileInfo;
import aserg.gtf.model.LogCommitInfo;
import aserg.gtf.task.AbstractTask;

public class GitLogExtractor extends AbstractTask<Map<String, LogCommitInfo>>{
	private static final Logger LOGGER = Logger.getLogger(GitLogExtractor.class);
	
	public GitLogExtractor(String repositoryPath, String repositoryName) {
		super("commitinfo.log", repositoryPath, repositoryName);
	}


	static String path = "";


	public Map<String, LogCommitInfo> execute() throws Exception{
		Map<String, LogCommitInfo> mapCommits = new HashMap<String, LogCommitInfo>();
		int countcfs = 0;
		try{	
			LOGGER.info("Extracting logCommits...  "+repositoryPath);
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(repositoryPath + fileName), "UTF8"));
			String sCurrentLine;
			String[] values;
			while ((sCurrentLine = br.readLine()) != null) {
				sCurrentLine = StringUtils.stripAccents(sCurrentLine);
				values = sCurrentLine.split("-;-");
				values = removeSemicolon(values, "&");
				if (values.length<7 || values.length>8)
					System.err.println("Problem in line  " + countcfs + ". Wrong number of columns.");
				Date authorDate = !values[3].isEmpty() ? new Timestamp(Long.parseLong(values[3]) * 1000L) : null;
				Date commiterDate = !values[6].isEmpty() ? new Timestamp(Long.parseLong(values[6]) * 1000L) : null;
				if (authorDate == null && commiterDate == null){
					System.err.println("Commit without date. Ignoring commit: "+values[0]);
					continue;
				}
				String msg = (values.length == 8) ? values[7] : "";
				
				countcfs++;
				
				if ((values[1]+values[2]+values[4]+values[5]).isEmpty()){
					System.err.println("Ignoring commit without developer info in  " + values[0]);
					continue;
				}
				mapCommits.put(values[0],
						new LogCommitInfo(repositoryName,
								values[0], values[1], values[2],
								authorDate, values[4], values[5],
								commiterDate, msg));
//				System.out.println(countcfs);
			}
			insertFiles(repositoryName, mapCommits);
			br.close();
			
		}
		catch(FileNotFoundException e ){
			throw new Exception("File not found: " + repositoryPath + fileName, e);
		}
		catch(Exception e ){
			throw new Exception(String.format("Error in file %s, line %d", repositoryName, countcfs));
		}
				
		return mapCommits;
		
	}

	// Remove internal semicolon character. Useful to further data process 
	private String[] removeSemicolon(String[] values, String newStr) {
		String[] newValues = new String[values.length];
		int i = 0;
		for (String value : values) {
			newValues[i++] = value.replace(";", newStr);
		}
		return newValues;
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
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(repositoryPath+"commitfileinfo.log"), "UTF8"));
		String sCurrentLine;
		String[] values;

		while ((sCurrentLine = br.readLine()) != null) {
			values = sCurrentLine.split(";");
			String sha = values[0];
			if (mapCommit.containsKey(sha)){
				LogCommitInfo commit = mapCommit.get(sha);
				commit.addCommitFile(new LogCommitFileInfo(commit, values[1], values[2], values[3]));
			}
			else
				System.err.println("Ignoring file " + values[3] + " Sha: "+sha);
		}
		br.close();
	}


	



}
