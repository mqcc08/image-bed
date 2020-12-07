package mc.image.bed.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class Util {

	/**
	 * @Desciption   成功状态码
	 */
	public static final int STATUS_CODE_SUCESS=200;

	/**
	 * @Desciption   错误状态码
	 */
	public static final int STATUS_CODE_FAIL=100;



	public static final String BACKEND_THEME = "default";





	public static Map<String, Object> getParamters(String reqString){
		try{
			String deReqString = new String(Base64.decodeBase64(reqString));
			return JSON.parseObject(deReqString, Map.class);
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}



	public static HashMap<String, Object> getReturnMap() {
		HashMap<String, Object> result=new HashMap<String, Object>();
		return result;
	}


	public static HashMap<String, Object> respErrorMsg(int code, String msg) {
		HashMap<String, Object> result=new HashMap<String, Object>();
		result.put("status", code);
		result.put("code", code);
		result.put("msg",msg);
		return result;
	}


	/**
	 * @Desciption 返回正确状态码和信息给客户端
	 * @param      msg
	 */
	public static HashMap<String, Object> respSuccessMsg(String msg) {
		HashMap<String, Object> result=new HashMap<String, Object>();
		result.put("status", STATUS_CODE_SUCESS);
		result.put("code", STATUS_CODE_SUCESS);
		result.put("msg",msg);
		result.put("systemTime",new Date());
		return result;
	}


	/**
	 * 返回正确状态码和信息给客户端(带数据)
	 * @param msg
	 * @param o
	 * @return
	 */
	public static HashMap<String, Object> respSuccessMsg(String msg, Object o) {
		HashMap<String, Object> result = respSuccessMsg(msg);
		result.put("data",o);
		return result;
	}






	/**
	 * 回错误状态码和信息给客户端
	 * @param msg
	 * @param o
	 * @return
	 */
	public static HashMap<String, Object> respErrorMsg(String msg, Object o) {
		HashMap<String, Object> result = respErrorMsg(msg);
		result.put("data", o);
		return result;
	}




	public static HashMap<String, Object> respErrorMsg(String msg) {
		return respErrorMsg(STATUS_CODE_FAIL, msg);
	}
	public static HashMap<String, Object> respErrorMsg(String code , String msg) {
		return respErrorMsg(STATUS_CODE_FAIL, msg);
	}




	public static String resFrontendPage(String modulePath , String pageName){
		String returnPageName = modulePath + "/" + pageName;
		log.info("====>returnPageName:"+returnPageName);
		return returnPageName;
	}



	public static Map<String, Object> respReturnMap(){
		HashMap<String, Object> returnMap =new HashMap<String, Object>();
		//returnMap.put(StaticEnumsConfig.parKey.templatePageName,"_template_main_blog_classic");
		return returnMap;
	}


	public static Map<String, Object> resReturnMap(){
		HashMap<String, Object> returnMap =new HashMap<String, Object>();
		return returnMap;
	}




	public static String createOrderCode() {
		String time=new SimpleDateFormat("yyMMddHHmmss").format(Calendar.getInstance().getTime());
		String millis=String.valueOf(System.nanoTime());
		millis=millis.substring(11,13);
		String random=createRandom(5,true);
		return time+millis+random;
	}

	public static String createRandom(int count,boolean allDigit) {
		String CODE="123456789abcdefghjkmnopqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";
		Random random=new Random();
		StringBuffer sb = new StringBuffer();
		if(allDigit){
			CODE=CODE.substring(0,9);
		}
		for(int i=0;i<count;i++){
			int r=random.nextInt(CODE.length());
			sb.append(CODE.charAt(r));
		}
		return sb.toString();
	}
}
