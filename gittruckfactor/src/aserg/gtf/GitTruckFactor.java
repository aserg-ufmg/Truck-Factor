package aserg.gtf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import aserg.gtf.model.LogCommitInfo;
import aserg.gtf.model.NewFileInfo;
import aserg.gtf.model.authorship.AuthorshipInfo;
import aserg.gtf.model.authorship.Developer;
import aserg.gtf.model.authorship.Repository;
import aserg.gtf.task.AliasHandler;
import aserg.gtf.task.DOACalculator;
import aserg.gtf.task.NewAliasHandler;
import aserg.gtf.task.extractor.FileInfoExtractor;
import aserg.gtf.task.extractor.GitLogExtractor;
import aserg.gtf.task.extractor.LinguistExtractor;
import aserg.gtf.truckfactor.GreedyTruckFactor;
import aserg.gtf.truckfactor.TruckFactor;
import aserg.gtf.util.Alias;
import aserg.gtf.util.FileInfoReader;
import aserg.gtf.util.LineInfo;

public class GitTruckFactor {

	public static void main(String[] args) throws IOException {
		String repositoryPath = "";
		String repositoryName = "";
		if (args.length>0)
			repositoryPath = args[0];
		if (args.length>1)
			repositoryName = args[1];
		
		
		repositoryPath = (repositoryPath.charAt(repositoryPath.length()-1) == '/') ? repositoryPath : (repositoryPath + "/");
		if (repositoryName.isEmpty())
			repositoryName = repositoryPath.split("/")[repositoryPath.split("/").length-1];
		

		Map<String, List<LineInfo>> filesInfo = FileInfoReader.getFileInfo("filtered-files.txt");
		Map<String, List<LineInfo>> aliasInfo = FileInfoReader.getFileInfo("alias.txt");
		Map<String, List<LineInfo>> modulesInfo = FileInfoReader.getFileInfo("modules.txt");
		
		GitLogExtractor gitLogExtractor = new GitLogExtractor(repositoryPath, repositoryName);	
		NewAliasHandler aliasHandler = new NewAliasHandler(aliasInfo.get(repositoryName));
		FileInfoExtractor fileExtractor = new FileInfoExtractor(repositoryPath, repositoryName);
		LinguistExtractor linguistExtractor =  new LinguistExtractor(repositoryPath, repositoryName);
		
		
		System.out.println("BEGIN at "+ new Date() + "\n\n");
		
		//gitLogExtractor.persist(commits);
		
		//printCoverageInTime(repositoryPath, repositoryName, filesInfo,	fileExtractor, linguistExtractor, gitLogExtractor,aliasHandler);
		
		calculateTF(repositoryPath, repositoryName, filesInfo, modulesInfo,	fileExtractor, linguistExtractor, gitLogExtractor,aliasHandler);
		

		
		
		System.out.println("\n\nEND at "+ new Date());
	}

	private static void printCoverageInTime(String repositoryPath,
			String repositoryName, Map<String, List<LineInfo>> filesInfo,
			FileInfoExtractor fileExtractor,
			LinguistExtractor linguistExtractor,
			GitLogExtractor gitLogExtractor, NewAliasHandler aliasHandler) throws IOException {
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2015);
		cal.set(Calendar.MONTH, Calendar.FEBRUARY);
		cal.set(Calendar.DAY_OF_MONTH, 25);
		String devsA[] = {"Yoann Delouis", "yDelouis"};
		//String devsA[] = {"WonderCsabo", "Damien"};
 		List<String> devs = Arrays.asList(devsA);
 		

		//cal.add(Calendar.MONTH, -24);

