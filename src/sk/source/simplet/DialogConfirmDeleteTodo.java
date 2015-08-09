package sk.source.simplet;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;

public class DialogConfirmDeleteTodo {
	 ShowTodoActivity showTodoActivity;
	 
     public DialogConfirmDeleteTodo(ShowTodoActivity showTodoActivity) {
    	 this.showTodoActivity = showTodoActivity;
     }
     
     /*
      *  Vytvori dialog na potvrdenie vymazania todo
      */
     public void dialogConfirmDelete(final long todo_id) {
 		Builder builder = new AlertDialog.Builder(showTodoActivity);
 		builder.setTitle("Delete todo");		
 		builder.setMessage("Do you really want to delete the selected tag?");
 		
 		builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
 					public void onClick(DialogInterface dialog, int id) {
 						showTodoActivity.deleteTodo(todo_id);
 						dialog.cancel();
 					}
 				})
 				.setPositiveButton("Cancel",
 						new DialogInterface.OnClickListener() {
 							@Override
 							public void onClick(DialogInterface dialog, int id) {
 								dialog.cancel();
 							}
 						});
 		// vytvori alert dialog
 		AlertDialog alertDialog = builder.create();
 		// zobrazi sa
 		alertDialog.show();  	    
     }
}
