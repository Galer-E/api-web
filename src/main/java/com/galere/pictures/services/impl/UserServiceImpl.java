package com.galere.pictures.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.galere.pictures.repositories.UserRepository;
import com.galere.pictures.services.IUserService;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserRepository repository;
	
	@Override
	public UserRepository getRepository() {
		return repository;
	}
	
}
