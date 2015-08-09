package sk.source.simplet;

import java.util.List;

import sk.source.simplet.adapter.TodoRow;
import sk.source.simplet.database.Database;
import sk.source.simplet.database.Todo;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

public class TodoActivity extends Activity {
	// components
	private ListView listView;
	private TextView titleTextView;
	private EditText newTodo;
	//todo parameters
	private int todo_priority = 1;

	private List<Todo> todoList;
	private Database db;
	private long tag_id = 0;
	private String tag_name = "";
	private int todo_position = 0;
	public static final String TAG_MESSAGE = "TAG_NAME";
	public static final String TAG_MESSAGE_ID = "TAG_ID";
	public static final String TODO_MESSAGE = "TODO_POSITION";
	
	private TodoRow adapter;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);
		
		Intent intent = getIntent();
		tag_name = intent.getStringExtra(MainActivity.TAG_MESSAGE);
		tag_id = intent.getLongExtra(MainActivity.TAG_MESSAGE_ID, tag_id);

		db = new Database(this);
		findViewsById(); // inicializovanie komponentov
		readTodo();

		// pozicia v liste
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View view, int position, long id) {
				view.setBackgroundResource(R.color.light_blue);
				todo_position = todoList.get(position).getId();
				finish();
				Intent showTodo = new Intent(getApplicationContext(), ShowTodoActivity.class);
				showTodo.putExtra(TAG_MESSAGE_ID, tag_id);
		    	showTodo.putExtra(TAG_MESSAGE, tag_name); 
		    	showTodo.putExtra(TODO_MESSAGE, todo_position); 
				startActivity(showTodo);
			}
		});
		
		 newTodo.setOnEditorActionListener(new OnEditorActionListener() {
	            @Override
	            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
	                int result = actionId & EditorInfo.IME_MASK_ACTION;
	                switch(result) {
	                case EditorInfo.IME_ACTION_DONE:
	                	String todo_name = newTodo.getText().toString();		
	            		if (todo_name.length() > 0) {
	            			Todo todo = new Todo(todo_name, 0, todo_priority);
	            			db.vytvorToDo(todo, new long[] { tag_id });
	            			readTodo();
	            		}
	            		newTodo.setText("");
	                    break;
	                case EditorInfo.IME_ACTION_NEXT:
	                    // next stuff
	                    break;
	                }
					return false;
	            }
	        });
		db.closeDB();
	}

	/*
	 * inicializuje komponenty podla id
	 */
	public void findViewsById() {
		titleTextView = (TextView) findViewById(R.id.textview_title);
		listView = (ListView) findViewById(R.id.todo_listview);
		newTodo = (EditText) findViewById(R.id.new_todo);
		Typeface typeFace=Typeface.createFromAsset(this.getAssets(),"roboto.ttf");
	    newTodo.setTypeface(typeFace);
	    titleTextView.setTypeface(typeFace);
	}

	/* Cita vsetky todo podla tagu */
	public void readTodo() {
		titleTextView.setText(tag_name);
		todoList = db.getAllToDosByTag(tag_name);
		adapter = new TodoRow(this, todoList);	
		listView.setAdapter(adapter);
	}

	/*
	 * Vymazanie todo z tabulky todo v databaze a tiez z prepojovacej tabulky
	 */
	public void deleteTodo(long todo_id) {
		db.deleteToDo(todo_id);
		db.deleteToDoTag(todo_id);
		adapter.notifyDataSetChanged();
		readTodo();
	}
	
	  @Override
	    public void onBackPressed() {
	    	NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
	    }
}