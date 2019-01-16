package util;

import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.image.AImage;

public class urlDownload {

	public static List<AImage> toImage(String[] url) {
		// url = "http://origin-nikeid.nike.com//services/imgredirect/mtr-1049507631/bgc-na/wid-574/vw-2";//
		// url = "https://img.zcool.cn/community/01f950571b8b156ac7253812e10163.jpg@1280w_1l_2o_100sh.jpg";
		String proxy = "10.8.211.251";
		int port = 8088;
		String curLine = "";
		String content = "";
		URL server = null;
		AImage aImg = null;
		List<AImage> imglist = new ArrayList<AImage>();
		HttpURLConnection connection = null;
		InputStream is = null;
		for(int i = 0; i < url.length; i++){
			try {
				server = new URL(url[i]);
				initProxy(proxy, port, "", "");
				connection = (HttpURLConnection) server.openConnection();
				connection.connect();
				is = connection.getInputStream();
				aImg = new AImage("aa", is);
				
				imglist.add(aImg);
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return imglist;
	}
	
    public static void initProxy(String host, int port, final String username,
            final String password) {
        Authenticator.setDefault(new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,
                        new String(password).toCharArray());
            }
        });
        System.setProperty("proxyType", "4");
        System.setProperty("proxyPort", Integer.toString(port));
        System.setProperty("proxyHost", host);
        System.setProperty("proxySet", "true");
    }
    
/*    public static InputStream toImage() {
        String url
        //= "http://origin-nikeid.nike.com//services/imgredirect/mtr-1049507631/bgc-na/wid-574/vw-2";//
        		="https://img.zcool.cn/community/01f950571b8b156ac7253812e10163.jpg@1280w_1l_2o_100sh.jpg";
        String proxy = "10.8.211.251";
        int port = 8088;
        String curLine = "";
        String content = "";
        URL server = null;
        HttpURLConnection connection = null ;
        InputStream is = null;
		try {
			server = new URL(url);
        initProxy(proxy, port, "", "");
        connection = (HttpURLConnection) server
                .openConnection();
        connection.connect();
        is = connection.getInputStream();
        
		} catch (IOException e1) {
			e1.printStackTrace();
		}finally{
//			if(is != null){
//				try {
//					is.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
		}
         下載到本地
        FileOutputStream out = null;
		try {
			out = new FileOutputStream("D:\\temp\\" + "ccaa.jpg");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int data;
		try {
			while ((data = is.read()) != -1) {
				out.write(data);
			}
			if(is != null){
				is.close();
			}
			if(out != null){
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return is;
		}*/

 /*   public static void main(String[] args) throws IOException {
        String url = "http://origin-nikeid.nike.com//services/imgredirect/mtr-1049507631/bgc-na/wid-574/vw-2";//
        		//"https://img.zcool.cn/community/01f950571b8b156ac7253812e10163.jpg@1280w_1l_2o_100sh.jpg";
        String proxy = "10.8.211.251";
        int port = 8088;
        String curLine = "";
        String content = "";
        URL server = new URL(url);
        initProxy(proxy, port, "", "");
        HttpURLConnection connection = (HttpURLConnection) server
                .openConnection();
        connection.connect();
        InputStream is = connection.getInputStream();
        
         下載到本地
        FileOutputStream out = null;
		try {
			out = new FileOutputStream("D:\\temp\\" + "ccaa.jpg");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int data;
		try {
			while ((data = is.read()) != -1) {
				out.write(data);
			}
			is.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
         轉換成位圖
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        ImageView imageView = (ImageView) findViewById(R.id.image_view);
        imageView.setImageBitmap(bitmap);
        


         讀取網頁內容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        while ((curLine = reader.readLine()) != null) {
           // content += curLine;
            System.out.println(curLine);
        }
        //System.out.println("content= " + content);
        is.close();
        
    }*/
}
