package ir.report;

import ir.DocumentIndex;
import ir.Query;
import ir.Stat;

import java.util.HashMap;

public abstract class Report
{
	protected ChordFileResult chordFileResult;
	protected Query query;
	protected HashMap<String, Stat> docTerms = new HashMap<String, Stat>();
	protected DocumentIndex index;
	protected long fileOffSet;
	protected String inputFile;
	protected StringBuffer rawDocument = new StringBuffer("");

	public Report() { }
	
	public Report(int docId, String inputFile, long fileOffSet, DocumentIndex index, Query q)
	{
		this.chordFileResult = new ChordFileResult();
		this.chordFileResult.setDocId(docId);
		this.index = index;
		this.query = q;
		this.fileOffSet = fileOffSet;
		this.inputFile = inputFile;
		parseDocument();
	}
	
	protected abstract void parseDocument();
	
	public double getSumOfSquares()
	{
		double sum = 0;
		for (String key : docTerms.keySet())
		{
			sum = sum + Math.pow(docTerms.get(key).getWeight(), 2);
		}
		return sum;
	}

	public double getLength()
	{
		return Math.sqrt(getSumOfSquares());
	}

	public double getDotProduct()
	{
		double dotProduct = 0.0;
		for (String term : query.getQueryTerms().keySet())
		{
			if (docTerms.containsKey(term))
			{
				dotProduct = dotProduct + (docTerms.get(term).getWeight() * query.getQueryTerms().get(term).getWeight());
			}
		}
		return dotProduct;
	}

	public double getSim()
	{
		return getDotProduct() / (this.getLength() * query.getLength());
	}

	/**
	 * @return the rawDocument
	 */
	public StringBuffer getRawDocument() {
		return rawDocument;
	}

	/**
	 * @param rawDocument the rawDocument to set
	 */
	public void setRawDocument(StringBuffer rawDocument) {
		this.rawDocument = rawDocument;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		for (String key : docTerms.keySet())
		{
			double weight = docTerms.get(key).getIdf() * docTerms.get(key).getTermFreq();
			sb.append(key + ":" + weight + "\n");
		}
		return sb.toString();
	}

	/**
	 * @return the chordFileResult
	 */
	public ChordFileResult getChordFileResult() {
		return chordFileResult;
	}

	/**
	 * @param chordFileResult the chordFileResult to set
	 */
	public void setChordFileResult(ChordFileResult chordFileResult) {
		this.chordFileResult = chordFileResult;
	}
}

