package test;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;

public class HTML {
	private String html;
	private String head;
	private String body;
	private List<String> a;
	
	public HTML(String page) {
		WebDriver driver = new ChromeWebDriver().getWebDriver();
		driver.get(page);
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {}
		html = driver.getPageSource();
		driver.close();
		try {
			head = "<head"+new TextFilter().findTxt("<head", "</head>", html).get(0)+"</head>";
		}catch(Exception e) {}
		try {
			body = "<body"+new TextFilter().findTxt("<body", "</body>", html).get(0)+"</body>";
		}catch(Exception e) {}
		a = new TextFilter().findTxt("<a", "</a>", html);
		List<String> aux = new ArrayList<>();
		for(String s:a) {
			s = "<a"+s+"</a>";
			aux.add(s);
		}
		a = aux;
	}

	public String getHtml() {
		return html;
	}

	public String getHead() {
		return head;
	}

	public String getBody() {
		return body;
	}

	public List<String> getA() {
		return a;
	}
	
}
