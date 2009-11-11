package ir.song;

import ir.DictionaryEntry;
import ir.DocInfo;
import ir.DocumentIndex;
import ir.PostingEntry;
import ir.Query;
import ir.Util;
import ir.report.Report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * When run this will read in the desired file. And error check with the desired
 * dictionary file. It will make an inverted file for look up and an array list
 * in order to sort the TermStat objects.
 * 
 * @author James Damaska
 * 
 */
public class SongIndex extends DocumentIndex
{
	public SongIndex() { }
	
	public SongIndex(String input, String dic, String inv, String docPos)
	{
		super(input, dic, inv, docPos);
	}

	public List<Report> search(Query q)
	{
		List<Report> ret = new ArrayList<Report>();
		ArrayList<Integer> usedIds = new ArrayList<Integer>();

		for (String term : q.getQueryTerms().keySet())
		{
			PostingEntry e = findWord(q.getQueryTerms().get(term).term, false);

			if (e != null)
			{
				for (Integer docId : e.getDocPositions().keySet())
				{
					if (!usedIds.contains(docId))
					{
						usedIds.add(docId);
						Long documentLoc = documentLocations.get(docId).getDocumentOffset();
						SongReport dr = new SongReport(docId, inputFileName, documentLoc, this, q);
						ret.add(dr);
					}
				}
			}
		}

		Collections.sort(ret, new Comparator<Report>()
		{
			public int compare(Report o1, Report o2)
			{
				if (o1.getSim() > o2.getSim())
				{
					return -1;
				}
				else if (o1.getSim() < o2.getSim())
				{
					return 1;
				}
				else
				{
					return 0;
				}
			}
		});

		if (ret.size() > q.getMaxResults())
			return ret.subList(0, q.getMaxResults());
		else
			return ret;
	}

	public void parseDocumentFile(ArrayList<PostingEntry> invFile)
	{
		RandomAccessFile raf = null;
		Pattern paraPat = Pattern.compile(Util.DOC_START_REGEX);
		int currDocId = 0;
		String currSongName = "";
		long previousLineStart = 0;
		
		try
		{
			File file = new File(getFileMgmt().getFullPath(inputFileName));
			raf = new RandomAccessFile(file, "rw");
			String line = raf.readLine();
			int termPosition = 1;
			while (line != null)
			{
				// check if it's a new paragraph
				Matcher m = paraPat.matcher(line);
				if (m.matches())
				{
					numDocumentsProcessed++;
					currDocId = Integer.parseInt(m.group(2));
					currSongName = m.group(4);
					documentLocations.put(currDocId, new DocInfo(previousLineStart, new HashSet<String>()));
					termPosition = 1;

					currSongName = normalizeLine(currSongName);

					StringTokenizer st = new StringTokenizer(currSongName);
					// go through each token (space delimited)

					while (st.hasMoreTokens())
					{
						String term = st.nextToken().trim();
						addTerm(term, currDocId, termPosition);
						termPosition++;
					}
				}
				previousLineStart = raf.getFilePointer();
				line = raf.readLine();
			}

			// make the dictionary w/o IDs
			for (String term : tempInvFile.keySet())
			{
				PostingEntry entry = tempInvFile.get(term);
				double idf = calculateIDF(entry);
				dictionary.add(new DictionaryEntry(term, entry, idf));
			}

			// sort the dictionary by alpha order
			Collections.sort(dictionary, new Comparator<DictionaryEntry>()
			{
				public int compare(DictionaryEntry o1, DictionaryEntry o2)
				{
					return o1.getTerm().compareTo(o2.getTerm());
				}
			});

			// make the inverted file and give both the dictionary and the
			// inverted file IDs
			long id = 0;
			for (DictionaryEntry dicEntry : dictionary)
			{
				dicEntry.setTermId(id);
				PostingEntry posEntry = dicEntry.getPos();
				posEntry.setId(id);
				invFile.add(posEntry);
				id++;
			}

			for (DictionaryEntry dicEntry : dictionary)
			{
				dictionaryHash.put(dicEntry.getTerm(), dicEntry);
			}

			numDistinctWords = dictionary.size();
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
			try
			{
				if (raf != null)
				{
					raf.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public String normalizeLine(String line)
	{
		line = line.replaceAll("\\\"|\'|\\[|\\]|\\+|\\=|\\!|\\@|\\$|\\%|\\^|\\&|\\<|\\>|\\:|\\;|\\?|\\*|\\.|,|-|\\#|////|\\(|\\)", "");
		return line.toLowerCase();
	}
}
