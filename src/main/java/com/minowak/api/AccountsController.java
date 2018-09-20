package com.minowak.api;

import com.minowak.model.Account;
import com.minowak.service.AccountsService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/account")
public class AccountsController {
    private final AccountsService accountsService = AccountsService.getInstance();
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Iterable<Account> getAccounts() {
        return accountsService.get();
    }

    @GET
    @Path("{number}")
    @Produces(MediaType.APPLICATION_JSON)
    public Account getAccount(@PathParam("number") String number) {
        return accountsService.get(number);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAccount(Account account) {
        boolean created = accountsService.add(account);
        return created ? Response.status(Response.Status.CREATED).build()
                : Response.status(Response.Status.CONFLICT).build();
    }

    @PUT
    @Path("{number}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAccount(Account account, @PathParam("number") String number) {
        accountsService.update(number, account);
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    public Response deleteAllAccounts() {
        accountsService.delete();
        return Response.status(Response.Status.GONE).build();
    }

    @DELETE
    @Path("{number}")
    public Response deleteAccount(@PathParam("number") String number) {
        accountsService.delete(number);
        return Response.status(Response.Status.GONE).build();
    }

}
