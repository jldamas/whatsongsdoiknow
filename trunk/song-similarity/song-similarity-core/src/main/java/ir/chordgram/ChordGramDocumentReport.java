package ir.chordgram;

import ir.DocumentIndex;
import ir.Query;
import ir.chord.ChordDocumentReport;

public class ChordGramDocumentReport extends ChordDocumentReport
{
	public ChordGramDocumentReport() { }
	
	public ChordGramDocumentReport(int docId, String inputFile, long fileOffSet, DocumentIndex index, Query q)
	{
		super(docId, inputFile, fileOffSet, index, q);
	}
}
