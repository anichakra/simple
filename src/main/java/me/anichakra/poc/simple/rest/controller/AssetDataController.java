package me.anichakra.poc.simple.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import me.anichakra.poc.simple.rest.domain.AssetData;
import me.anichakra.poc.simple.rest.service.DataService;

@RestController
@RequestMapping("/asset")

@Validated
public class AssetDataController {

    
    
    @Autowired
    private DataService dataService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{account}")
    @ResponseBody
    public List<AssetData> getAsset(@PathVariable("account") String account) {
        return dataService.getAsset(account);
    }

}
