package ir.artist;

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

public class ArtistReport extends Report
{
	public ArtistReport() { }
	
	public ArtistReport(int docId, String inputFile, long fileOffSet, DocumentIndex index, Query q)
	{
		super(docId, inputFile, fileOffSet, index, q);
	}

	@Override
	protected void parseDocument()
	{
		Pattern paraPat = Pattern.compile(Util.DOC_START_REGEX);
		RandomAccessFile raf = null;
		
		try
		{
			File file = new File(index.getFileMgmt().getFullPath(inputFile));
			raf = new RandomAccessFile(file, "r");
			raf.skipBytes((int) fileOffSet);
			String line = raf.readLine();
			
			// check if it's a new paragraph
			Matcher m = paraPat.matcher(line);
			if (m.matches())
			{
				rawDocument.append(line);
				chordFileResult.setArtistName(m.group(6));
				chordFileResult.setSongName(m.group(4));
				String searchArtistName = index.normalizeLine(chordFileResult.getArtistName());
				StringTokenizer st = new StringTokenizer(searchArtistName);
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
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				if (raf != null)
				{
					raf.close();
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}