 		for (int i = 0; i < 24; i++) {
 			Map<String, LogCommitInfo> commits = gitLogExtractor.execute();
 			commits = aliasHandler.execute(repositoryName, commits);
 			
 			List<NewFileInfo> files = fileExtractor.execute();
 			files = linguistExtractor.setNotLinguist(files);
 			applyFilterFiles(filesInfo.get(repositoryName), files);
 			
 			Map<String, LogCommitInfo> newCommits = filterCommitsByDate(commits, cal.getTime());
			DOACalculator doaCalculator = new DOACalculator(repositoryPath,	repositoryName, newCommits.values(), files);
			Repository repository = doaCalculator.execute();
			printCoverage(repository, devs, cal);
			
			cal.add(Calendar.MONTH, -1);
		}
	}
	
	private static void calculateTF(String repositoryPath,
			String repositoryName, 
			Map<String, List<LineInfo>> filesInfo, 
			Map<String, List<LineInfo>> modulesInfo,
			FileInfoExtractor fileExtractor,
			LinguistExtractor linguistExtractor,
			GitLogExtractor gitLogExtractor, NewAliasHandler aliasHandler) throws IOException {
		
			Map<String, LogCommitInfo> commits = gitLogExtractor.execute();
 			commits = aliasHandler.execute(repositoryName, commits);
 			 				
			List<NewFileInfo> files = fileExtractor.execute();
			files = linguistExtractor.setNotLinguist(files);		
			applyFilterFiles(filesInfo.get(repositoryName), files);
			
			if(modulesInfo.containsKey(repositoryName))
				setModules(modulesInfo.get(repositoryName), files);
			
			//filterByModule(files, "net");
			
			//applyRegexFilter(files, "^Library/Formula/.*");
			//applyRegexSelect(files, "^kernel/.*");
			
			
			//fileExtractor.persist(files);
			
			DOACalculator doaCalculator = new DOACalculator(repositoryPath, repositoryName, commits.values(), files);
			Repository repository = doaCalculator.execute();
			//doaCalculator.persist(repository);
			
			TruckFactor truckFactor = new GreedyTruckFactor();
			truckFactor.getTruckFactor(repository);
			
	}

	private static void filterModule(List<NewFileInfo> files, String module) {
		int count = 0;
		for (NewFileInfo file : files) {
			if (file.getModule().equals(module)){
				file.setFiltered(true);
				file.setFilterInfo("Module-filter");
			}
			else if (!file.getFiltered())
				count++;
		}
		System.out.println("Files without "+module + ": "+count);
	}
	private static void filterByModule(List<NewFileInfo> files, String module) {
		int count = 0;
		for (NewFileInfo file : files) {
			if (!file.getModule().equals(module)){
				file.setFiltered(true);
				file.setFilterInfo("Module-filter");
			}
			else if (!file.getFiltered())
				count++;
		}
		System.out.println(module + " - files: " +count);
	}

	private static void setModules(List<LineInfo> modulesInfo,
			List<NewFileInfo> files) {
		Map<String, String> moduleMap =  new HashMap<String, String>();
		for (LineInfo lineInfo : modulesInfo) {
			moduleMap.put(lineInfo.getValues().get(0), lineInfo.getValues().get(1));
		}
		for (NewFileInfo newFileInfo : files) {
			if (moduleMap.containsKey(newFileInfo.getPath()))
				newFileInfo.setModule(moduleMap.get(newFileInfo.getPath()));
			else
				System.out.println("Alert: module not found for file "+newFileInfo.getPath());
		}
		
	}

	private static void printCoverage(Repository repository, List<String> devsName, Calendar cal) {
		int nFiles = getTotalFiles(repository.getDevelopers());
		Set<String> devsFiles =  new HashSet<String>();
		for (String devName : devsName) {
			for (Developer developer : repository.getDevelopers()) {
				if (developer.getName().equalsIgnoreCase(devName)){
					devsFiles.addAll(getAuthorFiles(developer));
					
				}
					
			}
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("Coverage;"+ format.format(cal.getTime()) +";"+ devsFiles.size() +";"+ nFiles +";"+ (float)devsFiles.size()/nFiles);
	}

	private static int getTotalFiles(List<Developer> developers) {
		Set<String> files = new HashSet<String>();
		for (Developer developer : developers) {
			for (AuthorshipInfo authorship : developer.getAuthorshipInfos()) {
				files.add(authorship.getFile().getPath());
			}
		}
		return files.size();
	}

	private static Set<String> getAuthorFiles(Developer developer) {
		Set<String> paths = new HashSet<String>();
		for (AuthorshipInfo authorship : developer.getAuthorshipInfos()) {
			if (authorship.isDOAAuthor())
				paths.add(authorship.getFile().getPath());
		}		
		return paths;
	}

	private static Map<String, LogCommitInfo> filterCommitsByDate(
			Map<String, LogCommitInfo> commits, Date endDate) {
		Map<String, LogCommitInfo> newCommits =  new HashMap<String, LogCommitInfo>(commits);
		for (Entry<String, LogCommitInfo> entry : commits.entrySet()) {
			if (entry.getValue().getAuthorDate().after(endDate))
				newCommits.remove(entry.getKey());
		}
		return newCommits;
	}

	private static void applyRegexFilter(List<NewFileInfo> files, String exp) {
		int count = 0;
		for (NewFileInfo newFileInfo : files) {
			if (!newFileInfo.getFiltered()){
				if (newFileInfo.getPath().matches(exp)){
					count++;
					newFileInfo.setFiltered(true);
					newFileInfo.setFilterInfo("REGEX: "+ exp);
				}			
			}
		}
		System.out.println("REGEX FILTER = " + count);
	}
	
	private static void applyRegexSelect(List<NewFileInfo> files, String exp) {
		int count = 0;
		for (NewFileInfo newFileInfo : files) {
			if (!newFileInfo.getFiltered()){
				if (!newFileInfo.getPath().matches(exp)){
					count++;
					newFileInfo.setFiltered(true);
					newFileInfo.setFilterInfo("REGEX: "+ exp);
				}			
			}
		}
		System.out.println("REGEX FILTER = " + count);
	}

	private static void applyFilterFiles(List<LineInfo> filteredFilesInfo, List<NewFileInfo> files) {
		if (filteredFilesInfo != null ){
			for (LineInfo lineInfo : filteredFilesInfo) {
				String path = lineInfo.getValues().get(0);
				for (NewFileInfo newFileInfo : files) {
					if (newFileInfo.getPath().equals(path)){
						newFileInfo.setFiltered(true);
						newFileInfo.setFilterInfo(lineInfo.getValues().get(1));
						//System.out.println(path);
					}
					
				}
			}
		}
	}

}
