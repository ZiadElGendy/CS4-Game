package model.world;

import java.util.Random;

public class TrapCell extends Cell {
	private final int trapDamage;

	public TrapCell() {
		super();
		Random r = new Random(); 
		this.trapDamage = (r.nextInt(3)+ 1)*10;
	}

	public int getTrapDamage() {
		return trapDamage;
	}

	@Override
	public String toString() {
	//	if(isVisible()) {
			return "!";
	//	}
	//	else return "X";
	}

}
