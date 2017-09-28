package barcodescanner.service.impl;

import barcodescanner.service.BarcodeSaveService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Author: MaoMorn
 * Date: 2017/9/27
 * Time: 14:51
 * Description: 接口实现类，将数据存放到PostgreSQL中的操作
 */
public class BarcodeToPostgreSQL implements BarcodeSaveService{
    private String url = "jdbc:postgresql://127.0.0.1:5432/test";
    private String user = "postgres";
    private String password = "postgres123";
    private Connection connection = null;
    private PreparedStatement preStatement = null;
    String sql = "INSERT INTO test (barcode) VALUES (?)";

    /**
     * 将条码数据保存到PostgreSQL数据库
     * @param barcode 条形码数据
     */
    @Override
    public void save(String barcode) {
        try {
            if (connection == null) {
                Class.forName("org.postgresql.Driver");
                System.out.println("连接数据库");
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("是否成功连接pg数据库" + connection);
                preStatement = connection.prepareStatement(sql);
                preStatement.setString(1,barcode);
            }
            preStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 断开与PostgreSQL数据库的连接
     */
    @Override
    public void finish() {
        System.out.println("断开连接");
        try {
            if (connection != null) {
                connection.close();
                preStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
