package aserg.gtf.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.io.LineReader;

public class FileInfoReader {
			
	public static Map<String, List<LineInfo>> getFileInfo(String fileName) throws IOException{
		Map<String, List<LineInfo>> fileInfoMap =  new HashMap<String, List<LineInfo>>();
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		LineReader lineReader = new LineReader(br);
		String sCurrentLine;
		String[] values;
		int countcfs = 0;
		while ((sCurrentLine = lineReader.readLine()) != null) {
			if (sCurrentLine.startsWith("#"))
				continue;
			values = sCurrentLine.split(";");
			if (values.length<3)
				System.err.println("Erro na linha " + countcfs);
			String rep = values[0];
			if (!fileInfoMap.containsKey(rep)) {
				fileInfoMap.put(rep, new ArrayList<LineInfo>());
			}
			fileInfoMap.get(rep).add(new LineInfo(rep, Arrays.asList(values).subList(1, values.length)));
		}
		//lineReader.close();
		return fileInfoMap;
	}

}