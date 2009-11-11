package ir;

import ir.report.Report;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * When run this will read in the desired file. And error check with the desired
 * dictionary file. It will make an inverted file for look up and an array list
 * in order to sort the TermStat objects.
 * 
 * @author James Damaska
 * 
 */
public abstract class DocumentIndex
{
	
	protected ArrayList<DictionaryEntry> dictionary = new ArrayList<DictionaryEntry>();
	protected HashMap<String, DictionaryEntry> dictionaryHash = new HashMap<String, DictionaryEntry>();
	protected ArrayList<Integer> linesOfInvFile = new ArrayList<Integer>();
	protected HashMap<Integer, DocInfo> documentLocations = new HashMap<Integer, DocInfo>();
	protected String invFileName;
	protected String dicFileName;
	protected String inputFileName;
	protected String docPosFileName;

	protected HashMap<String, PostingEntry> tempInvFile = new HashMap<String, PostingEntry>();
	protected int numDocumentsProcessed = 0;
	protected int numDistinctWords = 0;
	protected int totalNumWords = 0;
	
	private FileManagement fileMgmt = new FileManagement();
	protected Util util;
	
	public DocumentIndex() { }
	
	public DocumentIndex(String input, String dic, String inv, String docPos)
	{
		fileMgmt.setFileRoot("/WEB-INF/indexes");
		inputFileName = input;
		dicFileName = dic;
		invFileName = inv;
		docPosFileName = docPos;
	}

