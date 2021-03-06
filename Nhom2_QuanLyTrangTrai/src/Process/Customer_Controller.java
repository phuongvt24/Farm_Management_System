/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Process;

import ConnectDB.ConnectionUtils;
import Model.Customer;
import Model.Order_Export;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author diuai
 */
public class Customer_Controller {
     public static List<Customer> findAllCus() throws SQLException, ClassNotFoundException {
        List<Customer> listCus = new ArrayList<>();
        Connection con = null;
        PreparedStatement stat = null;
        con = ConnectionUtils.getMyConnection();
        
        CallableStatement stmt = con.prepareCall("{call FIND_ALL_CUS(?)}");
        stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
        stmt.execute();
        
        ResultSet rs = (ResultSet) stmt.getObject(1);
        while(rs.next()) {
        Customer cus = new Customer(rs.getString("CUSID"), rs.getString("CUSNAME"),
                rs.getString("GENDER"), rs.getDate("DATEOFBIRTH"), rs.getString("CUSADD"),
                rs.getString("CUSPHONE"), rs.getString("CUSEMAIL"), rs.getString("CUSTYPE"),
                rs.getDouble("ACCRUED_MONEY"), rs.getString("USERID"));
        listCus.add(cus);
    }
        
    con.close();
    return listCus ;
    }


public static boolean insertCus(Customer cus) throws SQLException, ClassNotFoundException {
//        boolean check = true;
        Connection conn = null;
        PreparedStatement statement = null;
        conn = ConnectionUtils.getMyConnection();

        CallableStatement stmt = conn.prepareCall("{call insert_Customer(?,?,?,?,?,?,?,?,?)}");
        stmt.setString(1, cus.getCusName());
        stmt.setString(2, cus.getGender());
        stmt.setDate( 3,new java.sql.Date( cus.getDateOfBirth().getTime()));
        stmt.setString(4, cus.getCusAdd());
        stmt.setString(5, cus.getCusPhone());
        stmt.setString(6, cus.getCusEmail());
        stmt.setString(7, cus.getCusType());
        stmt.setDouble(8, cus.getAccrued_Money());
        stmt.setString(9, cus.getUserID());
        
        boolean check = stmt.execute();
        conn.close();
        
        if (check == false) {
            return true;
        }
        return false;
    }


