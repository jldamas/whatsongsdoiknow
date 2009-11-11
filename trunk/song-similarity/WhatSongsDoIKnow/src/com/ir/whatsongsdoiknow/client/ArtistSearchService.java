package com.ir.whatsongsdoiknow.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("ArtistSearch")
public interface ArtistSearchService extends RemoteService
{
	String searchArtist(String name);
}
