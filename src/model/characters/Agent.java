package model.characters;

import exceptions.InvalidTargetException;
import exceptions.NotEnoughActionsException;

public abstract class Agent extends Character {

	public Agent(String name, int maxHp, int attackDamage) {
		super(name, maxHp, attackDamage);
	}
	
	 public abstract void attack() throws NotEnoughActionsException,InvalidTargetException;
	
}
