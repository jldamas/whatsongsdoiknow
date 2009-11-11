package ir;

/**
 * Holds the term it self along with the information on it. The number of
 * documents and the total frequency can all be determined by analyzing the
 * HashMap holding the document numbers and frequency in the TermInfo
 * Object.
 * 
 * @author James Damaska
 * 
 */
public class DictionaryEntry
{
	private String term;
	private long termId;
	private long numberOfDocs;
	private long totalFreq;
	private long postingFileOffset;
	private double idf;
	private PostingEntry pos;

	public DictionaryEntry() { }
	
	public DictionaryEntry(String term, PostingEntry pos, double idf)
	{
		super();
		this.pos = pos;
		this.term = term;
		this.setIdf(idf);
		numberOfDocs = pos.getNumDocs();
		totalFreq = pos.getTotalFreq();
	}

	public DictionaryEntry(long termID, String term, long numberOfDocs, long totalFreq, long postingsOffset, double idf)
	{
		super();
		this.term = term;
		this.termId = termID;
		this.numberOfDocs = numberOfDocs;
		this.totalFreq = totalFreq;
		this.postingFileOffset = postingsOffset;
		this.setIdf(idf);
	}

	public long getPostingFileOffset()
	{
		return postingFileOffset;
	}

	public void setPostingFileOffset(long postingFileOffset)
	{
		this.postingFileOffset = postingFileOffset;
	}

	public long getTermId()
	{
		return termId;
	}

	public void setTermId(long termId)
	{
		this.termId = termId;
	}

	public String getTerm()
	{
		return term;
	}

	public void setTerm(String term)
	{
		this.term = term;
	}

	public PostingEntry getPos()
	{
		return pos;
	}

	public void setPos(PostingEntry pos)
	{
		this.pos = pos;
	}

	public long getNumberOfDocs()
	{
		return numberOfDocs;
	}

	public void setNumberOfDocs(long numberOfDocs)
	{
		this.numberOfDocs = numberOfDocs;
	}

	public long getTotalFreq()
	{
		return totalFreq;
	}

	public void setTotalFreq(long totalFreq)
	{
		this.totalFreq = totalFreq;
	}

	public double getWeight()
	{
		return getIdf();
	}

	public void setWeight(double weight)
	{
		this.setIdf(weight);
	}

	public String toString()
	{
		return termId + ":" + term + ":" + this.numberOfDocs + ":" + this.totalFreq + ":" + postingFileOffset + ":" + getIdf();
	}

	public void setIdf(double idf) {
		this.idf = idf;
	}

	public double getIdf() {
		return idf;
	}
}
