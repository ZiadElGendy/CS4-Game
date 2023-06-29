package model.characters;

import java.awt.*;
import java.util.*;

import engine.Game;
import exceptions.InvalidTargetException;
import exceptions.MovementException;
import exceptions.NoAvailableResourcesException;
import exceptions.NotEnoughActionsException;
import model.collectibles.Supply;
import model.collectibles.Vaccine;
import model.world.Cell;
import model.world.CharacterCell;
import model.world.CollectibleCell;
import model.world.TrapCell;

import static engine.Game.map;

public abstract class Hero extends Character {
	private int actionsAvailable;
	private final int maxActions;
	private boolean specialAction;
	private final ArrayList<Vaccine> vaccineInventory;
	private final ArrayList<Supply> supplyInventory;

	public int getActionsAvailable() {
		return actionsAvailable;
	}

	public void setActionsAvailable(int actionsAvailable) {
		this.actionsAvailable = actionsAvailable;
	}

	public boolean isSpecialAction() {
		return specialAction;
	}

	public void setSpecialAction(boolean specialAction) {
		this.specialAction = specialAction;
	}

	public int getMaxActions() {
		return maxActions;
	}

	public ArrayList<Vaccine> getVaccineInventory() {
		return vaccineInventory;
	}

	public ArrayList<Supply> getSupplyInventory() {
		return supplyInventory;
	}


	public Hero(String name, int maxHp, int attackDmg, int maxActions) {
		super(name, maxHp, attackDmg);
		this.maxActions = maxActions;
		this.actionsAvailable = maxActions;
		vaccineInventory = new ArrayList<Vaccine>();
		supplyInventory = new ArrayList<Supply>();
	}


/*	public void addCollectible (Object o){
		if (o instanceof Vaccine) {
			vaccineInventory.add((Vaccine) o);
		} else if (o instanceof Supply) {
			supplyInventory.add((Supply) o);
		}
	}

	public void useCollectible (Object o){
		try {
			if (o instanceof Vaccine) {
				vaccineInventory.remove((Vaccine) o);
			} else if (o instanceof Supply) {
				//supplyInventory.remove((Supply) o);
			}
		} catch (Exception e) {
			throw new NoAvailableResourcesException();
		}
	}*/


	@Override
	public void attack() throws NotEnoughActionsException, InvalidTargetException {

		if (getTarget() instanceof Hero) throw new InvalidTargetException("Can't attack Hero!");
		if (actionsAvailable <= 0) throw new NotEnoughActionsException();

		super.attack();
		actionsAvailable--;

	}

	public void useSpecial() throws NoAvailableResourcesException, InvalidTargetException {

		if (supplyInventory.isEmpty()) throw new NoAvailableResourcesException("Not enough supplies!");

		Supply s = supplyInventory.get(0);
		s.use(this);
	}

	public void cure() throws NoAvailableResourcesException, InvalidTargetException, NotEnoughActionsException {

		if (getTarget() == null || !(getTarget() instanceof Zombie) || !checkAdjacentCells(getTarget().getLocation()))
			throw new InvalidTargetException();
		if (actionsAvailable <= 0)
			throw new NotEnoughActionsException();
		if (vaccineInventory.isEmpty())
			throw new NoAvailableResourcesException("Not enough supplies!");

		Vaccine v = vaccineInventory.get(0);
		v.use(this);

		actionsAvailable--;
	}

