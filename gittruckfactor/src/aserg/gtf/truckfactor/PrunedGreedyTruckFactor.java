package aserg.gtf.truckfactor;

import java.util.ArrayList;
import java.util.Collections;
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

public class PrunedGreedyTruckFactor extends TruckFactor {
	private static final Logger LOGGER = Logger.getLogger(TruckFactor.class);

	private TFInfo tfInfo = new TFInfo();
	private float minPercentage;
	
	public PrunedGreedyTruckFactor(float minPercentage) {
		this.minPercentage = minPercentage; 
	}
	
	@Override
	public TFInfo getTruckFactor(Repository repository) {
		Map<Developer, Set<File>> authorsMap = getFilesAuthorMap(repository);
		//GREDDY TRUCK FACTOR ALGORITHM		
		int repFilesSize = repository.getFiles().size();
		int factor = 0;
		float coverage = 1;
		int nFilesTop1Dev = getNumFilesTopDev(authorsMap);
		while(authorsMap.size()>0){
			coverage = getCoverage(repFilesSize, authorsMap);
			if (coverage<0.5)
				break;			
			removeTopAuthor(repFilesSize, authorsMap);
			factor++;
		}
		tfInfo.setCoverage(getCoverage(repFilesSize, authorsMap));
		tfInfo.setTf(factor);
		tfInfo.setTotalFiles(repFilesSize);
		
		return pruneTF(tfInfo);
	}
	
	private TFInfo pruneTF(TFInfo tfInfo) {
		Developer topDev = getTopOneDev(tfInfo.getTfDevelopers());
		List<Developer> prunedDevs = new ArrayList<Developer>();
		for (Developer developer : tfInfo.getTfDevelopers()) {
			if ((float)developer.getAuthorshipFiles().size()/topDev.getAuthorshipFiles().size()<minPercentage)
				prunedDevs.add(developer);
		}
		for (Developer developer : prunedDevs) {
			tfInfo.getTfDevelopers().remove(developer);
		}
		tfInfo.setTf(tfInfo.getTf()-prunedDevs.size());
		return tfInfo;
	}

	private Developer getTopOneDev(List<Developer> tfDevelopers) {
		Developer topDev = null;
		for (Developer developer : tfDevelopers) {
			if (topDev ==null)
				topDev = developer;
			else if (developer.getAuthorshipFiles().size()>topDev.getAuthorshipFiles().size())
				topDev = developer;
		}
		return topDev;
	}

	private int getNumFilesTopDev(Map<Developer, Set<File>> authorsMap) {
		int maxFiles = 0;
		for (Set<File> files : authorsMap.values()) {
			if (files.size()>maxFiles)
				maxFiles = files.size();
		}
		return maxFiles;
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
			if (biggerDev!=null && entry.getValue().size()==biggerNumber)
				if(entry.getKey().getDevChanges() > biggerDev.getDevChanges())
					biggerDev = entry.getKey();
			
			
		}
		tfInfo.addDeveloper(biggerDev);
		authorsMap.remove(biggerDev);		
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
