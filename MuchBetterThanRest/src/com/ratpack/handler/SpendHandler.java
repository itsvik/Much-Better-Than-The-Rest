/**
 * 
 */
package com.ratpack.handler;

import static ratpack.jackson.Jackson.fromJson;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.async.NioEventLoops;
import com.aerospike.client.policy.GenerationPolicy;
import com.aerospike.client.policy.Policy;
import com.aerospike.client.policy.WritePolicy;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ratpack.database.DBClient;
import com.ratpack.model.Transection;
import com.ratpack.model.User;

import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.Handler;

/**
 * Handling /spend request
 * 
 * @author itsvik
 *
 */
@Singleton
public class SpendHandler implements Handler {

	static final Logger logger = Logger.getLogger(SpendHandler.class);
	private final DBClient dbClient;
	
	@Inject
	public SpendHandler(DBClient dbClient) {
		this.dbClient = dbClient;
	}

	
	@Override
	public void handle(Context ctx) throws Exception {
		ctx.parse(fromJson(Transection.class)).then(t -> {
			final String token = ctx.getRequest().getHeaders().get("Authorization");
			// Deducting spend amount from balance
			dbClient.asyncGetString(new Policy(), new Key("MIR", "users", token),
					"info").then(value -> {
						logger.info(new Gson().toJson(value));
						getExchangeRate(ctx, t.getCurrency()).onError(k->ctx.getResponse().send("Currency not supported")).then(r -> {
							logger.info("Rate : " + new Gson().toJson(r));
							User userInfo = new Gson().fromJson(value, User.class);
							Double inrEquivalant = t.getAmount() * Double.parseDouble(r);
							if (userInfo.getBalance() < inrEquivalant) {
								ctx.getResponse().send("User does not have sufficient balance.");
								return;
							}
							userInfo.setBalance(userInfo.getBalance() - inrEquivalant);
							WritePolicy writePolicy = new WritePolicy();
							writePolicy.generationPolicy = GenerationPolicy.NONE;
							dbClient.asyncPut( writePolicy,
									new Key("MIR", "users", token), new Bin("info", new Gson().toJson(userInfo)))
									.then(u -> {
										logger.info(u.userKey);
										// Getting transaction list from Transactions.transactionL and adding new
										// transaction
										dbClient.asyncGetString(new Policy(),
												new Key("MIR", "transactions", token), "transactionL").then(transaction -> {
													logger.info(new Gson().toJson(transaction));
													List<Transection> transactionsList = new ArrayList<>();
													if (transaction != null && !transaction.isEmpty()) {
														List json = new Gson().fromJson(transaction, List.class);
														transactionsList.addAll(json);
													}
													transactionsList.add(t);
													Bin transactionBin = new Bin("transactionL",
															new Gson().toJson(transactionsList));
													writePolicy.generationPolicy = GenerationPolicy.NONE;
													dbClient.asyncPut( writePolicy,
															new Key("MIR", "transactions", token), transactionBin)
															.then(tr -> {
																logger.info(tr.userKey);
																ctx.getResponse().send("Success");
															});
												});
									});
						});
					});

		});

	}
	/**
	 * Get Exchange rate of provided currency
	 * @param ctx
	 * @param currency
	 * @return
	 */
	private Promise<String> getExchangeRate(Context ctx, String currency) {
		String bin = "rate";
		DBClient dbClient = ctx.get(DBClient.class);
		Key key = new Key("MIR", "exchange", currency.toUpperCase());
		return dbClient.asyncGetString(new Policy(), key, bin).onNull(()->Promise.value(null));
	}

}
