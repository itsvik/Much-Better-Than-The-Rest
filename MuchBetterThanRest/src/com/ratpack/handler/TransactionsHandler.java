/**
 * 
 */
package com.ratpack.handler;

import org.apache.log4j.Logger;

import com.aerospike.client.Key;
import com.aerospike.client.async.NioEventLoops;
import com.aerospike.client.policy.Policy;
import com.ratpack.database.DBClient;

import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.Handler;

/**
 * @author itsvi
 *
 */
public class TransactionsHandler implements Handler {

	Logger logger = Logger.getLogger(TransactionsHandler.class);

	@Override
	public void handle(Context ctx) throws Exception {
		ctx.render(getTransactions(ctx));
	}

	private Promise<String> getTransactions(Context ctx) {
		DBClient client = ctx.get(DBClient.class);
		return client.asyncGetString( new Policy(),
				new Key("MIR", "transactions", ctx.getRequest().getHeaders().get("Authorization")), "transactionL");
	}

}
