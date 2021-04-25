package com.galere.pictures.services;

import java.util.List;

import com.galere.pictures.entities.Role;
import com.galere.pictures.repositories.RoleRepository;

public interface IRoleService {
	
	public RoleRepository getRepository();
	
	public boolean existsRole(String login);
	public List<Role> searchByTags(String tags);
	
}
