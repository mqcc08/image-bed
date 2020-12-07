package mc.image.bed.controller;


import com.alibaba.fastjson.JSON;
import mc.image.bed.util.PostImgUtil;
import mc.image.bed.util.Util;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/postimg")
public class PostImageController {



    @RequestMapping(value = "/jsons" , method = RequestMethod.POST)
    public Object jsons(String category , Integer page){
        String jsonImagesByCategory = PostImgUtil.jsonImagesByCategory(category, page);
        Map map = JSON.parseObject(jsonImagesByCategory, Map.class);
        return Util.respSuccessMsg("操作成功!" , map.get("images"));
    }

    @RequestMapping(value = "/jsonCategorys" , method = RequestMethod.POST)
    public Object jsonCategorys(){

        List<String> categorys = PostImgUtil.getCategorys();

        List<Map<String , String>> categoryMaps = new ArrayList<>();
        Map<String , String> tmpMap = null;
        for (String str : categorys){
            tmpMap = new HashMap<>();
            String[] split = str.split("\\|");
            tmpMap.put("key" , split[0]);
            tmpMap.put("value" , split[1]);
            categoryMaps.add(tmpMap);
        }
        return Util.respSuccessMsg("操作成功!" , categoryMaps);
    }
}
