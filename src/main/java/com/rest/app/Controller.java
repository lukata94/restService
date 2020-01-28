package com.rest.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.rest.app.model.IResponse;
import com.rest.app.service.ResponseService;

@RestController
public class Controller {
	private static final Logger LOG = LoggerFactory.getLogger(Controller.class);

	@Autowired
	private ResponseService responseService;

	@GetMapping("/users/{user}")
	public IResponse greeting(@PathVariable final String user) {
		LOG.info("New request for user: " + user);
		return responseService.processRequest(user);
	}
}
