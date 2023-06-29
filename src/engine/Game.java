package engine;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

import exceptions.InvalidTargetException;
import exceptions.NotEnoughActionsException;
import model.characters.*;
import model.characters.Character;
import model.collectibles.Supply;
import model.collectibles.Vaccine;
import model.world.Cell;
import model.world.CharacterCell;
import model.world.CollectibleCell;
import model.world.TrapCell;
import static model.characters.Direction.*;
import static model.world.Cell.isCharacterCell;
import static model.world.Cell.isCollectibleCell;

public class Game {
	public static ArrayList<Hero> availableHeroes = new ArrayList<>();
	public static ArrayList<Hero> heroes = new ArrayList<>();
	public static ArrayList<Zombie> zombies = new ArrayList<>();

	public static ArrayList<Agent> agents = new ArrayList<>();

	public static Cell [][] map = new Cell[15][15];
	
	public Game() {
	}
	
	public static void loadHeroes(String filePath) throws IOException {
		
		//Creates File object to use, I think it's not necessary, but I couldn't get it to work with constructing a reader with the string directly
		BufferedReader br = new BufferedReader(new FileReader(filePath)); //FileReader is used to open the file, BufferedReader is used for its readLine() method
		String line = ""; //Initialize variable to use in loopFile
		
		while ((line = br.readLine()) != null) { //Loops until line is empty
		    String[] heroData = line.split(","); //Variable line is each line in the CSV file, heroData is line split into array of Strings with commas being the splitter
		    
		    switch(heroData[1]) { // Type is in index 1 of line
		    
		    case "FIGH": Fighter f = new Fighter (heroData[0],
		    		Integer.parseInt(heroData[2]), // parseInt is used to convert from String to int, order is weird because the CSV doesn't match the order of the constructor
		    		Integer.parseInt(heroData[4]),
		    		Integer.parseInt(heroData[3]));
		    		availableHeroes.add(f);
		    		break;
		    		
		    case "EXP": Explorer e = new Explorer (heroData[0],
		    		Integer.parseInt(heroData[2]),
		    		Integer.parseInt(heroData[4]),
		    		Integer.parseInt(heroData[3]));
		    		availableHeroes.add(e);
		    		break;
		    		
		    case "MED": Medic m = new Medic (heroData[0],
		    		Integer.parseInt(heroData[2]),
		    		Integer.parseInt(heroData[4]),
		    		Integer.parseInt(heroData[3])); 
		    		availableHeroes.add(m);
		    		break;		
		    }		    
		}
		br.close();


	}
	
	public static void startGame(Hero h){

		try {
			loadHeroes("Heros.csv");
		}
		catch(Exception e){}

		//generate empty map
		for(int i = 0; i < map.length; i++){
			for(int j = 0; j < map[0].length; j++){
				Game.map[i][j] = new CharacterCell(null);
				Game.map[i][j].setVisible(false);
			}
		}

		//add 5 vaccine to map
		for(int i = 0; i < 5; i++){
			Point p = randomEmptyPoint();
			while(p.x == 0 && p.y == 0){
				p = randomEmptyPoint();
			}
			Vaccine v = new Vaccine();
			Game.map[p.x][p.y] = new CollectibleCell(v);
		}

		// 5 supply
		for(int i = 0; i < 5; i++){
			Point p = randomEmptyPoint();
			while(p.x == 0 && p.y == 0){
				p = randomEmptyPoint();
			}
			Supply s = new Supply();
			Game.map[p.x][p.y] = new CollectibleCell(s);
		}

		// 5 trap
		for(int i = 0; i < 5; i++){
			Point p = randomEmptyPoint();
			while(p.x == 0 && p.y == 0){
				p = randomEmptyPoint();
			}
			Game.map[p.x][p.y] = new TrapCell();
		}

		// 10 zombie
		for(int i = 0; i < 10; i++){
			Point p = randomEmptyPoint();
			while(p.x == 0 && p.y == 0){
				p = randomEmptyPoint();
			}
			Zombie z = new Zombie();
			z.setLocation(p);
			zombies.add(z);
			Game.map[p.x][p.y] = new CharacterCell(z);
		}



		//choose random hero from availableHeroes
		//Random r = new Random();
		//Hero newHero = Game.availableHeroes.get(r.nextInt(Game.availableHeroes.size()));

		//nope just uses the input hero
		availableHeroes.remove(h);
		heroes.add(h);
		h.setLocation(new Point(0,0));
		//h.updateVisibility(new Point(0,0));

		Game.map[0][0]= new CharacterCell(h);
		Game.map[0][0].setVisible(true);//character pos
		Game.map[1][0].setVisible(true); //above
		Game.map[1][1].setVisible(true); //diagonal
		Game.map[0][1].setVisible(true); //right

	}

