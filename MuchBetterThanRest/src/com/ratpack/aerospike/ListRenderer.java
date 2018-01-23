package com.ratpack.aerospike;

import java.util.List;

import com.google.gson.Gson;

import ratpack.handling.Context;
import ratpack.render.RendererSupport;

public class ListRenderer extends RendererSupport<List> {

	@Override
	public void render(Context ctx, List list) throws Exception {
		ctx.getResponse().status(202).send(new Gson().toJson(list));
	}

}
