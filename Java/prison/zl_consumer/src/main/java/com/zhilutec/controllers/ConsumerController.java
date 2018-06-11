package com.zhilutec.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/sub")
public class ConsumerController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

}
