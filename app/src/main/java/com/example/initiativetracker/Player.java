package com.example.initiativetracker;


public class Player implements Comparable<Player> {
    public String name;
    public String initiative;
    public String initiativeBonus;
    public String armorClass;
    Boolean isTurn;

    public Player(String name, String initiative, String initiativeBonus, String armorClass){
        this.name = name;
        this.initiative = initiative;
        this.initiativeBonus = initiativeBonus;
        this.armorClass = armorClass;
        this.isTurn = false;
    }

    //Getters

    public String getName(){
        return this.name;
    }

    public String getInitiative(){
        return this.initiative;
    }

    public String getInitiativeBonus(){
        return this.initiativeBonus;
    }

    public String getArmorClass(){
        return this.armorClass;
    }

    public Boolean getisTurn(){
        return this.isTurn;
    }

    //Setters

    public void setName(String name){
        this.name = name;
    }

    public void setInitiative(String initiative){
        this.initiative = initiative;
    }

    public void setInitiativeBonus(String initiativeBonus){
        this.initiativeBonus = initiativeBonus;
    }

    public void setArmorClass(String armorClass){
        this.armorClass = armorClass;
    }

    public void setIsTurn(Boolean turn){
        this.isTurn = turn;
    }


    @Override
    public int compareTo(Player player){
        int playerInitiative = Integer.parseInt(((Player)player).getInitiative());


        return playerInitiative - Integer.parseInt(this.initiative);
    }


}
