package ncsu.tinyurl.project;

import ncsu.tinyurl.project.UrlMap.AppInstance;

public class Encrypt {

	AppInstance ins;
	
	public Encrypt(AppInstance instance) {
		ins = instance;
	}
	
	public String encryptUrl(String url) {
		//implement a hashMap to return tinyurl for an input here
		String tinyUrl = "asd";				///Change this to hashmap Url

		ins.addUrl(url, tinyUrl);
		
		return tinyUrl;
	}
	
}
