package me.anichakra.poc.simple.rest.controller;

import java.net.UnknownHostException;
import java.util.Iterator;

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
    public String getNodeId(HttpServletRequest request) throws UnknownHostException {
        return System.getProperty("node.id") + ";local: " + getAddress(request) + ";headers: " + getRemote(request);
    }

    public String getAddress(HttpServletRequest request) throws UnknownHostException {
        return request.getLocalAddr();
    }

    public String getRemote(HttpServletRequest request) {
        Iterator <String>itr = request.getHeaderNames().asIterator();
        StringBuilder sb = new StringBuilder();
        while (itr.hasNext()) {
            String headerName = itr.next();
            sb.append( headerName).append("=").append(request.getHeader(headerName)).append("\n");
        }
      
        return sb.toString();
    }
}
