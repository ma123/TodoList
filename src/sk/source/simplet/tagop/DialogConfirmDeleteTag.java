package sk.source.simplet.tagop;

import sk.source.simplet.MainActivity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;

public class DialogConfirmDeleteTag {
	private MainActivity main;
	
    public DialogConfirmDeleteTag(MainActivity main) {
    	this.main = main;
    }
    
    /*
     *  Vytvori dialog na potvrdenie vymazania tagu
     */
    public void dialogConfirmDelete(final int tag_dialog_id, final String tag_dialog_name) {
		Builder builder = new AlertDialog.Builder(main);
		builder.setTitle("Delete tag");	
		builder.setMessage("Do you really want to delete the selected tag?");
		
		builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						main.deleteTag(tag_dialog_id, tag_dialog_name);
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
