package WebCollectorTest.Project_1;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HikangTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
			public static void main(String[] args) throws IOException {
				// TODO Auto-generated method stub
				//提取产品中心顶层各类产品
				LinkedHashMap<String, String> topUrlMap = unit();
				Iterator iter = topUrlMap.entrySet().iterator();  
				while (iter.hasNext()) {  
					Map.Entry entry = (Map.Entry) iter.next();
					String topName = entry.getKey().toString();
					String topUrl = entry.getValue().toString(); 
					System.out.println(topUrl);
					Document doc = Jsoup.connect(topUrl).get();
					doc.select("ul[class=clearfix]>$0");
				}
		
			}
			//提取产品中心顶层各类产品
			private static LinkedHashMap<String, String> unit() throws IOException {
				LinkedHashMap topUrlMap = new LinkedHashMap<String, String>();
				String url = "http://www.hikvision.com/cn/prlb_1009.html";
				Document doc = Jsoup.connect(url).get();
				
				Elements TopProductList = doc.select("div[class=nblnav1]>a");
				for (Element element : TopProductList) {
					System.out.println("添加顶级产品："+element.text());
					String proName = element.text();
					String proUrl = "http://www.hikvision.com/cn/"+element.attr("href");
					System.out.println("添加顶级产品url："+proUrl);
					topUrlMap.put(proName, proUrl);
				}
				System.out.println(topUrlMap.size());
				printHashMap(topUrlMap);
				return topUrlMap;
			}


			//打印hashmap
			private static void printHashMap(LinkedHashMap s){
				Iterator iter = s.entrySet().iterator();  
				while (iter.hasNext()) {  
				Map.Entry entry = (Map.Entry) iter.next();  
				Object key = entry.getKey();  
				Object value = entry.getValue();  
				//System.out.println(key + ":" + value);  
			}
			}
}
