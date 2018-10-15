package mybookstore.admin.service;

import mybookstore.admin.dao.AdminDao;
import mybookstore.admin.domain.Admin;
import mybookstore.user.domain.User;
import mybookstore.user.service.UserException;

public class AdminService {
	private AdminDao adminDao = new AdminDao();

	public  Admin login(Admin form) throws AdminException {
		Admin admin = adminDao.findByAdminname(form.getAdminname());
		if (admin == null)
			throw new AdminException("该用户不存在");
		if (!admin.getPassword().equals(form.getPassword()))
			throw new AdminException("密码错误");
		
		return admin;

	}
}
