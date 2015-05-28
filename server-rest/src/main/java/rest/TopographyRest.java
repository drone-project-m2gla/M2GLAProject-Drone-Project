package rest;

import org.apache.log4j.Logger;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dao.GeoIconDAO;
import entity.GeoIcon;

/**
 * @author jerem on 08/04/15.
 * @see TopographyRest service interacts for sending or creating topographic icons
 */
@Path("/topographie")
public class TopographyRest {

	private static final Logger LOGGER = Logger.getLogger(TopographyRest.class);

	/**
	 * Return a json list of static points on the map near specific GeoPosition
     * Note :  Implementation near (longitude, near latitude are not implemented and therefore missing..
	 * @return List<GeoIcon>
	 */
	@GET
	@Path("{Long}/{Latitude}/{Rayon}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<GeoIcon> getCoordinatedIcons(@PathParam("Long") double positionLongitude, @PathParam("Latitude") double positionLatitude, @PathParam("Rayon") long rayon) {

		GeoIconDAO gID = new GeoIconDAO();
		gID.connect();
		List<GeoIcon> res = gID.getAll();
		gID.disconnect();
		return res;
	}

	/**
	 * add icons for topographie
	 * @return Response
	 */
	@POST
	@Path("icontempo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response logIn(GeoIcon icon) {

		GeoIconDAO dao = new GeoIconDAO();


		if (!"".equals(icon.getColor()) && icon.getColor() != null &&
				(!"".equals(icon.getEntitled())) && icon.getEntitled() != null && (!"".equals(icon.getFilename())) && icon.getFilename()!= null){

			dao.connect();
			dao.create(icon);
			dao.disconnect();

			return Response.status(201).entity("L'icône créée est : " + icon.getEntitled() + "<BR/>" + dao.toString()).build();

		}else{
			return Response.status(406).entity("L'icône n'a pas été créée").build();

		}
	}
}