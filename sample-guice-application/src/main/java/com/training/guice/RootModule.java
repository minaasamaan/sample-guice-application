package com.training.guice;

import org.apache.bval.guice.ValidationModule;

import com.google.inject.AbstractModule;
import com.training.guice.jpa.JpaModule;
import com.training.guice.rest.RestServiceModule;
import com.training.guice.validation.RulesValidationModule;

public class RootModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new JpaModule());
		install(new ValidationModule());
		install(new RestServiceModule());
		install(new RulesValidationModule());
		bind(TestData.class).asEagerSingleton();
	}
}
