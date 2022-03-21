package com.zcy.blog.service;

import com.zcy.blog.dao.CommentRepository;
import com.zcy.blog.pojo.Comment;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {

        Sort sort=Sort.by(Sort.Direction.DESC,"createTime");
        List<Comment> comments=commentRepository.findByBlogIdAndParentCommentNull(blogId,sort);
        if(Objects.nonNull(comments))
        {
            return eachComment(comments);
        }
        return new ArrayList<>();
    }

    @Override
    public Comment saveComment(Comment comment) {
          Long parentCommentId=comment.getParentComment().getId();
          if(parentCommentId!=-1)
          {
              comment.setParentComment(commentRepository.getOne(parentCommentId));
          }
          else
          {
              comment.setParentComment(null);
          }
          comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }


    private List<Comment> eachComment(List<Comment> comments)
    {
        /*拿到第一级别的数据*/
        List<Comment> commentList=new ArrayList<>();
        for (Comment comment:comments) {
            Comment c=new Comment();
            BeanUtils.copyProperties(comment,c);
            commentList.add(c);
        }
        combineChildren(commentList);
        return commentList;

    }

    private void combineChildren(List<Comment> comments)
    {
        for (Comment comment:comments) {
            List<Comment> replays=comment.getReplyComments();
            for(Comment replay:replays)
            {
                recursively(replay);
            }
            comment.setReplyComments(tempReplys);
            tempReplys=new ArrayList<>();

        }

    }

    private List<Comment> tempReplys=new ArrayList<>();

    private void recursively(Comment comment)
    {
        tempReplys.add(comment);
        if(comment.getReplyComments().size()>0)
        {
            List<Comment> replys=comment.getReplyComments();
            for (Comment reply: replys) {
                tempReplys.add(reply);
                if(reply.getReplyComments().size()>0)
                {
                    recursively(reply);
                }
            }

        }

    }



}
