package com.zcy.blog.controller.admin;

import com.zcy.blog.pojo.Type;
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
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/admin")
public class TypeController {

    private  final Logger logger= LoggerFactory.getLogger(TypeController.class);

    @Autowired
    TypeService typeService;

    @GetMapping("/types")
    public String listType(@PageableDefault(size =5,sort = {"id"},direction = Sort.Direction.ASC) Pageable pageable, Model model)
    {
          model.addAttribute("page",typeService.listType(pageable));
          return "admin/types";
    }

   /* @PostMapping("/types")
    public String listType1(@PageableDefault(size =5,sort = {"id"},direction = Sort.Direction.ASC) Pageable pageable, Model model)
    {
      model.addAttribute("page",typeService.listType(pageable));
       System.out.println("post方法被执行了");
        return "admin/types";
    }*/


    @PostMapping("/types/add")
    public String add(Type type,@PageableDefault(size =5,sort = {"id"},direction = Sort.Direction.ASC) Pageable pageable, Model model)
    {
       try{
           if(typeService.saveType(type)!=null)
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

        model.addAttribute("page",typeService.listType(pageable));
        return "admin/types::typesListTable";

    }


    @GetMapping("/types/delete/{id}")
    public  String delete(@PathVariable Long id,Model model,@PageableDefault(size =5,sort = {"id"},direction = Sort.Direction.ASC) Pageable pageable)
    {
        try
        {
            typeService.deleteType(id);
            model.addAttribute("message","删除成功！");
        }
        catch (Exception e)
        {
            model.addAttribute("message","删除失败，可能该类型下有文章存在！");
        }
        model.addAttribute("page",typeService.listType(pageable));

        return "admin/types";
    }

    @PostMapping("/types/edit")
    public String edit(Type type,@PageableDefault(size =5,sort = {"id"},direction = Sort.Direction.ASC) Pageable pageable, Model model)
    {
        try{
            typeService.updateType(type.getId(),type);
            model.addAttribute("message","修改成功！");
        }
        catch (Exception e)
        {
            model.addAttribute("message","修改失败！");
        }

        model.addAttribute("page",typeService.listType(pageable));
        return "admin/types::typesListTable";
    }

    @PostMapping("/types/search")
    public String search(Type type,@PageableDefault(size =5,sort = {"id"},direction = Sort.Direction.ASC) Pageable pageable, Model model)
    {
        Page<Type> page= typeService.listTypes(pageable,type);
        System.out.println(page.getContent().get(0));
        model.addAttribute("page",page);
        return "admin/types::typesListTable";

    }



}