	public static boolean checkWin(){

			//checks all vaccines used
		//checks that no vaccines in map
		for (int i = 0; i < map.length; i++){
			for (int j = 0; j < map[0].length; j++){
				if(isCollectibleCell(map[i][j]) && ((CollectibleCell) (map[i][j])).getCollectible() instanceof Vaccine)
					return false;
			}
		}

		//checks that no vaccines in heroes inventory
		for(int i =0; i< heroes.size(); i++){
			Hero h = heroes.get(i);
			if(h.getVaccineInventory().size()!=0) return false;
		}

		//checks that there are 5 or more heroes
		return heroes.size() >= 5;
	}

	public static boolean checkGameOver(){
		//returns true if game is lost by death of all heroes
		//or won by using all vaccines with 5 or more heroes

		//if all heroes are dead then game over
		if(heroes.isEmpty()) return true;
		if(availableHeroes.isEmpty()) return true;

			//checks all vaccines used
		//checks that no vaccines in map
		for (int i = 0; i < map.length; i++){
			for (int j = 0; j < map[0].length; j++){
				if(isCollectibleCell(map[i][j]) && ((CollectibleCell) (map[i][j])).getCollectible() instanceof Vaccine)
					return false;
			}
		}
		//checks that no vaccines in heroes inventory
		for(int i =0; i< heroes.size(); i++){
			Hero h = heroes.get(i);
			if(h.getVaccineInventory().size()!=0) return false;
		}

		return true;
	}

	public static void endTurn() throws InvalidTargetException, NotEnoughActionsException {

/*		//kill zombies with 0 hp (redundancy)
		for(int i=0; i< zombies.size();i++) {
			if(zombies.get(i).getCurrentHp() == 0)
			(zombies.get(i)).onCharacterDeath();
		}*/

		//allow all zombies to attack
		for (int i = 0; i < zombies.size(); i++){
			zombies.get(i).attack();
		}

		//reset zombie targetting
		for (int i = 0; i < zombies.size(); i++){
			zombies.get(i).setTarget(null);
		}
		
/*		//kill heroes with 0 hp (redundancy)
		for(int i=0; i< heroes.size();i++) {
			if(heroes.get(i).getCurrentHp() == 0)
			(heroes.get(i)).onCharacterDeath();
		}*/

		//sets map to not visible
		for (int i = 0; i < map.length; i++){
			for (int j = 0; j < map[0].length; j++){
				map[i][j].setVisible(false);
				}
			}

		//add new zombie
		if (zombies.size() < 10) {
			Point zp = randomEmptyPoint();
			Zombie z = new Zombie();
			zombies.add(z);
			z.setLocation(zp);
			map[zp.x][zp.y] = new CharacterCell(z);
		}

		//reset actions target special , update map visibility
		for (int i = 0; i < heroes.size(); i++){

			heroes.get(i).setActionsAvailable(heroes.get(i).getMaxActions());
			heroes.get(i).setTarget(null);
			heroes.get(i).setSpecialAction(false);
			heroes.get(i).updateVisibility(heroes.get(i).getLocation());

		}
	}

	public static Point randomEmptyPoint(){
		//returns random point on map that is empty
		Random r = new Random();

		int x = r.nextInt(Game.map.length);
		int y = r.nextInt(Game.map[0].length);

		while (!isCharacterCell(Game.map[x][y]) || ((CharacterCell) (Game.map[x][y])).getCharacter() != null) {
			x = r.nextInt(Game.map.length);
			y = r.nextInt(Game.map[0].length);
		}

		return new Point(x,y);

	}

	//-----------------------------------------------
	//everything past this point is for testing purposes
	//-----------------------------------------------

	//public static void main(String[] args) throws Exception {

			//loadHeroes("Heros.csv");

