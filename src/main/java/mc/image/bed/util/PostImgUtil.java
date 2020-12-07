package mc.image.bed.util;

import mc.image.bed.entity.HttpResponseEntity;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.*;

public class PostImgUtil {



    static String RootPath = "https://postimg.cc";
    static String ImagePath = "https://i.postimg.cc";

    public static List<Map<String , String>> getImagesByCategory(String category , Integer page ){
        String categoryPath = RootPath+"/gallery/"+category;

        /**
         * action: list
         * album: jTQZ4rj
         * page: 2
         */
        Map<String , String> params = new HashMap<>();
        params.put("action" , "list");
        params.put("album" , category);
        params.put("page" , (page == null?"1":String.valueOf(page)));
        HttpResponseEntity httpResponseEntity = HttpClient.doGet(categoryPath, params);
        return  string2Document(httpResponseEntity.getContent());
    }



    public static String jsonImagesByCategory(String category , Integer page ){
        String categoryPath = RootPath+"/json";
        /**
         * action: list
         * album: jTQZ4rj
         * page: 2
         */
        Map<String , String> params = new HashMap<>();
        params.put("action" , "list");
        params.put("album" , category);
        params.put("page" , (page == null?"1":String.valueOf(page)));
        HttpResponseEntity httpResponseEntity = HttpClient.doGet(categoryPath, params);
        return  httpResponseEntity.getContent();
    }






    public static List<Map<String , String>> string2Document(String htmlString){
        Document parse = Jsoup.parse(htmlString);
        Elements elementsByClass = parse.getElementsByClass("thumb-container");
        List<Map<String , String>> maps = new ArrayList<>();
        Map<String , String>  tmpMap = null;
        for (Element element : elementsByClass){
            tmpMap = new HashMap<>();
            String dataThumbDir = element.attr("data-image");
            String dataHotlinkDir = element.attr("data-hotlink");
            String dataExt = element.attr("data-ext");
            String dataName = element.attr("data-name")+"."+dataExt;
            String thumbUrl = ImagePath + "/" + dataThumbDir + "/" + dataName;
            String imagebUrl = ImagePath + "/" + dataHotlinkDir + "/" + dataName;
            tmpMap.put("key" , thumbUrl);
            tmpMap.put("value" , imagebUrl);
            maps.add(tmpMap);
        }
        return maps;
    }











    public static List<String> getCategorys(){
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        String catrgoryString = null;
        List<String> catrgorys = null;
        try{
            Resource resource = new ClassPathResource("data/category.properties");
            is = resource.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            while((catrgoryString = br.readLine()) != null) {
                if (StringUtils.isNotBlank(catrgoryString)){
                    catrgorys = Arrays.asList(catrgoryString.split(":")[1].trim().split(","));
                }
            }
            br.close();
            isr.close();
            is.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return catrgorys;
    }






    public static void main(String[] args) {

        Map<String , String> params = new HashMap<>();


        HttpResponseEntity httpResponseEntity = HttpClient.doGet("https://picloli.com/album/jindo.LS6L", null);

        Document parse = Jsoup.parse(httpResponseEntity.getContent());

        Elements elements = parse.getElementsByTag("script").eq(9);


        String authTokenString = "";
        String authToken = "";
        for (Element element : elements){
            String[] split = element.data().split(";");
            for (String str : split){
                if (str.contains("PF.obj.config.auth_token")){
                    authTokenString = str.trim();
                    break;
                }
            }
        }
        if (StringUtils.isNotBlank(authTokenString)){
            String[] split = authTokenString.split("=");
            authToken = split[1].trim();
            authToken = authToken.substring(1 , authToken.length() -1);
        }

        System.out.println(authTokenString);
        System.out.println(authToken);

    }


}
