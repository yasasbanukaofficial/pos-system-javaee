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

@WebServlet(name = "orderServlet", urlPatterns = "/api/v1/order")
public class OrderServlet extends HttpServlet {
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
            String query = "SELECT * FROM orders";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rslt = ps.executeQuery();
            JsonArray orderList = new JsonArray();
            while (rslt.next()) {
                String id = rslt.getString("id");
                String customerId = rslt.getString("customerId");
                String itemId = rslt.getString("itemId");
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", id);
                jsonObject.addProperty("customerId", customerId);
                jsonObject.addProperty("itemId", itemId);
                orderList.add(jsonObject);
            }
            resp.getWriter().println(orderList);
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
        String customerId = jsonObject.get("customerId").getAsString();
        String itemId = jsonObject.get("itemId").getAsString();

        try {
            Connection connection = bs.getConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO orders (id, customerId, itemId) VALUES(?,?,?)");
            ps.setString(1, id);
            ps.setString(2, customerId);
            ps.setString(3, itemId);

            int rowsInserted = ps.executeUpdate();

            if (rowsInserted > 0) {
                resp.getWriter().println("Order Added Successfully");
            } else {
                resp.getWriter().println("Failed to Add Order");
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
        String customerId = jsonObject.get("customerId").getAsString();
        String itemId = jsonObject.get("itemId").getAsString();

        try {
            Connection connection = bs.getConnection();
            String query = "UPDATE orders SET customerId = ?, itemId = ? WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, customerId);
            ps.setString(2, itemId);
            ps.setString(3, id);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                resp.getWriter().println("Order Updated Successfully");
            } else {
                resp.getWriter().println("Failed to Update Order");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        try {
            Connection connection = bs.getConnection();
            String query = "DELETE FROM orders WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, id);
            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                resp.getWriter().println("Order Deleted Successfully");
            } else {
                resp.getWriter().println("Failed to Delete Order");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
