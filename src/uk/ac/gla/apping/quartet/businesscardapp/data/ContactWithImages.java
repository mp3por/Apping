package uk.ac.gla.apping.quartet.businesscardapp.data;

public class ContactWithImages extends Contact {
	// change from byte[] to appropriate type, when one will be found
	private byte[] frontImage;
	private byte[] backImage = null;
	
	// getters and setters
	
	public byte[] getFrontImage() {
		return frontImage;
	}

	public void setFrontImage(byte[] frontImage) {
		this.frontImage = frontImage;
	}

	public byte[] getBackImage() {
		return backImage;
	}

	public void setBackImage(byte[] backImage) {
		this.backImage = backImage;
	}

	//constructor
	public ContactWithImages() {
		super();
	}
	

}
