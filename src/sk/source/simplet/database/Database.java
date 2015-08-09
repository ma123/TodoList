package sk.source.simplet.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper{
	private static final String LOG = Database.class.getName();
	 // Verzia databazy
    private static final int DATABASE_VERSION = 1; 
    // Nazov databazy
    private static final String DATABASE_NAME = "todo";
    // Nazvy tabuliek
    private static final String TABLE_TODO = "poznamky";
    private static final String TABLE_TAG = "oblasti";
    private static final String TABLE_TODO_TAG = "poz_obl";
    // stlpce meno
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "vytvorene"; 
    // Poznamky tabulka
    private static final String KEY_TODO = "poznamka";
    private static final String KEY_STATUS = "status";
    private static final String KEY_PRIORITY = "priorita";
    private static final String KEY_DATE = "datum";
    // Oblasti tabulka
    private static final String KEY_TAG_NAME = "oblast";
    // Prepoj tabulka
    private static final String KEY_TODO_ID = "poz_id";
    private static final String KEY_TAG_ID = "obl_id";
    // Poznamky vytvor
    private static String CREATE_TABLE_TODO = "CREATE TABLE "
            + TABLE_TODO + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TODO
            + " TEXT," + KEY_STATUS + " INTEGER," + KEY_PRIORITY + " INTEGER,"
            + KEY_DATE + " DATETIME," + KEY_CREATED_AT + " DATETIME" + ")";
    
    // Oblast vytvor
    private static final String CREATE_TABLE_TAG = "CREATE TABLE " + TABLE_TAG
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TAG_NAME + " TEXT,"
            + KEY_CREATED_AT + " DATETIME" + ")";
    // Prepojovacia
    private static final String CREATE_TABLE_TODO_TAG = "CREATE TABLE "
            + TABLE_TODO_TAG + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TODO_ID + " INTEGER," + KEY_TAG_ID + " INTEGER,"
            + KEY_CREATED_AT + " DATETIME" + ")";
    
    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
    	// vytvorenie tabuliek databazy
        db.execSQL(CREATE_TABLE_TODO);       
        db.execSQL(CREATE_TABLE_TAG);
        db.execSQL(CREATE_TABLE_TODO_TAG);
        
        ContentValues cv = new ContentValues();
    	cv.put(KEY_ID, 1);
    	cv.put(KEY_TAG_NAME, "Inbox");
    	db.insert(Database.TABLE_TAG, null, cv);    	
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int staraVerzia, int novaVerzia) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO_TAG);
        onCreate(db);
    }
    
               /*Poznamky metody*/
    
    /**
     * Vytvorenie poznamok
     */
    public long vytvorToDo(Todo todo, long[] tag_ids) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_TODO, todo.getPoznamka());
        values.put(KEY_STATUS, todo.getStatus());
        values.put(KEY_PRIORITY, todo.getPriority());
        values.put(KEY_DATE, todo.getDateRealized());
        values.put(KEY_CREATED_AT, getDateTime());
 
        long todo_id = db.insert(TABLE_TODO, null, values);
 
        for (long tag_id : tag_ids) {
            vytvorTodoTag(todo_id, tag_id);
        }
        return todo_id;
    }
    
    /**
     * Vyber 1 poznamku
     */
    public Todo getTodo(long todo_id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        String selectQuery = "SELECT  * FROM " + TABLE_TODO + " WHERE " + KEY_ID + " = " + todo_id;
 
        Log.e(LOG, selectQuery);
 
        Cursor c = db.rawQuery(selectQuery, null);
 
        if (c != null) {
            c.moveToFirst();
        }
        Todo td = new Todo();
        td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
        td.setPoznamka((c.getString(c.getColumnIndex(KEY_TODO))));
        td.setStatus((c.getInt(c.getColumnIndex(KEY_STATUS))));
        td.setPriority((c.getInt(c.getColumnIndex(KEY_PRIORITY))));
        td.setDateRealized((c.getString(c.getColumnIndex(KEY_DATE))));
        td.setCreated(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
 
        return td;
    }
    
    /**
     * Vyberie vsetky poznamky
     * */
    public List<Todo> getAllToDos() {
        List<Todo> todos = new ArrayList<Todo>();
        String selectQuery = "SELECT  * FROM " + TABLE_TODO;
 
        Log.e(LOG, selectQuery);
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // prechadza vsetky riadky a pridava do listu
        if (c.moveToFirst()) {
            do {
            	Todo td = new Todo();
                td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                td.setPoznamka((c.getString(c.getColumnIndex(KEY_TODO))));
                td.setStatus((c.getInt(c.getColumnIndex(KEY_STATUS))));
                td.setPriority((c.getInt(c.getColumnIndex(KEY_PRIORITY))));
                td.setDateRealized((c.getString(c.getColumnIndex(KEY_DATE))));
                td.setCreated(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
 
                // prida to listu
                todos.add(td);
            } while (c.moveToNext());
        }
 
        return todos;
    }
    
    /**
     * Vyberie vsetky poznamky pod oblastou
     * */
    public List<Todo> getAllToDosByTag(String tag_name) {
        List<Todo> todos = new ArrayList<Todo>();
 
        String selectQuery = "SELECT  * FROM " + TABLE_TODO + " td, "
                + TABLE_TAG + " tg, " + TABLE_TODO_TAG + " tt WHERE tg."
                + KEY_TAG_NAME + " = '" + tag_name + "'" + " AND tg." + KEY_ID
                + " = " + "tt." + KEY_TAG_ID + " AND td." + KEY_ID + " = "
                + "tt." + KEY_TODO_ID;
 
        Log.e(LOG, selectQuery);
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
 
        // prechadza vsetky riadky a pridava do listu
        if (c.moveToFirst()) {
            do {
            	Todo td = new Todo();
                td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                td.setPoznamka((c.getString(c.getColumnIndex(KEY_TODO))));
                td.setStatus((c.getInt(c.getColumnIndex(KEY_STATUS))));
                td.setPriority((c.getInt(c.getColumnIndex(KEY_PRIORITY))));
                td.setDateRealized((c.getString(c.getColumnIndex(KEY_DATE))));
                td.setCreated(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
 
                todos.add(td);
            } while (c.moveToNext());
        }
 
        return todos;
    }
    
    /**
     * Vracia pocet poznamok v konkretnej oblasti 
     * @param tag_name
     * @return pom 
     */
    public int getToDoCount(String tag_name) {    	 
        String selectQuery = "SELECT  * FROM " + TABLE_TODO + " td, "
                + TABLE_TAG + " tg, " + TABLE_TODO_TAG + " tt WHERE tg."
                + KEY_TAG_NAME + " = '" + tag_name + "'" + " AND tg." + KEY_ID
                + " = " + "tt." + KEY_TAG_ID + " AND td." + KEY_ID + " = "
                + "tt." + KEY_TODO_ID;
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        int pom = 0;
        if (c.moveToFirst()) {
            do {
                pom++;
            } while (c.moveToNext());
        }
 
        return pom;
    }
    
    /**
     * Update poznamok
     */
    public int updateToDo(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(KEY_ID, todo.getId());
        values.put(KEY_TODO, todo.getPoznamka());
        values.put(KEY_STATUS, todo.getStatus());
        values.put(KEY_PRIORITY, todo.getPriority());
        values.put(KEY_DATE, todo.getDateRealized());
        return db.update(TABLE_TODO, values, KEY_ID + " = ?", new String[] { String.valueOf(todo.getId()) });
    }
    
    /**
     * Mazanie poznamok
     */
    public void deleteToDo(long todo_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, KEY_ID + " = ?", new String[] { String.valueOf(todo_id) });
    }
    
/**************************** Tag metody *****************************************/   
 
    /**
     * Vytvor Tag
     */
    public long createTag(Tag tag) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_TAG_NAME, tag.getOblast());
        values.put(KEY_CREATED_AT, getDateTime());
 
        // insert row
        long tag_id = db.insert(TABLE_TAG, null, values);
 
        return tag_id;
    }
    
    /**
     * Vyberie vsetky Tagy
     * */
    public List<Tag> getAllTags() {
        List<Tag> tags = new ArrayList<Tag>();
        String vyber = "SELECT  * FROM " + TABLE_TAG;
 
        Log.e(LOG, vyber);
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(vyber, null);
 
        if (c.moveToFirst()) {
            do {
            	Tag t = new Tag();
                t.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                t.setOblast(c.getString(c.getColumnIndex(KEY_TAG_NAME)));
 
                tags.add(t);
            } while (c.moveToNext());
        }
        return tags;
    }
    
    public int getTagCount() {    
         String vyber = "SELECT  * FROM " + TABLE_TAG;
  
         Log.e(LOG, vyber);
  
         SQLiteDatabase db = this.getReadableDatabase();
         Cursor c = db.rawQuery(vyber, null);
         int pom = 0;
  
         if (c.moveToFirst()) {
             do {
             	pom++;
             } while (c.moveToNext());
         }
         return pom;
    }
    
    
    /**
     * Update Tagu
     */
    public int updateTag(Tag tag) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_TAG_NAME, tag.getOblast());
 
        return db.update(TABLE_TAG, values, KEY_ID + " = ?", new String[] { String.valueOf(tag.getId()) });
    }
    
    /**
     * Vymaze tag
     */
    public void deleteTag(Tag tag, boolean should_delete_all_tag_todos) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        if (should_delete_all_tag_todos) {
            List<Todo> allTagToDos = getAllToDosByTag(tag.getOblast());
 
            for (Todo todo : allTagToDos) {
                deleteToDo(todo.getId());
                deleteToDoTag(todo.getId());
            }
        }
 
        db.delete(TABLE_TAG, KEY_ID + " = ?", new String[] { String.valueOf(tag.getId()) });
    }
    /** 
     * Vymaze vsetky tagy
     */
    
    public void deleteAllTag() {
    	SQLiteDatabase db = this.getWritableDatabase();
        
        List<Tag> allObl = getAllTags();
           for(Tag o: allObl) {
            List<Todo> allTagToDos = getAllToDosByTag(o.getOblast());
 
            for (Todo todo : allTagToDos) {
                deleteToDo(todo.getId());
                db.delete(TABLE_TAG, KEY_ID + " = ?", new String[] { String.valueOf(o.getId()) });
            }
           }      
    }
    
    /**
     * Vytvori todo_tag
     */
    public long vytvorTodoTag(long todo_id, long tag_id) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_TODO_ID, todo_id);
        values.put(KEY_TAG_ID, tag_id);
        values.put(KEY_CREATED_AT, getDateTime());
 
        long id = db.insert(TABLE_TODO_TAG, null, values);
 
        return id;
    }
    
    /**
     * Vyberie vsetky todo tag
     * */
    public List<TodoTag> getAllTodosTags() {
        List<TodoTag> todoTags = new ArrayList<TodoTag>();
        String vyber = "SELECT  * FROM " + TABLE_TODO_TAG;
 
        Log.e(LOG, vyber);
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(vyber, null);
 
        if (c.moveToFirst()) {
            do {
            	TodoTag t = new TodoTag();
                t.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                t.setTodo_id(c.getInt(c.getColumnIndex(KEY_TODO_ID)));
                t.setTag_id(c.getInt(c.getColumnIndex(KEY_TAG_ID)));
                todoTags.add(t);
            } while (c.moveToNext());
        }
        return todoTags;
    }
 
    /**
     * Update prepojovacej tabulky medzi todo a tag
     */
    public int updateNoteTag(long id, long tag_id) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_TAG_ID, tag_id);
 
        return db.update(TABLE_TODO, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }
 
    /**
     * Mazanie poznamok a oblasti v prepojovacej tabulke
     */
    public void deleteToDoTag(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO_TAG, KEY_ID + " = ?", new String[] { String.valueOf(id) });
    }
 
    // zatvorenie databazy
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
 
    /**
     * Ziskaj datum a cas
     * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
        Date date = new Date();
        return dateFormat.format(date);
    }  
}