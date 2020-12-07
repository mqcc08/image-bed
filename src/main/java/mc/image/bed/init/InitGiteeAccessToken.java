package mc.image.bed.init;

import lombok.extern.slf4j.Slf4j;
import mc.image.bed.util.GiteeContants;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InitGiteeAccessToken implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {


        log.info("-----------------------------------------------------------");
        //GiteeOssUtil.initGiteeAccessToken();
        log.info("-----------------------------------------------------------");
        log.info("====== > Gitee accessToken = {}" , GiteeContants.GiteeAccesToken);

    }




}
