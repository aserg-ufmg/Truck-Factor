package aserg.gtf.truckfactor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import aserg.gtf.GitTruckFactor;
import aserg.gtf.model.authorship.AuthorshipInfo;
import aserg.gtf.model.authorship.Developer;
import aserg.gtf.model.authorship.File;
import aserg.gtf.model.authorship.Repository;

public class GreedyTruckFactor extends TruckFactor {
	private static final Logger LOGGER = Logger.getLogger(TruckFactor.class);

	private List<String> tfAuthorInfo = new ArrayList<String>();
	
	@Override
	public int getTruckFactor(Repository repository) {
		Map<Integer, Float> trucKFactorMap = new HashMap<Integer, Float>();
		Map<Developer, Set<File>> authorsMap = getFilesAuthorMap(repository);
		
		//GREDDY TRUCK FACTOR ALGORITHM		
		int repFilesSize = repository.getFiles().size();
		int factor = 0;
		while(authorsMap.size()>0){
			Float coverage = getCoverage(repFilesSize, authorsMap);
			trucKFactorMap.put(factor, coverage);
			if (coverage<GitTruckFactor.config.getTfCoverage())
				break;			
			removeTopAuthor(repFilesSize, authorsMap);
			factor++;
		}
		printTF(repository.getFullName(), trucKFactorMap);
		return factor;
	}
	
	private Map<Developer, Set<File>> getFilesAuthorMap(Repository repository){
		Map<Developer, Set<File>> map = new HashMap<Developer, Set<File>>();
		List<Developer> developers = repository.getDevelopers();
		for (Developer developer : developers) {
			Set<File> devFiles = new HashSet<File>();
			List<AuthorshipInfo> authorships = developer.getAuthorshipInfos();
			for (AuthorshipInfo authorshipInfo : authorships) {
				if (authorshipInfo.isDOAAuthor())
					devFiles.add(authorshipInfo.getFile());		
				
			}
			if (devFiles.size()>0)
				map.put(developer, devFiles);
		}
		return map;
	}

	private float getCoverage(int repFilesSize, Map<Developer, Set<File>> authorsMap) {
		Set<File> authorsSet = new HashSet<File>();
		for (Entry<Developer, Set<File>> entry : authorsMap.entrySet()) {
			for (File file : entry.getValue()) {
				authorsSet.add(file);
				if(authorsSet.size()==repFilesSize)
					return 1f;
			}
		}
		return (float)authorsSet.size()/repFilesSize;
	}

	private void removeTopAuthor(int repFilesSize, Map<Developer, Set<File>> authorsMap) {
		int biggerNumber = 0;
		Developer biggerDev = null;
		for (Entry<Developer, Set<File>> entry : authorsMap.entrySet()) {
			if (entry.getValue().size()>biggerNumber){
				biggerNumber = entry.getValue().size();
				biggerDev = entry.getKey();
			}
		}
		tfAuthorInfo.add(String.format("%s;%d;%.2f", biggerDev.getName(), biggerNumber, ((float)biggerNumber)/repFilesSize*100));
		authorsMap.remove(biggerDev);		
	}
	
	
	private void printTF(String repName, Map<Integer, Float> truckMap) {
		if (!truckMap.isEmpty()){
			float coverage = truckMap.size() == 1 ? 0f : truckMap.get(tfAuthorInfo.size())*100;
			System.out.format("TF = %d (coverage = %.2f%%)\nTF authors (Developer;Files;Percentage):\n", tfAuthorInfo.size(), coverage);
			for (String tfInfo : tfAuthorInfo) {
				System.out.println(tfInfo);
			}
		}
		else
			LOGGER.error("No authorship information enough to calculate the TF. Verify the number of files and commit available after filter steps");
	}
	
//	//HELP METHODS:  Used only for tests propose 
	
//	private void printAuthorsFile(Set<File> set) {
//		for (File file : set) {
//			System.out.println(file.getPath());
//		}
//		
//	}
//
//	private void printAuthorsMap(Map<Long, Set<Long>> authorsMap) {
//		for (Entry<Long, Set<Long>> entry : authorsMap.entrySet()) {
//			System.out.print(entry.getKey() + ": ");
//			for (Long fileId : entry.getValue()) {
//				System.out.print(fileId + " ");
//			}
//			System.out.println();
//		}
//		
//	}
//	
//	private void printTruckMap(String repName, Map<Integer, Float> truckMap) {
//		Date date = new Date();
//		for (Entry<Integer, Float> entry : truckMap.entrySet()) {
//			System.out.format("%s;%d;%f\n", repName, entry.getKey(), entry.getValue());
//			
//		}
//		System.out.println("TF Developers: ");
//		for (String tfInfo : tfAuthorInfo) {
//			System.out.println(tfInfo);
//		}
//	}
	
}
