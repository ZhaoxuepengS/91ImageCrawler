package WebCollectorTest.Project_1;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO 自动生成的方法存根
		/*String url = "http://*********/forumdisplay.php?fid=19&page=2";
		Document doc = Jsoup.connect(url).get();
		//System.out.println(doc.toString());
		//Element content = doc.getElementById("content");
		//Elements links = content.getElementsByTag("a");
		Elements links = doc.select("div[id=threadlist]");
		Elements s = links.select("tbody");
		System.out.println(s.size());
		for(Element h:s){
			System.out.println(h.select("span>[href]").first().attr("href"));
		}*/
		test();
		//System.out.println(links);
		
	}
	public static void test() throws Exception{
		String url = "http://***********/viewthread.php?tid=245044&extra=page%3D2";
		Document doc = Jsoup.connect(url).get();
		Element div = doc.select("div[class=postmessage firstpost]").get(0);
		//System.out.println(div);
		Elements img = div.select("img[src]");
		for (Element t : img) {
			if(t.attr("file").isEmpty()){
				
			}else{
				String s = t.attr("file");
				String extensionName=s.split("/")[1];
				//String x = s.substring(s.indexOf('.'),s.length());
				System.out.println(extensionName);
				
			}
		}
		
	}
	public static void test2() throws Exception{
		String url = "http://www.hikvision.com/cn/prlb_747.html";
		Document doc = Jsoup.connect(url).get();
		Element div = doc.select("ul[class=newsbodyl]").get(0);
		//System.out.println(div);
		Elements img = div.select("img[src]");
		for (Element t : img) {
			if(t.attr("file").isEmpty()){
				
			}else{
				String s = t.attr("file");
				String extensionName=s.split("/")[1];
				//String x = s.substring(s.indexOf('.'),s.length());
				System.out.println(extensionName);
				
			}
		}
	}
}
