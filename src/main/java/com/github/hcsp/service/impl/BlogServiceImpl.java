package com.github.hcsp.service.impl;

import com.github.hcsp.dao.BlogDao;
import com.github.hcsp.entity.Blog;
import com.github.hcsp.entity.BlogResult;
import com.github.hcsp.service.BlogService;
import com.github.hcsp.utils.Pagination;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class BlogServiceImpl implements BlogService {

    @Resource
    private BlogDao blogDao;

    @Resource
    private AuthService authService;

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
    public Pagination<Blog> getBlogListByUserId(Map<String, Integer> pageNumAndPageSize, Integer userId) {
        int pageNum = pageNumAndPageSize.get("pageNum");
        int pageSize = pageNumAndPageSize.get("pageSize");
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<Blog> blogList = blogDao.getBlogListByUserId(userId);
            int total = (int) ((Page) blogList).getTotal();
            Integer totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
            return Pagination.pageOf(blogList, pageSize, pageNum, totalPage, true);
        } catch (Exception e) {
            return Pagination.pageOf(null, pageSize, pageNum, 0, false);
        }
    }

    @Override
    public BlogResult updateBlogById(Blog blog, int blogId) {
        Blog blog1 = blogDao.getBlogInfoById(blogId);
        if (blog1 == null) {
            return BlogResult.failure("博客不存在");
        } else {
            if (!blog1.getUser_id().equals(authService.getCurrentUser().get().getId())) {
                return BlogResult.failure("无法修改别人的博客");
            } else {
                blog.setId(blog1.getId());
                blogDao.updateBlogById(blog);
                return BlogResult.success("获取成功", blogDao.getBlogInfoById(blogId));
            }
        }
    }

    @Override
    public BlogResult deleteBlogById(int blogId) {
        Blog blog = blogDao.getBlogInfoById(blogId);
        if (blog == null) {
            return BlogResult.failure("博客不存在");
        } else {
            if (!blog.getUser_id().equals(authService.getCurrentUser().get().getId())) {
                return BlogResult.failure("无法修改别人的博客");
            } else {
                blogDao.deleteBlogById(blogId);
                return BlogResult.success("删除成功");
            }
        }
    }
}
