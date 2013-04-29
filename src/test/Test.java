package test;

import dao.UserDao;
import entity.User;

public class Test {
	public static void main(String[] args){
		int id = 1;
		
		UserDao userDao = new UserDao();
		User user = userDao.findUserById(id);
		if(user != null){
			System.out.println(user.getId());
			System.out.println(user.getUsername());
			System.out.println(user.getPassword());
		}
	}
}
