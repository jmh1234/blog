package com.github.hcsp.controller;

import com.github.hcsp.aspect.CacheAspect;
import com.github.hcsp.service.TestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;

@RestController
public class TestController {
    @Resource
    private TestService testService;

    @CacheAspect(cacheSecond = 2)
    @RequestMapping("/index")
    public ModelAndView index() {
        HashMap<String, Object> model = new HashMap<>();
        model.put("items", testService.getOrderRank());
        return new ModelAndView("index", model);
    }
}
