package ir.chordprogression;

import ir.DocInfo;
import ir.PostingEntry;
import ir.Query;
import ir.chord.ChordDocumentIndex;
import ir.chord.ChordDocumentReport;
import ir.report.Report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * When run this will read in the desired file. And error check with the desired
 * dictionary file. It will make an inverted file for look up and an array list
 * in order to sort the TermStat objects.
 * 
 * @author James Damaska
 * 
 */
public class ChordProgressionDocumentIndex extends ChordDocumentIndex
{
	public ChordProgressionDocumentIndex()
	{
	}

	public ChordProgressionDocumentIndex(String input, String dic, String inv, String docPos)
	{
		super(input, dic, inv, docPos);
	}

	public String normalizeLine(String line)
	{
		StringBuffer finalString = new StringBuffer();
		// regular expression for filtering out punctuation except # / and ( )
		line = line.replaceAll("\\\"|\'|\\[|\\]|\\+|\\=|\\!|\\@|\\$|\\%|\\^|\\&|\\<|\\>|\\:|\\;|\\?|\\*|\\.|,|-", " ");
		StringTokenizer st = new StringTokenizer(line);
		int count = 1;
		while (st.hasMoreTokens())
		{
			String term = st.nextToken().trim();
			if (util.getChordHash().contains(term))
			{
				if (count != 4)
				{
					finalString.append(term);
					count++;
				}
				else
				{
					finalString.append(term + " ");
					count = 1;
				}
			}
		}
		// System.out.println(finalString);
		return finalString.toString();
	}

	public List<Report> search(Query q)
	{
		List<Report> ret = new ArrayList<Report>();
		ArrayList<Integer> usedIds = new ArrayList<Integer>();

		for (String term : q.getQueryTerms().keySet())
		{
			PostingEntry postingEntry = findWord(q.getQueryTerms().get(term).term, false);

			if (postingEntry != null)
			{
				// go through the posting list and get all of the
				// document IDs
				for (Integer docId : postingEntry.getDocPositions().keySet())
				{
					// this is so we don't score the same documents over and
					// over
					if (!usedIds.contains(docId))
					{
						usedIds.add(docId);
						DocInfo info = documentLocations.get(docId);
						Long documentLoc = info.getDocumentOffset();

						ChordDocumentReport dr = new ChordDocumentReport(docId, inputFileName, documentLoc, this, q);
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
}
