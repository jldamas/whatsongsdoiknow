package com.ir.whatsongsdoiknow.server;

import ir.DocumentIndex;
import ir.artist.ArtistIndex;
import ir.chord.ChordDocumentIndex;
import ir.chordgram.ChordGramDocumentIndex;
import ir.chordprogression.ChordProgressionDocumentIndex;
import ir.song.SongIndex;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.repackaged.org.apache.commons.logging.Log;
import com.google.appengine.repackaged.org.apache.commons.logging.LogFactory;

public class IndexRepository
{
	protected final Log logger = LogFactory.getLog(getClass());

	private SongIndex songIndex;
	private ArtistIndex artistIndex;
	private ChordDocumentIndex chordIndex;
	private ChordProgressionDocumentIndex chordProgressionIndex;
	private ChordGramDocumentIndex chordGramIndex;

	private boolean reIndex = false;

	public IndexRepository()
	{
	}

	// TODO: Change this so it just reads the dictionary and inverted files
	// and have another process actually create them (i.e. cron, driver, etc.)
	public void init()
	{
//		if (reIndex)
//		{
//			List<CreateIndexThread> threads = new ArrayList<CreateIndexThread>();
//			CreateIndexThread song = new CreateIndexThread(songIndex, "Song Index");
//			song.start();
//			CreateIndexThread artist = new CreateIndexThread(artistIndex, "Artist Index");
//			artist.start();
//			CreateIndexThread chord = new CreateIndexThread(chordIndex, "Chord Index");
//			chord.start();
//			CreateIndexThread pro = new CreateIndexThread(chordProgressionIndex, "Chord Progression Index");
//			pro.start();
//			CreateIndexThread gram = new CreateIndexThread(chordGramIndex, "Chord Gram Index");
//			gram.start();
//
//			try
//			{
//				song.join();
//				artist.join();
//				chord.join();
//				pro.join();
//				gram.join();
//			}
//			catch (InterruptedException e1)
//			{
//				e1.printStackTrace();
//			}
//		}
//		else
//		{
//			ReadIndexThread song = new ReadIndexThread(songIndex, "Song Index");
//			song.start();
//			ReadIndexThread artist = new ReadIndexThread(artistIndex, "Artist Index");
//			artist.start();
//			ReadIndexThread chord = new ReadIndexThread(chordIndex, "Chord Index");
//			chord.start();
//			ReadIndexThread pro = new ReadIndexThread(chordProgressionIndex, "Chord Progression Index");
//			pro.start();
//			ReadIndexThread gram = new ReadIndexThread(chordGramIndex, "Chord Gram Index");
//			gram.start();

//			try
//			{
//				song.join();
//				artist.join();
//				chord.join();
//				pro.join();
//				gram.join();
//			}
//			catch (InterruptedException e1)
//			{
//				e1.printStackTrace();
//			}
//		}
		
		
		artistIndex = new ArtistIndex("Artist Index", "full_artist.dic", "full_artist.invf", "full_artist_doc_pos.txt");
		artistIndex.readDictionaryAndInvertedFile();
		logger.info("Done initializing all indexes");
	}

	class CreateIndexThread extends Thread
	{
		private DocumentIndex index;
		private String indexName;

		public CreateIndexThread(DocumentIndex index, String indexName)
		{
			super();
			this.index = index;
			this.indexName = indexName;
		}

		public void run()
		{
			logger.info("Started writing " + indexName);
			index.writeDictionaryAndInvertedFile();
			logger.info("Done writing " + indexName);
		}
	}

	class ReadIndexThread extends Thread
	{
		private DocumentIndex index;
		private String indexName;

		public ReadIndexThread(DocumentIndex index, String indexName)
		{
			super();
			this.index = index;
			this.indexName = indexName;
		}

		public void run()
		{
			index.readDictionaryAndInvertedFile();
			logger.info("Done reading " + indexName);
		}
	}

	/**
	 * @return the songIndex
	 */
	public SongIndex getSongIndex()
	{
		return songIndex;
	}

	/**
	 * @param songIndex
	 *            the songIndex to set
	 */
	public void setSongIndex(SongIndex songIndex)
	{
		this.songIndex = songIndex;
	}

	/**
	 * @return the artistIndex
	 */
	public ArtistIndex getArtistIndex()
	{
		return artistIndex;
	}

	/**
	 * @param artistIndex
	 *            the artistIndex to set
	 */
	public void setArtistIndex(ArtistIndex artistIndex)
	{
		this.artistIndex = artistIndex;
	}

	/**
	 * @return the chordIndex
	 */
	public ChordDocumentIndex getChordIndex()
	{
		return chordIndex;
	}

	/**
	 * @param chordIndex
	 *            the chordIndex to set
	 */
	public void setChordIndex(ChordDocumentIndex chordIndex)
	{
		this.chordIndex = chordIndex;
	}

	/**
	 * @return the chordProgressionIndex
	 */
	public ChordProgressionDocumentIndex getChordProgressionIndex()
	{
		return chordProgressionIndex;
	}

	/**
	 * @param chordProgressionIndex
	 *            the chordProgressionIndex to set
	 */
	public void setChordProgressionIndex(ChordProgressionDocumentIndex chordProgressionIndex)
	{
		this.chordProgressionIndex = chordProgressionIndex;
	}

	/**
	 * @return the chordGramIndex
	 */
	public ChordGramDocumentIndex getChordGramIndex()
	{
		return chordGramIndex;
	}

	/**
	 * @param chordGramIndex
	 *            the chordGramIndex to set
	 */
	public void setChordGramIndex(ChordGramDocumentIndex chordGramIndex)
	{
		this.chordGramIndex = chordGramIndex;
	}

	/**
	 * @return the reIndex
	 */
	public boolean isReIndex()
	{
		return reIndex;
	}

	/**
	 * @param reIndex
	 *            the reIndex to set
	 */
	public void setReIndex(boolean reIndex)
	{
		this.reIndex = reIndex;
	}
}
