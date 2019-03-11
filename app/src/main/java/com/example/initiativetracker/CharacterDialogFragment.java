package com.example.initiativetracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class CharacterDialogFragment extends DialogFragment {


    public interface CharacterDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog, ArrayList<EditText> l, int editCode, PlayerAdapter.MyViewHolder h);
        public void onDialogNegativeClick(DialogFragment dialog);
        public void onDialogNeutralClick(DialogFragment dialog, PlayerAdapter.MyViewHolder holder);
    }

    CharacterDialogListener listener;
    PlayerAdapter.MyViewHolder holder;
    ArrayList<EditText> characterStats = new ArrayList<>(4);


    public void addHolder(PlayerAdapter.MyViewHolder myHolder){
        this.holder = myHolder;
    }





    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        try {
            listener = (CharacterDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(e.toString() + " must implement CharacterDialogListener");
        }
    }



    private void setUpViewForEdit(View view, PlayerAdapter.MyViewHolder holder){
        String curName = holder.name.getText().toString();
        EditText name = (EditText) view.findViewById(R.id.characterNameEditText);
        name.setText(curName);
        String curAc = holder.armorClass.getText().toString();
        EditText ac = (EditText)view.findViewById(R.id.armorClassEditText);
        ac.setText(curAc);
        String currIB = holder.initiativeBonus.getText().toString();
        EditText ib = (EditText)view.findViewById(R.id.bonusEditText);
        ib.setText(currIB);
        String currInit = holder.initiative.getText().toString();
        EditText init = (EditText) view.findViewById(R.id.initiativeEditText);
        init.setText(currInit);
    }

    private void setUpStatsArray(View view, ArrayList l){
        EditText name = (EditText) view.findViewById(R.id.characterNameEditText);
        EditText ac = (EditText)view.findViewById(R.id.armorClassEditText);
        EditText ib = (EditText)view.findViewById(R.id.bonusEditText);
        EditText init = (EditText) view.findViewById(R.id.initiativeEditText);

        l.add(0, name);
        l.add(1,ac);
        l.add(2,ib);
        l.add(3,init);

    }




    public Dialog onCreateDialog(Bundle savedInstanceState){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        String where = getTag(); //which will be new or edit
        Log.i("Where", where);

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_create_character, null);


        switch (where){
            case "new":
                builder.setMessage(R.string.dialog_create_new_character);
                setUpStatsArray(view, characterStats);
                builder.setView(view);
                builder.setPositiveButton(R.string.dialog_create_new_character, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogPositiveClick(CharacterDialogFragment.this, characterStats, 1, holder); //1 for new character
                    }
                });

                builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogNegativeClick(CharacterDialogFragment.this);
                    }
                });
                return builder.create();

            case "edit":
                builder.setMessage(R.string.dialog_edit_character);
                //Set up the view with old information to be changed
                setUpViewForEdit(view, holder);
                setUpStatsArray(view, characterStats);
                //set the edited view
                builder.setView(view);

                builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogNegativeClick(CharacterDialogFragment.this);

                    }
                });

                builder.setPositiveButton(R.string.dialog_edit_done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogPositiveClick(CharacterDialogFragment.this, characterStats, 2, holder); //2 for edit character

                    }

                });

                builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogNeutralClick(CharacterDialogFragment.this, holder);

                    }
                });
                return builder.create();

            default:

                return null;

        }


    }


}
