package ir.chordprogression;

import ir.DocumentIndex;
import ir.Query;
import ir.chord.ChordDocumentReport;

public class ChordProgressionDocumentReport extends ChordDocumentReport
{
	public ChordProgressionDocumentReport() { }
	
	public ChordProgressionDocumentReport(int docId, String inputFile, long fileOffSet, DocumentIndex index, Query q)
	{
		super(docId, inputFile, fileOffSet, index, q);
	}
}
