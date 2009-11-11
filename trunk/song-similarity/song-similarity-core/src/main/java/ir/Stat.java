package ir;

public class Stat
{
	private int termFreq = 1;
	private double idf = 0.0;

	public Stat(DictionaryEntry dentry)
	{
		this.setIdf(dentry.getIdf());
		setTermFreq(1);
	}

	public double getWeight()
	{
//		return getTermFreq() * getIdf();
		 return getTermFreq();
//		return 1.0;
	}

	public void addOccurrance()
	{
		setTermFreq(getTermFreq() + 1);
	}

	public void setIdf(double idf)
	{
		this.idf = idf;
	}

	public double getIdf()
	{
		return idf;
	}

	public void setTermFreq(int termFreq)
	{
		this.termFreq = termFreq;
	}

	public int getTermFreq()
	{
		return termFreq;
	}
}
