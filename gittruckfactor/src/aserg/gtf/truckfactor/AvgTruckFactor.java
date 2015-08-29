package aserg.gtf.truckfactor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aserg.gtf.dao.authorship.FileDAO;
import aserg.gtf.dao.authorship.RepositoryDAO;
import aserg.gtf.model.authorship.File;
import aserg.gtf.model.authorship.Repository;

public class AvgTruckFactor extends TruckFactor{
	
	public Map<Integer, Float> getTruckFactor(Repository repository) {
		Map<Integer, Double> truckDist =  new HashMap<Integer, Double>();
		
//		List<File> files =  repDAO.getFiles(repName);
//		int numDevelopers = repDAO.getNumDevelopers();
		return null;
	}
	
	
}
