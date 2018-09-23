package com.minowak.api;

import com.minowak.ErrorResponse;
import com.minowak.model.Account;
import com.minowak.model.Transfer;
import com.minowak.model.User;
import com.minowak.service.TransferService;
import com.minowak.service.UsersService;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@ManagedBean
@Path("/transfer")
public class TransferResource {
    private TransferService transferService;
    private UsersService usersService;

    @Inject
    TransferResource(TransferService transferService, UsersService usersService) {
        this.transferService = transferService;
        this.usersService = usersService;
    }

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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTransfer(Transfer transfer) {
        boolean inputAccountExist = transfer.getInputNumber() == null || accountExists(transfer.getInputNumber());
        boolean outputAccountExists = transfer.getOutputNumber() == null || accountExists(transfer.getOutputNumber());

        if (!inputAccountExist) {
            return new ErrorResponse(Response.Status.CONFLICT,
                    String.format("Account %s does not exist", transfer.getInputNumber()))
                    .toResponse();
        }

        if (!outputAccountExists) {
            return new ErrorResponse(Response.Status.CONFLICT,
                    String.format("Account %s does not exist", transfer.getOutputNumber()))
                    .toResponse();
        }

        return transferService.add(transfer) ? Response.status(Response.Status.CREATED).build()
                : new ErrorResponse(Response.Status.CONFLICT,
                String.format("Transfer with id %d already exists", transfer.getId())).toResponse();
    }

    private boolean accountExists(String number) {
        for (User user : usersService.get()) {
            if (user.getAccounts().stream()
                    .map(Account::getNumber)
                    .anyMatch(n -> n.equals(number))) {
                return true;
            }
        }
        return false;
    }
}
