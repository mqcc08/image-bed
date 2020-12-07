package mc.image.bed.controller;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import mc.image.bed.util.CheveretoUtil;
import mc.image.bed.util.PostImgUtil;
import mc.image.bed.util.Util;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/che")
public class CheveretoController {

    @RequestMapping(value = "/jsonCategorys" , method = RequestMethod.POST)
    public Object jsonCategorys(){
        long currentTimeMillis = System.currentTimeMillis();
        log.info("====== >  获取 categories - 开始 ");

        List<Map<String, String>> categorys = CheveretoUtil.getCategorys();
        log.info("====== >  获取 categories - 结束 ,  耗时: {} 毫秒",(System.currentTimeMillis() - currentTimeMillis));
        log.info("------------------------------------------------------------------------");
        return Util.respSuccessMsg("操作成功!" , categorys);
    }




    @RequestMapping(value = "/imagePage" , method = RequestMethod.POST)
    public Object jsons(String category){
        long currentTimeMillis = System.currentTimeMillis();
        log.info("====== >  获取 imagePage - 开始 ");
        List<Map<String, String>> imagesPage = CheveretoUtil.getImagesPage(category);
        log.info("====== >  获取 imagePage - 结束 ,  耗时: {} 毫秒",(System.currentTimeMillis() - currentTimeMillis));
        log.info("------------------------------------------------------------------------");

        return Util.respSuccessMsg("操作成功!" , imagesPage);
    }


    @RequestMapping(value = "/imagePageNumber" , method = RequestMethod.POST)
    public Object jsons(String category , Integer page){
        long currentTimeMillis = System.currentTimeMillis();
        log.info("====== >  获取 imagePageNumber - 开始 ");
        List<Map<String, String>> imagesPage = CheveretoUtil.getImagesPageNumber(category , page);
        log.info("====== >  获取 imagePageNumber - 结束 ,  耗时: {} 毫秒",(System.currentTimeMillis() - currentTimeMillis));
        log.info("------------------------------------------------------------------------");

        return Util.respSuccessMsg("操作成功!" , imagesPage );
    }

}
