package catfish.booksore.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import cn.itcast.jdbc.TxQueryRunner;

import catfish.booksore.user.domain.User;



public class userDao {
	private static QueryRunner qr = new TxQueryRunner();

	/*
	 * 
	 * 按用户名查询
	 */
	public static User findByUsername(String username) {
		try {
			String sql = "select * from db_user where username=?";
			return (User) qr.query(sql, username, new BeanHandler(User.class));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static User findByEmail(String email) {
		try {
			String sql = "select * from db_user where email=?";
			return (User) qr.query(sql, email, new BeanHandler(User.class));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static void add(User user) {
		try {
			String sql = "insert into db_user values(?,?,?,?,?,?,?,?)";
			Object[] params = { user.getUid(), user.getUsername(),
					user.getPassword(), user.isState(), user.getCode(),
					user.getEmial(), user.getDescribe() };
				qr.update(sql,params);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
