package com.galere.pictures.services;

import java.util.List;

import com.galere.pictures.entities.User;
import com.galere.pictures.repositories.UserRepository;

public interface IUserService {
	
	public UserRepository getRepository();
	
	public boolean existsUser(String login);
	public List<User> searchByTags(String tags);
	
}
