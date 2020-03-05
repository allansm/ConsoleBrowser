package test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


 
public class ConsoleSurf {
	private List<String> buttons = null;
	private List<String> links = null;
	private String currentPage = null;
	private String find = "";
	private List<String> filter;
	public void openPage(String curPage) {
		//WebRobot chrome = new WebRobot();
		//System.out.print("type url:");
		//String curPage =new Scanner(System.in).next();
		curPage = (curPage.endsWith("/"))?curPage:curPage+"/";
		HTML html = new HTML(curPage);
		String page = html.getHtml();//findPage(curPage);
		List<String> lista  = html.getA();//= findTxt("<a ", "</a>",page);
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
				//s = "<a "+s+"</a>\n";
				s=s+"\n";
				System.out.println(s);
				data+=s;
				for(String ss:new TextFilter().findTxt(">", "<", s)) {
					data2+=ss;
					data4+=ss;
					verification+=ss;
				}
				data2+="\n";
				data4+=(verification.equals(""))?"withoutTitle >> ":" >> ";
				verification = "";
				for(String sss:new TextFilter().findTxt("href=\"", "\"", s)) {
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
	public void setRefs() {
		try {
			String file = new String(Files.readAllBytes(Paths.get("ref")));
			//System.out.println(file);
			buttons = new TextFilter().findTxt("", ">>", file);
			System.out.println(file);
			links = new TextFilter().findTxt(">> ", "\ndelimiterEnd", file);
			//buttons = Files.readAllLines(Paths.get("title"),StandardCharsets.ISO_8859_1);
			//links = Files.readAllLines(Paths.get("url"),StandardCharsets.UTF_8);
			System.out.println("refs stored");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void setCurrentPage() {
		try {
			String file = new String(Files.readAllBytes(Paths.get("currentPage")));
			currentPage = file;
			System.out.println("currentPage stored");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void showLinkTitles() {
		try {
			int i = 0;
			for(String s:buttons) {
				System.out.println("id:"+(i++)+":"+s);
			}
		}catch(Exception e) {
			System.out.println("no stored ref");
		}
	}
	public void showLinks() {
		try {
			int i = 0;
			for(String s:links) {
				System.out.println("id:"+(i++)+":"+s);
			}
		}catch(Exception e) {
			System.out.println("no stored ref");
		}
	}
	public void showRefs() {
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
	public void addCurrentPageToLinks() {
		try {
			List<String> list = new ArrayList<String>();
			String rootPage = new TextFilter().findTxt("//", "/", currentPage).get(0);
			rootPage = new TextFilter().findTxt("", "//", currentPage).get(0)+"//"+rootPage;
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
	public void openRef() {
		System.out.print("type id:");
		int id =new Scanner(System.in).nextInt();
		System.out.println("heading to :"+links.get(id));
		openPage(links.get(id));
		//System.out.println("ok now restart the console");
		//break;
	}
	public void download() {
		System.out.print("type url:");
		String url =new Scanner(System.in).next();
		System.out.print("type path:");
		String path = new Scanner(System.in).next();
		new Downloader().download(url, path);
	}
	public void showVars() {
		System.out.print("buttons:");
		System.out.println(buttons);
		System.out.print("links:");
		System.out.println(links);
		System.out.print("currentpage:");
		System.out.println(currentPage);
		System.out.print("filter:");
		System.out.println(filter);
	}
	public void showHtml() {
		try {
			System.out.println(new String(Files.readAllBytes(Paths.get("page"))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void find() {
		try {
			find = "";
			filter = new ArrayList<>();
			System.out.print("type start:");
			String start = new Scanner(System.in).next();
			System.out.print("type end:");
			String end = new Scanner(System.in).next();
			System.out.print("type (yes/no) to remove tag:");
			boolean removeTag = new Scanner(System.in).next().equals("yes");
			for(String s:new TextFilter().findTxt(start,end,new String(Files.readAllBytes(Paths.get("page"))))) {
				find +=start+s+end+"\n";
				filter.add(start+s+end);
			}
			if(removeTag) {
				String aux = "";
				for(String s:new TextFilter().findTxt(">", "<", find)) {
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
	public void filterLinks() {
		filter = new ArrayList<>();
		System.out.print("start:");
		String start = new Scanner(System.in).next();
		for(String s:links) {
			for(String ss:new TextFilter().findTxt(start,"/" ,s+"/")) {
				System.out.println("filter:"+ss);
				filter.add(ss);
			}
		}
	}
	public void editFilter() {
		System.out.print("start:");
		String start = new Scanner(System.in).next();
		System.out.print("end:");
		String end = new Scanner(System.in).next();
		List<String> newFilter = new ArrayList<>();
		for(String s:filter) {
			System.out.println("updating:"+s);
			newFilter.add(start+s+end);
		}
		filter = newFilter;
	}
	public void showFilter() {
		int i = 0;
		for(String s:filter) {
			System.out.println((i++)+":"+s);
		}
	}
	public void filterFilter() {
		System.out.print("start:");
		String start = new Scanner(System.in).next();
		System.out.print("end:");
		String end = new Scanner(System.in).next();
		List<String> newFilter = new ArrayList<>();
		for(String s:filter) {
			for(String ss:new TextFilter().findTxt(start,end ,s)) {
				System.out.println("filter:"+ss);
				newFilter.add(ss);
			}
		}
		filter = newFilter;
	}
	public void openFilter() {
		System.out.print("type id:");
		int id = new Scanner(System.in).nextInt();
		System.out.println("heading to:"+filter.get(id));
		openPage(filter.get(id));
	}
	public void downloadUsingVars() {
		System.out.print("var name:");
		String vname = new Scanner(System.in).next();
		if(vname.equals("buttons")) {
			System.out.print("id:");
			int id = new Scanner(System.in).nextInt();
			System.out.print("path:");
			String path = new Scanner(System.in).next();
			String fileName  = new Scanner(System.in).next();
			new Downloader().download(buttons.get(id), path+"/"+fileName);
		}else if(vname.equals("links")) {
			System.out.print("id:");
			int id = new Scanner(System.in).nextInt();
			System.out.print("path:");
			String path = new Scanner(System.in).next();
			String fileName  = new Scanner(System.in).next();
			new Downloader().download(links.get(id), path+"/"+fileName);
		}else if(vname.equals("filter")) {
			System.out.print("id:");
			int id = new Scanner(System.in).nextInt();
			System.out.print("path:");
			String path = new Scanner(System.in).next();
			System.out.print("file name:");
			String fileName  = new Scanner(System.in).next();
			new Downloader().download(filter.get(id), path+"/"+fileName);
		}
	}
	public void bye() {
		System.out.println("exiting...");
		System.exit(0);
	}
	public void updateAll() {
		setRefs();
		setCurrentPage();
		addCurrentPageToLinks();
	}
	public void help() {
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
		commands.add("updateAll >> update all variables");
		commands.add("find >> find tags or anything inside html based on start and end strings delimiters");
		commands.add("filterLinks >> filter links based on start and add to filter array");
		commands.add("editFilter >> edit filter array : add start and end");
		commands.add("showFilter >> show filter array");
		commands.add("filterFilter >> filter string inside filter and save to filter array");
		commands.add("openFilter >> try to open a page based on filter value");
		commands.add("downloadUsingVars >> select a array and download based on value inside this array");
		commands.add("bye >> exit");
		/*
		 * filterLinks
		 * editFilter
		 * showFilter
		 * filterFilter
		 * openFilter
		 * downloadUsingVars
		 * bye
		 * */
		for(String s:commands) {
			System.out.println(s);
		}
	}
	public void open() {
		System.out.print("type url:");
		String curPage =new Scanner(System.in).next();
		openPage(curPage);
	}
	public void run() {
		while(true) {
			System.out.print("WWW>");
			String function = new Scanner(System.in).next();
			if(function.equals("open")) {
				open();
			}
			else if(function.equals("setRefs")) {
				setRefs();
			}
			else if(function.equals("setCurrentPage")) {
				setCurrentPage();
			}else if(function.equals("currentPage")) {
				System.out.println(currentPage);
			}
			else if(function.equals("showLinkTitles")) {
				showLinkTitles();
			}
			else if(function.equals("showLinks")) {
				showLinks();
			}
			else if(function.equals("showRefs")) {
				showRefs();
			}
			else if(function.equals("addCurrentPageToLinks")) {
				addCurrentPageToLinks();
			}
			else if(function.equals("openRef")) {
				openRef();
			}
			else if(function.equals("download")) {
				download();
			}
			else if(function.equals("showVars")) {
				showVars();
			}
			else if(function.equals("showHtml")) {
				showHtml();
			}
			else if(function.equals("find")) {
				find();
			}
			else if(function.equals("filterLinks")) {
				filterLinks();
			}
			else if(function.equals("editFilter")) {
				editFilter();
			}
			else if(function.equals("showFilter")) {
				showFilter();
			}
			else if(function.equals("filterFilter")) {
				filterFilter();
			}
			else if(function.equals("openFilter")) {
				openFilter();
			}else if(function.equals("bye")) {
				bye();
			}else if(function.equals("downloadUsingVars")) {
				downloadUsingVars();
			}
			else if(function.equals("updateAll")) {
				updateAll();
			}
			else if(function.equals("help")) {
				help();
			}else {
				System.out.println("type help to see commands");
			}
		}
	}
	
	public static void main(String[]args) {
		new ConsoleSurf().run();
	}
}
 