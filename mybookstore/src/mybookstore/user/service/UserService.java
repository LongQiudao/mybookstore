package mybookstore.user.service;

import mybookstore.user.dao.UserDao;
import mybookstore.user.domain.User;

public class UserService {
	private UserDao userdao = new UserDao();
	/**
	 * 
	 * **/
	
	/**
	 * 登录功能
	 * @throws UserException 
	 * 
	 * 
	 * **/
	public User login(User form) throws UserException{
		User user = userdao.findByUsername(form.getUsername());
		if(user==null ) throw new UserException("该用户不存在");
		if(!user.getPassword().equals(form.getPassword()))throw new UserException("密码错误");
		if(!user.isState())throw new UserException("该用户还未激活");
		return user;
		
		
	}
	/**
	 * 激活功能
	 * @throws UserException 
	 * **/
	public void active(String code) throws UserException{
		
		User user = userdao.findByCode(code);
		if(user==null){
			throw new UserException("激活码无效");
		}
		if(user.isState()){
			throw new UserException("该用户已被激活");
		}
		userdao.updateState(user.getUid(),true);
	}
	/**
	 * 注册功能
	 * **/
	
	public void regist(User form)throws UserException{
		//校验用户名
		User user =userdao.findByUsername(form.getUsername());
		if(user != null) throw new UserException("该用户名已被注册请重新输入");
		//校验邮箱
		user =userdao.findByEmail(form.getEmail());
		if(user != null) throw new UserException("该邮箱已被注册请重新输入");
		//插入用户到数据库中
		userdao.add(form);
	}
}
