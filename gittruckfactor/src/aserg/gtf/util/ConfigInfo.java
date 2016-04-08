package aserg.gtf.util;

public class ConfigInfo {
	private float normalizedDOA;
	private float absoluteDOA;
	private float tfCoverage;
	
	public ConfigInfo(float normalizedDOA, float absoluteDOA, float tfCoverage) {
		super();
		this.normalizedDOA = normalizedDOA;
		this.absoluteDOA = absoluteDOA;
		this.tfCoverage = tfCoverage;
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
	
	
}
