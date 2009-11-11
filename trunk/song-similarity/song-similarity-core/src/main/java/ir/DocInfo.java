package ir;

import java.util.HashSet;

public class DocInfo
{
	private long documentOffset;
	private HashSet<String> vocab = new HashSet<String>();

	public DocInfo(long documentOffset, HashSet<String> vocab)
	{
		super();
		this.documentOffset = documentOffset;
		this.vocab = vocab;
	}
	
	public DocInfo(long documentOffset,	String vocabString)
	{
		super();
		this.documentOffset = documentOffset;
		vocabString.replace("<", "");
		vocabString.replace(">", "");
		for(String term : vocabString.split(","))
		{
			vocab.add(term);
		}
	}

	public long getDocumentOffset()
	{
		return documentOffset;
	}

	public void setDocumentOffset(long documentOffset)
	{
		this.documentOffset = documentOffset;
	}

	public HashSet<String> getVocab()
	{
		return vocab;
	}

	public void setVocab(HashSet<String> vocab)
	{
		this.vocab = vocab;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < vocab.size(); i++)
		{
			if(i == vocab.size()-1)
				sb.append(vocab.toArray()[i]);
			else
				sb.append(vocab.toArray()[i] + ",");
		}
		return documentOffset + ":<" + sb.toString() + ">";
	}
}
