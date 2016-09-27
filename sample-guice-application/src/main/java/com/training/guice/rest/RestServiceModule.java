package com.training.guice.rest;

import org.modelmapper.ModelMapper;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.training.guice.manager.UserManager;
import com.training.guice.manager.impl.UserManagerImpl;

public class RestServiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ExerciseService.class).to(ExerciseServiceImpl.class);
		bind(ObjectMapperProvider.class);
		bind(RestExceptionMapper.class);
		bind(ModelMapper.class).in(Singleton.class);
		bind(UserManager.class).to(UserManagerImpl.class).in(Singleton.class);
	}
}
