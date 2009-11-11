package ir.chord;

import ir.DictionaryEntry;
import ir.DocumentIndex;
import ir.Query;
import ir.Stat;
import ir.Util;
import ir.report.Report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChordDocumentReport extends Report
{
	public ChordDocumentReport() { }
	
	public ChordDocumentReport(int docId, String inputFile, long fileOffSet, DocumentIndex index, Query q)
	{
		super(docId, inputFile, fileOffSet, index, q);
	}

	protected void parseDocument()
	{
		Pattern paraPat = Pattern.compile(Util.DOC_START_REGEX);
		
		try
		{
			File file = new File(index.getFileMgmt().getFullPath(inputFile));
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			raf.skipBytes((int) fileOffSet);
			String line = raf.readLine();

			StringBuffer sBuf = new StringBuffer("");
			// read in the document
			while (!line.equalsIgnoreCase("</S>"))
			{
				// check if it's a new paragraph
				Matcher m = paraPat.matcher(line);
				if (m.matches())
				{
					chordFileResult.setDocId(Integer.parseInt(m.group(2)));
					chordFileResult.setSongName(m.group(4));
					chordFileResult.setArtistName(m.group(6));
				}
				else
				{
					sBuf.append(line + " ");
				}
				line = raf.readLine();
			}
			
			rawDocument.append(sBuf.toString() + "\n");
			// regular expression for filtering out punctuation
			String normDoc = index.normalizeLine(sBuf.toString());
			StringTokenizer st = new StringTokenizer(normDoc);
			// go through each token (space delimited)
			while (st.hasMoreTokens())
			{
				// make everything upper case
				String term = st.nextToken().trim();

				if (docTerms.containsKey(term))
				{
					docTerms.get(term).addOccurrance();
				}
				else
				{
					DictionaryEntry dentry = index.findDictionaryEntry(term);
					docTerms.put(term, new Stat(dentry));
				}
			}
			
			chordFileResult.setScore(getSim());

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
	}
}
