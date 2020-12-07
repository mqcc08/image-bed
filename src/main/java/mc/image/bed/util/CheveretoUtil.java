package mc.image.bed.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import mc.image.bed.entity.HttpResponseEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.cookie.Cookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;


@Slf4j
public class CheveretoUtil {

    static String globalDoname = "https://picloli.com"; //img.wenhairu.com/ picloli.com /dzimg.cn


    static String globalAuthToken = "";

    static String globalSeek = "";





    public static List<Map<String , String>> getCategorys(){
        long startLong = System.currentTimeMillis();
        HttpResponseEntity httpResponseEntity = HttpClientCheUtil.doGet(globalDoname+"/user/mqcc/albums", null);
        String content = httpResponseEntity.getContent();
        List<Map<String , String>> mapList = new ArrayList<>();
        Map<String , String> tmpMap = null;
        if (StringUtils.isNotBlank(content)){
            Document document = Jsoup.parse(content);
            Elements elements = document.select(".list-item");
            Element element = null;

            for (Element tmpElement : elements){
                tmpMap = new HashMap<>();
                element = tmpElement.select(".list-item-desc-title a").first();
                tmpMap.put("categoryName" ,element.text() );
                String href = element.attr("href");
                tmpMap.put("categoryHref" , href.substring(href.lastIndexOf("/")+1 , href.length()));
                element = tmpElement.select(".list-item-desc-title span").first();
                tmpMap.put("categoryCount" ,element.text().replace("图片",""));
                Elements select = tmpElement.select(".list-item-image a img");
                String dirThumb = "";
                if (select != null && select.first() != null){
                    dirThumb = select.first().attr("src");
                }
                tmpMap.put("categoryThumb" , dirThumb);
                mapList.add(tmpMap);
            }
        }
        return mapList;
    }


    public static List<Map<String , String>> getImagesPage(String category){
        HttpResponseEntity httpResponseEntity = HttpClientCheUtil.doGet(globalDoname+"/album/"+category, null);
        List<Map<String , String>> mapList = new ArrayList<>();
        if (httpResponseEntity != null && StringUtils.isNotBlank(httpResponseEntity.getContent())){
            Document document = Jsoup.parse(httpResponseEntity.getContent());
            Elements elements = document.getElementsByTag("script").eq(9);
            String authTokenString = "";
            String authToken = "";
            String seek = "";
            Elements elementsByAttribute = document.getElementsByAttribute("data-seek");
            seek = document.select(".content-listing-more button").attr("data-seek");
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
            }
            globalAuthToken = authToken;
            globalSeek = seek;
            mapList =  setPageData(document);
        }
        return mapList;
    }

    public static List<Map<String , String>> getImagesPageNumber(String category , Integer paheNumber){

        if (globalSeek == null || globalAuthToken == null){
            getImagesPage(category);
        }

        List<Map<String , String>> mapList = new ArrayList<>();
        Map<String , String> params = new HashMap<>();
        params.put("action","list");
        params.put("list","images");
        params.put("sort","date_desc");
        params.put("page",(paheNumber== null?"1":String.valueOf(paheNumber)));
        params.put("from","album");
        params.put("albumid",category);
        params.put("params_hidden[list]","images");
        params.put("params_hidden[from]","album");
        params.put("params_hidden[albumid]",category);
        params.put("params_hidden[params_hidden]","");
        params.put("seek",globalSeek);
        params.put("auth_token", globalAuthToken);
        Map<String , String> headers = new HashMap<>();
        headers.put("User-Agent" , "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Safari/537.36");
        //headers.put("Content-Type", "application/x-www-form-urlencoded");//设置http头部信息
        headers.put("origin" , "https://picloli.com");
        headers.put("referer" , "https://picloli.com/album/jindo.LS6L");
        HttpResponseEntity httpResponseEntity = HttpClientCheUtil.doPostHttps(globalDoname+"/json", params , headers);
        log.info("===> "+httpResponseEntity.getContent());

        if (httpResponseEntity != null && StringUtils.isNotBlank(httpResponseEntity.getContent())){

            Map map = JSON.parseObject(httpResponseEntity.getContent(), Map.class);
            if (map != null){
                Object html = map.get("html");
                if (html != null){
                    Document document = Jsoup.parse(String.valueOf(html));
                    mapList = setPageData(document);
                }
            }

        }
        return mapList;
    }

    public static List<Map<String , String>> setPageData(Document document){
        log.info(" ====== > 进入数据解析 开始");

        long l = System.currentTimeMillis();
        List<Map<String , String>> mapList = new ArrayList<>();
        Map<String , String> tmpMap = null;
        Elements elements = document.select(".list-item");
        Element element = null;
        for (Element tmpElement : elements){
            tmpMap = new HashMap<>();
            element = tmpElement.select(".list-item-image a img").first();
            String imageSrcThumb = element.attr("src");
            tmpMap.put("imageSrcThumb" , imageSrcThumb);
            tmpMap.put("imageSrcOrg" , imageSrcThumb.replace(".md",""));
            element = tmpElement.select(".list-item-desc  .list-item-desc-title-link").first();
            tmpMap.put("imageName" , element.text());
            mapList.add(tmpMap);
        }
        log.info(" ====== > 解析并封装数据耗时:{} 毫秒" , (System.currentTimeMillis() - l));
        return mapList;
    }

    public static void main(String[] args) {
        String category = "jindo.LS6L";
        getImagesPage(category);
        getImagesPageNumber(category , 2);
    }
}
