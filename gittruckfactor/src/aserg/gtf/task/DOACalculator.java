package aserg.gtf.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import aserg.gtf.dao.authorship.RepositoryDAO;
import aserg.gtf.model.LogCommitFileInfo;
import aserg.gtf.model.LogCommitInfo;
import aserg.gtf.model.NewFileInfo;
import aserg.gtf.model.Status;
import aserg.gtf.model.authorship.AuthorshipInfo;
import aserg.gtf.model.authorship.File;
import aserg.gtf.model.authorship.Repository;
import aserg.gtf.model.authorship.RepositoryStatus;
import aserg.gtf.util.ConfigInfo;

public class DOACalculator extends AbstractTask<Repository>{
	private static final Logger LOGGER = Logger.getLogger(DOACalculator.class);
	private Collection<LogCommitInfo> commits;
	private List<NewFileInfo> files;

	public DOACalculator(String repositoryPath, String repositoryName, Collection<LogCommitInfo> commits,
			List<NewFileInfo> files) {
		super(repositoryPath, repositoryName);
		this.commits = commits;
		this.files = files;
	}

	@Override
	public Repository execute() throws IOException {
		LOGGER.info(repositoryName + ": Extracting and calculating authorship information...");
		Repository repository = new Repository(repositoryName);
		repository.setFiles(getFiles(repository, commits, files));
		return repository;
	}

	@Override
	public void persist(Repository repository) throws IOException {
		RepositoryDAO reDAO = new RepositoryDAO();
		try{
			repository.setStatus(RepositoryStatus.DOA_CALCULATED);
			reDAO.persist(repository);
		}
		catch(Exception e){
			LOGGER.error("Persist error in repository " + repository.getFullName(), e);
		} 
		finally{
			reDAO.clear();
		}
	}

	private static List<File> getFiles(Repository repository, Collection<LogCommitInfo> commitFiles, List<NewFileInfo> filesInfo) {
		List<File> tempfiles = new ArrayList<File>();		
		Map<String,List<LogCommitFileInfo>> mapFiles = getCommitFilesMap(commitFiles);
		for (NewFileInfo fileInfo : filesInfo) {
			if (!fileInfo.getFiltered()){
				File file = new File(fileInfo.getPath());
				if (setFileHistory(file, repository, fileInfo, mapFiles))
					tempfiles.add(file);
			}
		}		
		return tempfiles;
	}
	
	private static Map<String, List<LogCommitFileInfo>> getCommitFilesMap(Collection<LogCommitInfo> commits) {
		Map<String, List<LogCommitFileInfo>> map = new HashMap<String, List<LogCommitFileInfo>>();
		for (LogCommitInfo commitInfo : commits) {
			if (commitInfo.getLogCommitFiles() != null){
				for (LogCommitFileInfo commitFile : commitInfo.getLogCommitFiles()) {
					String fileName = commitFile.getNewFileName();
					if (!map.containsKey(fileName))
						map.put(fileName, new ArrayList<LogCommitFileInfo>());
					map.get(fileName).add(commitFile);
				}
			}
		}
		return map;
	}
	

