package com.galere.pictures.services.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.galere.pictures.entities.User;
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
	
	@Override
	public boolean existsUser(String login) {
		return getRepository().findAll().stream().filter(user -> user.getLogin().equalsIgnoreCase(login)).count() > 0;
	}
	
	@Override
	public List<User> searchByTags(String tags) {
		List<String> tagList = Arrays.asList(tags.split(" "));
		
		return getRepository().findAll().stream().filter(
					u -> tagList.stream().filter(
									tag -> u.getLogin().contains(tag) ||
										   u.getHeightRole().getLabel().contains(tag) ||
										   u.getEntry().toString().equalsIgnoreCase(tag) || // Date equals
										   u.getId().toString().contains(tag)
						).count() > 0
				).collect(Collectors.toList());
	}
	
}
