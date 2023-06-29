package model.world;

import java.util.ArrayList;

import model.characters.Character;
import model.characters.Hero;

public class CharacterCell extends Cell {
	
	private Character character;
	private boolean isSafe;
	
	
	
	public CharacterCell(Character character) {
		super();
		this.character = character;
		this.isSafe = false;
	}
	
	public Character getCharacter() {
		return character;
	}
	
	public void setCharacter(Character character) {
		this.character = character;
	}
	
	public boolean isSafe() {
		return isSafe;
	}
	
	public void setSafe(boolean isSafe) {
		this.isSafe = isSafe;
	}

	@Override
	public String toString() {
	//	if(isVisible()) {
			if (character instanceof Hero) {
				return "H";
			} else if (character != null) {
				return "Z";
			}
			return ".";
	//	}
	//	else return "X";
	}
}
