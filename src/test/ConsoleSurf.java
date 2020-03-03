package test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


 
public class ConsoleSurf extends TestChrome {
	public void openPage(String curPage) {
		//WebRobot chrome = new WebRobot();
		//System.out.print("type url:");
		//String curPage =new Scanner(System.in).next();
		curPage = (curPage.endsWith("/"))?curPage:curPage+"/";
		String page = findPage(curPage);
		List<String> lista = findTxt("<a ", "</a>",page);
		/*File content = new File("content");
		File title = new File("title");
		File url = new File("url");
		File ref = new File("ref");*/
		String data = "";
		String data2 = "";
		String data3 = "";
		String data4 = "";
		String verification = "";
		try {
			for(String s:lista) {
				s = "<a "+s+"</a>\n";
				System.out.println(s);
				data+=s;
				for(String ss:findTxt(">", "<", s)) {
					data2+=ss;
					data4+=ss;
					verification+=ss;
				}
				data2+="\n";
				data4+=(verification.equals(""))?"withoutTitle >> ":" >> ";
				verification = "";
				for(String sss:findTxt("href=\"", "\"", s)) {
					data3+=sss+"\n";
					data4+=sss+"\n";
					verification = sss;
				}
				data3+="\n";
				data4+=(verification.equals(""))?"withoutLink\ndelimiterEnd\n":"delimiterEnd\n";
				verification = "";
				//data2+=chrome.findTxt(">", "<", s).get(0)+"\n";
				//data3+=chrome.findTxt("href=\"", "\"", s).get(0)+"\n";
				
			}
			Files.write(Paths.get("content"), data.getBytes());
			Files.write(Paths.get("title"), data2.getBytes());
			Files.write(Paths.get("url"), data3.getBytes());
			Files.write(Paths.get("ref"), data4.getBytes());
			Files.write(Paths.get("currentPage"), curPage.getBytes());
			Files.write(Paths.get("page"),page.getBytes());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[]args) {
		ConsoleSurf chrome = new ConsoleSurf();
		List<String> buttons = null;
		List<String> links = null;
		String currentPage = null;
		String find = "";
		while(true) {
			System.out.print("WWW>");
			String function = new Scanner(System.in).next();
			if(function.equals("open")) {
				System.out.print("type url:");
				String curPage =new Scanner(System.in).next();
				chrome.openPage(curPage);
				//System.out.println("ok now restart the console");
				//break;
			}
			else if(function.equals("setRefs")) {
				try {
					String file = new String(Files.readAllBytes(Paths.get("ref")));
					System.out.println(file);
					//buttons = chrome.findTxt("", ">>", file);
					//links = chrome.findTxt(">> ", "delimiterEnd", file);
					buttons = Files.readAllLines(Paths.get("title"),StandardCharsets.ISO_8859_1);
					links = Files.readAllLines(Paths.get("url"),StandardCharsets.UTF_8);
					System.out.println("refs stored");
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			else if(function.equals("setCurrentPage")) {
				try {
					String file = new String(Files.readAllBytes(Paths.get("currentPage")));
					currentPage = file;
					System.out.println("currentPage stored");
				}catch(Exception e) {
					e.printStackTrace();
				}
			}else if(function.equals("currentPage")) {
				System.out.println(currentPage);
			}
			else if(function.equals("showLinkTitles")) {
				try {
					int i = 0;
					for(String s:buttons) {
						System.out.println("id:"+(i++)+":"+s);
					}
				}catch(Exception e) {
					System.out.println("no stored ref");
				}
			}
			else if(function.equals("showLinks")) {
				try {
					int i = 0;
					for(String s:links) {
						System.out.println("id:"+(i++)+":"+s);
					}
				}catch(Exception e) {
					System.out.println("no stored ref");
				}
			}
			else if(function.equals("showRefs")) {
				try {
					int i = 0;
					for(String s:buttons) {
						System.out.println("id:"+(i)+":"+s+" >> "+links.get(i));
						i++;
					}
				}catch(Exception e) {
					System.out.println("no stored ref");
				}
			}
			else if(function.equals("addCurrentPageToLinks")) {
				try {
					List<String> list = new ArrayList<String>();
					String rootPage = chrome.findTxt("//", "/", currentPage).get(0);
					rootPage = chrome.findTxt("", "//", currentPage).get(0)+"//"+rootPage;
					for(String s:links) {
						if(s.startsWith("/") && currentPage != null) {
							System.out.println("updating "+s);
							s = rootPage+s;
						}else if(currentPage == null){
							int createError = 2/0;
						}
						list.add(s);
					}
					links = list;
				}catch(Exception e) {
					System.out.println("no current page reference");
				}
			}
			else if(function.equals("openRef")) {
				System.out.print("type id:");
				int id =new Scanner(System.in).nextInt();
				System.out.println("heading to :"+links.get(id));
				chrome.openPage(links.get(id));
				//System.out.println("ok now restart the console");
				//break;
			}
			else if(function.equals("download")) {
				System.out.print("type url:");
				String url =new Scanner(System.in).next();
				System.out.print("type path:");
				String path = new Scanner(System.in).next();
				chrome.download(url, path);
			}
			else if(function.equals("showVars")) {
				System.out.print("buttons:");
				System.out.println(buttons);
				System.out.print("links:");
				System.out.println(links);
				System.out.print("currentpage:");
				System.out.println(currentPage);
			}
			else if(function.equals("showHtml")) {
				try {
					System.out.println(new String(Files.readAllBytes(Paths.get("page"))));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(function.equals("find")) {
				try {
					find = "";
					System.out.print("type start:");
					String start = new Scanner(System.in).next();
					System.out.print("type end:");
					String end = new Scanner(System.in).next();
					System.out.print("type (yes/no) to remove tag:");
					boolean removeTag = new Scanner(System.in).next().equals("yes");
					for(String s:chrome.findTxt(start,end,new String(Files.readAllBytes(Paths.get("page"))))) {
						find +=start+s+end+"\n";
					}
					if(removeTag) {
						String aux = "";
						for(String s:chrome.findTxt(">", "<", find)) {
							aux += s+"\n";
						}
						find = aux;
					}
					System.out.println("result:\n-----------------------");
					System.out.println(find);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(function.equals("help")) {
				List<String> commands = new ArrayList<String>();
				commands.add("		commands:\n\ncommand >> info");
				commands.add("open >> get data from web page save into files");
				commands.add("setRefs >> store the data from files on variables");
				commands.add("setCurrentPage >> store current page on variable");
				commands.add("showLinkTitles >> show the title of the link");
				commands.add("showLinks >> show the links of the title");
				commands.add("showRefs >> show all the reference");
				commands.add("currentPage >> show current stored page");
				commands.add("addCurrentPageToLinks >> add current page to incomplete links");
				commands.add("openRef >> open link inside variables based on id");
				commands.add("showVars >> show all stored variables");
				commands.add("download >> download a file based on url");
				commands.add("showHtml >> show page html");
				commands.add("find >> find tags or anything inside html based on start and end strings delimiters");
				for(String s:commands) {
					System.out.println(s);
				}
			}else {
				System.out.println("type help to see commands");
			}
		}
	}
}
 