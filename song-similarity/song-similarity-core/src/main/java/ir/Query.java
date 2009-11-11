package ir;

import java.util.HashMap;
import java.util.StringTokenizer;

public class Query
{
	private HashMap<String, Term> queryTerms = new HashMap<String, Term>();
	private DocumentIndex index;
	private String rawQuery;
	private int maxResults = 50;

	public Query()
	{
	}

	public Query(String doc, DocumentIndex index, int maxResults)
	{
		this.rawQuery = doc;
		this.index = index;
		this.maxResults = maxResults;

		doc = doc.replace("\n", " ");
		doc = index.normalizeLine(doc);

		StringTokenizer st = new StringTokenizer(doc, " ");
		// go through each token (space delimited)

		while (st.hasMoreTokens())
		{
			String term = st.nextToken().trim();

			if (!queryTerms.containsKey(term))
			{
				queryTerms.put(term, new Term(term));
			}
			else
			{
				queryTerms.get(term).addOccurance();
			}
		}
	}

	public double getSumOfSquares()
	{
		double sum = 0;
		for (String key : queryTerms.keySet())
		{
			sum = sum + Math.pow(queryTerms.get(key).getWeight(), 2);
		}
		return sum;
	}

	public double getLength()
	{
		return Math.sqrt(getSumOfSquares());
	}

	public double getSim()
	{
		return getDotProduct() / (this.getLength() * getLength());
	}

	public double getDotProduct()
	{
		double dotProduct = 0.0;
		for (String term : queryTerms.keySet())
		{
			if (queryTerms.containsKey(term))
			{
				dotProduct = dotProduct + (queryTerms.get(term).getWeight() * queryTerms.get(term).getWeight());
			}
		}
		return dotProduct;
	}

	public class Term
	{
		public String term;
		public int freq = 1;

		public Term(String term)
		{
			this.term = term;
		}

		public double getWeight()
		{
			DictionaryEntry entry = index.findDictionaryEntry(term);
			if (entry != null)
//				return freq * entry.getIdf();
				return freq;
//				return 1.0;
			else
				return 0;
		}

		public void addOccurance()
		{
			freq++;
		}
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		for (String key : queryTerms.keySet())
		{
			double weight = queryTerms.get(key).getWeight();
			sb.append(key + " : TFxIDF = " + weight + "\n");
		}
		return sb.toString();
	}

	/**
	 * @return the queryTerms
	 */
	public HashMap<String, Term> getQueryTerms()
	{
		return queryTerms;
	}

	/**
	 * @param queryTerms
	 *            the queryTerms to set
	 */
	public void setQueryTerms(HashMap<String, Term> queryTerms)
	{
		this.queryTerms = queryTerms;
	}

	/**
	 * @return the index
	 */
	public DocumentIndex getIndex()
	{
		return index;
	}

	/**
	 * @param index
	 *            the index to set
	 */
	public void setIndex(DocumentIndex index)
	{
		this.index = index;
	}

	/**
	 * @return the rawQuery
	 */
	public String getRawQuery()
	{
		return rawQuery;
	}

	/**
	 * @param rawQuery
	 *            the rawQuery to set
	 */
	public void setRawQuery(String rawQuery)
	{
		this.rawQuery = rawQuery;
	}

	/**
	 * @return the maxResults
	 */
	public int getMaxResults()
	{
		return maxResults;
	}

	/**
	 * @param maxResults
	 *            the maxResults to set
	 */
	public void setMaxResults(int maxResults)
	{
		this.maxResults = maxResults;
	}
}
