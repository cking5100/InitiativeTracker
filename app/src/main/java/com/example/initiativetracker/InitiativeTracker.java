package com.example.initiativetracker;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InitiativeTracker extends AppCompatActivity implements CharacterDialogFragment.CharacterDialogListener {

    private RecyclerView recyclerView;
    private PlayerAdapter adapter;
    private List<Player> playerList;


    int currentTurnPosition = -1;
    int oldListSize;
    Boolean loweredInitOnEdit = false;

    Bundle savedState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initiative_tracker);

        recyclerView = (RecyclerView) findViewById(R.id.initiativeRecyclerView);

        playerList = new ArrayList<>();

        adapter = new PlayerAdapter(this, playerList);

        adapter.setOnItemClickListener(new PlayerAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(PlayerAdapter.MyViewHolder holder) {
                DialogFragment eChar = new CharacterDialogFragment();
                ((CharacterDialogFragment) eChar).addHolder(holder);
                eChar.show(getSupportFragmentManager(), "edit");

            }
        });



        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepPlayers(); //temporary. remove after i figure out dynamically adding
        oldListSize = playerList.size();

        savedState = savedInstanceState;
    }

    private void prepPlayers(){
        Player a = new Player("Arian Glass", "20", "4", "16");
        playerList.add(a);

        a = new Player("Mournzabel", "5", "2", "14");
        playerList.add(a);

        a = new Player("Precious", "25", "5", "17");
        playerList.add(a);

        a = new Player("Tippletoe", "10", "1", "16");
        playerList.add(a);

        a = new Player("Karly", "19", "4", "16");
        playerList.add(a);

        a = new Player("enemy", "13", "2", "14");
        playerList.add(a);

        a = new Player("Big Bad", "5", "3", "18");
        playerList.add(a);


        Collections.sort(playerList);
        adapter.notifyDataSetChanged();

    }

    public void startCombat(View view){
        if (playerList.size() == 0){
            return;
        }
        if (currentTurnPosition != -1){
            clearTurns();
        }

        Button startStop = (Button) view.findViewById(R.id.startButton);
        String text = startStop.getText().toString();

        if (text.equals("Stop")){
            currentTurnPosition = -1;
            startStop.setText("Start");
            adapter.notifyDataSetChanged();
            return;
        }

        currentTurnPosition = 0;
        startStop.setText("Stop");
        startTurn();

    }

    public void nextTurn(View view){
        if (playerList.size() == 0){
            return;
        }
        if (currentTurnPosition < 0){
            return;
        }

        if (loweredInitOnEdit){
            currentTurnPosition--;
            loweredInitOnEdit = false;
        }

        fixCurrentTurn();

        endTurn();

        if (currentTurnPosition == playerList.size()-1){
            currentTurnPosition = -1;
        }
        currentTurnPosition++;
        startTurn();
    }

    public void previousTurn(View view){
        if (playerList.size() == 0){
            return;
        }
        if (currentTurnPosition < 0){
            return;
        }

        if (loweredInitOnEdit){
            loweredInitOnEdit = false;
        }

        if (oldListSize != playerList.size()){
            if(fixCurrentTurn()){
                startTurn();
                return;
            }

        }

        endTurn();

        if (currentTurnPosition == 0){
            currentTurnPosition = playerList.size();
        }
        currentTurnPosition--;
        startTurn();
    }

    public void startTurn(){
        Player currentPlayer = playerList.get(currentTurnPosition);
        currentPlayer.isTurn = true;
        adapter.notifyDataSetChanged();
    }


    public void endTurn(){
        for (int i = 0; i < playerList.size(); i++){
            Player temp = playerList.get(i);
            if (temp.isTurn){
                temp.isTurn = false;
            }
        }

    }

    public boolean fixCurrentTurn(){
        if (oldListSize == playerList.size()){
            return false;
        }
        boolean noTurn = true;
        int newPosition = currentTurnPosition;
        for (int i = 0; i < playerList.size(); i++){
            Player temp = playerList.get(i);
            if (temp.isTurn){
                noTurn = false;
                newPosition = i;
                break;
            }
        }

        if(noTurn){
            if (currentTurnPosition == 0){
                currentTurnPosition = playerList.size()-1;
            }
            else {
                currentTurnPosition--;
            }
        }
        else{
            currentTurnPosition = newPosition;
            oldListSize = playerList.size();
            return noTurn;
        }
        oldListSize = playerList.size();
        return noTurn;

    }

    public void clearTurns(){
        for(int i = 0; i < playerList.size(); i++){
            Player temp = playerList.get(i);
            temp.isTurn = false;
        }
    }


    public void createNewCharacter(View view){
        DialogFragment newChar = new CharacterDialogFragment();
        newChar.show(getSupportFragmentManager(), "new");
    }


    public void deleteItemFromListByName(String name){
        for (int i = 0; i < playerList.size(); i++){
            if (name.equals(playerList.get(i).name)) {
                playerList.remove(i);

            }
        }

        adapter.notifyDataSetChanged();
    }




    @Override
    public void onDialogPositiveClick(DialogFragment dialog, ArrayList<EditText> l, int editCode, PlayerAdapter.MyViewHolder h) {
        Toast.makeText(this, "Creation Complete", Toast.LENGTH_SHORT).show();
        Log.i("positive", "click");

        String newName = l.get(0).getText().toString();
        String initiative = l.get(3).getText().toString();
        String initBonus = l.get(2).getText().toString();
        String ac = l.get(1).getText().toString();


        if(newName.equals("")){
            Log.i("name", "NULL!!");
            if(editCode == 1) {
                newName = "No Name";
            }else{
                newName = h.name.getText().toString();
            }
        }

        if(initiative.equals("")){
            Log.i("initiative", "NULL!!");
            if (editCode == 1) {
                initiative = "0";
            }else {
                initiative = h.initiative.getText().toString();
            }
        }
        if(initBonus.equals("")){
            Log.i("Initiative Bonus", "NULL!!");
            if (editCode == 1) {
                initBonus = "0";
            }else {
                initBonus = h.initiativeBonus.getText().toString();
            }

        }
        if(ac.equals("")){
            Log.i("AC", "NULL!!");
            if (editCode == 1) {
                ac = "0";
            }else {
                ac = h.armorClass.getText().toString();
            }
        }


        if (editCode == 1) {
            Player newPlayer = new Player(newName, initiative, initBonus, ac);
            playerList.add(newPlayer);
        }else {
            for (int i = 0; i < playerList.size(); i++){
                Player temp = playerList.get(i);
                if (temp.name.equals(h.name.getText().toString()) && temp.initiative.equals(h.initiative.getText().toString()) &&
                    temp.armorClass.equals(h.armorClass.getText().toString()) && temp.initiativeBonus.equals(h.initiativeBonus.getText().toString())){
                    temp.name = newName;
                    temp.initiativeBonus = initBonus;
                    if (Integer.parseInt(initiative) < Integer.parseInt(temp.initiative)){
                        loweredInitOnEdit = true;
                    }
                    temp.initiative = initiative;
                    temp.armorClass = ac;
                    break;
                }
            }

        }

        Collections.sort(playerList);
        adapter.notifyDataSetChanged();

        dialog.dismiss();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

        Toast.makeText(this, "Cancel new Character", Toast.LENGTH_SHORT).show();

        dialog.dismiss();
    }

    @Override
    public void onDialogNeutralClick(DialogFragment dialog, PlayerAdapter.MyViewHolder holder){
        Log.i("neutral", "click");
        deleteItemFromListByName(holder.name.getText().toString());

    }
}
