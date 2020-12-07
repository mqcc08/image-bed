package mc.image.bed.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import mc.image.bed.entity.AccordionEntity;
import mc.image.bed.service.IGiteeService;
import mc.image.bed.util.GiteeContants;
import mc.image.bed.util.PostImgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Controller
public class BaseController {



    @Autowired
    private IGiteeService giteeService;



    @RequestMapping("/")
    public String index(Model model
            , @RequestParam(value = "sha" , defaultValue = "") String sha
            , @RequestParam(value = "accordions" , defaultValue = "") String accordions
    ){
        Map<String , Object> returnMap = new HashMap<>();

        try{
            List<Map<String, String>> jTQZ4rj = null;//PostImgUtil.getImagesByCategory("jTQZ4rj");
            returnMap.put("files" , jTQZ4rj);
            model.addAllAttributes(returnMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return returnPage("index");
    }

    @RequestMapping("/index2")
    public String index2(Model model
            , @RequestParam(value = "sha" , defaultValue = "") String sha
            , @RequestParam(value = "accordions" , defaultValue = "") String accordions
    ){
        Map<String , Object> returnMap = new HashMap<>();

        try{
            //List<Map<String, String>> jTQZ4rj = PostImgUtil.getImagesByCategory("jTQZ4rj" ,null);
            //returnMap.put("files" , jTQZ4rj);
            model.addAllAttributes(returnMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return returnPage("waterfall-flow");
    }

    @RequestMapping("/images")
    public String images(HttpServletRequest request , Model model){
        Map<String , Object> paramsMap = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()){
            String paraName=(String)parameterNames.nextElement();
            paramsMap.put(paraName , request.getParameter(paraName));
        }
        model.addAllAttributes(paramsMap);
        return "images";
    }





    @RequestMapping("/refresh")
    public Object refresh(@RequestParam(value = "accordions" , defaultValue = "")String accordions){
        String sha = "";
        try{
            String decodeString = new String(Base64.getDecoder().decode(accordions) , GiteeContants.encoding);
            List<AccordionEntity> accordionEntities = giteeService.refresh(decodeString);

            if (accordionEntities != null && accordionEntities.size() > 1){
                AccordionEntity accordionEntity = accordionEntities.get(accordionEntities.size()-1);
                sha = accordionEntity.getSha();
                accordions = Base64.getEncoder().encodeToString(JSON.toJSONString(accordionEntities).getBytes());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/?sha="+sha+"&accordions="+accordions;
    }








    @RequestMapping("/che")
    public String chevereto(Model model
            , @RequestParam(value = "sha" , defaultValue = "") String sha
            , @RequestParam(value = "accordions" , defaultValue = "") String accordions
    ){
        Map<String , Object> returnMap = new HashMap<>();

        try{
            //List<Map<String, String>> jTQZ4rj = PostImgUtil.getImagesByCategory("jTQZ4rj" ,null);
            //returnMap.put("files" , jTQZ4rj);
            model.addAllAttributes(returnMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "chevereto-index";
    }


    @RequestMapping("/che/images")
    public String chevereto(HttpServletRequest  request , Model model){
        Map<String , Object> returnMap = new HashMap<>();

        try{
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()){
                String paraName=(String)parameterNames.nextElement();
                returnMap.put(paraName , request.getParameter(paraName));
            }
            model.addAllAttributes(returnMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "chevereto-images";
    }







    public String returnPage(String pagePath){
        return pagePath;
    }
}
