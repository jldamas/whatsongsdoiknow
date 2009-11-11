package ir;

import ir.artist.ArtistIndex;
import ir.chord.ChordDocumentIndex;
import ir.chordgram.ChordGramDocumentIndex;
import ir.chordprogression.ChordProgressionDocumentIndex;
import ir.report.Report;
import ir.song.SongIndex;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class AllChordTest {
	@Autowired
	@Qualifier("chordIndexBean")
	private ChordDocumentIndex chordIndex;
	@Autowired
	@Qualifier("chordProgressionIndexBean")
	private ChordProgressionDocumentIndex chordProgressionIndex;
	@Autowired
	@Qualifier("chordGramIndexBean")
	private ChordGramDocumentIndex chordGramIndex;
	@Autowired
	@Qualifier("songIndexBean")
	private SongIndex songIndex;
	@Autowired
	@Qualifier("artistIndexBean")
	private ArtistIndex artistIndex;

	@Autowired
	@Qualifier("fullChordIndexBean")
	private ChordDocumentIndex fullChordIndex;
	@Autowired
	@Qualifier("fullChordProgressionIndexBean")
	private ChordProgressionDocumentIndex fullChordProgressionIndex;
	@Autowired
	@Qualifier("fullChordGramIndexBean")
	private ChordGramDocumentIndex fullChordGramIndex;
	@Autowired
	@Qualifier("fullSongIndexBean")
	private SongIndex fullSongIndex;
	@Autowired
	@Qualifier("fullArtistIndexBean")
	private ArtistIndex fullArtistIndex;

	@Test
	public void testTest() {
		chordIndex.writeDictionaryAndInvertedFile();
		chordIndex.readDictionaryAndInvertedFile();

		chordProgressionIndex.writeDictionaryAndInvertedFile();
		chordProgressionIndex.readDictionaryAndInvertedFile();

		chordGramIndex.writeDictionaryAndInvertedFile();
		chordGramIndex.readDictionaryAndInvertedFile();

		songIndex.writeDictionaryAndInvertedFile();
		songIndex.readDictionaryAndInvertedFile();

		artistIndex.writeDictionaryAndInvertedFile();
		artistIndex.readDictionaryAndInvertedFile();

		System.out.println("Song Index");
		runSubQuery(new Query("stones", artistIndex, 10));
		System.out.println("Artist Index");
		runSubQuery(new Query("the", songIndex, 10));

		String query = chordGramIndex.getDocument(1);

		System.out.println("Chord Index");
		runQuery(new Query(query, chordIndex, 10));
		System.out.println("Chord Gram Index");
		runQuery(new Query(query, chordGramIndex, 10));
		System.out.println("Chord Progression Index");
		runQuery(new Query(query, chordProgressionIndex, 10));
	}

//	@Test
	public void testFull() {
		fullChordIndex.writeDictionaryAndInvertedFile();
		fullChordIndex.readDictionaryAndInvertedFile();

		fullChordProgressionIndex.writeDictionaryAndInvertedFile();
		fullChordProgressionIndex.readDictionaryAndInvertedFile();

		fullChordGramIndex.writeDictionaryAndInvertedFile();
		fullChordGramIndex.readDictionaryAndInvertedFile();

		fullSongIndex.writeDictionaryAndInvertedFile();
		fullSongIndex.readDictionaryAndInvertedFile();

		fullArtistIndex.writeDictionaryAndInvertedFile();
		fullArtistIndex.readDictionaryAndInvertedFile();

		System.out.println("Song Index");
		runSubQuery(new Query("stones", fullArtistIndex, 10));
		System.out.println("Artist Index");
		runSubQuery(new Query("the", fullSongIndex, 10));

		String query = chordGramIndex.getDocument(1);

		System.out.println("Chord Index");
		runQuery(new Query(query, fullChordIndex, 10));
		System.out.println("Chord Gram Index");
		runQuery(new Query(query, fullChordGramIndex, 10));
		System.out.println("Chord Progression Index");
		runQuery(new Query(query, fullChordProgressionIndex, 10));
	}

	public static int runSubQuery(Query q) {
		int docID = 0;
		int count = 0;
		System.out.println("Query: " + q.getRawQuery());
		System.out.println("Rank\tDocID\tScore\t\t\tArtist\t\tSongName");
		for (Report dr : q.getIndex().search(q)) {
			String output = (count + 1) + "\t" + dr.getChordFileResult().getDocId() + "\t" + dr.getSim()
					+ "\t" + dr.getChordFileResult().getArtistName() + "\t" + dr.getChordFileResult().getSongName();
			System.out.println(output);
			docID = dr.getChordFileResult().getDocId();
			count++;
		}
		System.out.println("Query score: " + q.getSim() + "\n");
		return docID;
	}

	public static void runQuery(Query q) {
		int count = 0;
		System.out.println("Rank\tDocID\tScore\t\t\tArtist\t\tSongName");

		for (Report dr : q.getIndex().search(q)) {
			String output = (count + 1) + "\t" + dr.getChordFileResult().getDocId() + "\t" + dr.getSim()
					+ "\t" + dr.getChordFileResult().getArtistName() + "\t" + dr.getChordFileResult().getSongName();
			System.out.println(output);
			count++;
		}
		System.out.println("Query score: " + q.getSim() + "\n");
	}

	/**
	 * @return the chordIndex
	 */
	public ChordDocumentIndex getChordIndex() {
		return chordIndex;
	}

	/**
	 * @param chordIndex
	 *            the chordIndex to set
	 */
	public void setChordIndex(ChordDocumentIndex chordIndex) {
		this.chordIndex = chordIndex;
	}

	/**
	 * @return the chordProgressionIndex
	 */
	public ChordProgressionDocumentIndex getChordProgressionIndex() {
		return chordProgressionIndex;
	}

	/**
	 * @param chordProgressionIndex
	 *            the chordProgressionIndex to set
	 */
	public void setChordProgressionIndex(
			ChordProgressionDocumentIndex chordProgressionIndex) {
		this.chordProgressionIndex = chordProgressionIndex;
	}

	/**
	 * @return the chordGramIndex
	 */
	public ChordGramDocumentIndex getChordGramIndex() {
		return chordGramIndex;
	}

	/**
	 * @param chordGramIndex
	 *            the chordGramIndex to set
	 */
	public void setChordGramIndex(ChordGramDocumentIndex chordGramIndex) {
		this.chordGramIndex = chordGramIndex;
	}

	/**
	 * @return the songIndex
	 */
	public SongIndex getSongIndex() {
		return songIndex;
	}

	/**
	 * @param songIndex
	 *            the songIndex to set
	 */
	public void setSongIndex(SongIndex songIndex) {
		this.songIndex = songIndex;
	}

	/**
	 * @return the artistIndex
	 */
	public ArtistIndex getArtistIndex() {
		return artistIndex;
	}

	/**
	 * @param artistIndex
	 *            the artistIndex to set
	 */
	public void setArtistIndex(ArtistIndex artistIndex) {
		this.artistIndex = artistIndex;
	}

}
