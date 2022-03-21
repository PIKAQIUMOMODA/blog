package com.zcy.blog.controller.admin;

import com.zcy.blog.pojo.Tag;
import com.zcy.blog.pojo.Type;
import com.zcy.blog.service.TagService;
import com.zcy.blog.service.TypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.awt.*;

@Controller
@RequestMapping("/admin")
public class LabelController {


    private  final Logger logger= LoggerFactory.getLogger(LabelController.class);

    @Autowired
    TagService tagService;

    @GetMapping("/tags")
    public String listTag(@PageableDefault(size =5,sort = {"id"},direction = Sort.Direction.ASC) Pageable pageable, Model model)
    {
        model.addAttribute("page",tagService.listTag(pageable));
        return "admin/tags";
    }


    @PostMapping("/tags/add")
    public String add(Tag tag, @PageableDefault(size =5,sort = {"id"},direction = Sort.Direction.ASC) Pageable pageable, Model model)
    {
        try{
            if(tagService.saveTag(tag)!=null)
            {

                model.addAttribute("message","添加成功！");
            }
            else
            {
                model.addAttribute("message","添加失败");
            }
        }
        catch (Exception e)
        {
            model.addAttribute("message","添加失败");
        }

        model.addAttribute("page",tagService.listTag(pageable));
        return "admin/tags::tagsListTable";

    }


    @GetMapping("/tags/delete/{id}")
    public  String delete(@PathVariable Long id, Model model, @PageableDefault(size =5,sort = {"id"},direction = Sort.Direction.ASC) Pageable pageable)
    {
        try
        {
            tagService.deleteTag(id);
            model.addAttribute("message","删除成功！");
        }
        catch (Exception e)
        {
            model.addAttribute("message","删除失败，可能该类型下有文章存在！");
        }
        model.addAttribute("page",tagService.listTag(pageable));

        return "admin/tags";
    }

    @PostMapping("/tags/edit")
    public String edit(Tag tag,@PageableDefault(size =5,sort = {"id"},direction = Sort.Direction.ASC) Pageable pageable, Model model)
    {
        try{
            tagService.updateTag(tag.getId(),tag);
            model.addAttribute("message","修改成功！");
        }
        catch (Exception e)
        {
            model.addAttribute("message","修改失败！");
        }

        model.addAttribute("page",tagService.listTag(pageable));
        return "admin/tags::tagsListTable";
    }

    @PostMapping("/tags/search")
    public String search(Tag tag,@PageableDefault(size =5,sort = {"id"},direction = Sort.Direction.ASC) Pageable pageable, Model model)
    {
        Page<Tag> page= tagService.listTags(pageable,tag);
        System.out.println(page.getContent().get(0));
        model.addAttribute("page",page);
        return "admin/tags::tagsListTable";

    }

}
