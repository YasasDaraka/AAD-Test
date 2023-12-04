package com.example.JavaEE;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(urlPatterns = "/customer")
public class HelloServlet extends HttpServlet {
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
            }else{
                System.out.println("Customer not added");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}