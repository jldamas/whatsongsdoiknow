package com.ir.whatsongsdoiknow.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface ArtistSearchServiceAsync
{
	void searchArtist(String input, AsyncCallback<String> callback);
}
