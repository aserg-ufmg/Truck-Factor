package aserg.gtf.model.authorship;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class AuthorshipInfo implements Comparable<AuthorshipInfo>{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	@ManyToOne(cascade = { CascadeType.REFRESH })
	private File file;
	@ManyToOne(cascade = { CascadeType.ALL })
	private Developer developer;
	private boolean firstAuthor;
	private boolean secondaryAuthor;
	private int nDeliveries;
	private int nAddDeliveries;
	private double doa;
	private double doaMultAuthor;
	private double doaAddDeliveries;
	
	public AuthorshipInfo() {
	}
	
	// Criteria for file authorship
	public boolean isDOAAuthor() {
		double bestFileDOA =  this.file.getBestDoaMult();
		double normDOA = doaMultAuthor/bestFileDOA;
		if (normDOA > 0.75 &&  (doaMultAuthor >= 3.293 || firstAuthor || secondaryAuthor))
			return true;		
		else
			return false;
	}
	// Ignore extra adds
	public double getDOA(){
		if (doa == 0 )
			doa = 3.293 + 1.098*(firstAuthor?1:0) + 0.164*nDeliveries - 0.321 * Math.log(1 + this.getnAcceptances());
		return doa;
	}

	// Take every new add for a new developer as first author. Ignore multiple Adds from the same developer.
	public double getDoaMultAuthor() {
		if (doaMultAuthor == 0 )
			doaMultAuthor = 3.293 + 1.098*(firstAuthor?1:(secondaryAuthor?1:0)) + 0.164*nDeliveries - 0.321 * Math.log(1 + this.getnAcceptances());
		return doaMultAuthor;
	}
	
	// Take every new add for a new developer as first author. Ignore multiple Adds from the same developer.
	public double getDoaAddDeliveries() {
		if (doaAddDeliveries == 0 )
			doaAddDeliveries = 3.293 + 1.098*(firstAuthor?1:0) + 0.164*(nDeliveries + nAddDeliveries) - 0.321 * Math.log(1 + this.getnAcceptancesWithAdds());
		return doaAddDeliveries;
	}
	
	public AuthorshipInfo(File file, Developer developer) {
		super();
		this.file = file;
		this.developer = developer;
		this.nDeliveries = 0;

		developer.addAuthorshipInfo(this);
		file.addAuthorshipInfo(this);
	}

	public void updateDOA(){
		this.doa = 3.293 + 1.098*(firstAuthor?1:(secondaryAuthor?1:0)) + 0.164*nDeliveries - 0.321 * Math.log(1 + this.getnAcceptances());
		this.doaMultAuthor = 3.293 + 1.098*(firstAuthor?1:(secondaryAuthor?1:0)) + 0.164*nDeliveries - 0.321 * Math.log(1 + this.getnAcceptances());
		this.doaAddDeliveries = 3.293 + 1.098*(firstAuthor?1:0) + 0.164*(nDeliveries + nAddDeliveries) - 0.321 * Math.log(1 + this.getnAcceptancesWithAdds());
		if (this.file.getBestDoa()<doa)
			this.file.setBestAuthorshipInfo(this);
		if (this.file.getBestDoaMult()<doaMultAuthor)
			this.file.setBestAuthorshipInfoMult(this);
		if (this.file.getBestDoaAddDeliveries()<doa)
			this.file.setBestAuthorshipAddDeliveries(this);
			
	}

	public void addNewDelivery(){
		this.nDeliveries++;
		this.file.addNewChange();
	}
	
	public void addNewAddDelivery(){
		this.nAddDeliveries++;
		this.file.addExtraAdds();
	}
	

	public Long getId() {
		return id;
	}
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public Developer getDeveloper() {
		return developer;
	}
	public void setDeveloper(Developer developer) {
		this.developer = developer;
	}
	public boolean isFirstAuthor() {
		return firstAuthor;
	}
	public void setFirstAuthor(boolean firstAdd) {
		this.firstAuthor = firstAdd;
	}
	public int getnDeliveries() {
		return nDeliveries;
	}
	public void setnDeliveries(int nDeliveries) {
		this.nDeliveries = nDeliveries;
	}
	public int getnAcceptances() {
		return this.file.getnChanges() - this.nDeliveries;
	}
	public int getnAcceptancesWithAdds() {
		return this.file.getnChanges() + this.file.getnExtraAdds() - this.nDeliveries - this.nAddDeliveries;
	}


	public void setAsFirstAuthor() {
		this.firstAuthor = true;		
	}

	@Override
	public int compareTo(AuthorshipInfo o) {
		return Double.compare(this.getDOA(), o.getDOA());
	}

	public boolean isSecondaryAuthor() {
		return secondaryAuthor;
	}

	public void setAsSecondaryAuthor() {
		this.secondaryAuthor = true;
	}

	public int getnAddDeliveries() {
		return nAddDeliveries;
	}

	public void setnAddDeliveries(int nAddDeliveries) {
		this.nAddDeliveries = nAddDeliveries;
	}




	
}
