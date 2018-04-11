package aserg.gtf.util;

public class ConfigInfo {
	private float normalizedDOA;
	private float absoluteDOA;
	private float tfCoverage;
	private float minPercentage;
	
	public ConfigInfo(float normalizedDOA, float absoluteDOA, float tfCoverage, float minPercentage) {
		super();
		this.normalizedDOA = normalizedDOA;
		this.absoluteDOA = absoluteDOA;
		this.tfCoverage = tfCoverage;
		this.minPercentage = minPercentage;
	}
	
	public float getNormalizedDOA() {
		return normalizedDOA;
	}
	public float getAbsoluteDOA() {
		return absoluteDOA;
	}
	public float getTfCoverage() {
		return tfCoverage;
	}

	public float getMinPercentage() {
		return minPercentage;
	}

	
	
}
