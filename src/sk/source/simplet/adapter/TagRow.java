package sk.source.simplet.adapter;

import java.util.List;

import sk.source.simplet.R;
import sk.source.simplet.database.Database;
import sk.source.simplet.database.Tag;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TagRow extends BaseAdapter {
	private Context mContext;
	private final List<Tag> tagsList;
	private Database db;
	private View grid;
	private TextView textView;
	private TextView numberTextView;
	private ImageView imageView;

	public TagRow(Context c, List<Tag> tagsList) {  
		mContext = c;
		this.tagsList = tagsList;
		db = new Database(mContext);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			grid = new View(mContext);
			grid = inflater.inflate(R.layout.tag_row, null);
			textView = (TextView) grid.findViewById(R.id.label_tag); // pridelenie objektu id v xml
			numberTextView = (TextView) grid.findViewById(R.id.number_tag); // pridelenie objektu id v xml  
			imageView = (ImageView) grid.findViewById(R.id.cell_tag_image); // pridelenie objektu id v xml
			Typeface typeFace=Typeface.createFromAsset(mContext.getAssets(),"roboto.ttf");
		    textView.setTypeface(typeFace);
		    numberTextView.setTypeface(typeFace);
		    int pocetTodo = db.getToDoCount(tagsList.get(position).getOblast());
			textView.setText(" " + tagsList.get(position).getOblast() + " ");
			numberTextView.setText(" [" + pocetTodo + "] ");
			
			imageView.setImageResource(R.drawable.bullet);
		} else {
			grid = (View) convertView;
		}
		return grid;
	}
	
	@Override
	public int getCount() {
		return tagsList.size();
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