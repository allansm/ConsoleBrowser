package test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;

public class Downloader {
	public static void download(String link,String path) {
		System.out.println("downloading:"+link);
		try {
			BufferedInputStream inputStream = new BufferedInputStream(new URL(link).openStream());
			System.out.println("download stream opened:"+link);
			if(!new File(path).exists()) {
				OutputStream os = new FileOutputStream(new File(path));
				//Thread.sleep(2000);
				FileOutputStream fileOS = new FileOutputStream(path); 
			    byte data[] = new byte[1024];
			    int byteContent;
			    System.out.println("writing download file..");
			    int counter = 0;
			    while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
			    	Thread.sleep(2);
			    	System.err.println(link+" "+(++counter/1024.0)+"/mb");
			        fileOS.write(data, 0, byteContent);
			    }
			    System.out.println("finished : "+link);
			    System.gc();
			}else {
				System.out.println("skipped file:"+path);
			}
		} catch (Exception e) {
		    // handles IO exceptions
			e.printStackTrace();
			System.out.println("download fail");
		}
	}
}
