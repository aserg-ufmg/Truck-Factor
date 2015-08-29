package aserg.gtf.util;


import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Directory{
	private String name;
	private Map<String, Directory> directoryMap;

	public Directory(String name, Map<String, Directory> directoryMap) {
		super();
		this.name = name;
		this.directoryMap = directoryMap;
	}
	public Directory(String name) {
		super();
		this.name = name;
		this.directoryMap = new HashMap<String, Directory>();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, Directory> getDirectoryMap() {
		return directoryMap;
	}
	public void setDirectoryMap(Map<String, Directory> directoryMap) {
		this.directoryMap = directoryMap;
	}
	public String toJson(){
		String str = "{\"name\": \"" + name+ "\",\n\"children\" :[\n";
		int count = 0;
		for (Entry<String, Directory> entry : directoryMap.entrySet()) {
			str += entry.getValue().toJson();
			if (count++<(directoryMap.size()-1))
				str += ",\n";
		}
		str+= "]}";
		return str;
	}
}
