package JJLearning;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by juanjomen on 1/14/2016.
 */
@Path("/myName")
public class PlayingAround {

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String getMessages(){

    ApplicationContext ctx= new FileSystemXmlApplicationContext("C:\\Users\\juanjomen\\Documents\\GitHub\\myWs\\src\\main\\webapp\\WEB-INF\\applicationContext.xml");
    JJClass jjClass = (JJClass) ctx.getBean("testAcces");
    System.out.println(jjClass.getName());


    return jjClass.getName();
  }
}
