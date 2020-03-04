package test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
 
public class TestChrome {
	public static void download(String link,String path) {
		new Downloader().download(link, path);
	}
	public List<String> findTxt(String start,String end,String html){
		return new TextFilter().findTxt(start, end, html);
	}
	public List<String> findVideoLink(String html) {
		System.out.println("searching video...");
		List<String> list = findTxt("<video ", "</video>", html);
		List<String> link = new ArrayList();
		try {
			for(String s:list) {
				Thread.sleep(5);
				System.out.println("<video "+s+"</video>");
				for(String ss:findTxt("http", "\"", s)) {
					Thread.sleep(5);
					System.out.println("found video : http"+ss);
					link.add("http"+ss);
				}
			}
			return link;
		}catch(Exception e) {
			System.out.println("vazio");
			return null;
		}
	}
	public List<String> findLinks(String html) {
		List<String> list = findTxt("href=\"","\"",html);
		List<String> links = new ArrayList();
		try {
			for(String s:list) {
				Thread.sleep(5);
				links.add(s);
			}
			return links;
		}catch(Exception e) {
			return null;
		}
	}
	public void browseAndFindVideo(String url) {
		System.out.println("current page:"+url);
		String page = findPage(url);
		List<String> videosLinks = findVideoLink(page);
		for(String s:videosLinks) {
			try {
				//OutputStream os = null;
				//os = new FileOutputStream(new File("c:/Users/allan/downloads/os.txt"));
				//FileInputStream in = new FileInputStream(new File("c:/Users/allan/downloads/os.txt"));
				//os.write((Files.readAllBytes(Paths.get("c:/Users/allan/downloads/os.txt")).toString()+"\n"+s).getBytes());
				String fn = s.replaceAll("/", "").replaceAll(":", "");
				fn = "E:\\robot_data\\"+fn+".mp4";
				System.out.println(fn);
				download(s,fn);
			}catch(Exception e) {
				
			}
		}
		List<String> links = findLinks(page);
		Collections.shuffle(links);
		String prev = "";
		for(String s:links) {
			try {
				if(s.charAt(0) == '/' && s.charAt(1) != '/' && !s.equals(prev)) {
					prev = s;
					System.out.println("redirecting to :"+url+s);
					for(String ss:findVideoLink(findPage(url+s))) {
						Thread.sleep(5);
						try {
							//OutputStream os = null;
							//os = new FileOutputStream(new File("c:/Users/allan/downloads/os.txt"));
							//FileInputStream in = new FileInputStream(new File("c:/Users/allan/downloads/os.txt"));
							//os.write((Files.rea(Paths.get("c:/Users/allan/downloads/os.txt"))+"\n"+ss).getBytes());
							String fn = ss.replaceAll("/", "").replaceAll(":", "");
							fn = "E:\\robot_data\\"+fn+".mp4";
							System.out.println(fn);
							download(ss,fn);
						}catch(Exception e) {
							
						}
						Collections.shuffle(links);
					}
				}else {
					if(s.charAt(0) == 'h' && s.charAt(1) == 't' && !s.equals(prev)) {
						prev = s;
						System.out.println("redirecting to :"+s);
						for(String ss:findVideoLink(findPage(s))) {
							Thread.sleep(5);
							try {
								//OutputStream os = null;
								//os = new FileOutputStream(new File("c:/Users/allan/downloads/os.txt"));
								//FileInputStream in = new FileInputStream(new File("c:/Users/allan/downloads/os.txt"));
								//os.write((Files.rea(Paths.get("c:/Users/allan/downloads/os.txt"))+"\n"+ss).getBytes());
								String fn = ss.replaceAll("/", "").replaceAll(":", "");
								fn = "E:\\robot_data\\"+fn+".mp4";
								System.out.println(fn);
								download(ss,fn);
							}catch(Exception e) {
								
							}
							Collections.shuffle(links);
						}
					}else {
						System.out.println("skip:"+s);
					}
				}
			}catch(Exception e) {
				
			}
		}
	}
	public String findPage(String page) {
		//change this
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\Allan\\Downloads\\chromedriver\\chromedriver.exe");
		String html = "";
		ChromeOptions op = new ChromeOptions();
		op.setHeadless(true);
		WebDriver driver = new ChromeDriver(op);
		driver.get(page);
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		html = driver.getPageSource();
		
		driver.close();
		try {
			Thread.sleep(5);
			//Runtime.getRuntime().exec("taskkill /f /im chrome*");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return html;
	}
	public static void main(String[]args) {
		Scanner s = new Scanner(System.in);
		System.out.print("enter the url:");
		new TestChrome().browseAndFindVideo(s.nextLine());
	}
}
 