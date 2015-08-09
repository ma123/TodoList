package sk.source.simplet.database;

public class Todo {
	private int id;
	private String poznamka;
	private int status = 0;
	private int priority = 0;
	private String dateRealized;
	private String created;

	// konstruktory
	public Todo() {
	}
	
	public Todo(int id) {
		this.id = id;
	}
	
	public Todo(String poznamka, int status, int priority) {
		this.poznamka = poznamka;
		this.status = status;
		this.priority = priority;
	}

	public Todo(String poznamka, int status, int priority, String dateRealized) {
		this.poznamka = poznamka;
		this.status = status;
		this.priority = priority;
		this.dateRealized = dateRealized;
	}
	
	public Todo(int id, String poznamka, int status, int priority) {
		this.id = id;
		this.poznamka = poznamka;
		this.status = status;
		this.priority = priority;
	}

	public Todo(int id, String poznamka, int status, int priority, String dateRealized) {
		this.id = id;
		this.poznamka = poznamka;
		this.status = status;
		this.priority = priority;
		this.dateRealized = dateRealized;
	}

	// getter a setter
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPoznamka() {
		return poznamka;
	}

	public void setPoznamka(String poznamka) {
		this.poznamka = poznamka;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getDateRealized() {
		return dateRealized;
	}

	public void setDateRealized(String dateRealized) {
		this.dateRealized = dateRealized;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}
}