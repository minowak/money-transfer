package com.minowak;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.ws.rs.core.Response;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private Response.Status status;
    private String message;

    public Response toResponse() {
        return Response.status(status).type("application/json").entity(this).build();
    }
}
