package uk.ac.gla.apping.quartet.businesscardapp.data;

public class Contact {
	private int id;
	private String name;
	private String email;
	private String number; // maybe int, doubt that we will perform search on number
	private String company;
	
	// change from byte[] to appropriate type, when one will be found
	private byte[] thumbnail;
	
	// getters and setters
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public byte[] getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(byte[] thumbnail) {
		this.thumbnail = thumbnail;
	}
	
	// Constructor
	public Contact() {
		this.id = -1;
	}
}
