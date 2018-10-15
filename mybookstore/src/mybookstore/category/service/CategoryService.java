package mybookstore.category.service;

import java.util.List;

import mybookstore.book.dao.BookDao;
import mybookstore.category.dao.CategoryDao;
import mybookstore.category.domain.Category;
import mybookstore.category.web.servlet.admin.CategoryException;

public class CategoryService {
	
	private CategoryDao categoryDao = new CategoryDao();
	private BookDao bookDao = new BookDao();
	/**
	 * C查询所有分类
	 * 
	 * **/
	public List<Category> findAll() {
		return categoryDao.findAll();

	}
	/**
	 * 添加分类
	 * */
	public void add(Category category) {
		categoryDao.add(category);
		
	}
	/**
	 * 删除分类
	 * @throws CategoryException 
	 * */
	public void delete(String cid) throws CategoryException {
		int count = bookDao.getCountByCid(cid);
		if(count>0)throw new CategoryException("该分类下还有图书因此不能删除");
		categoryDao.delete(cid);
	}
	/**
	 * 加载分类
	 * */
	public Category load(String cid) {
		return categoryDao.load(cid);
	}
	/*
	 * 修改分类
	 * */
	public void edit(Category category) {
		 categoryDao.edit(category);
		
	}
}
