package com.example.javahomework1;
import android.os.Message;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

class ReturnData{
    int id;
    Map result;
}

public class DBConnection {
    private static final String DBDRIVER = "com.mysql.jdbc.Driver";
    private static final String DBURL = "jdbc:mysql://106.52.165.12:3306/Soso?useUnicode=true&characterEncoding=utf-8&useSSL=false";
    private static final String DBUSER = "root";
    private static final String DBPASSWORD = "";

    public ReturnData linkMysql(String sql, OPTION op, Map<String, String> other_info) {
        Connection conn = null;
        Statement stmt = null;
        ReturnData returnData = new ReturnData();
        try {
            Class.forName(DBDRIVER);
        }
        catch (Exception e){
            e.printStackTrace();
            returnData.id = -1;
            return returnData;
        }
        try{
            conn = DriverManager.getConnection(DBURL,DBUSER,DBPASSWORD);
            stmt = conn.createStatement();
            System.out.println(sql);
            Message msg = new Message();
            switch (op){
                case EM_REGISTER:
                    stmt.executeUpdate(sql);
                    break;
                case EM_LOGIN:
                    ResultSet rs = stmt.executeQuery(sql);
                    returnData.result = new HashMap();
                    returnData.result = other_info;
                    returnData.result.put("CorrectPassword", "null");
                    while (rs.next()) {
                        returnData.result.put("CorrectPassword",rs.getString("PassWord"));
                        msg.obj = returnData.result;
                    }
                    rs.close();
                    break;
            }

            stmt.close();
            conn.close();

            msg.what = op.TurnToInt(op);
            returnData.id = msg.what;
            System.out.println("return db");
            return returnData;
        } catch (Exception e) {
            e.printStackTrace();
            returnData.id = -1;
            return returnData;
        }
        finally {
            if(conn!=null){
                try {
                    conn.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
