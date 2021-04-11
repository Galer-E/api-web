package com.galere.pictures.services;

public interface IEncryptionService {
	
	public String decrypt(String text) throws Exception;
	public String encrypt(String text) throws Exception;
	
	public void reloadKey();
	
}
