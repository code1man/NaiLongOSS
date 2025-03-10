package org.csu.demo.web;

import org.csu.demo.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@Controller
@Validated
public class BusinessController {
    @Autowired
    private BusinessService businessService;
}
