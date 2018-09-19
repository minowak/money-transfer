package com.minowak.api;

import com.minowak.model.Account;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user/account")
public class AccountsController {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Iterable<Account> getAccounts() {
        return null;
    }

    @GET
    @Path("{number}")
    @Produces(MediaType.APPLICATION_JSON)
    public Account getAccount(@PathParam("number") String number) {
        return null;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAccount(Account account) {
        return null;
    }

    @PUT
    @Path("{number}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAccount(Account account) {
        return null;
    }

    @DELETE
    public Response deleteAllAccounts() {
        return null;
    }

    @DELETE
    @Path("{number}")
    public Response deleteAccount(@PathParam("number") String number) {
        return null;
    }

}
