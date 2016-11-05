package ccy.work.commons.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AES加密解密工具类
 * chenchunyu test  123
 */
public class AESUtil {
	
	public static final String AES_ENCODE_FORMAT = "UTF-8";//"UTF-16LE";
	
	/**
	 * AES 加密
	 * @param str
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String str,String key)throws Exception{
		if ((null == str) || (str.trim().length() < 1)) {
		      return null;
		    }
		
	        byte[] keybytes = key.getBytes(AES_ENCODE_FORMAT);	
		    byte[] ivbytes = new byte[16];
		
		    SecretKeySpec skeySpec = new SecretKeySpec(keybytes, "AES");
		    IvParameterSpec iv = new IvParameterSpec(ivbytes);
		    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		    byte[] encrypted1 = str.getBytes(AES_ENCODE_FORMAT);;
		    byte[] original = cipher.doFinal(encrypted1);
		    return encode(new String(Base64.encodeBase64(original),AES_ENCODE_FORMAT));
	}

	/**
	 * AES 解密
	 * @param str
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String str, String key) throws Exception{
	    if ((null == str) || (str.trim().length() < 1)) {
	      return null;
	    }
	
	    byte[] keybytes = key.getBytes(AES_ENCODE_FORMAT);	
	    byte[] ivbytes = new byte[16];
	
	    SecretKeySpec skeySpec = new SecretKeySpec(keybytes, "AES");
	    IvParameterSpec iv = new IvParameterSpec(ivbytes);
	    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	    cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
	    byte[] decrypted1 = Base64.decodeBase64(decode(str).getBytes(AES_ENCODE_FORMAT));
	    byte[] original = cipher.doFinal(decrypted1);
	
	    return new String(original,AES_ENCODE_FORMAT);
	  }
	
	
	
	public static byte[] hexToByte(String s)throws IOException{
	    int i = s.length() / 2;
	    byte[] abyte0 = new byte[i];
	    int j = 0;
	    if (s.length() % 2 != 0) {
	      throw new IOException("hexadecimal string with odd number of characters");
	    }
	    for (int k = 0; k < i; k++) {
	      char c = s.charAt(j++);
	      int l = "0123456789abcdef0123456789ABCDEF".indexOf(c);
	      if (l == -1) {
	        throw new IOException("hexadecimal string contains non hex character");
	      }
	      int i1 = (l & 0xF) << 4;
	      c = s.charAt(j++);
	      l = "0123456789abcdef0123456789ABCDEF".indexOf(c);
	      i1 += (l & 0xF);
	      abyte0[k] = (byte)i1;
	    }
	    return abyte0;
    }
	 public static String byte2hex(byte[] b)
	  {
	    String hs = "";
	    String stmp = "";

	    for (int n = 0; n < b.length; n++) {
	      stmp = Integer.toHexString(b[n] & 0xFF);
	      if (stmp.length() == 1) hs = hs + "0" + stmp; else {
	        hs = hs.concat(stmp);
	      }
	    }
	    return hs.toUpperCase();
	  }
	 
	 /**
	  * 把字符串中的 "+"转为"-",把"/"转为"_"
	  * @return
	  */
	 private static String encode(String source){
		 Pattern pattern1 = Pattern.compile("\\+");
		 Matcher matcher1 = pattern1.matcher(source);
		 source = matcher1.replaceAll("-");
		 Pattern pattern2 = Pattern.compile("/");
		 Matcher matcher2 = pattern2.matcher(source);
		 return matcher2.replaceAll("_");
	 }
	 
	 /**
	  * 把字符串中的"-"转为"+",把"_"转为"/"
	  * @param source
	  * @return
	  */
	 private static String decode(String source){
		 Pattern pattern1 = Pattern.compile("-");
		 Matcher matcher1 = pattern1.matcher(source);
		 source = matcher1.replaceAll("+");
		 Pattern pattern2 = Pattern.compile("_");
		 Matcher matcher2 = pattern2.matcher(source);
		 return matcher2.replaceAll("/");
	 }
	
	
	
  
	/**
	 * src：6
	 * 测试key ：db922449-fdfb-43
	 * 测试后加密的src：8DO4zKhanGfDRr++ezQVuA==
	 * @param args
	 * @throws Exception
	 */
    /*public static void main(String[] args) throws Exception {  
        //String key = UUID.randomUUID().toString().substring(0, 16);
    	SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    	Date isused = new Date();//cookie生效时间
		Calendar cld=Calendar.getInstance();
		cld.setTime(isused);
		cld.add(Calendar.ALL_STYLES, 10);
		Date expires = cld.getTime();//cookie失效时间
		
        String key = "db922449-fdfb-43";
        System.out.println("key:"+key);
        String src = "jdadmin@jd.com||6||1"+"||"+dd.format(isused)+"||"+dd.format(expires);  
        System.out.println(src);  
        // 加密  
        long start = System.currentTimeMillis();  
        String enString = encrypt(src, key);  
        System.out.println("加密后的字串是：" + enString);  
  
        long useTime = System.currentTimeMillis() - start;  
        System.out.println("加密耗时：" + useTime + "毫秒");  
  
        // 解密  
        start = System.currentTimeMillis();  
        String DeString = decrypt(enString, key);  
        System.out.println("解密后的字串是：" + DeString);  
        useTime = System.currentTimeMillis() - start;  
        System.out.println("解密耗时：" + useTime + "毫秒");  
    }  */
	
	 /*public static void main(String[] args) throws Exception {
		String actId = "46";
		String key = "db922449-fdfb-43";
		String enString = encrypt(actId, key);  
		System.out.println("加密后的字串是：" + enString);  
		
	    String DeString = decrypt(enString, key);  
	    System.out.println("解密后的字串是：" + DeString);  
	}*/
	 
//	 public static void main(String[] args) {
//		String source = "Kd9UWF++XSUk//qMaWUj0IYw==";
//		System.out.println(encode(source));
//	}
////



    public static void main(String[] args) throws Exception {
        String str = DateUtil.format(new Date());
//        String str = String.valueOf(System.currentTimeMillis());
        String encryptStr = AESUtil.encrypt(str, "db922449-fdfb-43");
        String decryptStr = AESUtil.decrypt(encryptStr, "db922449-fdfb-43");
        System.out.printf("str:" + str);
        System.out.printf("encryptStr:" + encryptStr);
        System.out.printf("decryptStr:" + decryptStr);
    }
	
}
