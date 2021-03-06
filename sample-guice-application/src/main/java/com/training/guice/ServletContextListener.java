package com.training.guice;

import java.util.Arrays;
import java.util.List;

import com.google.inject.Module;
import com.squarespace.jersey2.guice.JerseyGuiceServletContextListener;

public class ServletContextListener extends JerseyGuiceServletContextListener {

	@Override
	protected List<? extends Module> modules() {
		return Arrays.asList(new RootModule());
	}
}
