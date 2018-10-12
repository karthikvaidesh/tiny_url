package ncsu.tinyurl.project;

import java.util.Scanner;

import ncsu.tinyurl.project.UrlMap.AppInstance;

/**
 * Hello world!
 *
 */
public class App 
{
	
	public static AppInstance singletonInstance;
	
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
 
        singletonInstance = new AppInstance();
        
        
        System.out.println("Please enter a url of your choice. Press 0 to exit\n");
        Scanner in = new Scanner(System.in);
        String input = in.next();
        
        while(!input.equals("0")) {
        		String tinyUrl = shortenUrl(input);
        		System.out.println(tinyUrl);
        		input = in.next();
        }
        
    }
    
    public static String shortenUrl(String input) {
	    	Encrypt instance = new Encrypt(singletonInstance);
	    	String output = instance.encryptUrl(input);
	    	return output;
    }
}