			/*int maxHp = 1;
			int attackDamage = 1;
			int maxActions = 1;


			Hero testHero0 = new Fighter("Mohamed El Test", maxHp, attackDamage, maxActions);
			Hero testHero1 = new Fighter("Boody Boolean", maxHp, attackDamage, maxActions);
			Hero testHero2 = new Fighter("Amr Error", maxHp, attackDamage, maxActions);
			Hero testHero3 = new Fighter("Chico Test Case", maxHp, attackDamage, maxActions);
			Hero testHero4 = new Fighter("Teezo Test Case", maxHp, attackDamage, maxActions);
			Hero testHero5 = new Fighter("Mahmoud Main", maxHp, attackDamage, maxActions);
			Hero testHero6 = new Fighter("Emad Exception", maxHp, attackDamage, maxActions);
			Hero testHero7 = new Fighter("Hamada Java", maxHp, attackDamage, maxActions);
			Hero testHero8 = new Fighter("Jiko intelliJ", maxHp, attackDamage, maxActions);

			Zombie testZombie = new Zombie();
			zombies.add(testZombie);

			startGame(testHero0);

			for(Zombie z : zombies) {
				if (!z.equals(testZombie)) {

					Point locationZ = z.getLocation();
					((CharacterCell) Game.map[locationZ.x][locationZ.y]).setCharacter(null);
				}
			}


			heroes.add(testHero1);
			heroes.add(testHero2);
			heroes.add(testHero3);
			heroes.add(testHero4);
			heroes.add(testHero5);
			heroes.add(testHero6);
			heroes.add(testHero7);
			heroes.add(testHero8);

			testZombie.setLocation(new Point(3,3));

			int zombieXLocation = 3;
			int zombieYLocation = 3;
			int charCount = 1;
			for (int i = -1; i <= 1; i++) {
				int cx = zombieXLocation + i;
				for (int j = -1; j <= 1; j++) {
					int cy = zombieYLocation + j;

					if (!(cx == zombieXLocation && cy == zombieYLocation)) {
						Point location2 = new Point(cx, cy);

						try {
							heroes.get(charCount).setLocation(location2);
						} catch (Exception e) {
						}

						map[cx][cy] = new CharacterCell(heroes.get(charCount));
						charCount++;
					}
				}
			}

			printMap();
			System.out.println();

			for (int i = 0; i < 8; i++) {

				try {
					endTurn();
					printMap();
					System.out.println();
				} catch (Exception e) {
				}
			}

			System.out.println(heroes.size() <= 1);*/

			//setEnviroment function
			/*
			 * int random = (int) (Math.random() * 1000); String nameHero = "Fighter " +
			 * random; int maxHpHero = (int) (Math.random() * 100); int attackDmgHero =
			 * (int) (Math.random() * 100); int maxActionsHero = (int) (Math.random() * 5);
			 * 
			 * Fighter fighter = new Fighter(nameHero, maxHpHero, attackDmgHero,
			 * maxActionsHero); CharacterCell charCell = new CharacterCell(fighter);
			 * 
			 * Zombie zombie = new Zombie();
			 * 
			 * ArrayList<Hero> medics = new ArrayList<Hero>();
			 * 
			 * Medic createdMedics1 = new Medic("ABC", 1000, 50, 20); Medic createdMedics2 =
			 * new Medic("123", 5000, 500, 100); Medic createdMedics3 = new Medic("XYZ", 50,
			 * 5, 1); medics.add(createdMedics1); medics.add(createdMedics2);
			 * medics.add(createdMedics3);
			 * 
			 * availableHeroes = medics;
			 * 
			 * 
			 * zombie.setLocation(new Point(1, 0));
			 * 
			 * for (Hero h : Game.availableHeroes) { h.setTarget(zombie); }
			 * 
			 * Cell[][] map = new Cell[5][6]; map[0][5] = (Cell) charCell;
			 * 
			 * zombies.add(zombie);
			 * 
			 * ArrayList<Object> returns = new ArrayList<Object>();
			 * returns.add(medics.get(0)); returns.add(zombie); returns.add(charCell);
			 * 
			 * Vaccine v = new Vaccine(); v.pickUp((Hero)returns.get(0));
			 * v.use((Hero)returns.get(0));
			 */
			
			/*int random = (int) (Math.random() * 1000);
			String nameHero = "Fighter " + random;
			int attackDmgHero = (int) (Math.random() * 100);
			int maxActionsHero = 51;
			
			map = new Cell[15][15];
			
			Fighter character = new Fighter(nameHero, 0, attackDmgHero, maxActionsHero);
			character.setLocation(new Point(4, 4));
			character.setActionsAvailable(maxActionsHero);
			
			Cell[][] m = engine.Game.map;
			CharacterCell c = new CharacterCell(null);
			for (int i = -1; i <= 1; i++) {
				int cx = 4 + i;
				for (int j = -1; j <= 1; j++) {
					int cy = 5 + j;
					if (cy >= 0 && cy <= m.length - 1) {
						m[cx][cy] = c;
					}
				}
			}
			m[4][4] = new CharacterCell((Character) character);
			
			character.move(RIGHT);
			Cell newC = (CharacterCell) m[4][5];
			Character newChar = ((CharacterCell) newC).getCharacter();
			System.out.println(newChar.toString());
			*/
			
