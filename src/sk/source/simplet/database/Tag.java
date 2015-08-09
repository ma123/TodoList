package sk.source.simplet.database;

public class Tag {
    private int id;
    private String oblast;
    
    // konstruktory
    public Tag() {
    }
    
    public Tag(String oblast) {
   	 this.oblast = oblast;
    }
    
    public Tag(int id, String oblast) {
   	 this.id = id;
   	 this.oblast = oblast;
    }
    
   // getter a setter
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOblast() {
		return oblast;
	}

	public void setOblast(String oblast) {
		this.oblast = oblast;
	}
}