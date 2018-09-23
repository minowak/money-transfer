package com.minowak.api;

import com.minowak.ErrorResponse;
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccounts(@PathParam("id") Long id) {
        User user = usersService.get(id);
        if (user != null) {
            return Response.ok(usersService.get(id).getAccounts()).build();
        } else {
            return new ErrorResponse(Response.Status.CONFLICT,
                    String.format("User with id %d doesn't exist", id)).toResponse();
        }
    }

    @GET
    @Path("{number}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccount(@PathParam("id") Long id, @PathParam("number") String number) {
        User user = usersService.get(id);
        if (user != null) {
            return Response.ok(usersService.get(id).getAccounts().stream()
                    .filter(a -> a.getNumber().equals(number))
                    .findFirst()
                    .orElse(null)).build();
        } else {
            return new ErrorResponse(Response.Status.CONFLICT,
                    String.format("User with id %d doesn't exist", id)).toResponse();
        }
    }

    @GET
    @Path("{number}/balance")
    @Produces(MediaType.APPLICATION_JSON)
    public Response calculateBalance(@PathParam("id") Long id, @PathParam("number") String number) {
        User user = usersService.get(id);
        if (user == null) {
            return new ErrorResponse(Response.Status.CONFLICT,
                    String.format("User with id %d does not exist", id)).toResponse();
        }
        if (user.getAccounts().stream().map(Account::getNumber).noneMatch(n -> n.equals(number))) {
            return new ErrorResponse(Response.Status.CONFLICT,
                    String.format("User with id %d does not have account with number %s", id, number))
                    .toResponse();
        }

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

        return Response.ok(new Balance(incomingValue.subtract(outgoingValue), accountTransfers)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAccount(Account account, @PathParam("id") Long id) {
        for (User user : usersService.get()) {
            if (user.getAccounts().contains(account)) {
                return new ErrorResponse(Response.Status.CONFLICT,
                        String.format("There is already an account with number %s", account.getNumber()))
                        .toResponse();
            }
        }
        User user = usersService.get(id);
        Set<Account> accounts = user.getAccounts();
        accounts.add(account);

        boolean updated = usersService.update(id, user.toBuilder().accounts(accounts).build());
        return updated
                ? Response.status(Response.Status.CREATED).build()
                : new ErrorResponse(Response.Status.CONFLICT,
                    String.format("User with id %d does not exist", id)).toResponse();
    }

    // TODO test
    @DELETE
    @Path("{number}")
    public Response deleteAccount(@PathParam("id") Long id, @PathParam("number") String number) {
        User user = usersService.get(id);
        if (user == null) {
            return new ErrorResponse(Response.Status.CONFLICT,
                    String.format("User with id %d doesn't exist", id)).toResponse();
        }

        User updatedUser = user.toBuilder().build();
        for (Account account : usersService.get(id).getAccounts()) {
            if (number.equals(account.getNumber())) {
                updatedUser.getAccounts().remove(account);
                if (usersService.update(user.getId(), updatedUser)) {
                    return Response.status(Response.Status.OK).build();
                } else {
                    return new ErrorResponse(Response.Status.CONFLICT,
                            String.format("User with id %d doesn't exist", id)).toResponse();
                }
            }
        }
        return new ErrorResponse(Response.Status.CONFLICT,
                String.format("User with id %d does not have an account with number %s", id, number))
                .toResponse();
    }

    @DELETE
    public Response deleteAccounts(@PathParam("id") Long id) {
        usersService.get(id).getAccounts().clear();
        return Response.status(Response.Status.OK).build();
    }
}
