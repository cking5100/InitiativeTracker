package com.example.initiativetracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.MyViewHolder> {

    private Context context;
    private List<Player> playerList;

    public interface OnItemClickListener{
        void onItemClicked(MyViewHolder holder);
    }

    OnItemClickListener mlistener;

    public void setOnItemClickListener(OnItemClickListener listener){
        mlistener = listener;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name, armorClass, initiativeBonus, initiative;
        public LinearLayout playerCard;


        public MyViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.nameTextView);
            armorClass = (TextView) view.findViewById(R.id.armorClassTextView);
            initiativeBonus = (TextView) view.findViewById(R.id.initiativeBonusTextView);
            initiative = (TextView) view.findViewById(R.id.initiativeTextView);
            playerCard = (LinearLayout) view.findViewById(R.id.playerCardLinearLayout);
        }
    }

    public PlayerAdapter(Context context, List<Player> playerList){
        this.context = context.getApplicationContext();
        this.playerList = playerList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_card, parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Player player = playerList.get(position);
        holder.name.setText(player.getName());
        if (player.isTurn) {
            holder.name.setBackgroundColor(Color.GREEN);
        } else {
            holder.name.setBackgroundColor(Color.WHITE);
        }

        holder.initiative.setText(player.getInitiative());
        holder.initiativeBonus.setText(player.getInitiativeBonus());
        holder.armorClass.setText(player.getArmorClass());

        holder.playerCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "longClick", Toast.LENGTH_SHORT).show();
                mlistener.onItemClicked(holder);

                return true;
            }
        });

    }

    @Override
    public int getItemCount(){
        return playerList.size();
    }








}
