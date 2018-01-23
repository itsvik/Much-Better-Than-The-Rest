package com.ratpack.database;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.async.EventLoop;
import com.aerospike.client.policy.Policy;
import com.aerospike.client.policy.WritePolicy;

import ratpack.exec.Promise;
@Service
public interface DBClient {
	/**
	 * Whether authentication token provided is valid or not
	 * @param authenticationToken
	 * @return
	 */
	public Boolean authenticate(String authenticationToken);
	/**
	 * TODO
	 * @param key
	 */
	void asyncDelete(Key key);
	/**
	 * Asynchronously read record header and bins for specified key.
	 * This method registers the command with an event loop and returns.
	 * The event loop thread will process the command and returns {@link Record} as Promise.
	 * @param eventLoop
	 * @param policy
	 * @param key
	 * @param bin
	 * @return
	 */
	public Promise<String> asyncGetString(Policy policy, Key key, String bin);
	/**
	 * Asynchronously write record bin(s). 
	 * This method registers the command with an event loop and returns.
	 * The event loop thread will process the command and returns {@link Key} as Promise.
	 * @param eventLoop
	 * @param policy
	 * @param key
	 * @param bins
	 * @return
	 */
	public Promise<Key> asyncPut(WritePolicy policy, Key key, Bin... bins);
	/**
	 * Asynchronously write record bin(s). 
	 * This method registers the command with an event loop and returns.
	 * The event loop thread will process the command and returns {@link Record} as Promise.
	 * @param eventLoop
	 * @param policy
	 * @param key
	 * @param bin
	 * @return
	 */
	public Promise<Object> asyncGet(Policy policy, Key key, String bin);
	/**
	 * Asynchronously write record bin(s). 
	 * This method registers the command with an event loop and returns.
	 * The event loop thread will process the command and returns {@link List} of {@link Record} as Promise.
	 * @param eventLoop
	 * @param policy
	 * @param key
	 * @param bin
	 * @return
	 */
	public Promise<List> asyncGetList(Policy policy, Key key, String bin);
}
