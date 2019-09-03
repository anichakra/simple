package me.anichakra.poc.simple.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import me.anichakra.poc.simple.rest.domain.PropertyData;
import me.anichakra.poc.simple.rest.service.DataService;

@RestController
@RequestMapping("/property")
@Validated
public class PropertyDataController {

	@Autowired
	private DataService dataService;

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{account}")
	@ResponseBody
	public PropertyData getProperty(@PathVariable("account") String account) {
		return dataService.getProperty(account);
	}

}
