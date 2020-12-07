package mc.image.bed.controller;


import mc.image.bed.util.Util;
import mc.image.bed.entity.HttpResponseEntity;
import mc.image.bed.service.IGiteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/gitee")
public class GiteeController extends BaseController {

    @Autowired
    private IGiteeService giteeService;



    @RequestMapping("/createFile")
    public Object createFile(@RequestParam(value = "file") MultipartFile file
            , @RequestParam(value = "dirPath" , defaultValue = "")String dirPath){
        String errMsg = "";
        try{
            HttpResponseEntity httpResponseEntity = giteeService.createFile(dirPath, file);
            if (httpResponseEntity.getStatus() == 201){
                return Util.respSuccessMsg("操作成功!");
            }else if (httpResponseEntity.getStatus() == 400){
                return Util.respErrorMsg(httpResponseEntity.getStatus() , "操作失败 : 文件已存在 !");
            }else{
                return Util.respErrorMsg("操作失败!");
            }
        }catch (Exception e){
            errMsg = e.getMessage();
            e.printStackTrace();
        }
        return Util.respErrorMsg("操作失败:"+errMsg);
    }


    @RequestMapping("/deleteFile")
    public Object deleteFile(@RequestParam(value = "fileName" , defaultValue = "")String fileName
            , @RequestParam(value = "dirPath" , defaultValue = "")String dirPath
            , @RequestParam(value = "sha" , defaultValue = "")String sha){
        String errMsg = "";
        try{
            HttpResponseEntity httpResponseEntity = giteeService.delete(fileName, dirPath, sha);
            if (httpResponseEntity.getStatus() == 201){
                return Util.respSuccessMsg("操作成功!");
            }else if (httpResponseEntity.getStatus() == 400){
                return Util.respErrorMsg(httpResponseEntity.getStatus() , "操作失败 : 文件已存在 !");
            }else{
                return Util.respErrorMsg("操作失败!");
            }
        }catch (Exception e){
            errMsg = e.getMessage();
            e.printStackTrace();
        }
        return Util.respErrorMsg("操作失败:"+errMsg);
    }



    @RequestMapping("/build")
    public Object buildPages(){
        return null;
    }



    @RequestMapping("/preImage")
    public Object preImage(String sha){

        Map<String , String> returnMap = new HashMap<>();
        String files = giteeService.getFileBlob(sha);
        returnMap.put("fileString" , files);
        returnMap.put("doname" , "http://liufeiworkwy.gitee.io/gitee-oss/");
        return Util.respSuccessMsg("操作成功!" , returnMap);
    }
}