    public static ArrayList<Customer> findCus(String cusname) throws SQLException, ClassNotFoundException {
        ArrayList<Customer> listCus = new ArrayList<>();
        Connection con = ConnectionUtils.getMyConnection();
        PreparedStatement stat = null;
        
        CallableStatement stmt = con.prepareCall("{call FIND_CUS_2(?,?)}");
        stmt.setString(1,cusname);
        stmt.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
        stmt.execute();
        
        ResultSet rs = (ResultSet) stmt.getObject(2);
        while (rs.next()) {
            String idCus=rs.getString("CUSID");
            String nameCus = rs.getString("CUSNAME");
            String genderCus = rs.getString("GENDER");
            Date datebirthCus =rs.getDate("DATEOFBIRTH");
            String addCus=rs.getString("CUSADD");
            String phoneCus=rs.getString("CUSPHONE");
            String emailCus=rs.getString("CUSEMAIL");
            String typeCus=rs.getString("CUSTYPE");
            Double moneyCus=rs.getDouble("ACCRUED_MONEY");
            String uidCus=rs.getString("USERID");
            
            listCus.add(new Customer(idCus, nameCus, genderCus, datebirthCus, addCus, phoneCus, emailCus, typeCus, moneyCus, uidCus));
            
        }
        
        con.close();
        return listCus;
    }
    
    
    public static void deleteCus(String cusID) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement statement = null;
        con = ConnectionUtils.getMyConnection();
        CallableStatement stmt = con.prepareCall("{call delete_Customer(?)}");
        stmt.setString(1, cusID);
        stmt.execute();
        con.close();
    }
    

    public static boolean updateCus(Customer cus) throws SQLException, ClassNotFoundException {
        boolean check = true;
        Connection con = null;
        PreparedStatement stat = null;
        con = ConnectionUtils.getMyConnection();
        
        CallableStatement stmt = con.prepareCall("{call update_Customer(?,?,?,?,?,?,?,?,?,?)}");
        stmt.setString(1, cus.getCusID());
        stmt.setString(2, cus.getCusName());
        stmt.setString(3, cus.getGender());
        stmt.setDate(4, new java.sql.Date(cus.getDateOfBirth().getTime()));
        stmt.setString(5, cus.getCusAdd());
        stmt.setString(6, cus.getCusPhone());
        stmt.setString(7, cus.getCusEmail());
        stmt.setString(8, cus.getCusType());
        stmt.setDouble(9, cus.getAccrued_Money());
        stmt.setString(10, cus.getUserID());
        check = stmt.execute();
        con.close();
        
        if (check == false) {
            return true;
        }
        return false;

    }
    
    public static String getCusID (String userID) throws SQLException, ClassNotFoundException {
        Connection con = ConnectionUtils.getMyConnection();
        CallableStatement stmt = con.prepareCall("{call getCusID(?, ?)}");
        stmt.registerOutParameter(2, java.sql.Types.VARCHAR);
        stmt.setString(1, userID);
        stmt.execute();
        
        String cusID = stmt.getString(2);
        con.close();
        return cusID;
    }
    
    public static ArrayList<Order_Export> historyPurchase(String userID) throws SQLException, ClassNotFoundException {
        ArrayList<Order_Export> listOrdHistory = new ArrayList<>();
        
        Connection con = ConnectionUtils.getMyConnection();
        CallableStatement stmt = con.prepareCall("{call history_purchase(?, ?)}");
        stmt.registerOutParameter(2, java.sql.Types.REF_CURSOR);
        stmt.setString(1, userID);
        stmt.execute();
        
        ResultSet rs = (ResultSet) stmt.getObject(2);
        while (rs.next()) {
            String ord_Ex_Num = rs.getString("ord_Ex_Num");
            String transID = rs.getString("transID");
            int statusTrans = rs.getInt("statusTrans");
            String deli_Address = rs.getString("deli_Address");
            String payBy = rs.getString("payBy");
            Date dateOrdered = rs.getDate("dateOrdered");
            double orderTotal = rs.getDouble("orderTotal");
            
            listOrdHistory.add(new Order_Export(ord_Ex_Num, dateOrdered, deli_Address, orderTotal, payBy, transID, statusTrans));
        }

        con.close();
        return listOrdHistory;
    }
    
    
    
     public static ArrayList<Customer> Search_Customer(String ccusid, String ccusname, String cgender,
        String cadd, String cphone, String cemail, String ctype, String cmoney, String cuserid) throws ClassNotFoundException, SQLException {   
                    
        ArrayList<Customer> ProList = new ArrayList<>();
        Connection conn = ConnectionUtils.getMyConnection();
        PreparedStatement statement = null;
        
        CallableStatement stmt = conn.prepareCall("{call search_Customer_forAll(?,?,?,?,?,?,?,?,?,?)}");
        stmt.setString(1, ccusid);
        stmt.setString(2, ccusname);
        stmt.setString(3, cgender);
        stmt.setString(4, cadd);
        stmt.setString(5, cphone);
        stmt.setString(6, cemail);
        stmt.setString(7, ctype);
        stmt.setString(8, cmoney);
        stmt.setString(9, cuserid);
        stmt.registerOutParameter(10, oracle.jdbc.OracleTypes.CURSOR);
        stmt.execute();
        ResultSet rs = (ResultSet) stmt.getObject(10);

        while (rs.next()) {
            String cusid = rs.getString("CUSID");
            String cusname = rs.getString("CUSNAME");
            String gender = rs.getString("GENDER");
            Date datebirthCus =rs.getDate("DATEOFBIRTH");
            String add = rs.getString("CUSADD");
            String phone = rs.getString("CUSPHONE");
            String email = rs.getString("CUSEMAIL");
            String type = rs.getString("CUSTYPE");
            Double moneyCus=rs.getDouble("ACCRUED_MONEY");
            String userid = rs.getString("USERID");

            ProList.add(new Customer(cusid, cusname, gender,datebirthCus, add, phone,email,type,moneyCus, userid));
        }
        
        conn.close();
        return ProList;
    }
    
}
