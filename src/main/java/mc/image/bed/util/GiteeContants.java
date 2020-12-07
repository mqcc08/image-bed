package mc.image.bed.util;

import org.springframework.context.annotation.Configuration;

@Configuration
public class GiteeContants {

    // 全局静态变量
    public static String GiteeAccesToken;

    public static String encoding = "UTF-8";


    public static String owner = "liufeiworkwy";
    public static String repo = "gitee-oss";
    public static String scope = "user_info projects gists";


    // gitee 静态地址
    /**
     *  owner - 个人的地址path
     *  repo - 仓库路径(path)
     *  sha - 可以是分支名(如master)、Commit或者目录Tree的SHA值
     *
     */
    public static String trees = "https://gitee.com/api/v5/repos/{owner}/{repo}/git/trees/{sha}";
    public static String createFileAddr = "https://gitee.com/api/v5/repos/{owner}/{repo}/contents/{path}";
    public static String buildPagesAddr = "https://gitee.com/api/v5/repos/{owner}/{repo}/pages/builds";
    public static String fileBlobAddr = "https://gitee.com/api/v5/repos/{owner}/{repo}/git/blobs/{sha}";








    //
    public static String username = "liufeiworkwy@163.com";
    public static String password;
    public static String authAddr;
    public static String clientId;
    public static String clientSecret;


    public void setUsername(String username) {
        GiteeContants.username = username;
    }

    public void setPassword(String password) {
        GiteeContants.password = password;
    }

    public void setAuthAddr(String authAddr) {
        GiteeContants.authAddr = authAddr;
    }

    public void setClientId(String clientId) {
        GiteeContants.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        GiteeContants.clientSecret = clientSecret;
    }

    public void setScope(String scope) {
        GiteeContants.scope = scope;
    }
}
