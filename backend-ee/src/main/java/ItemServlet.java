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

@WebServlet(name = "itemServlet", urlPatterns = "/api/v1/item")
public class ItemServlet extends HttpServlet {
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
            String query = "SELECT * FROM item";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rslt = ps.executeQuery();
            JsonArray itemList = new JsonArray();
            while (rslt.next()) {
                String id = rslt.getString("id");
                String name = rslt.getString("name");
                double price = rslt.getDouble("price");
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", id);
                jsonObject.addProperty("name", name);
                jsonObject.addProperty("price", price);
                itemList.add(jsonObject);
            }
            resp.getWriter().println(itemList);
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
        double price = jsonObject.get("price").getAsDouble();

        try {
            Connection connection = bs.getConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO item (id, name, price) VALUES(?,?,?)");
            ps.setString(1, id);
            ps.setString(2, name);
            ps.setDouble(3, price);

            int rowsInserted = ps.executeUpdate();

            if (rowsInserted > 0) {
                resp.getWriter().println("Item Added Successfully");
            } else {
                resp.getWriter().println("Failed to Add Item");
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
        double price = jsonObject.get("price").getAsDouble();

        try {
            Connection connection = bs.getConnection();
            String query = "UPDATE item SET name = ?, price = ? WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setString(3, id);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                resp.getWriter().println("Item Updated Successfully");
            } else {
                resp.getWriter().println("Failed to Update Item");
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
            String query = "DELETE FROM item WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, id);
            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                resp.getWriter().println("Item Deleted Successfully");
            } else {
                resp.getWriter().println("Failed to Delete Item");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
