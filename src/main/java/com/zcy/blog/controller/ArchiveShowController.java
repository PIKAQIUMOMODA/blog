package com.zcy.blog.controller;

import com.zcy.blog.pojo.Blog;
import com.zcy.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ArchiveShowController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/archives")
    public String archives(Model model)
    {
        List<String> yearsList=blogService.findGroupYear();
        Map<String,List<String>> monthsList=new HashMap<>();
        Map<String,List<Blog>> blogsList=new HashMap<>();
        for (String year:yearsList) {
            List<String> months=blogService.findGroupYearMonth(year);
            monthsList.put(year,months);
            for (String month:months) {
                List<Blog> blogs=blogService.findByMonth(month);
                blogsList.put(month,blogs);
            }
        }


        model.addAttribute("years",yearsList);
        model.addAttribute("months",monthsList);
        model.addAttribute("blogs",blogsList);
        return "archives1";
    }
}
