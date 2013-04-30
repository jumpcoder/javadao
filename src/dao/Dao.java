package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;


public abstract class Dao<T> {
	private static BasicDataSource ds = null;
	
	static{
		Properties pro = new Properties();
		try {
			//getResourceAsStream中相对路径的默认前缀是类的根目录，也就是包含所有了所有包的那个文件夹
			pro.load(Dao.class.getClassLoader().getResourceAsStream("config/db.properties"));
			String driver = pro.getProperty("driver");
			String url = pro.getProperty("url");
			String username = pro.getProperty("username");
			String password = pro.getProperty("password");
 
			ds = new BasicDataSource();
			ds.setDriverClassName(driver);
			ds.setUrl(url);
			ds.setUsername(username);
			ds.setPassword(password);	
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	private Connection conn = null;
	private String sql = null;
	private PreparedStatement pstmt = null;
	private boolean hasSet = false;
	private ResultSet rs = null;


	protected void setSql(String sql, Object...objects) throws DaoException {
		try {
			this.conn = ds.getConnection();
		} catch (SQLException e) {
			throw new DaoException("初始化连接失败");
		}
		this.sql = sql;
		try {
			this.pstmt = this.conn.prepareStatement(this.sql);
			for(int i = 0; i < objects.length; i++){
				this.pstmt.setObject(i + 1, objects[i]);
			}
			this.hasSet = true;
		} catch (SQLException e) {
			throw new DaoException("设置SQL语句错误",e);
		}
	}

	protected List<T> query() throws DaoException {
		if(!this.hasSet){
			throw new DaoException("未设置SQL");
		}
		List<T> objectList = new ArrayList<T>();
		try {			
			this.rs = this.pstmt.executeQuery();
			try{
				while(this.rs.next()){
					T t = mapToObject();
					objectList.add(t);
				}	
			}catch (SQLException e){
				e.printStackTrace();
				throw new DaoException("映射错误",e);
			}
		} catch (SQLException e) {
			throw new DaoException("数据库查询语句异常",e);
		}finally{
			close();
		}

		return objectList;
	}
	
	protected ResultSet getResultSet() throws DaoException{
		if(this.rs == null){
			throw new DaoException("未执行查询语句");
		}
		return this.rs;
	}
	
	protected abstract T mapToObject() throws SQLException, DaoException;

	protected void update() throws DaoException {
		if(!this.hasSet){
			throw new DaoException("未设置SQL");
		}
		try {
			this.pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("数据库更新语句异常",e);
		}finally{
			close();
		}
	}

	private void close() {
		this.sql = null;
		this.hasSet = false;
		if(this.rs != null){
			try {
				this.rs.close();
				this.rs = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				if(this.pstmt != null){
					try {
						this.pstmt.close();
						this.pstmt = null;
					} catch (SQLException e) {
						e.printStackTrace();
					}finally{
						if(this.conn != null){
							try {
								this.conn.close();
								this.conn = null;
							} catch (SQLException e) {
								e.printStackTrace();
							}

						}
					}
					
				}
			}
		}
		
	}
}
