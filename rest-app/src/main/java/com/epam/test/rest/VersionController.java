package com.epam.test.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by vlad on 24.2.17.
 */
@RestController
public class VersionController {

    private static final String VERSION = "1.0";

    @RequestMapping(value = "/version",method = RequestMethod.GET)
    public String getVersion(){
        return VERSION;
    }

}
