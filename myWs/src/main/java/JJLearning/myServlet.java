package JJLearning;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by juanjomen on 1/27/2016.
 */
public class myServlet extends HttpServlet {

  protected int myParam = 5;

  public void init(ServletConfig servletConfig) throws ServletException{
   // this.myParam = Integer.parseInt(servletConfig.getInitParameter("myParam"));
  }
  protected void doGet(HttpServletRequest request, HttpServletResponse res) throws ServletException, IOException {
    request.getMethod();
    res.setContentType("text/html");//setting the content type
    PrintWriter pw=res.getWriter();//get the stream to write the data

   String myContextParam= request.getSession().getServletContext().getInitParameter("context");
//writing html in the stream
    pw.println("<html><body>");
    pw.println("Welcome to servlet");
    pw.println(myParam+5);
    pw.println(request.getMethod());
    ServletContext context = request.getSession().getServletContext();
    context.setAttribute("someValue", "aValue");
    Object attribute = context.getAttribute("someValue");
    pw.println(attribute.toString());
    pw.println(myContextParam);
    pw.println("</body></html>");

    pw.close();//closing the stream
  }
}
