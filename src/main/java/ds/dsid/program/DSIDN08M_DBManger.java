package ds.dsid.program;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import util.Common;

public class DSIDN08M_DBManger {
	private static final String URL = "jdbc:oracle:thin:@10.8.1.28:1521:FTLDDB01";
	private static final String PASS = "running2011";
	private static final String USER = "dsod";
	private static final String DIVER = "oracle.jdbc.driver.OracleDriver";
	
	// 加載數據庫驅動
	static {
		try {
			Class.forName(DIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// 獲取數據庫連接
	public  static Connection getConnection(){
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, USER, PASS);
			System.out.println(conn.getClass()+"====");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	// 獲取數據庫連接
	public  static Connection getConnectionDB1(){
		String url ="jdbc:oracle:thin:@10.8.1.32:1521:FTLDB1";
		String user="DSOD";
		String pass ="ora@it2013";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, pass);
			System.out.println(conn.getClass()+"====");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	// 關閉數據庫連接
	public static void closeConnection(Statement st,Connection conn,ResultSet rs){
		try{
			if(rs != null){
				rs.close();
			}
			if(st != null){
				st.close();
			}if(conn != null){
				conn.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
    public static void batchUpdate(String sqlTemplate, List<List<Object>> list) {  
        Connection conn = Common.getDbConnection();  
        PreparedStatement ps = null;  
        try {  
            ps = conn.prepareStatement(sqlTemplate);  
            conn.setAutoCommit(false);  
            int size = list.size();  
            List<Object> o = null;  
            for (int i = 0; i < size; i++) {  
                o = list.get(i);  
                for (int j = 0; j < o.size(); j++) {  
                    ps.setObject(j + 1, o.get(j));  
                }  
                ps.addBatch();  
            }  
  
            ps.executeBatch();  
            conn.commit();  
            conn.setAutoCommit(true);  
        } catch (SQLException e) {  
            e.printStackTrace();  
            try {  
                conn.rollback();  
                conn.setAutoCommit(true);  
            } catch (SQLException e1) {  
                e1.printStackTrace();  
            }  
        } finally {  
        	closeConnection(ps,conn,null);
        }  
    }  
	public static void main(String[] args) {
		Connection conn = Common.getDbConnection(); 
		try{
		System.out.println("Connection===== "+ conn.isClosed());
		closeConnection(null, conn, null);
		System.out.println("Connection===== "+ conn.isClosed());
		}catch(Exception e){
		  e.printStackTrace();
		}
	}

}
