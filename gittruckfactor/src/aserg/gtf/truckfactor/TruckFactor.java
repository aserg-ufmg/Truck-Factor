package aserg.gtf.truckfactor;

import aserg.gtf.model.authorship.Repository;

public abstract class TruckFactor {
	
	public TruckFactor() {
	}
	
	public abstract TFInfo getTruckFactor(Repository repository);
	
	
}
