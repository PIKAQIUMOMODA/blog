package com.zcy.blog.controller;

import com.zcy.blog.pojo.Tag;
import com.zcy.blog.pojo.Type;
import com.zcy.blog.service.BlogService;
import com.zcy.blog.service.TagService;
import com.zcy.blog.service.TypeService;
import com.zcy.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TagShowController {

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/tags/{id}")
    public String types(@PageableDefault(size =1,sort = {"updateTime"},direction = Sort.Direction.DESC)
                                 Pageable pageable,
                                 Model model,
                             @PathVariable Long id)
    {
        List<Tag> types= tagService.listTagsTop(200);
        if(id==-1)
        {
            id=types.get(0).getId();
        }
        model.addAttribute("tags",types);
        model.addAttribute("page",blogService.listBlog(id,pageable));
        model.addAttribute("activeTagId",id);
        return "tags";

    }
}
