package model.characters;

import engine.Game;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughActionsException;
import model.world.Cell;
import model.world.CharacterCell;

import java.awt.*;
import java.util.Random;

import static engine.Game.map;
import static model.world.Cell.isCharacterCell;

public class Zombie extends Character {
	private static int ZOMBIES_COUNT = 1;

	public Zombie() {
		super("Zombie " + ZOMBIES_COUNT, 40, 10);
		ZOMBIES_COUNT++;
	}

	public void setTarget() {
		//looks at adjacent cells and selects the first hero to the zombie

		//prevents "Ghost" zombies attacking in empty maps
		/*if			(isCharacterCell(map[getLocation().x][getLocation().y])
				&& (((CharacterCell)map[getLocation().x][getLocation().y]).getCharacter()) != this)
					return;*/

		Point p = getLocation();
		int x = p.x;
		int y = p.y;

		Cell[] adjacentCells = new Cell[8];

		if(x < map.length-1) 						adjacentCells[0] = map[x+1][y];
		if(x > 0) 									adjacentCells[1] = map[x-1][y];
		if(y < map[0].length-1) 					adjacentCells[2] = map[x][y+1];
		if(y > 0) 									adjacentCells[3] = map[x][y-1];
		if(x < map.length-1 && y < map[0].length-1) adjacentCells[4] = map[x+1][y+1];
		if(x > 0 && y > 0)							adjacentCells[5] = map[x-1][y-1];
		if(x < map.length-1 && y > 0) 				adjacentCells[6] = map[x+1][y-1];
		if(x > 0 && y < map[0].length-1) 			adjacentCells[7] = map[x-1][y+1];

		for (int i = 0; i < adjacentCells.length; i++) {
			if (isCharacterCell(adjacentCells[i])) {
				if (((CharacterCell)adjacentCells[i]).getCharacter() != null && ((CharacterCell)adjacentCells[i]).getCharacter() instanceof Hero) {
					setTarget(((CharacterCell)adjacentCells[i]).getCharacter());
					return;
				}
			}
		}
		setTarget(null);
	}

	@Override
	public void attack() throws NotEnoughActionsException, InvalidTargetException {
		setTarget();
		if(getTarget()==null) return;
		if (getTarget() instanceof Zombie) throw new InvalidTargetException();
		super.attack();
	}

	@Override
	public void onCharacterDeath() {

		Game.zombies.remove(this);
		super.onCharacterDeath();

		Point p = Game.randomEmptyPoint();
		Zombie z = new Zombie();
		Game.zombies.add(z);
		z.setLocation(p);

	}

}
