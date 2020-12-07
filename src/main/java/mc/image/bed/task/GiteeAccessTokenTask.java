package mc.image.bed.task;

import mc.image.bed.util.GiteeOssUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class GiteeAccessTokenTask {



    @Scheduled(cron = "1 0 0 * * ? ")
    public void refreshAccessToken(){
        GiteeOssUtil.getGiteeAccessToken();
    }




}
