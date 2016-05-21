package dev4a.graphicalview;

import java.util.ArrayList;

import dev4a.system.BettingSystem;

public class Menu {
	
	
	
	protected BettingSystem bettingSystem;
	
	protected String storedPass;
	
	protected ArrayList<Menu> possibleMenus = new ArrayList<>();
	
	public Menu(BettingSystem bs, String storredPass){
		this.bettingSystem = bs;
		this.storedPass = storredPass;
	}
	
	protected void showMenu(){
		
	}
	
	protected int takeAction(int selected){
		return 0;
	}
	
	protected Menu setBettingSystem(BettingSystem bettingSystem, String password) {
		this.bettingSystem = bettingSystem;
		this.storedPass = password;
		return this;
	}
	
}
