package ncsu.tinyurl.project.UrlMap;

import java.util.HashMap;
import java.util.Map;

//This is a singleton class which adds the key-value to a Map. 

public class AppInstance {

	public String errorString = CONSTANTS.ERROR_STRING;
	public Map<String,String> urlMap = new HashMap<String,String>();
	
	
	//Returns 0 if successful, otherwise 1
	public int addUrl(String key, String value) {
		if(!urlMap.containsKey(key)) urlMap.put(key, value);
		else {  // If key already exists Return error
			System.out.println("This key already exists.");
			return 0;
		}
		return 1;	
	}
	
	//Returns the url of given key. Else returns Error_String
	public String getUrl(String key) {
		if(urlMap.containsKey(key)) return urlMap.get(key);
		else return errorString;
	}
	
	
	
}
