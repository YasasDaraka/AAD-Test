package com.example.JSON;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
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
            ResultSet resultSet = con.prepareStatement(sql).executeQuery();
            JsonObjectBuilder objectB = Json.createObjectBuilder();
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            while (resultSet.next()) {
                String id     =   resultSet.getString(1);
                String name   =   resultSet.getString(2);
                String address=  resultSet.getString(3);
                double salary =   resultSet.getDouble(4);

                objectB.add("id",id);
                objectB.add("name",name);
                objectB.add("address",address);
                objectB.add("salary",salary);
                arrayBuilder.add(objectB.build());
            }
            JsonObjectBuilder finalOB = Json.createObjectBuilder();
            finalOB.add("data",arrayBuilder.build());
            finalOB.add("message","Done");
            finalOB.add("status","200");

            resp.getWriter().write(String.valueOf(finalOB.build()));
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
        resp.setContentType("application/json");//always set content Type
        JsonObjectBuilder finalOB = Json.createObjectBuilder();
        finalOB.add("data","");
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
                finalOB.add("status","200");
                finalOB.add("message","true");
                resp.getWriter().write(String.valueOf(finalOB.build()));
            }else{
                System.out.println("Customer not added");
                finalOB.add("status","400");
                finalOB.add("message","false");
                resp.getWriter().write(String.valueOf(finalOB.build()));
            }
        } catch (SQLException throwables) {
            finalOB.add("status","400");
            finalOB.add("message","false");
            resp.getWriter().write(String.valueOf(finalOB.build()));
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            finalOB.add("status","400");
            finalOB.add("message","false");
            resp.getWriter().write(String.valueOf(finalOB.build()));
            e.printStackTrace();
        }
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
      /*
      ///json format
      {
    "id": "C001",
    "name":"Yasas",
    "addresss":"Galle",
    "salary":100000
    }
    ///////Receive Json file manually///////////
      ServletInputStream stream = req.getInputStream();//can catch any type request
      int read;
      while ((read = stream.read()) != -1){
          System.out.print((char) read);
      }*/
        ////////Receive Json file using Json///////////
        JsonReader reader = Json.createReader(req.getReader());
        System.out.println(reader);//return org.glassfish.json.JsonReaderImpl@7014802
        JsonObject jsonObject = reader.readObject();
        System.out.println(jsonObject);//return {"id":"C001","name":"Yasas","address":"Galle","salary":100000}
        String id = jsonObject.getString("id");
        String name = jsonObject.getString("name");
        String address = jsonObject.getString("address");
        int salary = jsonObject.getInt("salary");
        System.out.println(id+name+address+salary); //return C001YasasGalle100000
    }

}