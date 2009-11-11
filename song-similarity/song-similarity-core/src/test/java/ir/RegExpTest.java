package ir;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class RegExpTest
{
	@Test
	public void testRegEx()
	{
		String line = "<S ID=1 NAME=\"borrowed time\" ARTIST=\"a fine frenzy\">";
		Pattern pat = Pattern.compile("^(<S ID=)([0-9]+)( NAME=\")(.*)(\" ARTIST=\")(.*)(\">)$");
		// Pattern pat2 = Pattern.compile(
		// "\\\\|\\\"|\'|\\[|\\]|\\+|\\=|\\!|\\@|\\#|\\$|\\%|\\^|\\&|\\(|\\)|\\<|\\>|\\:|\\;|\\?|\\*|\\.|,"
		// );
		Matcher m = pat.matcher(line);

		System.out.println(line);
		System.out.println(pat);
		System.out.println(m.matches());

		if (m.matches())
		{
			for (int i = 0; i <= m.groupCount(); i++)
			{
				System.out.println("group " + i + ": " + m.group(i));
			}
		}

		runTest();
	}

	public static void runTest()
	{
		TreeSet<String> set = new TreeSet<String>();

		try
		{
			File file = new ClassPathResource("chords2.list").getFile();
			RandomAccessFile raf = null;
			
			raf = new RandomAccessFile(file, "rw");
			String line = raf.readLine();
			while (line != null)
			{
				line = raf.readLine();
				set.add(line.trim());
			}
			raf.close();
		}
		catch (Exception e)
		{

		}

		RandomAccessFile raf2;
		try
		{
			raf2 = new RandomAccessFile(new File("newChordList.txt"), "rw");
			ArrayList<String> list = new ArrayList<String>();
			for (Object s : set.toArray())
			{
				list.add((String) s);
			}

			Collections.sort(list, new Comparator<String>()
			{

				public int compare(String arg0, String arg1)
				{
					return arg0.compareTo(arg1);
				}

			});

			for (String c : list)
			{
				raf2.writeBytes(c + "\n");
			}
			raf2.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}
}
