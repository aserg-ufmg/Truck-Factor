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

import aserg.gtf.dao.LogCommitDAO;
import aserg.gtf.dao.ProjectInfoDAO;
import aserg.gtf.model.LogCommitFileInfo;
import aserg.gtf.model.LogCommitInfo;
import aserg.gtf.model.ProjectInfo;
import aserg.gtf.task.AbstractTask;
import aserg.gtf.util.CRLFLineReader;

public class GitLogExtractor extends AbstractTask<Map<String, LogCommitInfo>>{
	
	public GitLogExtractor(String repositoryPath, String repositoryName) {
		super("commitinfo.log", repositoryPath, repositoryName);
	}


	static String path = "";
	
//	public static void main(String[] args) throws IOException {
//		String repName = "";
//		if (args.length>0)
//			path = args[0];
//		if (args.length>1)
//			repName = args[1];
//		
//		path = (path.charAt(path.length()-1) == '/') ? path : (path + "/");
//		GitLogExtractor gitLoggerExtractor = new GitLogExtractor(path, repName);
//		
//		System.out.println("BEGIN at "+ new Date() + "\n\n");
//		gitLoggerExtractor.simpleExtract();
//		System.out.println("\n\nEND at "+ new Date());
//	}
	

	public Map<String, LogCommitInfo> execute() throws IOException{
		Map<String, LogCommitInfo> mapCommits = new HashMap<String, LogCommitInfo>();
		int countcfs = 0;
		try{			
			System.out.println(": Extracting logCommits...  "+repositoryPath);
			BufferedReader br = new BufferedReader(new FileReader(
					repositoryPath + fileName));
			String sCurrentLine;
			String[] values;
			while ((sCurrentLine = br.readLine()) != null) {
				values = sCurrentLine.split(";");
				if (values.length<7)
					System.err.println("Erro na linha " + countcfs);
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
			System.err.format("Error in file %s, line %d\n%s", repositoryName, countcfs, e.getMessage() );
		}
				
		return mapCommits;
		
	}


	public void persist(Map<String, LogCommitInfo> map) throws IOException {
		LogCommitDAO lcDAO = new LogCommitDAO();
		try{
			lcDAO.persistAll(map.values());
		}
		catch(Exception e){
			System.err.println("Error in fileInfo extraction \n"+e.toString());
		} 
		finally{
			lcDAO.clear();
		}
		
	}
	
	
//	static public Map<String, List<LogCommitFileInfo>> extractProject(String localPath, String projectName) throws IOException{
//		Map<String, List<LogCommitFileInfo>> map = new HashMap<String, List<LogCommitFileInfo>>();
//		List<LogCommitFileInfo> logCommitFiles;
//		int countcfs = 0;
//		System.out.println(projectName+": Extracting logCommitFiles...");
//		String fileName = projectName.replace('/', '-')+".txt";
//		BufferedReader br = new BufferedReader(new FileReader(localPath+fileName));
//		String sCurrentLine;
//		String[] values;
//
//		while ((sCurrentLine = br.readLine()) != null) {
//			values = sCurrentLine.split(";");
//			String sha = values[0];
//			if (map.containsKey(sha))
//				logCommitFiles = map.get(sha);
//			else{
//				logCommitFiles = new ArrayList<LogCommitFileInfo>();
//				map.put(sha, logCommitFiles);
//			}
//			logCommitFiles.add(new LogCommitFileInfo(values[1], values[2], values[3]));
//		}
//		br.close();
//		return map;
//	}
	
	private void insertFiles(String projectName, Map<String, LogCommitInfo> mapCommit) throws IOException{
		System.out.println(projectName+": Extracting logCommitFiles...");
		String fileName = projectName.replace('/', '-')+".txt";
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
