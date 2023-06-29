package model.characters;

import exceptions.InvalidTargetException;
import exceptions.NoAvailableResourcesException;

public class Medic extends Hero {

	public Medic(String name, int maxHp, int attackDmg, int maxActions) {
		super(name, maxHp, attackDmg, maxActions);
	}

	@Override
	public void useSpecial() throws InvalidTargetException, NoAvailableResourcesException {
		
		Character target= getTarget();

		if(target instanceof Zombie) throw new InvalidTargetException();
		if(target == null) throw new InvalidTargetException();
		if(!checkAdjacentCells(target.getLocation())) throw new InvalidTargetException();

		super.useSpecial();
		target.setCurrentHp(target.getMaxHp());
	}
}
