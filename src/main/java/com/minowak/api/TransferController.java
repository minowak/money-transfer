package com.minowak.api;

import com.minowak.model.Transfer;
import com.minowak.service.TransferService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Path("/transfer")
public class TransferController {
    private final TransferService transferService = TransferService.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Transfer> getTransfers() {
        return transferService.get();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Transfer getTransfer(@PathParam("id") Long id) {
        return transferService.get(id);
    }

    // TODO check if accounts exist
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTransfer(Transfer transfer) {
        return transferService.add(transfer) ? Response.status(Response.Status.CREATED).build()
                : Response.status(Response.Status.CONFLICT).build();
    }
}
