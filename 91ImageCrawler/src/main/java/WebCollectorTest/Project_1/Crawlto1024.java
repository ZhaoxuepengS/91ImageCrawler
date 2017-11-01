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
     * ����һ�����ڲ�����DB������
     * ������DB�ļ���ΪcrawlPath��crawlPath��ά������ʷURL����Ϣ
     * ��ͬ����Ҫʹ����ͬ��crawlPath
     * ����ʹ����ͬcrawlPath�����沢����ȡ���������
     *
     * @param crawlPath ������DBʹ�õ��ļ���
     */
    static File file = new File("D:\\1.txt");
    static File urllog = new File("D:\\2.txt");
    
    
    public Crawlto1024(String crawlPath) {
        super(crawlPath, true);

        //ֻ����autoParse��autoDetectImg��Ϊtrue�������
        //����Ż��Զ�����ͼƬ����
        getConf().setAutoDetectImg(true);

        //���ʹ��Ĭ�ϵ�Requester����Ҫ��������������һ����ҳ��С����
        //������ܻ���һ����������ҳ��
        //�������н�ҳ���С��������Ϊ10M
        //getConf().setMaxReceiveSize(1024 * 1024 * 10);

        //�������URL
        //addSeed("http://www.meishij.net");
        for(int i = 1; i <= 10; i++) {
            this.addSeed("http://#######/forumdisplay.php?fid=19&page="+i, "list");
        }
        
        //�޶���ȡ��Χ
        //addRegex("http://www.meishij.net/.*");
        //addRegex("http://45.32.78.185/htm_data.*.html");
        //addRegex("-.*#.*");
        //addRegex("-.*\\?.*");
        //����Ϊ�ϵ���ȡ������ÿ�ο������涼��������ȡ
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
					fout.write(new String("�����������:"+"http://*********/"+h.select("span>[href]").first().attr("href")).getBytes());
					fout.write("\r\n".getBytes());// д��һ������  
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
        	//��ȡ���ӱ���
            String title = page.select("div[id=threadtitle]>h1").text();
            //��ȡ
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
        	//����ͼƬ��������~~~~~~~
        	String imageurl = page.url();
        	String extendname = imageurl.substring(imageurl.lastIndexOf('/'),imageurl.length());
        	byte[] image = page.content();
        	File imageFile = new File(baseDir, extendname);
        	try {
				FileUtils.write(imageFile, image);
				FileOutputStream fout = new FileOutputStream(file,true);
				fout.write(new String("����ͼƬ "+page.url()+" �� "+ imageFile.getAbsolutePath()).getBytes());
				fout.write("\r\n".getBytes());// д��һ������  
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
        //����httpͷ�е�Content-Type��Ϣ���жϵ�ǰ��Դ����ҳ����ͼƬ
        String contentType = page.contentType();
        try {
			FileOutputStream fout = new FileOutputStream(file);
			fout.write("----------------------------------".getBytes());
			fout.write("��ȡ�����ӣ���ʼ��ȡͼƬ".getBytes());
			fout.write(("contentType="+contentType).getBytes());
		} catch (Exception e1) {
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();
		}
        //����Content-Type�ж��Ƿ�ΪͼƬ
        if(contentType!=null && contentType.startsWith("image")){
            //��Content-Type�л�ȡͼƬ��չ��
            String extensionName=contentType.split("/")[1];
            try {
                byte[] image = page.content();
                //����ͼƬMD5�����ļ���
                String fileName = String.format("%s.%s",MD5Utils.md5(image), extensionName);
                File imageFile = new File(baseDir, fileName);
                FileUtils.write(imageFile, image);
                System.out.println("����ͼƬ "+page.url()+" �� "+ imageFile.getAbsolutePath());
            } catch (Exception e) {
                ExceptionUtils.fail(e);
            }
        }
    }*/

    public static void main(String[] args) throws Exception {
    	
    	
    	Crawlto1024 demoImageCrawler = new Crawlto1024("crawl");
        demoImageCrawler.setRequester(new OkHttpRequester());
        //����Ϊ�ϵ���ȡ������ÿ�ο������涼��������ȡ
        demoImageCrawler.setResumable(false);
        demoImageCrawler.start(3);
    }
}