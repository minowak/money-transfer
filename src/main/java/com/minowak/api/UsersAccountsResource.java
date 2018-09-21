package com.minowak.api;

import com.minowak.model.Account;
import com.minowak.model.Balance;
import com.minowak.model.Transfer;
import com.minowak.model.User;
import com.minowak.service.TransferService;
import com.minowak.service.UsersService;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ManagedBean
@Path("/user/{id}/account")
public class UsersAccountsResource {
    private UsersService usersService;
    private TransferService transferService;

    @Inject
    UsersAccountsResource(UsersService usersService, TransferService transferService) {
        this.usersService = usersService;
        this.transferService = transferService;
    }

    @GET
    @Path("{id}/account")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Account> getAccounts(@PathParam("id") Long id) {
        return usersService.get(id).getAccounts();
    }

    @GET
    @Path("{id}/account/{number}")
    @Produces(MediaType.APPLICATION_JSON)
    public Account getAccount(@PathParam("id") Long id, @PathParam("number") String number) {
        return usersService.get(id).getAccounts().stream()
                .filter(a -> a.getNumber().equals(number))
                .findFirst()
                .orElse(null);
    }

    @GET
    @Path("{id}/account/{number}/balance")
    @Produces(MediaType.APPLICATION_JSON)
    public Balance calculateBalance(@PathParam("id") Long id, @PathParam("number") String number) {
        BigInteger outgoingValue = transferService.get().stream()
                .filter(t -> number.equals(t.getInputNumber()))
                .map(Transfer::getValue)
                .reduce(BigInteger::add)
                .orElse(BigInteger.ZERO);
        BigInteger incomingValue = transferService.get().stream()
                .filter(t -> number.equals(t.getOutputNumber()))
                .map(Transfer::getValue)
                .reduce(BigInteger::add)
                .orElse(BigInteger.ZERO);

        List<Transfer> accountTransfers = transferService.get().stream()
                .filter(t -> number.equals(t.getInputNumber()) || number.equals(t.getOutputNumber()))
                .collect(Collectors.toList());

        return new Balance(incomingValue.subtract(outgoingValue), accountTransfers);
    }

    @POST
    @Path("{id}/account")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAccount(Account account, @PathParam("id") Long id) {
        User user = usersService.get(id);
        Set<Account> accounts = user.getAccounts();
        accounts.add(account);

        boolean updated = usersService.update(id, user.toBuilder().accounts(accounts).build());
        return updated
                ? Response.status(Response.Status.CREATED).build()
                : Response.status(Response.Status.CONFLICT).build();
    }

    @DELETE
    @Path("{id}/account")
    public Response deleteAccounts(@PathParam("id") Long id) {
        usersService.get(id).getAccounts().clear();
        return Response.status(Response.Status.GONE).build();
    }
}
