package uistuff;

import android.content.DialogInterface;

import androidx.fragment.app.DialogFragment;

public interface DialogListener {

    void onDialogButtonClick(DialogFragment dialogFragment, int which);

} // End of Dialog Listener
