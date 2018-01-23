/**
 * 
 */
package com.ratpack.handler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ratpack.database.DBClient;

import ratpack.handling.Context;
import ratpack.handling.Handler;

/**
 * Handles authentication for the various requests
 * 
 * @author itsvik
 *
 */
@Singleton
public class AuthenticationHandler implements Handler {
	
	private final DBClient dbClient;
	
	@Inject
	public AuthenticationHandler(DBClient dbClient) {
		this.dbClient = dbClient;
	}



	@Override
	public void handle(Context ctx) throws Exception {
		String token = ctx.getRequest().getHeaders().get("Authorization");
		if (ctx.getRequest().getPath().endsWith("login")) {
			ctx.next();
		}
		else if (token != null && !token.isEmpty()) {
			Boolean authenticate = dbClient.authenticate(token);
			if (authenticate) {
				ctx.next();
			} else {
				ctx.getResponse().send("Invalid authorizaton header");
			}
		} else {
			ctx.getResponse().send("Please provide authorizaton header");
		}
	}

}
