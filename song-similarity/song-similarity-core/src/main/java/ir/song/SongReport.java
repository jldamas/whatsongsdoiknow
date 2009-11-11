package ir.song;

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

public class SongReport extends Report
{
	public SongReport() { }
	
	public SongReport(int docId, String inputFile, long fileOffSet, DocumentIndex index, Query q)
	{
		super(docId, inputFile, fileOffSet, index, q);
	}

	@Override
	protected void parseDocument()
	{
		Pattern paraPat = Pattern.compile(Util.DOC_START_REGEX);

		try
		{
			File file = new File(index.getFileMgmt().getFullPath(inputFile));
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			raf.skipBytes((int) fileOffSet);
			String line = raf.readLine();
			Matcher m = paraPat.matcher(line);
			if (m.matches())
			{
				rawDocument.append(line);
				// regular expression for filtering out punctuation
				chordFileResult.setSongName(m.group(4));
				chordFileResult.setArtistName(m.group(6));
				String searchSongName = index.normalizeLine(chordFileResult.getSongName());
				StringTokenizer st = new StringTokenizer(searchSongName);
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
