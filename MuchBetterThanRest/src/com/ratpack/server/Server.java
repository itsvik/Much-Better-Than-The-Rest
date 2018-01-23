/**
 * 
 */
package com.ratpack.server;

import com.aerospike.client.async.NioEventLoops;
import com.ratpack.aerospike.AerospikeKeyRenderer;
import com.ratpack.aerospike.ListRenderer;
import com.ratpack.handler.AuthenticationHandler;
import com.ratpack.handler.BalanceHandler;
import com.ratpack.handler.LoginHandler;
import com.ratpack.handler.ServiceModule;
import com.ratpack.handler.SpendHandler;
import com.ratpack.handler.TransactionsHandler;

import ratpack.guice.Guice;
import ratpack.registry.Registry;
import ratpack.server.RatpackServer;

/**
 * Ratpack {@link Server}<br/>
 * @author itsvik
 *
 */
public class Server {
	public static void main(String... args) throws Exception {
		
		RatpackServer.start(server -> server
				.registry(Guice.registry(services -> services.module(ServiceModule.class)))
				.handlers(chain -> chain.register(Registry.single(new AerospikeKeyRenderer()))
						.register(Registry.single(new ListRenderer()))
//						.register(Registry.builder().add(nioEventLoops).build())
						.all(AuthenticationHandler.class)
						.get("login", LoginHandler.class)
						.get("balance", BalanceHandler.class)
						.get("transactions", TransactionsHandler.class)
						.post("spend", SpendHandler.class)));
	}
}