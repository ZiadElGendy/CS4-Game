package model.world;

public abstract class Cell {
	private boolean isVisible;
	
	public Cell(){}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}


	public static boolean isCollectibleCell(Cell c){
		return c instanceof CollectibleCell;
	}
	public static boolean isCharacterCell(Cell c){
		return c instanceof CharacterCell;
	}
	public static boolean isTrapCell(Cell c){
		return c instanceof TrapCell;
	}



}
