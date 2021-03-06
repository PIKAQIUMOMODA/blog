package com.zcy.blog.controller.admin;

import com.zcy.blog.enumerate.Result;
import com.zcy.blog.pojo.Blog;
import com.zcy.blog.pojo.User;
import com.zcy.blog.service.BlogService;
import com.zcy.blog.service.TagService;
import com.zcy.blog.service.TypeService;
import com.zcy.blog.vo.BlogQuery;
import org.hibernate.annotations.GeneratorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping(value = {"/blogs"})
    public String blogs(@PageableDefault(size=2,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable, Model model, BlogQuery blog)
    {
        model.addAttribute("types",typeService.listTypes());
        model.addAttribute("page",blogService.listBlog(pageable,blog));
        return "admin/blogs";
    }


    @GetMapping(value = {"/blogs-input"})
    public String release()
    {
        return "admin/blogs-input";
    }


    @PostMapping(value = {"/blogs/search"})
    public String search(@PageableDefault(size=2,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable, Model model, BlogQuery blog)
    {
        model.addAttribute("page",blogService.listBlog(pageable,blog));
        return "admin/blogs :: blogList";
    }


    /*新增博客*/
    @GetMapping("/blogs/input")
    public String input(Model model)
    {
        setTypeAndTag(model);
        model.addAttribute("blog",new Blog());
        return "admin/blogs-input";

    }

    /*修改博客*/
    @GetMapping("/blogs/edit/{id}")
    public String edit(@PathVariable("id") Long id,Model model)
    {
        setTypeAndTag(model);
        Blog blog=blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        return "admin/blogs-input";

    }

    private void setTypeAndTag(Model model)
    {
        model.addAttribute("types",typeService.listTypes());
        model.addAttribute("tags",tagService.listTags());
    }


    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session)
    {
        blog.setUser((User)session.getAttribute("userz"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTags(blog.getTagIds()));
        Blog b=null;
        if(blog.getId()==null)
        {
            b=blogService.saveBlog(blog);
        }
        else
        {
            b=blogService.updateBlog(blog.getId(),blog);
        }
        if(b==null)
        {
            attributes.addFlashAttribute("messsage","操作失败");
        }
        else {
            attributes.addFlashAttribute("message","操作成功");
        }
        return "redirect:/admin/blogs";
    }

    @PostMapping("/blogs/updateRecommend")
    @ResponseBody
    public String updateRecommend(@RequestBody Blog blog)
    {
           blogService.updateRecommend(blog);
        return Result.SUCCESS.toString();
    }

    @GetMapping("/blogs/del/{id}")
    public String delete(@PathVariable Long id,RedirectAttributes attributes)
    {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/blogs";

    }

}
