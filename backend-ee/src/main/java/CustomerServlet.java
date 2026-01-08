import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "customerServlet",urlPatterns = "/api/v1/customer")
public class CustomerServlet extends HttpServlet {
    BasicDataSource bs;
    @Override
    public void init() throws ServletException {
        ServletContext sc = getServletContext();
        bs = (BasicDataSource) sc.getAttribute("datasource");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Connection connection = bs.getConnection();
            String query = "SELECT * FROM customer";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rslt = ps.executeQuery();
            JsonArray customerList = new JsonArray();
            while (rslt.next()) {
                String id = rslt.getString("id");
                String name = rslt.getString("name");
                String address = rslt.getString("address");
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id",id);
                jsonObject.addProperty("name",name);
                jsonObject.addProperty("address",address);
                customerList.add(jsonObject);
            }
            resp.getWriter().println(customerList);
            resp.setContentType("application/json");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(req.getReader(), JsonObject.class);
        String id = jsonObject.get("id").getAsString();
        String name = jsonObject.get("name").getAsString();
        String address = jsonObject.get("address").getAsString();

        try {
            Connection connection = bs.getConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO customer (id, name, address) VALUES(?,?,?)");
            ps.setString(1,id);
            ps.setString(2,name);
            ps.setString(3,address);

            int rowsInserted = ps.executeUpdate();

            if (rowsInserted > 0) {
                resp.getWriter().println("Customer Added Successfully");
            } else {
                resp.getWriter().println("Failed to Add Customer");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(req.getReader(), JsonObject.class);
        String id = jsonObject.get("id").getAsString();
        String name = jsonObject.get("name").getAsString();
        String address = jsonObject.get("address").getAsString();

        try {
            Connection connection = bs.getConnection();
            String query = "UPDATE customer SET name = ?, address = ? WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1,name);
            ps.setString(2,address);
            ps.setString(3,id);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                resp.getWriter().println("Customer Updated Successfully");
            } else {
                resp.getWriter().println("Failed to Update Customer");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("cid");
        try {
            Connection connection = bs.getConnection();
            String query = "DELETE FROM customer WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1,id);
            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                resp.getWriter().println("Customer Deleted Successfully");
            } else {
                resp.getWriter().println("Failed to Delete Customer");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
