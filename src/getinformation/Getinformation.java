package getinformation;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Getinformation {
	
	public static Document getdocument(String url) {
		Document document=null;
		try {
			document=Jsoup.connect(url).timeout(10000).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return document;
	}

	public static String gettitle(Document document) {
		return document.getElementsByTag("h1").text();
	}
	public static String getcontent(Document document) {
		
		return document.getElementById("content").text().replace(Jsoup.parse("&nbsp;&nbsp;&nbsp;&nbsp;").text(),"\r\n    ");
	}
	public static String[] geturl(String url) {
		StringBuilder cat=new StringBuilder();
		
		int lastindex=url.lastIndexOf("/");
		String rooturl=url.substring(0, lastindex+1);
		Document doccatalog=getdocument(url);
		Element dl=doccatalog.select("dl").first();
		String regex="<a href=\"(.*?)\">(.*?)</a>";
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(dl.toString());
		
		while(matcher.find()) {
			StringBuilder temp=new StringBuilder(rooturl+matcher.group(1)+"!");
			cat.append(temp);
		}
		String[] catalog=cat.toString().split("!");
		return catalog;
	}
}