	public String getDocument(int id)
	{
		StringBuffer rawDocument = new StringBuffer("");
		long fileOffSet = documentLocations.get(id).getDocumentOffset();

		try
		{
			File file = new File(fileMgmt.getFullPath(inputFileName));
			RandomAccessFile raf;
			
			raf = new RandomAccessFile(file, "r");
			raf.skipBytes((int) fileOffSet);
			String line = raf.readLine();

			// read in the document
			while (!line.equalsIgnoreCase("</S>"))
			{
				rawDocument.append(line + "\n");
				line = raf.readLine();
			}

			raf.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return rawDocument.toString();
	}

	public abstract List<Report> search(Query q);

	public PostingEntry findWord(String term, boolean printPostingsList)
	{
		DictionaryEntry dicEntry = findDictionaryEntry(term);
		if (dicEntry != null)
		{
			PostingEntry entry = getPostingList(Integer.parseInt("" + dicEntry.getTermId()));
			if (printPostingsList)
			{
				System.out.println("'" + term + "': \n  Number Of Documents: \t" + dicEntry.getNumberOfDocs() + "\n  Total Frequency: \t"
						+ dicEntry.getTotalFreq() + "\n  File offset: \t" + dicEntry.getPostingFileOffset());
				System.out.println("  Postings List: \n" + entry.toStringHumanFriendly());
			}
			return entry;
		}
		else
			return null;
	}

	public DictionaryEntry findDictionaryEntry(String term)
	{
		return dictionaryHash.get(term);
	}

	protected PostingEntry getPostingList(int id)
	{
		try
		{
			File file = new File(fileMgmt.getFullPath(invFileName));
			RandomAccessFile raf;
			
			raf = new RandomAccessFile(file, "r");
			raf.skipBytes((int) dictionary.get(id).getPostingFileOffset());
			String line = raf.readLine();

			String[] dicIDAndDocs = line.split("\\|");

			String dicId = dicIDAndDocs[0];
			String occurances = dicIDAndDocs[1];

			String[] docAndPositions = occurances.split(">;");
			HashMap<Integer, ArrayList<Integer>> tempHash = new HashMap<Integer, ArrayList<Integer>>();

			for (int i = 0; i < docAndPositions.length; i++)
			{
				String[] docIDAndPositions = docAndPositions[i].split(":<");
				String docID = docIDAndPositions[0];

				String[] positions = docIDAndPositions[1].split(",");
				ArrayList<Integer> temp = new ArrayList<Integer>();
				for (int j = 0; j < positions.length; j++)
				{
					temp.add(Integer.parseInt(positions[j]));
				}

				tempHash.put(Integer.parseInt(docID), temp);
			}

			raf.close();
			return new PostingEntry(Long.parseLong(dicId), tempHash);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	protected double calculateIDF(PostingEntry entry)
	{
		double idf = 0;
		idf = log2(numDocumentsProcessed / (entry.getNumDocs() * 1.0));
		return idf;
	}

	public double log2(double num)
	{
		return (Math.log(num) / Math.log(2));
	}

	public abstract void parseDocumentFile(ArrayList<PostingEntry> invFile);

	public abstract String normalizeLine(String line);

	
	
	
	public void readDictionaryAndInvertedFile()
	{
		try
		{
		
			System.out.println("DicNAME@:  " + fileMgmt.getFullPath(dicFileName));
			System.out.println("res@:  " + Class.class.getResource(fileMgmt.getFullPath(dicFileName)));
			
			BufferedReader br = null;
			try
			{
				br = new BufferedReader(new FileReader(new File(Class.class.getResource(fileMgmt.getFullPath(dicFileName)).toURI())));
			}
			catch (URISyntaxException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//BufferedReader br = new BufferedReader(new FileReader(fileMgmt.getFullPath(dicFileName)));
			// should be the file description
			br.readLine();// should be format
			String line = br.readLine();// the stats for the file
			String[] stats = line.split(":");
			numDocumentsProcessed = Integer.parseInt(stats[0]);
			numDistinctWords = Integer.parseInt(stats[1]);
			totalNumWords = Integer.parseInt(stats[2]);

			br.readLine();// should be format

			line = br.readLine();
			while (line != null)
			{
				String[] entry = line.split(":");
				// term, id, number of documents its in, total frequency in the
				// entire corpus
				DictionaryEntry de = new DictionaryEntry(Long.valueOf(entry[0]), entry[1], Long.valueOf(entry[2]), Long.valueOf(entry[3]),
						Long.valueOf(entry[4]), Double.valueOf(entry[5]));
				dictionary.add(de);
				line = br.readLine();
			}

			for (DictionaryEntry dicEntry : dictionary)
			{
				dictionaryHash.put(dicEntry.getTerm(), dicEntry);
			}

			br.close();

			BufferedReader br2 = new BufferedReader(new FileReader(fileMgmt.getFullPath(docPosFileName)));
			br2.readLine();// should be format
			line = br2.readLine();
			while (line != null)
			{
				String[] pos = line.split(":");
				documentLocations.put(Integer.parseInt(pos[0]), new DocInfo(Long.parseLong(pos[1]), pos[2]));
				line = br2.readLine();
			}
			br2.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	protected void addTerm(String term, int currDocument, int position)
	{
		totalNumWords++;
		if (tempInvFile.containsKey(term))
		{
			tempInvFile.get(term).addOccurance(currDocument, position);
		}
		else
		{
			tempInvFile.put(term, new PostingEntry(currDocument, position));
		}
	}

	public void writeDictionaryAndInvertedFile()
	{
		ArrayList<PostingEntry> invFile = new ArrayList<PostingEntry>();
		
		parseDocumentFile(invFile);
		
		// sort the inverted file based on ids
		Collections.sort(invFile, new Comparator<PostingEntry>()
		{
			public int compare(PostingEntry o1, PostingEntry o2)
			{
				return (int) (o1.getId() - o2.getId());
			}
		});

		try
		{
			// write inverted file
			String lineSeperator = System.getProperty("line.separator");
			// write inverted file
			new File(fileMgmt.getFullPath(invFileName)).delete();
			RandomAccessFile raf = new RandomAccessFile(new File(fileMgmt.getFullPath(invFileName)), "rw");
			raf.writeBytes("ID|DOC#:<pos,pos,...,pos>;...;DOC#:<pos,pos,...,pos>;" + lineSeperator);
			for (PostingEntry entry : invFile)
			{
				raf.writeBytes(entry.toString() + lineSeperator);
			}
			raf.close();

			// add in the file pointers to line numbers
			RandomAccessFile invRAF = new RandomAccessFile(new File(fileMgmt.getFullPath(invFileName)), "r");
			// should be document description
			String invLine = invRAF.readLine();// line 0
			long pointer = invRAF.getFilePointer();
			invLine = invRAF.readLine();// line 1
			int lineNumber = 1;
			while (invLine != null)
			{
				dictionary.get(lineNumber - 1).setPostingFileOffset(pointer);
				pointer = invRAF.getFilePointer();
				invLine = invRAF.readLine();
				lineNumber++;
			}
			invRAF.close();

			// write out dictionary
			new File(fileMgmt.getFullPath(dicFileName)).delete();
			RandomAccessFile dicRAF = new RandomAccessFile(new File(fileMgmt.getFullPath(dicFileName)), "rw");
			dicRAF.writeBytes("#DOCS:#DISTINCTWORDS:#TOTALWORDS" + lineSeperator + numDocumentsProcessed + ":" + numDistinctWords
					+ ":" + totalNumWords + lineSeperator);
			dicRAF.writeBytes("ID:TERM:#DOC:TOTFREQ:POSTINGFILEOFFSET:IDF" + lineSeperator);
			for (DictionaryEntry entry : dictionary)
			{
				dicRAF.writeBytes(entry.toString() + lineSeperator);
			}
			dicRAF.close();

			// write out doc pos name
			new File(fileMgmt.getFullPath(docPosFileName)).delete();
			RandomAccessFile docPos = new RandomAccessFile(new File(fileMgmt.getFullPath(docPosFileName)), "rw");
			docPos.writeBytes("DOC#:POSITION:<term, ..., term>" + lineSeperator);
			for (Integer docId : documentLocations.keySet())
			{
				docPos.writeBytes(docId + ":" + documentLocations.get(docId) + lineSeperator);
			}
			docPos.close();

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @return the invFileName
	 */
	public String getInvFileName() {
		return invFileName;
	}

	/**
	 * @param invFileName the invFileName to set
	 */
	public void setInvFileName(String invFileName) {
		this.invFileName = invFileName;
	}

	/**
	 * @return the dicFileName
	 */
	public String getDicFileName() {
		return dicFileName;
	}

	/**
	 * @param dicFileName the dicFileName to set
	 */
	public void setDicFileName(String dicFileName) {
		this.dicFileName = dicFileName;
	}

	/**
	 * @return the inputFileName
	 */
	public String getInputFileName() {
		return inputFileName;
	}

	/**
	 * @param inputFileName the inputFileName to set
	 */
	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
	}

	/**
	 * @return the docPosFileName
	 */
	public String getDocPosFileName() {
		return docPosFileName;
	}

	/**
	 * @param docPosFileName the docPosFileName to set
	 */
	public void setDocPosFileName(String docPosFileName) {
		this.docPosFileName = docPosFileName;
	}

	/**
	 * @return the fileMgmt
	 */
	public FileManagement getFileMgmt() {
		return fileMgmt;
	}

	/**
	 * @param fileMgmt the fileMgmt to set
	 */
	public void setFileMgmt(FileManagement fileMgmt) {
		this.fileMgmt = fileMgmt;
	}

	/**
	 * @return the util
	 */
	public Util getUtil() {
		return util;
	}

	/**
	 * @param util the util to set
	 */
	public void setUtil(Util util) {
		this.util = util;
	}
}
