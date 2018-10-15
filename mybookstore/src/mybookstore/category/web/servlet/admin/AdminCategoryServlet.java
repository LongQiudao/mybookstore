package mybookstore.category.web.servlet.admin;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

import mybookstore.admin.service.AdminException;
import mybookstore.category.domain.Category;
import mybookstore.category.service.CategoryService;

public class AdminCategoryServlet extends BaseServlet {
	private CategoryService categoryService = new CategoryService();
	/**
	 * 修改分类
	 * */
	public String edit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, AdminException, CategoryException {
		/*
		 * 封装表单数据
		 * 调用service方法完成修改工作
		 * 调用findAll方法
		 * */
		Category category = CommonUtils.toBean(req.getParameterMap(), Category.class);
		categoryService.edit(category);
		return findAll(req,resp);
	
	}
	/**
	 * 修改之前的加载工作
	 * */
	public String editPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, AdminException, CategoryException {
		String cid = req.getParameter("cid");
		req.setAttribute("category", categoryService.load(cid));
		return "f:/adminjsps/admin/category/mod.jsp";
	}
	/**
	 * 删除分类
	 * */
	public String delete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, AdminException, CategoryException {
		/*
		 * 获取参数cid
		 * 调用Service方法传递cid
		 * 			如果抛出异常，保存异常信息转发到msg.jsp
		 * 调用findall方法			
		 * */
		String cid = req.getParameter("cid");
		try{
		categoryService.delete(cid);
		return findAll(req,resp);
		}catch(CategoryException e){
			req.setAttribute("msg", e.getMessage());
			return "f:/adminjsps/msg.jsp";
		}
		
		
	}
	/**
	 * 查询所有分类
	 * */
	public String findAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("categoryList", categoryService.findAll());
		return "f:/adminjsps/admin/category/list.jsp";
	}
	/**
	 * 添加分类
	 * */
	public String add(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.封装表单数据
		 * 2.补全cid
		 * 3.调用service方法完成添加工作
		 * 4.调用findAll方法
		 * */
		Category category = CommonUtils.toBean(req.getParameterMap(), Category.class);
		category.setCid(CommonUtils.uuid());
		categoryService.add(category);
		return findAll(req,resp);
	}
	
}	
