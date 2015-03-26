package com.mobitexoft.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

public class SHA256Hash {
	public static byte[] getHash(String value) {
		if (value != null) {
			try {
				MessageDigest digest = MessageDigest.getInstance("SHA-256");
				digest.reset();
				digest.update(value.getBytes("UTF-8")); 
		        return digest.digest();
		    } catch (NoSuchAlgorithmException e) {
		    } catch (UnsupportedEncodingException e) {
			}
		}
		return null;
	}
	
	public static byte[] getHash(String key, String value) { // HMAC
		if (!TextUtils.isEmpty(key) && value != null) {
			try {
			    SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
			    Mac mac = Mac.getInstance("HmacSHA256");
			    //mac.reset();
			    mac.init(secretKey);
			    mac.update(value.getBytes("UTF-8"));
			    return mac.doFinal();
			} catch (UnsupportedEncodingException e) {
			} catch (InvalidKeyException e) {
			} catch (NoSuchAlgorithmException e) {
			}
		}	    
	    return null;
	}
	
	public static String toHexString(byte[] bytes) {
		if(bytes != null) {
			StringBuffer stringBuffer = new StringBuffer();
			for (byte value : bytes) {
				String hex = Integer.toHexString(0xFF & value);
				if (hex.length() == 1) {
					stringBuffer.append("0");
				}
				stringBuffer.append(hex);
			}
			return stringBuffer.toString()/*.toUpperCase(Locale.ENGLISH)*/;
		} else {
			return null;
		}
	}
	
	public static String toBase64String(byte[] bytes) {
		if(bytes != null) {
			return Base64.encodeToString(bytes, Base64.DEFAULT | Base64.NO_WRAP);		
		} else {
			return null;
		}
	}
}
