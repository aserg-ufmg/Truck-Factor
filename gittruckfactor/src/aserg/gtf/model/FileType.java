package aserg.gtf.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jcabi.immutable.Array;

public enum FileType {
	JAVA, JAVASCRIPT, PHP, RUBY, PYTHON, C_LIKE, DOCUMENTATION, EXAMPLES, JSON, HTML, CSS, OTHERLANGUAGES, LIBRARY, NOTIDENTIFIED, OTHER, THIRD;

	
	//Usar preferencialmente por ter melhor desempenho
	static String patters[][] = { 
//		/*JAVA*/ {"%.java", "%.jsp"},
//		/*JAVASCRIPT*/ {"%.js"},
//		/*PHP*/ {"%.php", "%.php_","%.phtml"},
//		/*RUBY*/ {"%.rb", "%.erb"},
//		/*PYTHON*/ {"%.py","%.py_"},
//		/*CLIKE*/ {"%.c","%.cpp","%.c++","%.cc","%.cp","%.cxx","%.h","%.h++","%.hh","%.hpp","%.hxx"},
//		/*JSON*/ {"%.json", "%.json5", "%.jsonld"},
//		/*HTML*/ {"%.html", "%.htm", "%.xhtml"},
//		/*CSS*/ {"%.css", "%.scss"},
//		/*OTHERLANGUAGES*/ {"%.js.coffee"},
		/*DOCUMENTATION*/ {"%/docs/%"},
		/*EXAMPLES*/ {"examples/%", "%/examples/%"},
		};
	
	public static List<String> getPatterns(FileType fTypes){
		String result[] = null;
//		if (fTypes == FileType.JAVA)
//			result = patters[0];
//		else if (fTypes == FileType.JAVASCRIPT)
//			result = patters[1];
//		else if (fTypes == FileType.PHP)
//			result = patters[2];
//		else if (fTypes == FileType.RUBY)
//			result = patters[3];
//		else if (fTypes == FileType.PYTHON)
//			result = patters[4];
//		else if (fTypes == FileType.C_LIKE)
//			result = patters[5];
//		else if (fTypes == FileType.JSON)
//			result = patters[6];
//		else if (fTypes == FileType.HTML)
//			result = patters[7];
//		else if (fTypes == FileType.CSS)
//			result = patters[8];
//		else if (fTypes == FileType.OTHERLANGUAGES)
//			result = patters[9];
//		else if (fTypes == FileType.DOCUMENTATION)
//			result = patters[10];
//		else if (fTypes == FileType.EXAMPLES)
//			result = patters[11];
//		else if (fTypes == FileType.LIBRARY)
//			result = patters[12];
//		else 
//			return null;
		
		if (fTypes == FileType.DOCUMENTATION)
			result = patters[0];
		else if (fTypes == FileType.EXAMPLES)
			result = patters[1];
		else if (fTypes == FileType.LIBRARY)
			result = patters[2];
		else 
			return null;
		return Arrays.asList(result);
	}
	
	//Usar apenas quando n�o for poss�vel usar padr�o LIKE
	static String POSIXpatters[][] = { 
//		/*JAVA*/ {},
//		/*JAVASCRIPT*/ {},
//		/*PHP*/ {},
//		/*RUBY*/ {},
//		/*PYTHON*/ {},
//		/*CLIKE*/ {},
//		/*DOCUMENTATION*/ {},
//		/*EXAMPLES*/ {},
//		/*JSON*/ {},
//		/*HTML*/ {},
//		/*CSS*/ {},
//		/*OTHERLANGUAGES*/ {},
//		/*LIBRARIES - Jquery*/ {"(^|/)jquery([^.]*)\\.js$", "(^|/)jquery\\-\\d\\.\\d+(\\.\\d+)?\\.js$",
//		/*LIBRARIES - Jquery*/ 	
//		/*LIBRARIES - Jquery*/ 	
//		/*LIBRARIES - Jquery*/ 	
//		/*LIBRARIES - Jquery*/ 	
//				}
		};
	
	public static List<String> getPosixPatterns(FileType fTypes){
		String result[] = null;
		if (fTypes == FileType.JAVA)
			result = POSIXpatters[0];
		if (fTypes == FileType.JAVASCRIPT)
			result = POSIXpatters[1];
		if (fTypes == FileType.PHP)
			result = POSIXpatters[2];
		if (fTypes == FileType.RUBY)
			result = POSIXpatters[3];
		if (fTypes == FileType.PYTHON)
			result = POSIXpatters[4];
		if (fTypes == FileType.C_LIKE)
			result = POSIXpatters[5];
		if (fTypes == FileType.DOCUMENTATION)
			result = POSIXpatters[6];
		if (fTypes == FileType.EXAMPLES)
			result = POSIXpatters[7];
		if (fTypes == FileType.JSON)
			result = POSIXpatters[8];
		if (fTypes == FileType.HTML)
			result = POSIXpatters[9];
		if (fTypes == FileType.CSS)
			result = POSIXpatters[10];
		if (fTypes == FileType.OTHERLANGUAGES)
			result = POSIXpatters[11];
		if (fTypes == FileType.LIBRARY)
			result = POSIXpatters[12];
		return Arrays.asList(result);
	}
	
	public static FileType getType(String name){
		if (name.equalsIgnoreCase("java"))
			return JAVA;
		else if (name.equalsIgnoreCase("javascript"))
			return JAVASCRIPT;
		else if (name.equalsIgnoreCase("php"))
			return PHP;
		else if (name.equalsIgnoreCase("ruby"))
			return RUBY;
		else if (name.equalsIgnoreCase("python"))
			return PYTHON;
		else if (name.equalsIgnoreCase("c"))
			return C_LIKE;
		else if (name.equalsIgnoreCase("c++"))
			return C_LIKE;
		else if (name.equalsIgnoreCase("json"))
			return JSON;
		else if (name.equalsIgnoreCase("html"))
			return HTML;
		else if (name.equalsIgnoreCase("css"))
			return CSS;
		else
			return OTHERLANGUAGES;
	}
}
