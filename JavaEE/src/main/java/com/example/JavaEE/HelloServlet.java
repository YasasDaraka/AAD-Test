package com.example.JavaEE;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(urlPatterns = "/customer")
public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Connection con = null;
        try {
            con = DBConnection.getInstance().getConnection();
            String sql = "SELECT * FROM customer";
            String allJson= "";
            ResultSet resultSet = con.prepareStatement(sql).executeQuery();
            while (resultSet.next()) {
             String id     =   resultSet.getString(1);
             String name   =   resultSet.getString(2);
             String address=  resultSet.getString(3);
             double salary =   resultSet.getDouble(4);

             String cusDetails = "{\"id\": \""+id+"\" ,\"name\": \""+name+"\" , \"address\": \""+address+"\" ,\"salary\": \""+salary+"\" },";
             allJson = allJson+cusDetails;
            }
            String finalJson = "["+allJson.substring(0,allJson.length()-1)+"]";
            resp.getWriter().write(finalJson);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id=  req.getParameter("cusID");
        String name=  req.getParameter("cusName");
        String address=  req.getParameter("cusAddress");
        String salary=  req.getParameter("cusSalary");

        try {
            Connection con = DBConnection.getInstance().getConnection();
            String sql = "INSERT INTO customer(cusId, cusName, cusAddress, cusSalary) " +
                    "VALUES(?, ?, ?, ?)";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, id);
            pstm.setString(2, name);
            pstm.setString(3, address);
            pstm.setDouble(4, Double.parseDouble(salary));

            boolean isSaved = pstm.executeUpdate() > 0;

            if (isSaved){
                System.out.println("Customer added");
                resp.getWriter().write("true");
            }else{
                System.out.println("Customer not added");
                resp.getWriter().write("false");
            }
        } catch (SQLException throwables) {
            resp.getWriter().write("false");
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            resp.getWriter().write("false");
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection con = null;
        String cusID = req.getParameter("cusId");
        try {
            con = DBConnection.getInstance().getConnection();
            String sql = "DELETE FROM customer WHERE cusId = ?";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, cusID);

            boolean isSaved = pstm.executeUpdate() > 0;
            if (isSaved){
                System.out.println("Customer Deleted");
                resp.getWriter().write("true");
            }else{
                System.out.println("Customer not Deleted");
                resp.getWriter().write("false");
            }
        } catch (SQLException throwables) {
            resp.getWriter().write("false");
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            resp.getWriter().write("false");
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id=  req.getParameter("cusID");
        String name=  req.getParameter("cusName");
        String address=  req.getParameter("cusAddress");
        String salary=  req.getParameter("cusSalary");

        try {
            Connection con = DBConnection.getInstance().getConnection();
            String sql = "UPDATE customer SET cusName = ?, cusAddress = ?, cusSalary = ? WHERE cusId = ?";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, name);
            pstm.setString(2, address);
            pstm.setDouble(3, Double.parseDouble(salary));
            pstm.setString(4, id);

            boolean isSaved = pstm.executeUpdate() > 0;

            if (isSaved){
                System.out.println("Customer Updated");
                resp.getWriter().write("true");
            }else{
                System.out.println("Customer not Updated");
                resp.getWriter().write("false");
            }
        } catch (SQLException throwables) {
            resp.getWriter().write("false");
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            resp.getWriter().write("false");
            e.printStackTrace();
        }
    }
}