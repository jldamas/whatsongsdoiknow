package com.ir.whatsongsdoiknow.server;

import ir.artist.ArtistIndex;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ir.whatsongsdoiknow.client.ArtistSearchService;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ArtistSearchServiceImpl extends RemoteServiceServlet implements ArtistSearchService
{
	
	public String searchArtist(String input)
	{
		File f = new File("blah.txt");
		try
		{
			System.out.println(f.getCanonicalPath());
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String path = "/WEB-INF/indexes/full_artist.dic";
		System.out.println("DicNAME@:  " + path);
		System.out.println("reesoura@:  " + getServletContext().getResourceAsStream(path));
		System.out.println("context path@:  " + getServletContext().getServletContextName());
		
		ArtistIndex artistIndex = new ArtistIndex("Artist Index", "full_artist.dic", "full_artist.invf", "full_artist_doc_pos.txt");
		artistIndex.readDictionaryAndInvertedFile();
		
		//TODO: have the Indexes take a file or stream instead of the name
		return getServletContext().getResourceAsStream(path).toString();
		
		
//		IndexRepository ir = new IndexRepository();
//		ir.init();
//		
//		List<ChordFileResult> results = new ArrayList<ChordFileResult>();
//
//		Query query = new Query(input, ir
//				.getArtistIndex(), 10);
//
//		StringBuffer sb = new StringBuffer();
//		
//		for (Report report : ir.getArtistIndex().search(query)) {
//			sb.append(report.getChordFileResult().getArtistName());
//			results.add(report.getChordFileResult());
//		}
	
//		String serverInfo = getServletContext().getServerInfo();
//		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
//		return "Hello, " + input + "!<br><br>I am running " + serverInfo + ".<br><br>It looks like you are using:<br>" + userAgent;
//		return sb.toString();
	}
}
