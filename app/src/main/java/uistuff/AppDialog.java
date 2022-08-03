package uistuff;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.familytree.R;

import org.jetbrains.annotations.NotNull;

import userObjects.Member;


public class AppDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private static final String TAG = "AppDialog";

    DialogListener classImp;

    private Member member;


    public AppDialog() {}

    public AppDialog(Member member) { Log.d(TAG, "Constructor");

        this.member = member;

    }


    @Override
    public void onAttach(@NonNull @NotNull Context context) { Log.d(TAG, "Attaching");

        super.onAttach(context);

        try{

            if(context instanceof Activity) {

                classImp = (DialogListener) context;

            }

        } catch (ClassCastException e){

            throw new ClassCastException(getActivity().toString() + " must implementDialogListener");

        }  // End of try/catch

    } // End of onAttach



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) { Log.d(TAG, "Creating Dialog");

        Dialog dialog = null;


        switch (getTag()) {

            case "AddMember.Parent": case "AddMember.Child": case "TreeView.User":

                dialog = createAddMemberDialog();
                break;

            case "DateChooser":

                dialog = createDateChooserDialog();
                break;

            case "NewMemberCard":

                dialog = createNewMemberCardDialog();
                break;

        } // End of switch


        return dialog;

    } // End of onCreateDialog


    @Override  /** DialogInterface  Method **/
    public void onClick(DialogInterface dialog, int which) {

            classImp.onDialogButtonClick(this, which);

    } // End of onClick()


    private Dialog createAddMemberDialog() {  Log.d(TAG, "AddMemberDialog");


        /** Need to make an identifier for different created dialogs.  Tried making a custom dialog but cannot cast to my custom type using builder.create()
         *  Can use a tag to identify!!  Once an identifer can be used to create different Dialogs from the same framework, finally create a dialog that will
         *  delete a family member.  At that point, the application should have enough features to create local room.database */



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = requireActivity().getLayoutInflater().inflate(R.layout.dialog_add_member, null);

        Spinner relation = (Spinner) view.findViewById(R.id.dialog_add_member_relation_spinner);

        TextView relationLabel = (TextView) view.findViewById(R.id.relation_label);


        if (getTag().equals("AddMember.Child")) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.new_child_add_options, android.R.layout.simple_spinner_dropdown_item);
            relation.setAdapter(adapter);

            builder.setTitle("Add a Sibling");
            relationLabel.setText("Choose Relation");

        } else if (getTag().equals("AddMember.Parent")) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.new_parent_add_options, android.R.layout.simple_spinner_dropdown_item);
            relation.setAdapter(adapter);

            builder.setTitle("Add a Parent");
            relationLabel.setText("Choose Relation");

        } else if (getTag().equals("TreeView.User")) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.new_user_add_options, android.R.layout.simple_spinner_dropdown_item);
            relation.setAdapter(adapter);

            builder.setTitle("Add Yourself!");
            relationLabel.setText("Male or Female?");

        }


        builder.setView(view);

        builder.setPositiveButton("NEXT", this::onClick);
        builder.setNegativeButton("CANCEL", this::onClick);

        Dialog dialog = builder.create();

        dialog.setCanceledOnTouchOutside(false);

        return dialog;


    } // End of createAddMemberDialog


    private Dialog createDateChooserDialog() {  Log.d(TAG, "DateChooserDialog");

        View view = requireActivity().getLayoutInflater().inflate(R.layout.dialog_date_chooser, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(view);

        builder.setTitle("Choose Birthdate!");

        builder.setPositiveButton("SET BIRTHDATE", this::onClick);

        builder.setNegativeButton("I DON'T KNOW", this::onClick);

        Dialog dialog = builder.create();

        dialog.setCanceledOnTouchOutside(false);

        return dialog;


    } // End of DateChooserDialog()


    private Dialog createNewMemberCardDialog() { Log.d(TAG, "NewMemberCardDialog");



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();


        builder.setTitle("Add " + member.firstName + "?");


        View view = inflater.inflate(R.layout.dialog_new_member_card, null);

        ( (TextView) view.findViewById(R.id.dialog_new_member_firstname)).setText(member.firstName);
        ( (TextView) view.findViewById(R.id.dialog_new_member_lastname)).setText(member.lastName);
        ( (TextView) view.findViewById(R.id.dialog_new_member_birthdate)).setText(member.birthdate);
        ( (TextView) view.findViewById(R.id.dialog_new_member_middlename)).setText(member.middleName);


        builder.setView(view);

        builder.setPositiveButton("Add " + member.firstName, this::onClick);
        builder.setNegativeButton("Cancel", this::onClick);

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;

    } // End of createNewMemberCardDialog

}  // End of Class
