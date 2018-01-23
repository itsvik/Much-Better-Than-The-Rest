package com.ratpack.handler;

import com.aerospike.client.Host;
import com.aerospike.client.async.NioEventLoops;
import com.aerospike.client.policy.ClientPolicy;
import com.google.inject.AbstractModule;
import com.ratpack.aerospike.AerospikeCustomClient;
import com.ratpack.database.DBClient;
import com.ratpack.server.Properties;

public class ServiceModule extends AbstractModule {
	
	@Override
    protected void configure() {
      bind(AuthenticationHandler.class);
      bind(BalanceHandler.class);
      bind(LoginHandler.class);
      bind(SpendHandler.class);
      bind(TransactionsHandler.class);
      ClientPolicy policy = new ClientPolicy();
      policy.eventLoops = new NioEventLoops(Properties.NO_THREADS);
	bind(DBClient.class).toInstance(new AerospikeCustomClient(policy, new Host(Properties.HOST, Properties.PORT)));
    }
  }