	private static boolean setFileHistory(File file, Repository repository, NewFileInfo fileInfo, Map<String, List<LogCommitFileInfo>> mapFiles) {
		List<LogCommitFileInfo> fileCommits = mapFiles.get(fileInfo.getPath());
		// Rarely, but some files do not have any commit. Should be verified each case to understand the impact. Normally is irrelevant. 
		if (fileCommits == null){
			LOGGER.warn("No commits for " + file);
			return false;
		}
			
		List<LogCommitFileInfo> logFilesObjectInfo = expandCommitFileList(fileCommits, mapFiles);
		String firstAuthor = null;
		for (LogCommitFileInfo commitFile : logFilesObjectInfo) {
			//ci.name, ci.email, lcfi.oldfilename, lcfi.newfilename, lcfi.status, lcfi.id, username
			LogCommitInfo commitInfo = commitFile.getCommitInfo();
			AuthorshipInfo authorshipInfo = repository.getAuthorshipInfo(commitInfo.getAuthorName(), commitInfo.getAuthorEmail(), commitInfo.getUserName(),  file);
			Status status = commitFile.getStatus();
			
			if (status == Status.ADDED){
				if (firstAuthor == null){ //FIRST ADD
					firstAuthor = authorshipInfo.getDeveloper().getNewUserName();
					authorshipInfo.setAsFirstAuthor();
				}
				else if (!authorshipInfo.isFirstAuthor()){ //New ADD made by a different developer of the first add
					String debugStr = String.format("New add;%s;%s;%s;%s", repository.getFullName(), file.getPath(), firstAuthor, authorshipInfo.getDeveloper().getNewUserName());
					LOGGER.debug(debugStr);
					authorshipInfo.setAsSecondaryAuthor();
					authorshipInfo.addNewAddDelivery();
				}
				else{ //Treat as delivery if the extra add was made by the first author 
					authorshipInfo.addNewAddDelivery();
				}
					
				
			}
			else if (status == Status.MODIFIED){
				authorshipInfo.addNewDelivery();
				//file.addNewChange();					
			}
			else if (status == Status.RENAMED_TREATED){
				// Considering a rename as a new delivery
				authorshipInfo.addNewDelivery();
				//file.addNewChange();		
			}
			else System.err.println("Invalid Status: "+ status);
		}
		
		double bestDoaValue = 0;
		for (AuthorshipInfo authorshipInfo : file.getAuthorshipInfos()) {
			double authorshipDoa = authorshipInfo.getDOA();
			if (authorshipDoa > bestDoaValue){
				bestDoaValue = authorshipDoa;
				file.setBestAuthorshipInfo(authorshipInfo);
			}	
		}
		double bestDoaValueMult = 0;
		for (AuthorshipInfo authorshipInfo : file.getAuthorshipInfos()) {
			double authorshipDoaMult = authorshipInfo.getDoaMultAuthor();
			if (authorshipDoaMult > bestDoaValueMult){
				bestDoaValueMult = authorshipDoaMult;
				file.setBestAuthorshipInfoMult(authorshipInfo);
			}	
		}
		double bestDoaValueAddDeliveries = 0;
		for (AuthorshipInfo authorshipInfo : file.getAuthorshipInfos()) {
			double authorshipDoaAddDeliveries = authorshipInfo.getDoaAddDeliveries();
			if (authorshipDoaAddDeliveries > bestDoaValueAddDeliveries){
				bestDoaValueAddDeliveries = authorshipDoaAddDeliveries;
				file.setBestAuthorshipAddDeliveries(authorshipInfo);
			}	
		}
		
		return true;
	}
	
	/** Expand commitsfile list by adding renames history 
	 *  Only used when renames are not treated using git facilities */
	private static List<LogCommitFileInfo> expandCommitFileList(
			List<LogCommitFileInfo> logCommitFiles, Map<String, List<LogCommitFileInfo>> mapFiles) {
		
		Map<String, LogCommitFileInfo> map =  new HashMap<String, LogCommitFileInfo>();

		for (LogCommitFileInfo commitFile : logCommitFiles)
			map.put(commitFile.getTempId().toString(), commitFile);
 		
 		while (hasRenamed(map.values())) {
 			List<LogCommitFileInfo> tempCommitFiles = new ArrayList<LogCommitFileInfo>(map.values());
			for (LogCommitFileInfo commitFile : tempCommitFiles) {
				
				Status status = commitFile.getStatus();
				if (status == Status.RENAMED) {
					List<LogCommitFileInfo> newList = mapFiles.get(commitFile.getOldFileName());
					// Some renamed files do not have any commit, even in GitHub.
					if (newList==null) {
						LOGGER.warn("ERROR in RENAME: No commits for old filename: " + commitFile.getOldFileName());
					}
					else{
						for (LogCommitFileInfo newCommitFile : newList) {
							if (!map.containsKey(newCommitFile.getTempId()
									.toString()))
								map.put(newCommitFile.getTempId().toString(),
										newCommitFile);
						}
					}
					commitFile.setStatus(Status.RENAMED_TREATED);
				}
			}
		}	
		
		return new ArrayList<LogCommitFileInfo>(map.values());
	}


	
	
	

	private static boolean hasRenamed(Collection<LogCommitFileInfo> commitFiles) {
		for (LogCommitFileInfo commitFile : commitFiles) {
			if (commitFile.getStatus() == Status.RENAMED)
				return true;
		}
		return false;
	}
	
	private static void printRepository(Repository repo) {
		System.out.println("Repository: "+repo.getFullName());
		for (File file : repo.getFiles()) {
			printFile(file);
		}
		
	}

	private static void printFile(File file) {
		System.out.println("--File: "+file.getPath());
		Collections.sort(file.getAuthorshipInfos());
		Collections.reverse(file.getAuthorshipInfos());
		for (AuthorshipInfo authorshioinfo : file.getAuthorshipInfos()) {
			printAuthorshipInfo(authorshioinfo);
		}
		
	}

	private static void printAuthorshipInfo(AuthorshipInfo authorshioinfo) {
		System.out.format("---- %s: %b - %d - %d - (%f)\n", 
				authorshioinfo.getDeveloper().getNewUserName(),
				authorshioinfo.isFirstAuthor(), 
				authorshioinfo.getnDeliveries(),
				authorshioinfo.getnAcceptances(),
				authorshioinfo.getDOA());
		
	}

	

	
	
}
