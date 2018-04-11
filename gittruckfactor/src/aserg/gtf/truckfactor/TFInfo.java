package aserg.gtf.truckfactor;

import java.util.ArrayList;
import java.util.List;

import aserg.gtf.model.authorship.Developer;

public class TFInfo {
	private int tf;
	private int totalFiles;
	private float coverage;
	private List<Developer> tfDevelopers;
	
	public TFInfo() {
		super();
		this.tfDevelopers = new ArrayList<Developer>();
	}
	public void setTf(int tf) {
		this.tf = tf;
	}
	public void setTotalFiles(int totalFiles) {
		this.totalFiles = totalFiles;
	}
	public void setCoverage(float coverage) {
		this.coverage = coverage;
	}
	public void addDeveloper(Developer dev){
		tfDevelopers.add(dev);
	}
	
	public int getTf() {
		return tf;
	}
	public int getTotalFiles() {
		return totalFiles;
	}
	public float getCoverage() {
		return coverage;
	}
	public List<Developer> getTfDevelopers() {
		return tfDevelopers;
	}
	@Override
	public String toString() {
		String retStr = String.format("TF = %d (coverage = %.2f%%)\n", tf, coverage*100);
		retStr += "TF authors (Developer;Files;Percentage):\n";
		for (Developer developer : tfDevelopers) {
			int devFiles = developer.getAuthorshipFiles().size();
			retStr += String.format("%s;%d;%.2f\n",developer.getName(),devFiles,(float)devFiles/totalFiles*100);
		}
		return retStr;
	}
	public String getFormatedInfo(String repository){
		String retStr = String.format("%s;*TF*;%d;%d\n", repository, tf, totalFiles);
		if(Float.isNaN(coverage))
			coverage = 0;
		retStr += String.format("%s;*Coverage*;%.2f;%d\n", repository,  coverage*100, Math.round(totalFiles*coverage));
		
		for (Developer developer : tfDevelopers) {
			int devFiles = developer.getAuthorshipFiles().size();
			retStr += String.format("%s;%s;%.2f;%d\n", repository,  developer.getName(),(float)devFiles/totalFiles*100, devFiles);
		}
		return retStr;
	}
	public String getSimpleFormatedInfo(String repository){
		String retStr = "";
		for (Developer developer : tfDevelopers) {
			int devFiles = developer.getAuthorshipFiles().size();
			retStr += String.format("%s;%s;%.2f;%d**", repository,  developer.getName(),(float)devFiles/totalFiles*100, devFiles);
		}
		return retStr;
	}
	
}
