package sk.source.simplet;

import java.util.Calendar;

import sk.source.simplet.database.Database;
import sk.source.simplet.database.Todo;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class ShowTodoActivity extends Activity {
	private Database db;
	public static final String TAG_MESSAGE = "TAG_NAME";
	public static final String TAG_MESSAGE_ID = "TAG_ID";
	public static final String TODO_MESSAGE = "TODO_POSITION";
	private long tag_id;
	private String tag_name;
	private int todo_position;

	private int todo_id;
	private String todo_name;
	private int todo_status;
	private int todo_priority;
	private String todo_date_realized;

	private ImageButton delButton;
	private ImageButton checkButton;
	private Button setDateButton;
	private TextView todoName;
	private EditText editTodo;
	private ImageButton priorityButton01;
	private ImageButton priorityButton02;
	private ImageButton priorityButton03;
	private ViewSwitcher switcher;
	private Todo todo;

	private Calendar calendar;
	private int year, month, day;
	private DialogConfirmDeleteTodo dialogConfirmDeleteTodo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_todo);

		Intent intent = getIntent();
		tag_name = intent.getStringExtra(TodoActivity.TAG_MESSAGE);
		tag_id = intent.getLongExtra(TodoActivity.TAG_MESSAGE_ID, tag_id);
		todo_position = intent.getIntExtra(TodoActivity.TODO_MESSAGE, todo_position);

		db = new Database(this);
		dialogConfirmDeleteTodo = new DialogConfirmDeleteTodo(this);
		findViewsById(); // inicializovanie komponentov
		fillContainer();

		delButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogConfirmDeleteTodo.dialogConfirmDelete(todo_id);
			}
		});

		checkButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (todo_status == 1) {
					checkButton.setImageResource(R.drawable.check_false);
					todo_status = 0;
				} else {
					checkButton.setImageResource(R.drawable.check_true);
					todo_status = 1;
				}
			}
		});

		setDateButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onCreateDialog(999).show();
			}
		});

		db.closeDB();
	}

	private void fillContainer() {
		todo = new Todo();
		todo = db.getTodo(todo_position);
		todo_id = todo.getId();
		todo_name = todo.getPoznamka();
		todo_status = todo.getStatus();
		todo_priority = todo.getPriority();
		todo_date_realized = todo.getDateRealized();

		todoName.setText(todo_name);
		
		priorityButton01.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				priorityButton01.setBackgroundResource(R.color.dark_cyan);
				//priorityButton02.setBackgroundColor(Color.WHITE);
				//priorityButton03.setBackgroundColor(Color.WHITE);
				todo_priority = 1;
			}
		});

		priorityButton02.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//priorityButton01.setBackgroundColor(Color.WHITE);
				priorityButton02.setBackgroundResource(R.color.dark_cyan);
				//priorityButton03.setBackgroundColor(Color.WHITE);
				todo_priority = 2;
			}
		});

		priorityButton03.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//priorityButton01.setBackgroundColor(Color.WHITE);
				//priorityButton02.setBackgroundColor(Color.WHITE);
				priorityButton03.setBackgroundResource(R.color.dark_cyan);
				todo_priority = 3;
			}
		});
		
		todoName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switcher.showNext();
				editTodo.setText(todo_name);
			}
		});

		if (todo_status == 1) {
			checkButton.setImageResource(R.drawable.check_true);
		} else {
			checkButton.setImageResource(R.drawable.check_false);
		}

		/* datum splnenia */
		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		
		if(todo_date_realized != null && !todo_date_realized.isEmpty()) {
			String[] parts = todo_date_realized.split("-");
			int days = Integer.valueOf(parts[0]);
			int months = Integer.valueOf(parts[1]);
			int years = Integer.valueOf(parts[2]);

			setDateButton.setText(days + "." + months + "." + years);
			if((days == day) && (months == (month + 1)) && (years == year)) {
				setDateButton.setTextColor(getResources().getColor(R.color.simple_red));
			}
			else {
				setDateButton.setTextColor(Color.BLACK);
			}
		}
	}

	public void findViewsById() {
		delButton = (ImageButton) findViewById(R.id.button_del_todo);
		checkButton = (ImageButton) findViewById(R.id.button_check);
		setDateButton = (Button) findViewById(R.id.button_realize);
		todoName = (TextView) findViewById(R.id.text_name);
		editTodo = (EditText) findViewById(R.id.edit_todo);
		priorityButton01 = (ImageButton) findViewById(R.id.button01_show);
		priorityButton02 = (ImageButton) findViewById(R.id.button02_show);
		priorityButton03 = (ImageButton) findViewById(R.id.button03_show);
		switcher = (ViewSwitcher) findViewById(R.id.my_switcher);

		Typeface typeFace = Typeface.createFromAsset(this.getAssets(), "roboto.ttf");
		setDateButton.setTypeface(typeFace);
		todoName.setTypeface(typeFace);
		editTodo.setTypeface(typeFace);
	}

	private void changeTodo() {
		String todo_name_edit = editTodo.getText().toString();
		Todo todo;
		if (todo_name_edit.length() == 0) {
			todo = new Todo(todo_id, todo_name, todo_status, todo_priority, todo_date_realized);
		} else {
			todo = new Todo(todo_id, todo_name_edit, todo_status, todo_priority, todo_date_realized);
		}
		db.updateToDo(todo);
	}

	/*
	 * Vymazanie todo z tabulky todo v databaze a tiez z prepojovacej tabulky
	 */
	public void deleteTodo(long todo_id) {
		db.deleteToDo(todo_id);
		db.deleteToDoTag(todo_id);
		finish();
		Intent intent = new Intent(getApplicationContext(), TodoActivity.class);
		intent.putExtra(TAG_MESSAGE_ID, tag_id);
		intent.putExtra(TAG_MESSAGE, tag_name);
		startActivity(intent);
	}

	/***************** date picker *******************/
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		if (id == 999) {
			return new DatePickerDialog(this, myDateListener, year, month, day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker arg0, int year, int month, int day) {
			showDate(year, month + 1, day);
		}
	};

	private void showDate(int y, int m, int d) {
		setDateButton.setText(d + "." + m + "." + y);
		todo_date_realized = d + "-" + m + "-" +y;
		if((d == day) && (m == (month + 1)) && (y == year)) {
			setDateButton.setTextColor(getResources().getColor(R.color.simple_red));
		}
		else {
			setDateButton.setTextColor(Color.BLACK);
		}
	}

	@Override
	public void onBackPressed() {
		changeTodo();
		finish();
		Intent intent = new Intent(getApplicationContext(), TodoActivity.class);
		intent.putExtra(TAG_MESSAGE_ID, tag_id);
		intent.putExtra(TAG_MESSAGE, tag_name);
		startActivity(intent);
	}
}
