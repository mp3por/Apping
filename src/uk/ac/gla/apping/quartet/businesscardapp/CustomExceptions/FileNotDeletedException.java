package uk.ac.gla.apping.quartet.businesscardapp.CustomExceptions;

public class FileNotDeletedException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public FileNotDeletedException(String string) {
		super(string);
	}

}
