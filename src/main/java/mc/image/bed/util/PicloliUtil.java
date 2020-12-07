package mc.image.bed.util;

import lombok.extern.slf4j.Slf4j;
import mc.image.bed.entity.HttpResponseEntity;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PicloliUtil {


    static String authTokenAddr = "https://picloli.com/album/jindo.LS6L";

    public static String getAuthToken() {

        long startLong = System.currentTimeMillis();
        HttpResponseEntity httpResponseEntity = HttpClient.doGet("https://picloli.com/album/jindo.LS6L", null);
        Document parse = Jsoup.parse(httpResponseEntity.getContent());
        Elements elements = parse.getElementsByTag("script").eq(9);
        String authTokenString = "";
        String authToken = "";
        for (Element element : elements) {
            String[] split = element.data().split(";");
            for (String str : split) {
                if (str.contains("PF.obj.config.auth_token")) {
                    authTokenString = str.trim();
                    break;
                }
            }
        }
        if (StringUtils.isNotBlank(authTokenString)) {
            String[] split = authTokenString.split("=");
            authToken = split[1].trim();
            authToken = authToken.substring(1, authToken.length() - 1);
            log.info("===== > 获取authToken={} , 耗时: {} 毫秒", authToken, (System.currentTimeMillis()-startLong));
        }
        return authToken;
    }

    public static String getImagesByCategory(String authToken , Integer paheNumber){
        Map<String , String> params = new HashMap<>();
        params.put("action","list");
        params.put("list","images");
        params.put("sort","date_desc");
        params.put("page",(paheNumber== null?"1":String.valueOf(paheNumber)));
        params.put("from","album");
        params.put("albumid","LS6L");
        params.put("params_hidden[list]","images");
        params.put("params_hidden[from]","album");
        params.put("params_hidden[albumid]","LS6L");
        params.put("params_hidden[params_hidden]","");
        params.put("seek","xbo61");
        params.put("auth_token",authToken);
        HttpResponseEntity httpResponseEntity = HttpClient.doPost("https://picloli.com/json", params , null);
        System.out.println(httpResponseEntity.getContent());
        return httpResponseEntity.getContent();
    }



    public static String uploadFile(String authToken ){

        File file = new File("F://xj.jpg");

        byte[] bytes = File2byte(file);
        String s = new String(Base64.getEncoder().encode(bytes));
        Map<String , String> params = new HashMap<>();
        params.put("source",s);
        params.put("type","file");
        params.put("action","upload");
        params.put("timestamp",String.valueOf(System.currentTimeMillis()));
        params.put("nsfw","0");
        params.put("auth_token",authToken);

        Map<String , String> headers = new HashMap<>();


        headers.put("User-Agent" , "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Safari/537.36");
        headers.put("Content-Type", "application/x-www-form-urlencoded");//设置http头部信息
        headers.put("origin" , "https://picloli.com");
        headers.put("referer" , "https://picloli.com/explore/recent");
        headers.put("content-type" , "multipart/form-data; boundary=----WebKitFormBoundary0FfwUAiwyBI8fbnG");

        HttpResponseEntity httpResponseEntity = HttpClient.doPost("https://picloli.com/json", params ,headers);
        System.out.println(httpResponseEntity.getContent());
        return httpResponseEntity.getContent();
    }

    public static byte[] File2byte(File tradeFile){
        byte[] buffer = null;
        try
        {
            FileInputStream fis = new FileInputStream(tradeFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return buffer;
    }
    public static void main(String[] args) {
        String authToken = getAuthToken();
        if (StringUtils.isNotBlank(authToken)){
            uploadFile(authToken);
        }
    }
}
