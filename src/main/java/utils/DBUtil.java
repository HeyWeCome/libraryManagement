package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @description:
 * @author: HeyWeCome
 * @createDate: 2020/10/1 14:21
 * @version: 1.0
 */
public class DBUtil {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/library?serverTimezone=GMT%2B8";   // 数据库连接地址
    private static final String USER = "root";                                                        // 用户名
    private static final String PASSWORD = "123456";                                                  // 密码
    private static final String DRIVERPATH = "com.mysql.cj.jdbc.Driver";                              // 驱动路径
    private static Connection conn = null;
    Statement stmt = null;

    // 注册驱动可以放在静态块中，进行类的初始化：
    static {
        try {
            Class.forName(DRIVERPATH);
        }catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 获取数据库连接
    public static Connection getConn() {
        synchronized(DBUtil.class) {
            try {
                if(null == conn || conn.isClosed()) {
                    synchronized(DBUtil.class) {
                        conn = DriverManager.getConnection(URL,USER,PASSWORD);
                    }
                }
            }catch(SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }
}
