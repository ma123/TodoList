package sk.source.simplet;

import java.util.ArrayList;
import java.util.List;

import sk.source.simplet.adapter.TagRow;
import sk.source.simplet.database.Database;
import sk.source.simplet.database.Tag;
import sk.source.simplet.tagop.DialogEditTag;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class MainActivity extends Activity {
    private ListView tagList; 
    private EditText newTag;
    public EditText editTag;
    public ImageButton delButton;
    
	private Database db;
	public List<Tag> tagsList = new ArrayList<Tag>();
    private TagRow tagsAdapter;
    private DialogEditTag tagEdit;
    private long tag_id;
    private String tag_name;
    private Intent activityTodo;
    public static final String TAG_MESSAGE = "TAG_NAME";
    public static final String TAG_MESSAGE_ID = "TAG_ID";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 // otvorenie databazy
        db = new Database(this);
          
        findViewsById();
        readTags();
			
		tagList.setOnItemLongClickListener(new OnItemLongClickListener() {
			   @Override
			   public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				    view.setBackgroundResource(R.color.dark_cyan);
			        tagEdit.dialogEditTag(parent, view, position, id);
			        return false;
			   }
		});     
        
        tagList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {	
            	view.setBackgroundResource(R.color.light_blue);
                onMenuItemClick(parent, view, position, id);
            }           
        });  
        
        newTag.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch(result) {
                case EditorInfo.IME_ACTION_DONE:
                    addNewTag(); 
                    break;
                case EditorInfo.IME_ACTION_NEXT: break;
                }
				return false;
            }
        });
                           
        tagEdit = new DialogEditTag(this);
        // zatvorenie databazy
        db.closeDB();
    } 
	
	 /*
     * Inicializuje komponenty v xml layoutoch
     */
    private void findViewsById() {
    	tagList = (ListView) findViewById(R.id.activity_main_menu_listview);
		newTag = (EditText) findViewById(R.id.new_tag);
        Typeface typeFace=Typeface.createFromAsset(getAssets(),"roboto.ttf");
        newTag.setTypeface(typeFace);
	}
    
    /*
     * update oblast
     */
    public void updateTag(int tag_id) {
    	String str = editTag.getText().toString();
		 if(str.length() > 0) {
		   Tag tag = new Tag(tag_id, str);
		   db.updateTag(tag);
		   readTags();
		   tagsAdapter.notifyDataSetChanged();
		 }
    }
    
    /*
     * Vymaze oblast
     */
    public void deleteTag(int tag_id, String tag_name) {
		   Tag tag = new Tag(tag_id, tag_name);
		   db.deleteTag(tag, true);
		   readTags();
		   tagsAdapter.notifyDataSetChanged();
    }
       
    /*
     * Nacita tagy z databazy a umiestni do tagsList
     */
    public void readTags() {   	
		tagsList = db.getAllTags();
		tagsAdapter = new TagRow(this, tagsList);	
		tagList.setAdapter(tagsAdapter);
	}
    
    /**
     * Prida novy tag 
     */
    public void addNewTag() {
    	String str = newTag.getText().toString();
		 if(str.length() > 0) {
		   Tag tag = new Tag(str);  
		   db.createTag(tag);
		   readTags();
		   tagsAdapter.notifyDataSetChanged();
		   newTag.setText("");
		   openTag();
		 }
		 newTag.setText("");    
	}
   
    /*
     * Vysunie fragment s dotycnim id
     */
    private void onMenuItemClick(AdapterView<?> parent, View view, int position, long id) {
        tag_id = tagsList.get(position).getId();
        tag_name = tagsList.get(position).getOblast();
        startActivityTodo();  
    }
    
    private void openTag() {
    	int position = tagsList.size();
    	tag_id = tagsList.get(position-1).getId();
        tag_name = tagsList.get(position-1).getOblast();
        startActivityTodo();
    }
    
    private void startActivityTodo() {
    	activityTodo = new Intent(getApplicationContext(), TodoActivity.class);
    	activityTodo.putExtra(TAG_MESSAGE_ID, tag_id);
    	activityTodo.putExtra(TAG_MESSAGE, tag_name); 
    	startActivity(activityTodo);
    }
    
    @Override
    public void onBackPressed() {
        finish();
    }
}