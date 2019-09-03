package me.anichakra.poc.simple.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/node")
@Validated
public class NodeController {
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @ResponseBody
    public String getNodeId() {
        return System.getProperty("node.id");
    }

}
