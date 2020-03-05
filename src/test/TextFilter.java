package test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextFilter {
	public List<String> findTxt(String start,String end,String html){
		List<String> tag = new ArrayList();
		try {
			//System.out.println("pattern:"+start+"(.*?)"+end);
			Pattern pattern = Pattern.compile(start+"(.*?)"+end);
			//System.out.println("compile<-------------");
			Matcher matcher = pattern.matcher(html);
			while(matcher.find()) {
				Thread.sleep(5);
				//System.out.println("searching txt..");
				try {
					tag.add(matcher.group(1));
					//System.out.println("found txt"+matcher.group(1));
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			return tag;
		}catch(Exception e) {
			//e.printStackTrace();
			return null;
		}
	}
}
