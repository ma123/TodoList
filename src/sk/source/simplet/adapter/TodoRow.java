package sk.source.simplet.adapter;

import java.util.List;

import sk.source.simplet.R;
import sk.source.simplet.database.Todo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TodoRow extends BaseAdapter {
	private Context mContext;
	private final List<Todo> todoList;
	
	private View grid;
	private TextView textView;
	private ImageView imageView;
	private ImageView imagePriorityView;

	public TodoRow(Context c, List<Todo> todoList) {  
		mContext = c;
		this.todoList = todoList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			grid = new View(mContext);
			grid = inflater.inflate(R.layout.todo_row, null);
			textView = (TextView) grid.findViewById(R.id.label_todo); // pridelenie objektu id v xml  
			imageView = (ImageView) grid.findViewById(R.id.cell_todo_image); // pridelenie objektu id v xml
			imagePriorityView = (ImageView) grid.findViewById(R.id.cell_priority_image); // pridelenie objektu id v xml
			textView.setText(todoList.get(position).getPoznamka());
			Typeface typeFace=Typeface.createFromAsset(mContext.getAssets(),"roboto.ttf");
		    textView.setTypeface(typeFace);
			if(todoList.get(position).getStatus() == 1) {
				imageView.setImageResource(R.drawable.checkeddot);
				grid.setBackgroundColor(Color.alpha(35));
				textView.setPaintFlags((textView).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			}
			else{
				imageView.setImageResource(R.drawable.dot);
			}
			showPriorityImage(todoList.get(position).getPriority());	
		} else {
			grid = (View) convertView;
		}
		return grid;
	}
	
	private void showPriorityImage(int priority) {
		switch(priority) {
		  case 1:
			  imagePriorityView.setImageResource(R.drawable.priority_green);
	      break;
		  case 2:
			  imagePriorityView.setImageResource(R.drawable.priority_orange);
	      break;
		  case 3:
			  imagePriorityView.setImageResource(R.drawable.priority_red);
	      break;
	      default:
	    	  imagePriorityView.setImageResource(R.drawable.priority_green);
	      break;
		}
		
	}

	@Override
	public int getCount() {
		return todoList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
}