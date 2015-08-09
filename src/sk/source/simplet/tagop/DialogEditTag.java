package sk.source.simplet.tagop;

import sk.source.simplet.MainActivity;
import sk.source.simplet.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;

public class DialogEditTag {
	private MainActivity main;
	
	private DialogConfirmDeleteTag dialogConfirmDeleteTag;

	public DialogEditTag(MainActivity main) {
		this.main = main;	
	}
	
    public void dialogEditTag(AdapterView<?> parent, final View view, int position, long id) {
    	dialogConfirmDeleteTag = new DialogConfirmDeleteTag(main);
    	final int tag_dialog_id = main.tagsList.get(position).getId();
        final String tag_dialog_name = main.tagsList.get(position).getOblast();
    	
		Builder builder = new AlertDialog.Builder(main);
		LayoutInflater inflater = LayoutInflater.from(main);
		View promptView = inflater.inflate(R.layout.dialog_edit_delete_tag, null);
		builder.setView(promptView);
		
		main.editTag = (EditText) promptView.findViewById(R.id.edit_tag);
		main.editTag.setText(tag_dialog_name);
		main.delButton = (ImageButton) promptView.findViewById(R.id.button_del);
			
		builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						main.updateTag(tag_dialog_id);					
						view.setBackgroundResource(R.color.white);
						dialog.cancel();
					}
				})
				.setPositiveButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								view.setBackgroundResource(R.color.white);
								dialog.cancel();
							}
						});
		// vytvori alert dialog
		final AlertDialog alertDialog = builder.create();
		
		main.delButton.setOnClickListener(new OnClickListener() {
	       	    @Override
	       	    public void onClick(View v) {
	       	    	main.delButton.setBackgroundResource(R.color.light_blue);
	       		    dialogConfirmDeleteTag.dialogConfirmDelete(tag_dialog_id, tag_dialog_name);
	            } 
	    });
		// zobrazi sa
		alertDialog.show();
	}
}
