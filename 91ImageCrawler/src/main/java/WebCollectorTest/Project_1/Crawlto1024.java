package WebCollectorTest.Project_1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.plugin.net.OkHttpRequester;
import cn.edu.hfut.dmic.webcollector.util.FileUtils;



public class Crawlto1024 extends BreadthCrawler {
    File baseDir = new File("images");
    /**
     * 构造一个基于伯克利DB的爬虫
     * 伯克利DB文件夹为crawlPath，crawlPath中维护了历史URL等信息
     * 不同任务不要使用相同的crawlPath
     * 两个使用相同crawlPath的爬虫并行爬取会产生错误
     *
     * @param crawlPath 伯克利DB使用的文件夹
     */
    static File file = new File("D:\\1.txt");
    static File urllog = new File("D:\\2.txt");
    
    
    public Crawlto1024(String crawlPath) {
        super(crawlPath, true);

        //只有在autoParse和autoDetectImg都为true的情况下
        //爬虫才会自动解析图片链接
        getConf().setAutoDetectImg(true);

        //如果使用默认的Requester，需要像下面这样设置一下网页大小上限
        //否则可能会获得一个不完整的页面
        //下面这行将页面大小上限设置为10M
        //getConf().setMaxReceiveSize(1024 * 1024 * 10);

        //添加种子URL
        //addSeed("http://www.meishij.net");
        for(int i = 1; i <= 10; i++) {
            this.addSeed("http://#######/forumdisplay.php?fid=19&page="+i, "list");
        }
        
        //限定爬取范围
        //addRegex("http://www.meishij.net/.*");
        //addRegex("http://45.32.78.185/htm_data.*.html");
        //addRegex("-.*#.*");
        //addRegex("-.*\\?.*");
        //设置为断点爬取，否则每次开启爬虫都会重新爬取
//        demoImageCrawler.setResumable(true);
        setThreads(50);
        getConf().setTopN(100);

    }
    public void visit(Page page, CrawlDatums next) {
    	String url = page.url();
    	
    	
        if (page.matchType("list")) {
        	Elements links = page.select("div[id=threadlist]");
        	Elements s = links.select("tbody");
        	
    		for(Element h:s){
    			next.add("http://*******/"+h.select("span>[href]").first().attr("href")).type("content");
    			FileOutputStream fout;
				try {
					fout = new FileOutputStream(urllog,true);
					fout.write(new String("添加帖子链接:"+"http://*********/"+h.select("span>[href]").first().attr("href")).getBytes());
					fout.write("\r\n".getBytes());// 写入一个换行  
					fout.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
    		}
        }else if(page.matchType("content")) {
            /*if type is "content"*/
            /*extract title and content of news by css selector*/
        	//提取帖子标题
            String title = page.select("div[id=threadtitle]>h1").text();
            //提取
            Element div = page.select("div[class=postmessage firstpost]").get(0);
            Elements img = div.select("img[src]");
            for (Element t : img) {
    			if(t.attr("file").isEmpty()){
    				
    			}else{
    				next.add("http://**********/"+t.attr("file")).type("image");
    				//System.out.println(t.attr("file"));
    			}
    		}
        }else if(page.matchType("image")){
        	//下载图片，待完善~~~~~~~
        	String imageurl = page.url();
        	String extendname = imageurl.substring(imageurl.lastIndexOf('/'),imageurl.length());
        	byte[] image = page.content();
        	File imageFile = new File(baseDir, extendname);
        	try {
				FileUtils.write(imageFile, image);
				FileOutputStream fout = new FileOutputStream(file,true);
				fout.write(new String("保存图片 "+page.url()+" 到 "+ imageFile.getAbsolutePath()).getBytes());
				fout.write("\r\n".getBytes());// 写入一个换行  
				fout.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }
    }
  /*  public void visit(Page page, CrawlDatums next) {
        //根据http头中的Content-Type信息来判断当前资源是网页还是图片
        String contentType = page.contentType();
        try {
			FileOutputStream fout = new FileOutputStream(file);
			fout.write("----------------------------------".getBytes());
			fout.write("获取到链接，开始抽取图片".getBytes());
			fout.write(("contentType="+contentType).getBytes());
		} catch (Exception e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
        //根据Content-Type判断是否为图片
        if(contentType!=null && contentType.startsWith("image")){
            //从Content-Type中获取图片扩展名
            String extensionName=contentType.split("/")[1];
            try {
                byte[] image = page.content();
                //根据图片MD5生成文件名
                String fileName = String.format("%s.%s",MD5Utils.md5(image), extensionName);
                File imageFile = new File(baseDir, fileName);
                FileUtils.write(imageFile, image);
                System.out.println("保存图片 "+page.url()+" 到 "+ imageFile.getAbsolutePath());
            } catch (Exception e) {
                ExceptionUtils.fail(e);
            }
        }
    }*/

    public static void main(String[] args) throws Exception {
    	
    	
    	Crawlto1024 demoImageCrawler = new Crawlto1024("crawl");
        demoImageCrawler.setRequester(new OkHttpRequester());
        //设置为断点爬取，否则每次开启爬虫都会重新爬取
        demoImageCrawler.setResumable(false);
        demoImageCrawler.start(3);
    }
}