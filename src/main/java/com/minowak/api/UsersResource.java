package com.minowak.api;

import com.minowak.ErrorResponse;
import com.minowak.model.User;
import com.minowak.service.UsersService;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@ManagedBean
@Path("/user")
public class UsersResource {

    private UsersService usersService;

    @Inject
    UsersResource(UsersService usersService) {
        this.usersService = usersService;
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
                : new ErrorResponse(Response.Status.CONFLICT,
                    String.format("User with id %d already exists", user.getId())).toResponse();
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
        return usersService.delete(id) ? Response.status(Response.Status.GONE).build()
                : new ErrorResponse(Response.Status.CONFLICT,
                    String.format("User with id %d does not exist", id)).toResponse();
    }

}
