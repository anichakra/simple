package me.anichakra.poc.simple.rest.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
    @GetMapping("/info")
    @ResponseBody
    public String getNodeId(HttpServletRequest request) {
        return System.getProperty("node.id") + "-" + getAddress(request);
    }

    private String getAddress(HttpServletRequest request) {
        return request.getLocalAddr();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/headers")
    @ResponseBody
    public Map<String, String> getHeaders(HttpServletRequest request) {
        Iterator <String>itr = request.getHeaderNames().asIterator();
        Map<String, String> headers = new HashMap<>();
        while (itr.hasNext()) {
            String headerName = itr.next();
           headers.put(headerName, request.getHeader(headerName));
        }
        return headers;
    }
}
