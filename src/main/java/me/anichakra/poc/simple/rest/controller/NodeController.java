package me.anichakra.poc.simple.rest.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping("/node")
@Validated
public class NodeController {
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/node")
    @ResponseBody
    public String getNodeId() {
        return System.getProperty("node.id");
    }
    
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/localAddress")
    @ResponseBody
    public String getLocalAddress() {
        HttpServletRequest request = ((ServletRequestAttributes)    RequestContextHolder.getRequestAttributes()).getRequest();

       return request.getLocalAddr();
    }

    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"};


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/remoteAddress")
    @ResponseBody
    public String getRemoteAddress() {
        HttpServletRequest request = ((ServletRequestAttributes)    RequestContextHolder.getRequestAttributes()).getRequest();
        for (String header : IP_HEADER_CANDIDATES) {
            String ipList = request.getHeader(header);
            if (ipList != null && ipList.length() != 0 && !"unknown".equalsIgnoreCase(ipList)) {
                String ip = ipList.split(",")[0];
                return ip;
            }
        }

        return request.getRemoteAddr();
     }
}