			/*Medic createdMedics1 = new Medic("Eminem", 1000, 50, 20);
			Explorer zombieObject = new Explorer("Eminem", 1000, 50, 20);
			
			createdMedics1.setTarget(zombieObject);
			createdMedics1.setLocation(new Point(0, 0));
			zombieObject.setLocation(new Point(0, 0));
			
			Supply supply = new Supply();
			//ArrayList<Supply> supplyArray = new ArrayList<>();
			//supplyArray.add((Supply) supply);
			createdMedics1.getSupplyInventory().add(supply);
			
			Object currentHp = zombieObject.getCurrentHp();
			Object maxHp = zombieObject.getMaxHp();

			System.out.println( currentHp );
			System.out.println( maxHp );

			createdMedics1.useSpecial();

			System.out.println( currentHp );
			System.out.println( maxHp );
			System.out.println( currentHp == maxHp );*/

				//resetGameStatics
			/*zombies = new ArrayList<>();
			heroes = new ArrayList<>();
			
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[i].length; j++) {
					map[i][j] = new CharacterCell(null);
					map[i][j].setVisible(false);
				}
			}*/
			
				/*//loadChampions
			PrintWriter csvWriter = new PrintWriter("test_heros.csv");

			ArrayList<Object> heros = new ArrayList<Object>();
			
			for (int i = 0; i < 7; i++) {
				int maxHp = ((int) (Math.random() * 100) + 10);
				int dmg = ((int) (Math.random() * 200) + 10);
				int actions = ((int) (Math.random() * 5) + 5);
				String name = "Hero_" + i;

				String type = "";
				if (i == 0 || i == 4) {
					type = "FIGH";
					Object createdHero = new Fighter(name, maxHp, dmg, actions);
					heros.add(createdHero);

				} else {
					if (i % 2 == 0) {
						type = "MED";
						Object createdHero = new Medic(name, maxHp, dmg, actions);
						heros.add(createdHero);

					} else {
						type = "EXP";
						Object createdHero = new Explorer(name, maxHp, dmg, actions);
						heros.add(createdHero);

					}
				}
				String line = name + "," + type + "," + maxHp + "," + actions + "," + dmg;
				csvWriter.println(line);
			}

			csvWriter.flush();
			csvWriter.close();
			
			availableHeroes = new ArrayList<>();
			loadHeroes("test_heros.csv");
			
			 	//createFighter
			int maxHp = ((int) (Math.random() * 100) + 10);
			int dmg = ((int) (Math.random() * 4) + 3);
			int actions = ((int) (Math.random() * 5) + 5);
			String name = "Hero_";

			Fighter fighter = new Fighter(name, maxHp, dmg, actions);
			
			heros.add(fighter);
			startGame(fighter);
			
			int count = 0;
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[i].length; j++) {
					if (map[i][j] != null && isCollectibleCell(map[i][j])) {
						if ( ((CollectibleCell)map[i][j]).getCollectible() != null
								&&  ((CollectibleCell)map[i][j]).getCollectible() instanceof Vaccine)
							count++;
					}
				}
			}

			
			System.out.print(count);*/
			
			/*int maxHp = 2;
			int dmg = ((int) (Math.random() * 10) + 3);
			int actions = ((int) (Math.random() * 5) + 5);
			String name = "Hero_";

			System.out.println("maxHp: " + maxHp + " dmg: " + dmg + " actions: " + actions);
			
			Object h1 = new Medic(name, maxHp, dmg, actions);
			//heros.add(h1);
			Object z2 = new Zombie();
			zombies.add((Zombie) z2);
			
			((Character) z2).setTarget((Character) h1);

			System.out.println(h1.toString() + " " + z2.toString());

			((Character) z2).setLocation(new Point(5, 5));
			((Character) h1).setLocation(new Point(5, 4));
			
			endTurn();
			
			System.out.println(((CharacterCell)map[5][4]).getCharacter());
			
			
		}

	public static void printMap(){
		for (int i = map.length-1; i >= 0; i--){
			for (int j = 0; j < map[0].length; j++){
				System.out.print(map[i][j].toString());
			}
			System.out.println();
		}
		System.out.println();
	}*/
}
