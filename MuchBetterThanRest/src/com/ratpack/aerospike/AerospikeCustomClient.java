package com.ratpack.aerospike;

import java.util.List;

import org.apache.log4j.Logger;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Bin;
import com.aerospike.client.Host;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.async.EventLoop;
import com.aerospike.client.async.EventLoops;
import com.aerospike.client.listener.RecordListener;
import com.aerospike.client.listener.WriteListener;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.client.policy.Policy;
import com.aerospike.client.policy.WritePolicy;
import com.google.gson.Gson;
import com.ratpack.database.DBClient;

import ratpack.exec.Promise;

/**
 * {@link AerospikeClient} Implementation of {@link DBClient}
 * 
 * @author itsvik
 *
 */
public class AerospikeCustomClient extends AerospikeClient implements DBClient {

	Logger logger = Logger.getLogger(AerospikeCustomClient.class);
	private EventLoops eventLoops;

	public AerospikeCustomClient(ClientPolicy policy, Host host) {
		super(policy, host);
		eventLoops = policy.eventLoops;
	}

	@Override
	public Promise<String> asyncGetString(Policy policy, Key key, String bin) {
		EventLoop eventLoop = eventLoops.next();
		logger.info("asyncGetString : EventLoop : " + eventLoop + ", WritePolicy : " + new Gson().toJson(policy) + "Key"
				+ new Gson().toJson(key) + "Bins" + new Gson().toJson(bin));
		return Promise.<String>async(downstream -> get(eventLoop, new RecordListener() {
			@Override
			public void onSuccess(Key key, Record record) {
				logger.info("asyncGetString - Success : " + new Gson().toJson(record));
				if (record == null)
					downstream.error(new Exception("Record not found"));
				else
					downstream.success(record.getString(bin));
			}

			@Override
			public void onFailure(AerospikeException exception) {
				downstream.error(exception);
			}
		}, policy, key, bin));
	}

	@Override
	public Promise<Object> asyncGet(Policy policy, Key key, String bin) {
		EventLoop eventLoop = eventLoops.next();
		return Promise.<Object>async(downstream -> get(eventLoop, new RecordListener() {
			@Override
			public void onSuccess(Key key, Record record) {
				downstream.success(record.getValue(bin));
			}

			@Override
			public void onFailure(AerospikeException exception) {
				downstream.error(exception);
			}
		}, policy, key, bin));
	}

	@Override
	public Promise<Key> asyncPut( WritePolicy policy, Key key, Bin... bins) {
		EventLoop eventLoop = eventLoops.next();
		logger.info("asyncPut : EventLoop : " + eventLoop + ", WritePolicy : " + new Gson().toJson(policy) + "Key"
				+ new Gson().toJson(key) + "Bins" + new Gson().toJson(bins));
		return Promise.async(downstream -> put(eventLoop, new WriteListener() {
			@Override
			public void onFailure(AerospikeException exception) {
				logger.info("asyncPut - Failure : " + new Gson().toJson(exception));
				downstream.error(exception);
			}

			@Override
			public void onSuccess(Key key) {
				logger.info("asyncPut - Success : " + new Gson().toJson(key));
				downstream.success(key);
			}
		}, policy, key, bins));
	}

	@Override
	public void asyncDelete(Key key) {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean authenticate(String authenticationToken) {
		Key key = new Key("MIR", "users", authenticationToken);
		Record record = get(null, key);
		return (record != null && record.getInt("active") == 1) ? true : false;
	}

	@Override
	public Promise<List> asyncGetList( Policy policy, Key key, String bin) {
		EventLoop eventLoop = eventLoops.next();
		logger.info("asyncGetList : EventLoop : " + eventLoop + ", WritePolicy : " + new Gson().toJson(policy) + "Key"
				+ new Gson().toJson(key) + "Bins" + new Gson().toJson(bin));
		return Promise.<List>async(downstream -> get(eventLoop, new RecordListener() {
			@Override
			public void onSuccess(Key key, Record record) {
				logger.info("asyncGetList - Success : " + new Gson().toJson(record));
				downstream.success(record.getList(bin));
			}

			@Override
			public void onFailure(AerospikeException exception) {
				downstream.error(exception);
			}
		}, policy, key, bin));
	}

}
