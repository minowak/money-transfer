package com.minowak.api;

import com.minowak.model.Account;
import com.minowak.model.User;
import com.minowak.service.UsersService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Set;

@Path("/user")
public class UsersController {
    private final UsersService usersService = UsersService.getInstance();

    // TODO update account

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

    @PUT
    @Path("{id}/account/{number}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAccount(Account account, @PathParam("id") Long id, @PathParam("number") String number) {
        User user = usersService.get(id);
        Account accountBefore = user.getAccounts().stream()
                .filter(a -> a.getNumber().equals(number))
                .findFirst()
                .orElse(null);

        if (accountBefore == null) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        user.getAccounts().remove(accountBefore);
        user.getAccounts().add(account);

        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("{id}/account")
    public Response deleteAccounts(@PathParam("id") Long id) {
        usersService.get(id).getAccounts().clear();
        return Response.status(Response.Status.GONE).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<User> getUsers() {
        return usersService.get();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("id") Long id) {
        return usersService.get(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(User user) {
        boolean created = usersService.add(user);
        return created ? Response.status(Response.Status.CREATED).build()
                : Response.status(Response.Status.CONFLICT).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(User user, @PathParam("id") Long id) {
        usersService.update(id, user);
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    public Response deleteAllUsers() {
        return usersService.delete() ? Response.status(Response.Status.GONE).build()
                : Response.serverError().build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        return usersService.delete(id) ? Response.status(Response.Status.GONE).build() : Response.serverError().build();
    }

}
