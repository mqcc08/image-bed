package mc.image.bed.util;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import mc.image.bed.entity.GiteeAccessToken;
import mc.image.bed.entity.HttpResponseEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class GiteeOssUtil {


    static String authAddr = "https://gitee.com/oauth/token";
    public static String clientId = "16b830c562f6d82f4c3c659eeb86fc41c7ab56c4f66bb9c64db28227007157b1";
    public static String clientSecret = "8260f7bb96e2aa9f244bb060945526256c1a75d080a7f6fba6879469c07a3257";


    public static void initGiteeAccessToken(){
        GiteeAccessToken gat = null;
        try{
            HttpResponseEntity httpResponseEntity = getGiteeAccessToken();
            if (httpResponseEntity != null && httpResponseEntity.getStatus() == 200){
                log.info(httpResponseEntity.getContent());
                gat = JSON.parseObject(httpResponseEntity.getContent() , GiteeAccessToken.class);
            }
            if (gat == null){
                log.info("|-------------初始化 Gitee accessToken 失败----------------|");
                return;
            }
            String accessToken = gat.getAccess_token();
            if (StringUtils.isNotBlank(accessToken)){
                GiteeContants.GiteeAccesToken = accessToken;
                log.info("|-------------初始化 Gitee accessToken 成功----------------|");
            }else{
                log.info("|-------------初始化 Gitee accessToken 失败----------------|");
            }
        }catch (Exception e){
            log.info("|-------------初始化 Gitee accessToken 失败----------------|");
            e.printStackTrace();
        }

    }
    /**
     *
     *
     * curl -X POST --data-urlencode "grant_type=password" --data-urlencode "username={email}" --data-urlencode "password={password}" --data-urlencode "client_id={client_id}" --data-urlencode "client_secret={client_secret}" --data-urlencode "scope=projects user_info issues notes" https://gitee.com/oauth/token
     *
     * "https://gitee.com/oauth/token?grant_type=authorization_code&code={code}&client_id={client_id}&redirect_uri={redirect_uri}&client_secret={client_secret}";
     *
     * @return
     */
    public static HttpResponseEntity getGiteeAccessToken(){
        HttpResponseEntity httpResponseEntity = null;
        try{
            Map<String , String> params = new HashMap<>();
            params.put("username" , ("liufeiworkwy@163.com"));
            params.put("password" , ("LiuFei520"));
            params.put("client_id" , (clientId));
            params.put("client_secret" , (clientSecret));
            params.put("grant_type" , ("password"));
            params.put("scope" , ("user_info projects gists"));
            httpResponseEntity = HttpClient.doPost(authAddr, params , null);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return  httpResponseEntity;
    }

    /**
     *  获取文件
     * @param sha
     * @return
     */
    public static String getFiles(String sha){
        String responseBody = null;
        try{
            Map<String , String> params = new HashMap<>();
            params.put("access_token" , GiteeContants.GiteeAccesToken);
            //params.put("recursive" , "");

            String treesAddr = GiteeContants.trees;
            treesAddr = treesAddr.replace("{owner}" , GiteeContants.owner)
                    .replace("{repo}" , GiteeContants.repo)
                    .replace("{sha}" , sha);
            HttpResponseEntity httpResponseEntity = HttpClient.doGet(treesAddr, params);
            responseBody = httpResponseEntity.getContent();
        }catch (Exception e){
            e.printStackTrace();
        }
        return responseBody;
    }
    /**
     *  创建文件
     * @param fileName 文件名
     * @param dirNme    文件夹名
     * @param base64String 文件Base64 字符串
     * @return
     */
    public static HttpResponseEntity createFile(String fileName , String dirNme , String base64String){
        HttpResponseEntity httpResponseEntity = null;
        try{
            httpResponseEntity = updateFile(fileName , dirNme , base64String , null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return httpResponseEntity;
    }

    /**
     * 删除文件
     * @param fileName 文件名
     * @param dirNme    文件夹名
     * @param sha  单个文件的sha
     * @return
     */
    public static HttpResponseEntity deleteFile(String fileName , String dirNme , String sha){
        HttpResponseEntity httpResponseEntity = null;
        try{
            httpResponseEntity = updateFile(fileName , dirNme , null , sha);
        }catch (Exception e){
            e.printStackTrace();
        }
        return httpResponseEntity;
    }



    /**
     *
     * @param fileName 文件名
     * @param dirNme    文件夹名
     * @param base64String 文件Base64 字符串
     * @return
     */
    public static HttpResponseEntity updateFile(String fileName , String dirNme , String base64String , String sha){
        HttpResponseEntity httpResponseEntity = null;
        try{
            Map<String , String> params = new HashMap<>();
            params.put("access_token" , GiteeContants.GiteeAccesToken);

            params.put("message" , "创建文件");
            params.put("branch" , "master");
            params.put("committer[name]" , GiteeContants.username);
            params.put("committer[email]" , GiteeContants.username);
            params.put("author[name]" , GiteeContants.username);
            params.put("author[email]" , GiteeContants.username);

            if (StringUtils.isBlank(sha)){
                // 新增文件
                params.put("content" , base64String);
            }else{
                params.put("sha" , sha);
                if (StringUtils.isNotBlank(base64String)){
                    // 更新文件
                    params.put("content" , base64String);
                }else{
                    // 删除文件
                }
            }
            // https://gitee.com/api/v5/repos/{owner}/{repo}/contents/{path}
            String createFileAddr = GiteeContants.createFileAddr;
            createFileAddr = createFileAddr.replace("{owner}" , GiteeContants.owner)
                    .replace("{repo}" , GiteeContants.repo)
                    .replace("{path}" , dirNme+"/"+fileName);
            httpResponseEntity = HttpClient.doPost(createFileAddr, params);
        }catch (Exception e){
            e.printStackTrace();
        }
        return httpResponseEntity;
    }



    public HttpResponseEntity buildPages(){
        HttpResponseEntity httpResponseEntity = null;

        String buildPages = GiteeContants.buildPagesAddr;
        buildPages = buildPages.replace("{owner}" , GiteeContants.owner)
                .replace("{repo}" , GiteeContants.repo);
        HttpClient.doGet(buildPages , null);

        return httpResponseEntity;
    }



    public static HttpResponseEntity getFileBlob(String sha){
        HttpResponseEntity httpResponseEntity = null;
        Map<String , String> params = new HashMap<>();
        params.put("access_token" , GiteeContants.GiteeAccesToken);
        String fileBlobAddr = GiteeContants.fileBlobAddr;
        fileBlobAddr = fileBlobAddr.replace("{owner}" , GiteeContants.owner)
                .replace("{repo}" , GiteeContants.repo)
                .replace("{sha}" , sha);
        try{
            httpResponseEntity = HttpClient.doGet(fileBlobAddr, params);
        }catch (Exception e){
            e.printStackTrace();
        }

        return httpResponseEntity;
    }














    public static void main(String[] args) {
        //String giteeAccessToken = getGiteeAccessToken();
        //System.out.println(giteeAccessToken);

        String str = "------------------------------------------";
        try{
            String s = Base64.getEncoder().encodeToString(str.getBytes("UTF-8"));
            System.out.println(s);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
