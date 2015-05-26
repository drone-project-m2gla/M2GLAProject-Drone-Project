package rest;


import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dao.GeoImageDAO;
import entity.GeoImage;

/**
 * Created by arno on 12/02/15.
 */
@Path("/images")
public class ImageRest {
    @GET
    public Response getAllImages() {
        GeoImageDAO gID = new GeoImageDAO();
        gID.connect();
        List<GeoImage> res = gID.getAll();
        gID.disconnect();
        return Response.ok(res).build();
    }

    @GET
    @Path("near/{latitude}/{longitude}")
    public Response getAllImagesNear(@PathParam("latitude") float latitude, @PathParam("longitude") float longitude) {
        GeoImageDAO gID = new GeoImageDAO();
        gID.connect();
        List<GeoImage> res = gID.getAllImagesNear(latitude, longitude);
        gID.disconnect();
        return Response.ok(res).build();
    }

}
