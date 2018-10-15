package catfish.booksore.user.service;




import catfish.booksore.user.dao.userDao;
import catfish.booksore.user.domain.User;


public class UserService {
	private userDao userdao=new userDao();
	public void regist(User form)throws UserException{
		User user =userDao.findByUsername(form.getUsername());
		if(user != null) throw new UserException("这个老刁抢先注册用户名啦");
		user =userDao.findByEmail(form.getEmial());
		if(user != null) throw new UserException("这个老刁抢先注册email啦");
	}
}
