package com.zcy.blog.controller;

import com.zcy.blog.pojo.Comment;
import com.zcy.blog.pojo.User;
import com.zcy.blog.service.BlogService;
import com.zcy.blog.service.CommentService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    /*用来获取指定博客下的评论*/
    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model)
    {
        model.addAttribute("comments",commentService.listCommentByBlogId(blogId));
        return "blog::commentList";
    }

    @PostMapping("/comments")
    public String post(Comment comment, HttpSession httpSession)
    {
        Long blogId=comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogId));
        User user=(User) httpSession.getAttribute("userz");
        if(user!=null)
        {
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        }
        if(Strings.isBlank(comment.getAvatar())) {
            comment.setAvatar(avatar);
        }
        commentService.saveComment(comment);
        return "redirect:/comments/"+blogId;
    }





    @GetMapping("/givelike/{blogId}")
    public String updateGivelike(@PathVariable Long blogId,Model model)
    {
        blogService.updateGivelike(blogId);
      model.addAttribute("blog", blogService.getBlog(blogId));
       return  "blog::appreciation";

    }

    @GetMapping("/dislike/{blogId}")
    public String updateDislike(@PathVariable Long blogId,Model model)
    {
        blogService.updateDislike(blogId);
        model.addAttribute("blog", blogService.getBlog(blogId));
        return "blog::appreciation";
    }

}
