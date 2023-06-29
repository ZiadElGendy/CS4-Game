package model.world;

import model.collectibles.Collectible;

public class CollectibleCell extends Cell {
	private final Collectible collectible;

	public CollectibleCell(Collectible collectible) {
		super();
		this.collectible = collectible;
	}

	public Collectible getCollectible() {
		return collectible;
	}

	@Override
	public String toString() {
	//	if(isVisible()) {
			if (collectible instanceof model.collectibles.Supply) {
				return "S";
			}
			else if (collectible instanceof model.collectibles.Vaccine) {
				return "V";
			}
	//	}
		return "X";
	}
}
