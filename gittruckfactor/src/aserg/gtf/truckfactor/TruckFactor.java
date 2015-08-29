package aserg.gtf.truckfactor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aserg.gtf.dao.authorship.FileDAO;
import aserg.gtf.dao.authorship.RepositoryDAO;
import aserg.gtf.model.authorship.File;
import aserg.gtf.model.authorship.Repository;

public abstract class TruckFactor {
	
	public TruckFactor() {
	}
	
	public abstract Map<Integer, Float> getTruckFactor(Repository repository);
	
	
}