	public void move(Direction d) throws NotEnoughActionsException, MovementException {

		//if(getCurrentHp() <= 0) return;
		if(actionsAvailable <= 0) throw new NotEnoughActionsException();

		Point oldLocation = getLocation();
		Point newLocation;

		//generates new point based on direction input
		switch(d){
			case UP:
				newLocation = new Point(getLocation().x+1,getLocation().y);
				if(isOutOfBounds(newLocation)) throw new MovementException();
				break;
			case DOWN:
				newLocation = new Point(getLocation().x-1,getLocation().y);
				if(isOutOfBounds(newLocation)) throw new MovementException();
				break;
			case LEFT:
				newLocation = new Point(getLocation().x,getLocation().y-1);
				if(isOutOfBounds(newLocation)) throw new MovementException();
				break;
			case RIGHT:
				newLocation = new Point(getLocation().x,getLocation().y+1);
				if(isOutOfBounds(newLocation)) throw new MovementException();
				break;
			default:
				throw new MovementException();
		}

		actionsAvailable--;

		//Update maps and inventories if necessary
		int newX = newLocation.x;
		int newY = newLocation.y;

		if(Cell.isCollectibleCell(map[newX][newY])){

			((CollectibleCell) map[newX][newY]).getCollectible().pickUp(this);
			updateMap(newLocation, oldLocation);
			setLocation(newLocation);

		}
		else if(Cell.isCharacterCell(map[newX][newY])){

			if( ((CharacterCell) map[newX][newY]).getCharacter() == null){
				updateMap(newLocation, oldLocation);
				setLocation(newLocation);
			}
			else throw new MovementException("Invalid move!");

		}
		else if(Cell.isTrapCell(map[newX][newY])){

			int newHp = getCurrentHp() - ((TrapCell) map[newX][newY]).getTrapDamage();
			if (newHp <= 0) {
				//removeVisibility(newLocation);
				map[newLocation.x][newLocation.y] = new CharacterCell(null);
			}
			else {
				updateMap(newLocation, oldLocation);
				setLocation(newLocation);
			}
			setCurrentHp(newHp);
		}

		updateVisibility(newLocation);

	}


	public void updateMap(Point newP, Point oldP){
		//moves hero in map and makes old location empty
		if(getCurrentHp() > 0)  map[newP.x][newP.y] = new CharacterCell(this);
		map[oldP.x][oldP.y] = new CharacterCell(null);
	}

	public void updateVisibility(Point newP){
		//makes all adjacent cells visible
		if(getCurrentHp() <= 0) return;
		int x = newP.x;
		int y = newP.y;
														map[x][y].setVisible(true);
		if (x < map.length-1) 							map[x+1][y].setVisible(true);
		if (x > 0) 										map[x-1][y].setVisible(true);
		if (y < map[0].length-1) 						map[x][y+1].setVisible(true);
		if (y > 0) 										map[x][y-1].setVisible(true);
		if (x < map.length-1 && y <map[0].length-1)		map[x+1][y+1].setVisible(true);
		if (x > 0 && y > 0) 							map[x-1][y-1].setVisible(true);
		if (x < map.length-1 && y > 0) 					map[x+1][y-1].setVisible(true);
		if (x > 0 && y < map[0].length-1) 				map[x-1][y+1].setVisible(true);
	}
	
	/*public void removeVisibility(Point newP){
		//makes all adjacent cells invisible

		int x = newP.x;
		int y = newP.y;
														map[x][y].setVisible(false);
		if (x < map.length-1) 							map[x+1][y].setVisible(false);
		if (x > 0) 										map[x-1][y].setVisible(false);
		if (y < map[0].length-1) 						map[x][y+1].setVisible(false);
		if (y > 0) 										map[x][y-1].setVisible(false);
		if (x < map.length-1 && y <map[0].length-1)		map[x+1][y+1].setVisible(false);
		if (x > 0 && y > 0) 							map[x-1][y-1].setVisible(false);
		if (x < map.length-1 && y > 0) 					map[x+1][y-1].setVisible(false);
		if (x > 0 && y < map[0].length-1) 				map[x-1][y+1].setVisible(false);
	}*/

	public boolean isOutOfBounds(Point p) throws MovementException {
		//checks if point is out of bounds of map
		int x = p.x;
		int y = p.y;
		if(x < 0 || x > 14 || y < 0 || y > 14){
			throw new MovementException("You can't move out of bounds!");
		}
		return false;
	}

	@Override
	public void onCharacterDeath() {
		Game.heroes.remove(this);
		super.onCharacterDeath();
	}

}
