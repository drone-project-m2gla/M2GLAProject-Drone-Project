package rest;


import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import dao.GeoImageDAO;
import entity.GeoImage;

/**
 * Created by arno on 12/02/15.
 */
@Path("/images")
public class Images {

    @GET
    @Path("image")
    public Response getImages(String images) {

        return Response.status(200).entity("Les images sont : " + images).build();

    }

    @GET
    public List<GeoImage> getAllImages() {

        GeoImageDAO gID = new GeoImageDAO();
        gID.connect();
        List<GeoImage> res = gID.getAll();
        gID.disconnect();
        // TODO fix that
        return res;

    }

}
