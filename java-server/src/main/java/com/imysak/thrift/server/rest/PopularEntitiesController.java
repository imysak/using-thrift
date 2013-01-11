package com.imysak.thrift.server.rest;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Controller;

/**
 * REST resource.
 * @author Ihor Mysak (ihor.mysak@gmail.com))
 */
@Controller
@Path("")
@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" , "application/x-javascript; charset=UTF-8", "application/javascript; charset=UTF-8" })
public class PopularEntitiesController {

    // @Autowired
    // private AppFeedReviewService appFeedReviewService;
    //
    // /**
    // * Get most active users
    // * @return user id list. The HTTP response code is <code>200 OK</code>
    // */
    // @GET
    // @Path("")
    // public List<String> getPopularUsers(@QueryParam("offset") Integer offset, @QueryParam("size") Integer size) {
    // if (size == null || size < 1 || size > 100) {
    // size = 20;
    // }
    // if (offset == null || offset < 1) {
    // offset = 0;
    // }
    // // final String userId = AuthenticationManager.getUserUID();
    // final List<String> result = appFeedReviewService.getMostActiveUsers(offset, size);
    // return result;
    // }

}
