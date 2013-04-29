javadao
=======

Java的DAO程序

提供了一个名为Dao的抽象类作为其它Dao类的父类。所有的新创建的Dao类必须继承自Dao类。
比如，我们定义一个UserDao类

    public class UserDao extends Dao<User>{
        
    }
        
    
Dao类为其子类提供了4个实例方法setSql()、update()、query()、getResultSet()及一个抽象方法mapToObject()。下面分别介绍这些方法：
#####void setSQL(String sql, Object... objects)方法：
setSQL()方法用于设定PreparedStatement，所以需要传入一个sql语句以及sql语句中的问号所对应的值。

#####void update()方法：
update()方法需要在setSQL()方法调用后调用，用于执行insert、update及delete类型的sql语句。

#####List<T> query()方法：
query()方法需要在setSQL()方法调用后调用，用于执行select类型的语句。query()方法将返回由实体类对象组成的列表。

#####ResultSet getResultSet()方法和抽象方法T mapToObject()方法:
继承自Dao的类必须实现mapToObject()方法。mapToObject()方法用于将查询结果中的每一行映射为一个实体类的对象。子类在实现mapToObject方法时可以通过调用getResultSet()得到所需的结果集，mapToObject方法需要将结果集转化为一个实体类的对象，并返回这个对象。

继续以UserDao为例，为其定义一个User findUserById(int id)方法：


    public class UserDao extends Dao<User>{
        protected User mapToObject(){
            User user = new User();
            ResultSet rs = getResultSet();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            return user;
        }
        public User findUserById(int id){
            setSQL("select id, username, password from user where id = ?", id);
            List<User> userList = query();
            if(userList.size() == 0){
                return null;
            }else if{
                return userList.get(0);
            }else{
                //error
            }
        }   
    }
