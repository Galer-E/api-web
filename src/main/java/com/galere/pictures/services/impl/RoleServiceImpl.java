package com.galere.pictures.services.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.galere.pictures.entities.Role;
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
	
	@Override
	public boolean existsRole(String label) {
		return getRepository().findAll().stream().filter(role -> role.getLabel().equalsIgnoreCase(label)).count() > 0;
	}
	
	@Override
	public List<Role> searchByTags(String tags) {
		List<String> tagList = Arrays.asList(tags.split(" "));
		
		return getRepository().findAll().stream().filter(
					r -> tagList.stream().filter(
									tag -> r.getLabel().contains(tag) ||
										   r.getId().toString().contains(tag) ||
										   r.getUsersToString().contains(tag)
										   
						).count() > 0
				).collect(Collectors.toList());
	}
	
}
