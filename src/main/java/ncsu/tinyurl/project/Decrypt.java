package ncsu.tinyurl.project;

import ncsu.tinyurl.project.UrlMap.AppInstance;

public class Decrypt {

	public AppInstance ins;
	
	public Decrypt(AppInstance instance) {
		this.ins = instance;
	}
	
	public String decryptUrl(String value) {
		return ins.getUrl(value);
	}
	
}
