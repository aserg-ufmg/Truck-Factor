package aserg.gtf.task;

import java.io.IOException;
import java.util.List;

import aserg.gtf.model.NewFileInfo;

public abstract class AbstractTask <T>{
	protected String repositoryPath;
	protected String repositoryName;
	protected String fileName;

	public AbstractTask(String repositoryPath, String repositoryName) {
		super();
		this.repositoryPath = repositoryPath;
		this.repositoryName = repositoryName;
	}
	
	public AbstractTask(String fileName, String repositoryPath, String repositoryName) {
		super();
		this.fileName = fileName;
		this.repositoryPath = repositoryPath;
		this.repositoryName = repositoryName;
	}
	
	public abstract T execute() throws Exception;
	public abstract void persist(T objects) throws IOException;
}
