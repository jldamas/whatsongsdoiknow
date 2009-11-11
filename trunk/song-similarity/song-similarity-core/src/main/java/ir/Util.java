package ir;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

public class Util implements ApplicationContextAware {
	public static final String DOC_START_REGEX = "^(<S ID=)([0-9]+)( NAME=\")(.*)(\" ARTIST=\")(.*)(\">)$";
	public static final String DOC_END_REGEX = "^(</S>)$";

	private ApplicationContext applicationContext;
	private HashSet<String> chordHash;

	public void init() {
		chordHash = new HashSet<String>();

		try {
			Resource resource = applicationContext
					.getResource("classpath:chords.list");
			BufferedReader bis = new BufferedReader(new InputStreamReader(
					resource.getInputStream()));

			String chord = bis.readLine();

			while (chord != null) {
				chordHash.add(chord.trim());
				chord = bis.readLine();
			}
			bis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HashSet<String> getChordHash() {
		return chordHash;
	}

	/**
	 * @param chordHash
	 *            the chordHash to set
	 */
	public void setChordHash(HashSet<String> chordHash) {
		this.chordHash = chordHash;
	}

	/**
	 * @return the applicationContext
	 */
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * @param applicationContext
	 *            the applicationContext to set
	 */
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
