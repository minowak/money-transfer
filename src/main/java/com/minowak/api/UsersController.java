package com.minowak.api;

import com.google.common.collect.Sets;
import com.minowak.model.Account;
import com.minowak.model.User;
import com.minowak.service.AccountsService;
import com.minowak.service.UsersService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/user")
public class UsersController {
    private final UsersService usersService = UsersService.getInstance();
    private final AccountsService accountsService = AccountsService.getInstance();

    @GET
    @Path("{id}/account")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Account> getAccounts(@PathParam("id") Long id) {
        Set<Account> userAccounts = Sets.newHashSet();
        for (Account account : accountsService.get()) {
            if (account.getUserId().equals(id)) {
                userAccounts.add(account);
            }
        }
        return userAccounts;
    }

    @DELETE
    @Path("{id}/account")
    public Response deleteAccounts(@PathParam("id") Long id) {
        Set<Account> toDelete = Sets.newHashSet();
        for (Account account : accountsService.get()) {
            if (account.getUserId().equals(id)) {
                toDelete.add(account);
            }
        }
        toDelete.forEach(account -> accountsService.delete(account.getNumber()));
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
        return (usersService.delete() && accountsService.delete()) ? Response.status(Response.Status.GONE).build()
                : Response.serverError().build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        if (usersService.delete(id)) {
            Set<Account> toDelete = accountsService.get().stream().filter(a -> a.getUserId().equals(id))
                    .collect(Collectors.toSet());
            toDelete.forEach(a -> accountsService.delete(a.getNumber()));
            return Response.status(Response.Status.GONE).build();
        }
        return Response.serverError().build();
    }

}
