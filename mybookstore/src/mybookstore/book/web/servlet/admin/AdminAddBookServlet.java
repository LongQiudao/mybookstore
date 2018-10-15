package mybookstore.book.web.servlet.admin;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;



import cn.itcast.commons.CommonUtils;

import mybookstore.book.domain.Book;
import mybookstore.book.service.BookService;
import mybookstore.category.domain.Category;
import mybookstore.category.service.CategoryService;

public class AdminAddBookServlet extends HttpServlet {
	private BookService bookService = new BookService();
	private CategoryService categoryService = new CategoryService();

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1，把表单数据封装到book对象中
		 */
		DiskFileItemFactory factory = new DiskFileItemFactory(40 * 1024,
				new File("G:/temp"));
		ServletFileUpload sfu = new ServletFileUpload(factory);
		sfu.setFileSizeMax(40 * 1024);
		try {
			List<FileItem> fileItemList = sfu.parseRequest(req);
			/*
			 * 把 fileItem中的数据封装到book对象中 把普通表单项封装到map中 在把map中的数据封装到 book中
			 */
			Map<String, String> map = new HashMap<String, String>();
			for (FileItem fileItem : fileItemList) {
				if (fileItemList.get(0).isFormField()) {
					map.put(fileItem.getFieldName(),
							fileItem.getString("UTF-8"));
				}
			}
			Book book = CommonUtils.toBean(map, Book.class);
			book.setBid(CommonUtils.uuid());
			Category category = CommonUtils.toBean(map, Category.class);
			book.setCategory(category);
			/*
			 * 2.保存上传的文件 保存的目录 保存的文件名称
			 */
			String savePath = this.getServletContext().getRealPath("book_img");
			String fileName = CommonUtils.uuid() + "_"
					+ fileItemList.get(1).getName();
			/*
			 * 校验文件的扩展名
			 */
			if (!fileName.toLowerCase().endsWith(".jpg")){
				req.setAttribute("msg", "你上传的图片格式不正确");
				req.setAttribute("categoryList",categoryService.findAll());
				req.getRequestDispatcher("/adminjsps/admin/book/add.jsp")
						.forward(req, resp);
				
			}
			File destFile = new File(savePath, fileName);
			fileItemList.get(1).write(destFile);// 保存上传文件到目标文件位置
			/*
			 * 3把图片的路径设置成为book的image
			 */
			book.setImage("book_img/" + fileName);
		
				/*
				 * 4使用bookService完成保存
				 */
				bookService.add(book);
				/*
				 * 校验图片的尺寸
				 * */
				
				Image image = new ImageIcon(destFile.getAbsolutePath()).getImage();
				if(image.getWidth(null)>200||image.getHeight(null)>200){
					destFile.delete();
					req.setAttribute("msg", "你上传的文件尺寸超过了200x200");
					destFile.delete();
					req.setAttribute("categoryList",categoryService.findAll());
					req.getRequestDispatcher("/adminjsps/admin/book/add.jsp")
							.forward(req, resp);
					
				}
			
			/*
			 * 5返回
			 */
			req.getRequestDispatcher("/admin/AdminBookServlet?method=findAll")
					.forward(req, resp);
		} catch (Exception e) {
			if (e instanceof FileUploadBase.FileSizeLimitExceededException) {
				req.setAttribute("msg", "你上传的文件超出了40kb");
				req.setAttribute("categoryList",categoryService.findAll());
				req.getRequestDispatcher("/adminjsps/admin/book/add.jsp")
						.forward(req, resp);
			}
		}

	}

}
