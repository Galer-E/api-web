package com.galere.pictures.services.impl;

import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;
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
		
		List<User> users = this.users.getRepository().findAll();
		
		for (User u : users) {
			try {
				
				if (u.getLogin().contains("ilias")) {
					u.setPass(encrypt("ilias59"));
				} else {
					u.setPass(encrypt("admin"));
				}
				this.users.getRepository().save(u);
				
			} catch (Exception e) {e.printStackTrace();}
		}
		
	}
	
}
