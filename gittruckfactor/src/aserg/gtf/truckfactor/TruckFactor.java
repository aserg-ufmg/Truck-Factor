package aserg.gtf.truckfactor;

import java.util.Map;

import aserg.gtf.model.authorship.Repository;

public abstract class TruckFactor {
	
	public TruckFactor() {
	}
	
	public abstract Map<Integer, Float> getTruckFactor(Repository repository);
	
	
}
