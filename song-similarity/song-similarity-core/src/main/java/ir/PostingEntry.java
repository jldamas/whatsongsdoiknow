package ir;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Holds the frequencies for each document.
 * 
 * @author James Damaska
 * 
 */
public class PostingEntry
{
	// doc #, positions
	private HashMap<Integer, ArrayList<Integer>> docPositions = new HashMap<Integer, ArrayList<Integer>>();
	// should correspond with the dictionary ID
	private long id;

	public PostingEntry() { }
	
	public PostingEntry(int currPara, int position)
	{
		ArrayList<Integer> positions = new ArrayList<Integer>();
		positions.add(position);
		docPositions.put(currPara, positions);
	}

	public PostingEntry(long id, HashMap<Integer, ArrayList<Integer>> docPositions)
	{
		this.docPositions = docPositions;
		this.id = id;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public void addOccurance(int docNum, int position)
	{
		if (docPositions.containsKey(docNum))
		{
			ArrayList<Integer> oldPositionList = docPositions.get(docNum);
			oldPositionList.add(position);
			docPositions.put(docNum, oldPositionList);
		}
		else
		{
			ArrayList<Integer> positionList = new ArrayList<Integer>();
			positionList.add(position);
			docPositions.put(docNum, positionList);
		}
	}

	public int getNumDocs()
	{
		return docPositions.keySet().size();
	}

	public int getTotalFreq()
	{
		int total = 0;

		for (Integer i : docPositions.keySet())
		{
			total = total + docPositions.get(i).size();
		}

		return total;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer(id + "|");
		for (Integer docNum : docPositions.keySet())
		{
			sb.append(docNum + ":<");
			boolean isFirst = true;
			for (Integer pos : docPositions.get(docNum))
			{
				if (isFirst)
				{
					sb.append(pos);
					isFirst = false;
				}
				else
				{
					sb.append("," + pos);
				}
			}
			sb.append(">;");
		}

		return sb.toString();
	}

	public String toStringHumanFriendly()
	{
		StringBuffer sb = new StringBuffer("    Term ID: " + id + "\n");
		for (Integer docNum : docPositions.keySet())
		{
			sb.append("      Appeared in Document " + docNum + " in position(s) ");
			boolean isFirst = true;
			for (Integer pos : docPositions.get(docNum))
			{
				if (isFirst)
				{
					sb.append(pos);
					isFirst = false;
				}
				else
				{
					sb.append(", " + pos);
				}
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	/**
	 * @return the docPositions
	 */
	public HashMap<Integer, ArrayList<Integer>> getDocPositions() {
		return docPositions;
	}

	/**
	 * @param docPositions the docPositions to set
	 */
	public void setDocPositions(HashMap<Integer, ArrayList<Integer>> docPositions) {
		this.docPositions = docPositions;
	}
}
