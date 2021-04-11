package com.galere.pictures.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.galere.pictures.repositories.RoleRepository;
import com.galere.pictures.services.IRoleService;

@Service
public class RoleServiceImpl implements IRoleService {

	@Autowired
	private RoleRepository repository;
	
	@Override
	public RoleRepository getRepository() {
		return repository;
	}
	
}
