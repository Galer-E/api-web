package com.galere.pictures.services.impl;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.galere.pictures.entities.User;
import com.galere.pictures.services.IEncryptionService;
import com.galere.pictures.services.IUserService;

@Service
public class EncryptionServiceImpl implements IEncryptionService {

	@Value( "${encryption.key}" )
	private String key;
	
	private String previousKey = null;
	private String initialKey = null;
	
	@Autowired
	private IUserService users;

	@Override
	public String decrypt(String encrypted) throws Exception {
		String decrypted="";
		
		try {
			
			Cipher c = Cipher.getInstance("AES");

	        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

	        c.init(Cipher.DECRYPT_MODE, secretKeySpec);
	        decrypted = new String(c.doFinal(Base64.getDecoder().decode(encrypted)));
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return decrypted;
	}

	@Override
	public String encrypt(String text) throws Exception {
		String encrypted="";
		
		try {
			Cipher c = Cipher.getInstance("AES");

	        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
			
	        c.init(Cipher.ENCRYPT_MODE, secretKeySpec);
	        byte[] encBytes = c.doFinal(text.getBytes("UTF-8"));
	        encrypted = Base64.getEncoder().encodeToString(encBytes);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return encrypted;
	}

	@Override
	public void reloadKey() {
		
		if (initialKey == null)
			initialKey = key;
		previousKey = key;
		
		try {
			key = generateNewKey();
			
			for (User u : users.getRepository().findAll()) {
				u.setPass(encrypt(decrypt(u.getPass(), previousKey)));
				users.getRepository().save(u);
			}
		} catch (Exception e) { e.printStackTrace(); }
	
	}
	
	@Override
	public void loadInitialKey() {
		
		if (initialKey != null) {
			previousKey = key;
			key = initialKey;
			
			try {
				for (User u : users.getRepository().findAll()) {
					u.setPass(encrypt(decrypt(u.getPass(), previousKey)));
					users.getRepository().save(u);
				}
			} catch (Exception e) { e.printStackTrace(); }
		} 
		
	}
	
	@Override
	public void test() {
		try {
			
			for (User u : users.getRepository().findAll()) {
				if (u.getLogin().contains("ilias"))
					u.setPass(encrypt("ilias59"));
				else
					u.setPass(encrypt("admin"));
				
				users.getRepository().save(u);
			}
			
		} catch (Exception e) {e.printStackTrace();}
	}
		
	private String generateNewKey() throws NoSuchAlgorithmException {
		String newS;
		int size, count = 0;
		
		do {
			SecureRandom r = new SecureRandom();
			byte[] salt = new byte[16];
			r.nextBytes(salt);
			
		    // Use the largest AES key length which is supported by the OS
		    final KeyGenerator generator = KeyGenerator.getInstance("AES");
	        generator.init(128, r);
			
	        newS = new String(salt);
	        size = newS.getBytes().length;
	        
	        count++;
		} while (size != 16 && size != 24 && size != 32);
        
		System.out.println(count);
		
	    return newS;
	}

	private String decrypt(String encrypted, String key) throws Exception {
		String decrypted="";
		
		try {
			
			Cipher c = Cipher.getInstance("AES");

	        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

	        c.init(Cipher.DECRYPT_MODE, secretKeySpec);
	        decrypted = new String(c.doFinal(Base64.getDecoder().decode(encrypted)));
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return decrypted;
	}
	
}
