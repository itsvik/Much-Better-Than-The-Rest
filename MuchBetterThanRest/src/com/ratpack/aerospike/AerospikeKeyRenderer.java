package com.ratpack.aerospike;

import com.aerospike.client.Key;

import ratpack.handling.Context;
import ratpack.render.RendererSupport;

public class AerospikeKeyRenderer extends RendererSupport<Key> {

	@Override
	public void render(Context ctx, Key key) throws Exception {
		ctx.getResponse().status(202).send(key.userKey.toString());
	}

}
