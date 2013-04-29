package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import entity.User;

public class UserDao extends Dao<User>{
	
	protected User mapToObject() throws SQLException, DaoException{
		User user = new User();
		ResultSet rs = getResultSet();

		user.setId(rs.getInt("id"));
		user.setUsername(rs.getString("username"));
		user.setPassword(rs.getString("password"));

		return user;
	}
	
	public User findUserById(int id) {
		String sql = "select id, username, password from user where id = ?";
		

		try {
			this.setSql(sql, id);
			List<User> userList = this.query();
			if(userList.size() == 0){
				//userList长度为0，说明未找到用户
				return null;
			}else if(userList.size() == 1){
				return userList.get(0);
			}else{
				
			}
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return null;
		
	}
}
