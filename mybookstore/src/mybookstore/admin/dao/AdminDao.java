package mybookstore.admin.dao;

import java.sql.SQLException;

import mybookstore.admin.domain.Admin;
import mybookstore.user.domain.User;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import cn.itcast.jdbc.TxQueryRunner;

public class AdminDao {
	private QueryRunner qr = new TxQueryRunner();

	public  Admin findByAdminname(String adminname) {
		try {
			String sql = "select * from admins where adminname=?";
			return qr.query(sql, new BeanHandler<Admin>(Admin.class), adminname);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}


	
}
