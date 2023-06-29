package model.characters;

import engine.Game;
import exceptions.InvalidTargetException;
import exceptions.NoAvailableResourcesException;

public class Explorer extends Hero {

	public Explorer(String name, int maxHp, int attackDmg, int maxActions) {
		super(name, maxHp, attackDmg, maxActions);
	}

	@Override
	public void useSpecial() throws InvalidTargetException, NoAvailableResourcesException {

		super.useSpecial();
		for (int i = 0; i < Game.map.length; i++){
			for (int j = 0; j < Game.map[0].length; j++){
				Game.map[i][j].setVisible(true);
				}

	}
	}


}
