import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.commons.dbcp2.BasicDataSource;

@WebListener
public class DBConnection implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        BasicDataSource bs = new BasicDataSource();
        bs.setDriverClassName("com.mysql.cj.jdbc.Driver");

        bs.setUrl("jdbc:mysql://localhost:3306/pos_system_ee");
        bs.setUsername("root");
        bs.setPassword("mysql");

        bs.setInitialSize(5);
        bs.setMaxTotal(20);

        sc.setAttribute("datasource",bs);
    }
}
