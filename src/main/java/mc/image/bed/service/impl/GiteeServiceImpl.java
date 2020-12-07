package mc.image.bed.service.impl;

import com.alibaba.fastjson.JSON;
import mc.image.bed.util.GiteeOssUtil;
import mc.image.bed.entity.AccordionEntity;
import mc.image.bed.entity.GiteeOssFile;
import mc.image.bed.entity.GiteeOssResponse;
import mc.image.bed.entity.HttpResponseEntity;
import mc.image.bed.service.IGiteeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;


@Service
public class GiteeServiceImpl implements IGiteeService {


    @Override
    public List<AccordionEntity> refresh(String accordionEntityString) {

        try{

            List<AccordionEntity> returnAccordionEntities = new ArrayList<>();
            List<AccordionEntity> accordionEntities = JSON.parseArray(accordionEntityString, AccordionEntity.class);
            String sha = "";
            AccordionEntity accordionEntity = null;
            for (AccordionEntity entity : accordionEntities){
                if (!"根目录".equals(entity.getName().trim())){
                    GiteeOssResponse files = this.getFiles(sha);
                    List<GiteeOssFile> tree = files.tree;
                    for (GiteeOssFile giteeOssFile : tree){
                        if ("tree".equals(giteeOssFile.getType()) && entity.getName().trim().equals(giteeOssFile.getPath())){
                            sha = giteeOssFile.getSha();
                            accordionEntity = new AccordionEntity();
                            accordionEntity.setName(giteeOssFile.getPath());
                            accordionEntity.setSha(sha);
                            returnAccordionEntities.add(accordionEntity);
                            break;
                        }
                    }
                }else{
                    accordionEntity = new AccordionEntity();
                    accordionEntity.setName(entity.getName());
                    accordionEntity.setSha(sha);
                    returnAccordionEntities.add(accordionEntity);
                }
            }
            return returnAccordionEntities;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public HttpResponseEntity createFile(String dirPath , MultipartFile multipartFile) {
        HttpResponseEntity httpResponseEntity = null;
        try{
            byte[] bytes = multipartFile.getBytes();
            String encodeToString = Base64.getEncoder().encodeToString(bytes);
            httpResponseEntity = GiteeOssUtil.createFile(multipartFile.getOriginalFilename(), dirPath, encodeToString);
        }catch (Exception e){
            e.printStackTrace();
        }
        return httpResponseEntity;
    }

    @Override
    public HttpResponseEntity delete(String fileName , String dirPath, String sha) {
        HttpResponseEntity httpResponseEntity = null;
        try{
            httpResponseEntity = GiteeOssUtil.deleteFile(fileName, dirPath, sha);

        }catch (Exception e){
            e.printStackTrace();
        }
        return httpResponseEntity;
    }


    @Override
    public GiteeOssResponse getFiles(String sha) {
        if (StringUtils.isBlank(sha)){
            sha = "master";
        }
        GiteeOssResponse grr = null;
        try{
            String files = GiteeOssUtil.getFiles(sha);
            if (StringUtils.isNotBlank(files)){
                grr = JSON.parseObject(files , GiteeOssResponse.class);
                return grr;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



    @Override
    public String  getFileBlob(String sha) {
        HttpResponseEntity fileBlob = GiteeOssUtil.getFileBlob(sha);
        Map<String , String> map = JSON.parseObject(fileBlob.getContent(), Map.class);

        return map.get("content");
    }
}
