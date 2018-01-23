package com.ratpack.handler;

import org.apache.log4j.Logger;

import com.aerospike.client.Key;
import com.aerospike.client.async.NioEventLoops;
import com.aerospike.client.policy.Policy;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ratpack.database.DBClient;

import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.Handler;

/**
 * Handles /balance request
 * 
 * @author itsvik
 *
 */
@Singleton
public class BalanceHandler implements Handler {
	Logger logger = Logger.getLogger(BalanceHandler.class);
	private final DBClient dbClient;
	
	@Inject
	public BalanceHandler(DBClient dbClient) {
		this.dbClient = dbClient;
	}


	@Override
	public void handle(Context ctx) throws Exception {
		ctx.render(getBalance(ctx));
	}

	private Promise<String> getBalance(Context ctx) {
		return dbClient.asyncGetString(new Policy(),
				new Key("MIR", "users", ctx.getRequest().getHeaders().get("Authorization")), "info");
	}

}
