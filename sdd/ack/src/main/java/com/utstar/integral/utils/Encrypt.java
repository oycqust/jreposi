package com.utstar.integral.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密工具类
 * 
 * md5加密出来的长度是32位 sha加密出来的长度是40位 3DES加密解密
 */
public class Encrypt {

	private static final String ALGORITHM_DESEDE = "DESede";
	private static final String PASSWORD_CRYPT_KEY = "ivaspassword";

	/**
	 * 测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// md5加密测试

		System.out.println(e("nanchuan_2019"));
		
		
		String msg = "qwerqwer";
        System.out.println("【加密前】：" + msg);
        
        //加密
		String secretArr = Encrypt.desedeEncoder(msg);
		System.out.println("【加密后】：" + secretArr);
        
        //解密
		String myMsgArr = Encrypt.desedeDecoder(secretArr);
		System.out.println("【解密后】：" + myMsgArr);
	}

	/**
	 * 加密
	 * 
	 * @param inputText
	 * @return
	 */
	public static String e(String inputText) {
		return md5(inputText);
	}

	/**
	 * 二次加密，应该破解不了了吧？
	 * 
	 * @param inputText
	 * @return
	 */
	public static String md5AndSha(String inputText) {
		return sha(md5(inputText));
	}

	/**
	 * md5加密
	 * 
	 * @param inputText
	 * @return
	 */
	public static String md5(String inputText) {
		return encrypt(inputText, "md5");
	}

	/**
	 * sha加密
	 * 
	 * @param inputText
	 * @return
	 */
	public static String sha(String inputText) {
		return encrypt(inputText, "sha-1");
	}

	/**
	 * md5或者sha-1加密
	 * 
	 * @param inputText
	 *            要加密的内容
	 * @param algorithmName
	 *            加密算法名称：md5或者sha-1，不区分大小写
	 * @return
	 */
	private static String encrypt(String inputText, String algorithmName) {
		if (inputText == null || "".equals(inputText.trim())) {
			throw new IllegalArgumentException("请输入要加密的内容");
		}
		if (algorithmName == null || "".equals(algorithmName.trim())) {
			algorithmName = "md5";
		}
		String encryptText = null;
		try {
			MessageDigest m = MessageDigest.getInstance(algorithmName);
			m.update(inputText.getBytes("UTF8"));
			byte s[] = m.digest();
			// m.digest(inputText.getBytes("UTF8"));
			return hex(s);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encryptText;
	}

	/**
	 * 返回十六进制字符串
	 * 
	 * @param arr
	 * @return
	 */
	private static String hex(byte[] arr) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; ++i) {
			sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1,
					3));
		}
		return sb.toString();
	}

	/** 
     * 3DES加密 
     *  
     * @param src 
     * @return 
     * @throws Exception 
     */  
    public static String desedeEncoder(String src) {  
        byte[] b = null;
		try {
			SecretKey secretKey = new SecretKeySpec(build3DesKey(PASSWORD_CRYPT_KEY), ALGORITHM_DESEDE);  
			Cipher cipher = Cipher.getInstance(ALGORITHM_DESEDE);  
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);  
//			b = cipher.doFinal(src.getBytes("UTF-8"));
			b = cipher.doFinal(src.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
          
        return byte2HexStr(b);  
    }  
      
    /** 
     * 3DES解密 
     *  
     * @param dest 
     * @return 
     * @throws Exception 
     */  
    public static String desedeDecoder(String dest) {  
        String str = null;
		try {
			SecretKey secretKey = new SecretKeySpec(build3DesKey(PASSWORD_CRYPT_KEY), ALGORITHM_DESEDE);  
			Cipher cipher = Cipher.getInstance(ALGORITHM_DESEDE);  
			cipher.init(Cipher.DECRYPT_MODE, secretKey);  
			byte[] b = cipher.doFinal(str2ByteArray(dest));
			str = new String(b);
//			str = new String(b, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}  
        return str;  
      
    }  
    
    /** 
     * 字节数组转化为大写16进制字符串 
     *  
     * @param b 
     * @return 
     */  
    private static String byte2HexStr(byte[] b) {  
        StringBuilder sb = new StringBuilder();  
        for (int i = 0; i < b.length; i++) {  
            String s = Integer.toHexString(b[i] & 0xFF);  
            if (s.length() == 1) {  
                sb.append("0");  
            }  
              
            sb.append(s.toUpperCase());  
        }  
          
        return sb.toString();  
    }  
    
    /** 
     * 字符串转字节数组 
     *  
     * @param s 
     * @return 
     */  
    private static byte[] str2ByteArray(String s) {  
        int byteArrayLength = s.length()/2;  
        byte[] b = new byte[byteArrayLength];  
        for (int i = 0; i < byteArrayLength; i++) {  
            byte b0 = (byte) Integer.valueOf(s.substring(i*2, i*2+2), 16).intValue();  
            b[i] = b0;  
        }  
          
        return b;  
    }  
      
    /** 
     * 构造3DES加解密方法key 
     *  
     * @param keyStr 
     * @return 
     * @throws Exception 
     */  
    private static byte[] build3DesKey(String keyStr) throws Exception {  
        byte[] key = new byte[24];  
        for(int i = 0; i < key.length; i++){
        	key[i] = '0';
        }
        byte[] temp = keyStr.getBytes();  
//        byte[] temp = keyStr.getBytes("UTF-8");  
        if (key.length > temp.length) {  
            System.arraycopy(temp, 0, key, 0, temp.length);  
        } else {  
            System.arraycopy(temp, 0, key, 0, key.length);  
        }  
        return key;  
    } 
	
}
