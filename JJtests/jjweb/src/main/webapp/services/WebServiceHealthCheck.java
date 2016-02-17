
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


@Path("healthcheck")
public class WebServiceHealthCheck extends WebServiceBase {

    /** Once the system is up, the system is always considered up  */
    boolean isOK = false;

    /**
     * Health-check heartbeat. This returns an HTTP 200 (OK) if certain criteria are met.
     *
     * @return Status "OK" if health check is OK, "Service Unavailable" otherwise
     */
    @GET
    @Path("/heartbeat")
    public Response heartbeat(
        @Context HttpHeaders headers) {
        // Always return OK once the system comes up as "OK" ...
        if (isOK) {
            return Response.ok().build();
        }

        // The system is considered "OK" if there is a readable service
        long startTime = System.currentTimeMillis();
        if (getReadableService() != null) {
            isOK = true;
            log(null, "/healthcheck/heartbeat", WebConstants.METHOD_GET, Status.OK.getStatusCode(), headers, WebConstants.MSG_TIME, "" + (System.currentTimeMillis() - startTime));
            return Response.ok().build();
        }

        return Response.status(Status.SERVICE_UNAVAILABLE).build();
    }
}
