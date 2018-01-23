package com.ratpack.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.async.NioEventLoops;
import com.aerospike.client.policy.WritePolicy;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ratpack.database.DBClient;
import com.ratpack.model.Transection;
import com.ratpack.model.User;

import ratpack.handling.Context;
import ratpack.handling.Handler;

/**
 * Handles /login request and provides a new authentication token
 * for further requests by setting up a new account with balance 400.00 INR
 * 
 * @author itsvik
 *
 */
@Singleton
public class LoginHandler implements Handler {

	Logger logger = Logger.getLogger(LoginHandler.class);
	private final DBClient dbClient;
	
	@Inject
	public LoginHandler(DBClient dbClient) {
		this.dbClient = dbClient;
	}


	@Override
	public void handle(Context ctx) throws Exception {
		ctx.render(createLogin(ctx));
	}

	private String createLogin(Context ctx) {
		final String token = System.currentTimeMillis() + "" + (new Random(System.currentTimeMillis())).nextLong();
		List<Transection> transactionList = new ArrayList<>();
		dbClient.asyncPut( new WritePolicy(), new Key("MIR", "transactions", token),
				new Bin("transactionL", new Gson().toJson(transactionList))).then(u -> {
					dbClient.asyncPut( new WritePolicy(),
							new Key("MIR", "users", token), new Bin("info", new Gson().toJson(new User(400.00, "INR"))),
							new Bin("active", true)).then(t -> {
							});
					;
				});
		return token;
	}
}
