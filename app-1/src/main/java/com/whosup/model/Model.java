package com.whosup.model;

import java.util.UUID;

public abstract class Model {

	public static UUID getUUID()
	{
		return UUID.randomUUID();
	}

}
