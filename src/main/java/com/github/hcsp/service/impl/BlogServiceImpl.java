package com.github.hcsp.service.impl;

import com.github.hcsp.dao.BlogDao;
import com.github.hcsp.entity.Blog;
import com.github.hcsp.entity.BlogListResult;
import com.github.hcsp.entity.BlogResult;
import com.github.hcsp.entity.User;
import com.github.hcsp.service.BlogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BlogServiceImpl implements BlogService {

    private final BlogDao blogDao;

    @Inject
    public BlogServiceImpl(BlogDao blogDao) {
        this.blogDao = blogDao;
    }

    @Override
    public BlogResult addBlogInfo(Blog blog) {
        blogDao.addBlogInfo(blog);
        return BlogResult.success("博客新建成功", blogDao.getBlogInfoById(blog.getId()));
    }

    @Override
    public Blog getBlogInfoById(int blogId) {
        return blogDao.getBlogInfoById(blogId);
    }

    @Override
    public BlogListResult getBlogListByUserId(Map<String, Integer> pageNumAndPageSize, Blog blog) {
        int pageNum = pageNumAndPageSize.get("pageNum");
        int pageSize = pageNumAndPageSize.get("pageSize");
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<Blog> blogList = blogDao.getBlogListByUserId(blog);
            int total = (int) ((Page) blogList).getTotal();
            Integer totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
            return BlogListResult.success(blogList, total, pageNum, totalPage);
        } catch (NullPointerException e) {
            return BlogListResult.success(new ArrayList<>(), 0, pageNum, 0);
        } catch (Exception e) {
            e.printStackTrace();
            return BlogListResult.failure("系统异常");
        }
    }

    @Override
    public BlogResult updateBlogById(int blogId, Blog blog) {
        Blog oldBlogInfo = blogDao.getBlogInfoById(blogId);
        blog.setId(blogId);
        if (oldBlogInfo == null) {
            return BlogResult.failure("博客不存在");
        } else {
            if (!oldBlogInfo.getUserId().equals(blog.getUserId())) {
                return BlogResult.failure("无法修改别人的博客");
            } else {
                blog.setId(oldBlogInfo.getId());
                blogDao.updateBlogById(blog);
                return BlogResult.success("获取成功", blogDao.getBlogInfoById(blogId));
            }
        }
    }

    @Override
    public BlogResult deleteBlogById(int blogId, User user) {
        Blog blog = blogDao.getBlogInfoById(blogId);
        if (blog == null) {
            return BlogResult.failure("博客不存在");
        } else {
            if (!blog.getUserId().equals(user.getId())) {
                return BlogResult.failure("无法修改别人的博客");
            } else {
                blogDao.deleteBlogById(blogId);
                return BlogResult.success("删除成功");
            }
        }
    }
}
