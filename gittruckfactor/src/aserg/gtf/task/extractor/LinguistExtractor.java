package aserg.gtf.task.extractor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import aserg.gtf.dao.NewFileInfoDAO;
import aserg.gtf.model.NewFileInfo;
import aserg.gtf.task.AbstractTask;

public class LinguistExtractor extends AbstractTask<List<NewFileInfo>>{
	private static final Logger LOGGER = Logger.getLogger(LinguistExtractor.class);
	
	public LinguistExtractor(String repositoryPath, String repositoryName) {
		super("linguistfiles.log", repositoryPath, repositoryName);
	}
	
	
	public List<NewFileInfo> execute() {
		List<NewFileInfo> linguistFiles = new ArrayList<NewFileInfo>();
		try {
			Map<String, List<String>> languageMap = new HashMap<String, List<String>>();
			//System.out.format("%s (%s): Extracting file language information...\n",repositoryName, new Date());
			BufferedReader br = new BufferedReader(new FileReader(repositoryPath
					+ fileName));
			String sCurrentLine;
			String[] values;
			while ((sCurrentLine = br.readLine()) != null) {
				values = sCurrentLine.split(";");
				String language = values[0];
				if (language.contains("\'"))
					language = language.replace("'", "''");
				String path = values[1];				
				linguistFiles.add(new NewFileInfo(repositoryName, path, language));
			}
			br.close();
		} catch (FileNotFoundException e) {
			LOGGER.warn(fileName + " not found. Executing without Linguist filter.");
			linguistFiles = null;
		}
		catch (Exception e) {
			LOGGER.error("Erro no projeto "+repositoryName,e);
			linguistFiles = null;
		}
	
		return linguistFiles;
	}
	
	public List<NewFileInfo> setNotLinguist(List<NewFileInfo> files) throws IOException{

		Map<String, NewFileInfo> linguistFilesMap = new HashMap<String, NewFileInfo>();
		List<NewFileInfo> linguistFiles = execute();
		if (linguistFiles != null) {
			for (NewFileInfo linguistFile : linguistFiles) {
				linguistFilesMap.put(linguistFile.getPath(), linguistFile);
			}
			for (NewFileInfo file : files) {
				if (linguistFilesMap.containsKey(file.getPath())) {
					file.setLanguage(linguistFilesMap.get(file.getPath())
							.getLanguage());
				} else {
					file.setFiltered(true);
					file.setFilterInfo("NOTLINGUIST");
				}
			}
		}
		return files;
	}
	
	
	
	public void extractAndPersist() throws IOException {
		NewFileInfoDAO fiDAO = new NewFileInfoDAO();
		fiDAO.setAllAsNotLinguist();
		List<NewFileInfo> files = execute();
		
		try {
			Map<String, List<String>> languageMap = new HashMap<String, List<String>>();
			System.out.format(
					"%s (%s): Extracting file language information...",
					repositoryName, new Date());
			BufferedReader br = new BufferedReader(new FileReader(repositoryPath
					+ fileName));
			String sCurrentLine;
			String[] values;
			while ((sCurrentLine = br.readLine()) != null) {
				values = sCurrentLine.split(";");
				String language = values[0];
				if (language.contains("\'"))
					language = language.replace("'", "''");
				String path = values[1];
				List<String> paths;
				if (languageMap.containsKey(language))
					paths = languageMap.get(language);
				else {
					paths = new ArrayList<String>();
					languageMap.put(language, paths);
				}
				paths.add(path);
				files.add(new NewFileInfo(repositoryName, path, language));
			}
			br.close();
			for (Entry<String, List<String>> entry : languageMap.entrySet()) {
				fiDAO.updateLanguageFileInfo(
						repositoryName, entry.getKey(),
						entry.getValue());
			}
		} catch (Exception e) {
			System.err.println("Erro no projeto "+repositoryName);
			System.err.println(e.getMessage());
		}
		
		
		
	}

	@Override
	public void persist(List<NewFileInfo> files) throws IOException {
		NewFileInfoDAO fiDAO = new NewFileInfoDAO();
		try{
			fiDAO.persistAll(files);
		}
		catch(Exception e){
			System.err.println("Error in fileInfo extraction \n"+e.toString());
		} 
		finally{
			fiDAO.clear();
		}
	}
	
	
}
