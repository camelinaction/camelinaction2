package camelinaction;

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.Uri;

@WebServlet(name = "HelloServlet", urlPatterns = {"/*"}, loadOnStartup = 1)
public class HelloServlet extends HttpServlet {

    @Inject @Uri("direct:hello")
    private ProducerTemplate producer;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String name = req.getParameter("name");
        ServletOutputStream out = res.getOutputStream();

        if (name == null) {
            out.print("There is no name query parameter, try adding ?name=donald");
        } else {
            // call the Camel route
            String result = producer.requestBody("direct:hello", name, String.class);
            out.print(result);
        }
    }
}
