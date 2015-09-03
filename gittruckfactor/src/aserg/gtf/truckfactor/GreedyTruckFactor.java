package aserg.gtf.truckfactor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.Query;

import aserg.gtf.model.authorship.AuthorshipInfo;
import aserg.gtf.model.authorship.Developer;
import aserg.gtf.model.authorship.File;
import aserg.gtf.model.authorship.FileAuthors;
import aserg.gtf.model.authorship.Repository;
import aserg.gtf.task.DOACalculator;

import com.google.common.collect.Sets;

public class GreedyTruckFactor extends TruckFactor {

	private List<String> tfAuthorInfo = new ArrayList<String>();
	
	@Override
	public Map<Integer, Float> getTruckFactor(Repository repository) {
		Map<Integer, Float> trucKFactorMap = new HashMap<Integer, Float>();
		Set<File> repFiles =  new HashSet<File>(repository.getFiles());
		Map<Developer, Set<File>> authorsMap = getFilesAuthorMap(repository);
		int factor = 0;
		Set<File> clonedRepFiles = new  HashSet<File>(repFiles);
		while(authorsMap.size()>0){
			Float coverage = getCoverage(repFiles, authorsMap);
			trucKFactorMap.put(factor++, coverage);
			if (coverage<0.5)
				break;			
			removeBig(clonedRepFiles, authorsMap);
		}
//		printAuthorsMap(authorsMap);
//		printTruckMap(repository.getFullName(), trucKFactorMap);
		printTF(repository.getFullName(), trucKFactorMap);
		return trucKFactorMap;
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

	private float getCoverage(Set<File> repFiles, Map<Developer, Set<File>> authorsMap) {
		Set<File> authorsSet = new HashSet<File>();
		for (Entry<Developer, Set<File>> entry : authorsMap.entrySet()) {
			for (File file : entry.getValue()) {
				authorsSet.add(file);
				if(authorsSet.size()==repFiles.size())
					return 1f;
			}
		}
		return (float)authorsSet.size()/repFiles.size();
	}

	private void removeBig(Set<File> repFiles, Map<Developer, Set<File>> authorsMap) {
		int biggerNumber = 0;
		Developer biggerDev = null;
		for (Entry<Developer, Set<File>> entry : authorsMap.entrySet()) {
			if (entry.getValue().size()>biggerNumber){
				biggerNumber = entry.getValue().size();
				biggerDev = entry.getKey();
			}
		}
		tfAuthorInfo.add(String.format("%s;%d;%.2f", biggerDev.getName(), biggerNumber, ((float)biggerNumber)/repFiles.size()*100));
//		System.out.format("removed: %s:%s - %d (%.2f)\n" , biggerDev.getName(), biggerDev.getNewUserName(), biggerNumber, ((float)biggerNumber)/repFiles.size());
		//printAuthorsFile(authorsMap.get(biggerDev));
		authorsMap.remove(biggerDev);
		
	}
	
	
	

	private void printAuthorsFile(Set<File> set) {
		for (File file : set) {
			System.out.println(file.getPath());
		}
		
	}

	private void removeBest(Set<Long> repFiles, Map<Long, Set<Long>> authorsMap) {
		int bestIntersection = 0;
		Long bestDev = null;
		for (Entry<Long, Set<Long>> entry : authorsMap.entrySet()) {
			int intersection = Sets.intersection(repFiles,entry.getValue()).size();
			if (intersection>bestIntersection){
				bestIntersection = intersection;
				bestDev = entry.getKey();
			}
		}
		authorsMap.remove(bestDev);
		
	}
	
	private void removeBest2(Set<Long> clonedRepFiles, Map<Long, Set<Long>> authorsMap) {
		int bestIntersection = 0;
		Long bestDev = null;
		for (Entry<Long, Set<Long>> entry : authorsMap.entrySet()) {
			int intersection = Sets.intersection(clonedRepFiles,entry.getValue()).size();
			if (intersection>bestIntersection){
				bestIntersection = intersection;
				bestDev = entry.getKey();
			}
		}
		clonedRepFiles.remove(authorsMap.get(bestDev));
		authorsMap.remove(bestDev);
		
	}
			

	private void printAuthorsMap(Map<Long, Set<Long>> authorsMap) {
		for (Entry<Long, Set<Long>> entry : authorsMap.entrySet()) {
			System.out.print(entry.getKey() + ": ");
			for (Long fileId : entry.getValue()) {
				System.out.print(fileId + " ");
			}
			System.out.println();
		}
		
	}
	
	private void printTruckMap(String repName, Map<Integer, Float> truckMap) {
		Date date = new Date();
		for (Entry<Integer, Float> entry : truckMap.entrySet()) {
			System.out.format("%s;%d;%f\n", repName, entry.getKey(), entry.getValue());
			
		}
		System.out.println("TF Developers: ");
		for (String tfInfo : tfAuthorInfo) {
			System.out.println(tfInfo);
		}
	}
	private void printTF(String repName, Map<Integer, Float> truckMap) {
		float coverage = truckMap.size() == 1 ? 0f : truckMap.get(tfAuthorInfo.size())*100;
		System.out.format("TF = %d (coverage = %.2f%%)\nTF authors (Developer;Files;Percentage):\n", tfAuthorInfo.size(), coverage);
		for (String tfInfo : tfAuthorInfo) {
			System.out.println(tfInfo);
		}
	}

}
