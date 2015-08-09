package sk.source.simplet.database;

public class TodoTag {
	 private int id;
	 private int todo_id;
	 private int tag_id;
	 
     public TodoTag() {}
     
     public TodoTag(int todo_id, int tag_id) {
    	 this.todo_id = todo_id;
    	 this.tag_id = tag_id;
     }
     
     public TodoTag(int id, int todo_id, int tag_id) {
    	 this.id = id;
    	 this.todo_id = todo_id;
    	 this.tag_id = tag_id;
     }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTodo_id() {
		return todo_id;
	}

	public void setTodo_id(int todo_id) {
		this.todo_id = todo_id;
	}

	public int getTag_id() {
		return tag_id;
	}

	public void setTag_id(int tag_id) {
		this.tag_id = tag_id;
	} 
}
