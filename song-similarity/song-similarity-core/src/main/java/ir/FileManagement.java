package ir;

import java.io.File;

public class FileManagement {
	private String fileRoot;

	/**
	 * @return the fileRoot
	 */
	public String getFileRoot() {
		return fileRoot;
	}

	/**
	 * @param fileRoot
	 *            the fileRoot to set
	 */
	public void setFileRoot(String fileRoot) {
		this.fileRoot = fileRoot;
	}

	public String getFullPath(String file) {
		StringBuffer sb = new StringBuffer();

		sb.append(fileRoot);
		sb.append(File.separator);
		sb.append(file);

		return sb.toString();
	}
}
