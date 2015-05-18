package rest;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dao.InterventionDAO;
import entity.Intervention;
import entity.Mean;
import entity.Position;
import service.PushService.TypeClient;
import service.impl.PushServiceImpl;
import service.impl.RetrieveAddressImpl;

/**
 * Created by arno on 12/02/15.
 *
 * Service rest du type intervention
 */
@Path("/intervention")
public class InterventionRest {



    @GET
    @Path("/{id}/moyen/{idmean}")
    @Produces({MediaType.APPLICATION_JSON})
    public Mean getMeanForIntervention(@PathParam("id") long id,@PathParam("idmean") long idmean) {

        InterventionDAO iD = new InterventionDAO();
        Mean res = null;
        iD.connect();
        List<Mean> meanList = iD.getById(id).getMeansList();
        iD.disconnect();

        for (Mean mean : meanList) {
            if (mean.getId() == idmean) {
                res = mean;
            }
        }

        return res;

    }

    @POST
    @Path("/{id}/moyen/emplace")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Mean validateMeanPositionForIntervention(@PathParam("id") long id, Mean mean) {

        InterventionDAO iD = new InterventionDAO();
        Mean res = null;
        iD.connect();
        Intervention intervention = iD.getById(id);
        List<Mean> meanList = intervention.getMeansList();

        for (Mean m : meanList) {
            if (m.getId() == mean.getId()) {
                m.setInPosition(true);
                res = m;
            }
        }
        iD.update(intervention);
        iD.disconnect();
        return res;
    }

    @POST
    @Path("/{id}/moyen/positionner")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Mean updateMeanPositionForIntervention(@PathParam("id") long id, Mean mean) throws IOException {
        InterventionDAO iD = new InterventionDAO();
        Mean res = null;
        iD.connect();
        Intervention intervention = iD.getById(id);
        List<Mean> meanList = intervention.getMeansList();

        for (Mean m : meanList) {
            if (m.getId() == mean.getId()) {
                m.setCoordinates(mean.getCoordinates());
                m.setInPosition(false);
                PushServiceImpl.getInstance().sendMessage(TypeClient.SIMPLEUSER, "moyenMove", m);
                res = m;
            }
        }
        iD.update(intervention);
        iD.disconnect();
        return res;
    }

    @POST
    @Path("/{id}/moyenextra")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Mean getMeanListForIntervention(@PathParam("id") long id,Mean meanXtra) {
        InterventionDAO iD = new InterventionDAO();
        iD.connect();
        Intervention intervention = iD.getById(id);
        intervention.getMeansXtra().add(meanXtra);

        iD.update(intervention);

        iD.disconnect();

        try {
            PushServiceImpl.getInstance().sendMessage(TypeClient.CODIS, "xtra",intervention);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return intervention.getMeansXtra().get(intervention.getMeansXtra().size()-1);
    }

    @GET
    @Path("/{id}/moyen")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Mean> getMeanListForIntervention(@PathParam("id") long id) {
        InterventionDAO iD = new InterventionDAO();
        iD.connect();
        List<Mean> res = iD.getById(id).getMeansList();
        iD.disconnect();
        return res;
    }


    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Intervention getIntervention(@PathParam("id") long id) {
        InterventionDAO iD = new InterventionDAO();
        iD.connect();
        Intervention res = iD.getById(id);
        iD.disconnect();
        return res;
    }

    @GET
    @Path("")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Intervention> getAllIntervention() {
        InterventionDAO iD = new InterventionDAO();
        iD.connect();
        List<Intervention> res = iD.getAll();
        iD.disconnect();
        return res;
    }

    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Intervention setIntervention(Intervention intervention) {
        InterventionDAO iD = new InterventionDAO();
        iD.connect();

        RetrieveAddressImpl adresseIntervention = new RetrieveAddressImpl(intervention.getAddress(), intervention.getPostcode(), intervention.getCity()); 
        
        Position coordinatesIntervention = adresseIntervention.getCoordinates();
        intervention.setCoordinates(coordinatesIntervention);
 
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        formatter.format(now);
        intervention.setDateCreate(now);
       
        // Génération de la liste des moyens
        intervention.generateMeanList();
        Intervention res = iD.create(intervention);
        iD.disconnect();

        return res;
    }
}
