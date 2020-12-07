package mc.image.bed.util;

import com.alibaba.fastjson.JSON;
import mc.image.bed.entity.HttpResponseEntity;

import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ImageBedUtil {


    static String uploadUrl = "https://kfupload.alibaba.com/mupload";
    public static void uploadFile(){
        Map<String , String> params = new HashMap<>();
        File file = new File("F://xj.jpg");
        byte[] bytes = File2byte(file);
        String encodeToString = Base64.getEncoder().encodeToString(bytes);
        params.put("file" , encodeToString);
        params.put("name" , UUID.randomUUID().toString()+".jpg");
        params.put("scene" , "aeMessageCenterV2ImageRule");


        HttpResponseEntity httpResponseEntity = HttpClient.doPost(uploadUrl, params , null);
        System.out.println(JSON.toJSONString(httpResponseEntity));
    }
    public static byte[] File2byte(File tradeFile){
        byte[] buffer = null;
        try
        {
            FileInputStream fis = new FileInputStream(tradeFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return buffer;
    }
    public static void main(String[] args) {
        uploadFile();
    }

}
