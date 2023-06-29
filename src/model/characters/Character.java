package model.characters;

import engine.Game;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughActionsException;
import model.world.CharacterCell;

import java.awt.Point;

public abstract class Character {
	private final String name;
	private Point location;
	private final int maxHp;
	private int currentHp;
	private final int attackDmg;
	private Character target;

	public String getName() {
		return name;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
		if(currentHp > 0) Game.map[getLocation().x][getLocation().y] = new CharacterCell(this);

	}

	public int getMaxHp() {
		return maxHp;
	}

	public int getCurrentHp() {
		return currentHp;
	}

	public void setCurrentHp(int currentHp) {

		if(currentHp <= 0) {
			this.currentHp = 0;
			onCharacterDeath();
		}

		else if(currentHp < maxHp)
			this.currentHp = currentHp;

		else this.currentHp = maxHp;
	}

	public int getAttackDmg() {
		return attackDmg;
	}

	public Character getTarget() {
		return target;
	}

	public void setTarget(Character target) {
		this.target = target;
	}

	public Character(String name,int maxHp, int attackDmg) {
		this.name = name;
		this.maxHp = maxHp;
		currentHp = maxHp;
		this.attackDmg = attackDmg;
	}


	public void attack() throws NotEnoughActionsException, InvalidTargetException {

		if (target == null) throw new InvalidTargetException();

		Point targetLocation = target.getLocation();

		if(checkAdjacentCells(targetLocation)){
			target.setCurrentHp(target.getCurrentHp() - attackDmg);
			target.defend(this);
		}
		else throw new InvalidTargetException();
	}

	public void defend(Character c){
		target = c;
		target.setCurrentHp(target.getCurrentHp() - (attackDmg/2));
	}

	public void onCharacterDeath(){
		Game.map[location.x][location.y] = new CharacterCell(null);
	}
	
	public boolean checkAdjacentCells(Point targetLocation){
		if (location == null || targetLocation == null) return false;
		if (targetLocation.x == location.x && targetLocation.y == location.y) return true;
		if (targetLocation.x == location.x && targetLocation.y == location.y + 1) return true;
		if (targetLocation.x == location.x && targetLocation.y == location.y - 1) return true;
		if (targetLocation.x == location.x + 1 && targetLocation.y == location.y) return true;
		if (targetLocation.x == location.x - 1 && targetLocation.y == location.y) return true;
		if (targetLocation.x == location.x + 1 && targetLocation.y == location.y + 1) return true;
		if (targetLocation.x == location.x + 1&& targetLocation.y == location.y - 1) return true;
		if (targetLocation.x == location.x - 1 && targetLocation.y == location.y + 1) return true;
		return targetLocation.x == location.x - 1 && targetLocation.y == location.y - 1;
	}
	

}
