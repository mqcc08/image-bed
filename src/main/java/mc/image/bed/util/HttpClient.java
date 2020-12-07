package mc.image.bed.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import mc.image.bed.entity.HttpResponseEntity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")


@Slf4j
public class HttpClient {





    public static HttpResponseEntity doGet(String url  , Map<String , String> params) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();


        long startLong = System.currentTimeMillis();
        CookieStore httpCookieStore = new BasicCookieStore();

        // get请求返回结果
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        try{
            httpClient = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore).build();
            //装填参数
            if (params!=null){
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                if (params != null) {
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                    }
                }
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(pairs, "utf-8");
                urlEncodedFormEntity.setContentType("application/x-www-form-urlencoded");
                url += "?" + EntityUtils.toString(urlEncodedFormEntity);
                log.info(" ==== > 请求地址：" + url);
                log.info(" ==== > 请求参数：" + pairs.toString());
            }


            // 发送get请求
            httpGet = new HttpGet(url);
            httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");//设置http头部信息
            httpGet.setHeader("User-Agent" , "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Safari/537.36");

            HttpResponse response = httpClient.execute(httpGet);
            log.info("状态码: {} , 耗时: {}毫秒" , response.getStatusLine() , (System.currentTimeMillis() - startLong));
            httpResponseEntity.setStatus(response.getStatusLine().getStatusCode());
            httpResponseEntity.setCookies(httpCookieStore.getCookies());
            // 请求发送成功，并得到响应
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                // 读取服务器返回过来的json字符串数据
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity, "utf-8");
                httpResponseEntity.setContent(responseString);
            }
            else{
                log.error("get请求提交失败:" + url);
            }
        }
        catch (IOException e){
            log.error("get请求提交失败:" + url, e);
        }finally{
            if (httpGet != null){
                httpGet.releaseConnection();
            }
        }
        return httpResponseEntity;
    }
    public static String doPost(String requestUrl , String requestString) {
        // get请求返回结果
        String responseString = "";
        org.apache.http.client.HttpClient httpClient = null;
        HttpPost httpPost = null;

        try{
            httpClient = new DefaultHttpClient();
            // 设置POST请求
            httpPost = new HttpPost(requestUrl);

            //添加body
            ByteArrayEntity byteArrayEntity = new ByteArrayEntity(requestString.getBytes("UTF-8"));;
            //byteArrayEntity.setContentType("application/json; charset=utf-8");


            httpPost.setEntity(byteArrayEntity);
            HttpResponse response = httpClient.execute(httpPost);
            log.info("状态码：" + response.getStatusLine());

            // 请求发送成功，并得到响应
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                // 读取服务器返回过来的json字符串数据
                HttpEntity entity = response.getEntity();
                responseString = EntityUtils.toString(entity, "utf-8");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            httpPost.releaseConnection();
        }
        return responseString;
    }

    public static HttpResponseEntity doPost(String requestUrl , Map<String , String> params , Map<String , String> headers) {
        // get请求返回结果

        log.info(" ======> url = {} ", requestUrl);
        log.info(" ======> params = {} ", JSON.toJSONString(params));

        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();

        DefaultHttpClient httpClient = null;
        HttpPost httpPost = null;
        try{
            httpClient = new DefaultHttpClient();
            // 设置POST请求
            httpPost = new HttpPost(requestUrl);
            // 创建参数列表
            if (params != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : params.keySet()) {
                    paramList.add(new BasicNameValuePair(key, params.get(key).toString()));
                }
                // 模拟表单
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(paramList,"utf-8");
                urlEncodedFormEntity.setContentType("application/x-www-form-urlencoded");
                httpPost.setEntity(urlEncodedFormEntity);

                if (headers !=null && headers.size() >0){
                    for (Map.Entry<String , String> entity : headers.entrySet()){
                        httpPost.setHeader(entity.getKey() , entity.getValue());

                    }
                }
            }
            HttpResponse response = httpClient.execute(httpPost);
            log.info("状态码：" + response.getStatusLine());
            //log.info("状态码：" + JSON.toJSONString(response));
/*
*/
            httpResponseEntity.setStatus(response.getStatusLine().getStatusCode());
            // 请求发送成功，并得到响应
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                // 读取服务器返回过来的json字符串数据
                HttpEntity entity = response.getEntity();

                System.out.println(JSON.toJSONString(entity));

                String responseString = EntityUtils.toString(entity, "utf-8");
                httpResponseEntity.setContent(responseString);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            httpPost.releaseConnection();
        }
        return httpResponseEntity;
    }

    public static HttpResponseEntity doPost(String requestUrl , Map<String , String> params) {
        // get请求返回结果

        log.info(" ======> url = {} ", requestUrl);
        log.info(" ======> params = {} ", JSON.toJSONString(params));

        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();

        org.apache.http.client.HttpClient httpClient = null;
        HttpPost httpPost = null;
        try{
            httpClient = new DefaultHttpClient();
            // 设置POST请求
            httpPost = new HttpPost(requestUrl);
            // 创建参数列表
            if (params != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : params.keySet()) {
                    paramList.add(new BasicNameValuePair(key, params.get(key).toString()));
                }
                // 模拟表单
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(paramList,"utf-8");
                urlEncodedFormEntity.setContentType("application/x-www-form-urlencoded");
                httpPost.setEntity(urlEncodedFormEntity);


            }
            HttpResponse response = httpClient.execute(httpPost);
            log.info("状态码：" + response.getStatusLine());
            log.info("状态码：" + JSON.toJSONString(response));
            /*
             */
            httpResponseEntity.setStatus(response.getStatusLine().getStatusCode());
            // 请求发送成功，并得到响应
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                // 读取服务器返回过来的json字符串数据
                HttpEntity entity = response.getEntity();

                System.out.println(JSON.toJSONString(entity));

                String responseString = EntityUtils.toString(entity, "utf-8");
                httpResponseEntity.setContent(responseString);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            httpPost.releaseConnection();
        }
        return httpResponseEntity;
    }

    public static String doPost(String requestUrl) {
        // get请求返回结果
        String responseString = "";
        org.apache.http.client.HttpClient httpClient = null;
        HttpPost httpPost = null;

        try{
            httpClient = new DefaultHttpClient();
            // 设置POST请求
            httpPost = new HttpPost(requestUrl);
            // 创建参数列表
            HttpResponse response = httpClient.execute(httpPost);
            log.info("状态码：" + response.getStatusLine());

            // 请求发送成功，并得到响应
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                // 读取服务器返回过来的json字符串数据
                HttpEntity entity = response.getEntity();
                responseString = EntityUtils.toString(entity, "utf-8");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            httpPost.releaseConnection();
        }
        return responseString;
    }



    public static HttpResponseEntity doPostHttps(String requestUrl , Map<String , String> params , Map<String , String> headers) {
        // get请求返回结果

        log.info(" ======> url = {} ", requestUrl);
        log.info(" ======> params = {} ", JSON.toJSONString(params));

        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();

        DefaultHttpClient httpClient = null;
        HttpPost httpPost = null;
        try{
            httpClient = new SSLClient();
            // 设置POST请求
            httpPost = new HttpPost(requestUrl);
            // 创建参数列表
            if (params != null) {
//                List<NameValuePair> paramList = new ArrayList<>();
//                for (String key : params.keySet()) {
//                    paramList.add(new BasicNameValuePair(key, params.get(key).toString()));
//                }
//                // 模拟表单
//                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(paramList,"utf-8");
//                urlEncodedFormEntity.setContentType("application/x-www-form-urlencoded");
//                httpPost.setEntity(urlEncodedFormEntity);


                httpPost.setEntity(new StringEntity(JSON.toJSONString(params)));

            }
            if (headers !=null && headers.size() >0){
                for (Map.Entry<String , String> entity : headers.entrySet()){
                    httpPost.setHeader(entity.getKey() , entity.getValue());

                }
            }
            HttpResponse response = httpClient.execute(httpPost);
            log.info("状态码：" + response.getStatusLine());
            //log.info("状态码：" + JSON.toJSONString(response));
            /*
             */
            httpResponseEntity.setStatus(response.getStatusLine().getStatusCode());
            // 请求发送成功，并得到响应
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                // 读取服务器返回过来的json字符串数据
                HttpEntity entity = response.getEntity();

                System.out.println(JSON.toJSONString(entity));

                String responseString = EntityUtils.toString(entity, "utf-8");
                httpResponseEntity.setContent(responseString);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            httpPost.releaseConnection();
        }
        return httpResponseEntity;
    }


















    static class MyAuth extends Authenticator
    {
        private String user;
        private String pass;

        public MyAuth(String user, String pass)
        {
            this.user  = user;
            this.pass = pass;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return  new PasswordAuthentication(user, pass.toCharArray());
        }
    }

}

