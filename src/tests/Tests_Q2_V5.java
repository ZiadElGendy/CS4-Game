package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import org.junit.Test;

import engine.Game;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughActionsException;
import model.characters.Character;
import model.characters.Fighter;
import model.characters.Hero;
import model.characters.Medic;
import model.characters.Zombie;
import model.collectibles.Vaccine;
import model.world.Cell;
import model.world.CharacterCell;
import model.world.CollectibleCell;

@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
public class Tests_Q2_V5{

	String characterPath = "model.characters.Character";
	String collectiblePath = "model.collectibles.Collectible";
	String vaccinePath = "model.collectibles.Vaccine";
	String supplyPath = "model.collectibles.Supply";
	String fighterPath = "model.characters.Fighter";

	private String charCell = "model.world.CharacterCell";
	private String cellPath = "model.world.Cell";
	private String collectibleCellPath = "model.world.CollectibleCell";
	private String trapCellPath = "model.world.TrapCell";
	private String enumDirectionPath = "model.characters.Direction";

	String gamePath = "engine.Game";
	String medicPath = "model.characters.Medic";
	String explorerPath = "model.characters.Explorer";
	String heroPath = "model.characters.Hero";

	String gameActionExceptionPath = "exceptions.GameActionException";
	String invalidTargetExceptionPath = "exceptions.InvalidTargetException";
	String movementExceptionPath = "exceptions.MovementException";
	String noAvailableResourcesExceptionPath = "exceptions.NoAvailableResourcesException";
	String notEnoughActionsExceptionPath = "exceptions.NotEnoughActionsException";

	String zombiePath = "model.characters.Zombie";
	String agentPath = "model.characters.Agent";
	String militantPath = "model.characters.Militant";
	String revolutionaryPath = "model.characters.Revolutionary";

	public void generateGameWithCharacter(Character c1, Character c2, Point p1, Point p2) throws IllegalArgumentException, IllegalAccessException {
		Hero h = new Medic("Medic", 100, 10, 2);
		Game.startGame(h);
		for (int i = 0; i < Game.map.length; i++) {
			for (int j = 0; j < Game.map[i].length; j++) {
				Game.map[i][j] = new CharacterCell(null);
			}
		}
		Class agentClass = null;
		try {
			agentClass = Class.forName(agentPath);
		} catch (ClassNotFoundException e) {
			assertFalse("Class Agent not found.", true);
		}

		Field f = null;

		try {
			f = Game.class.getDeclaredField("agents");
		} catch (NoSuchFieldException e) {
			assertFalse("Attribute agents not found.", true);
		} catch (SecurityException e) {
			assertFalse("Attribute agents not found.", true);
		}

		f.setAccessible(true);

		ArrayList<Object> agents = (ArrayList<Object>)f.get(null);


		Game.heroes.clear();
		Game.zombies.clear();
		agents.clear();

		Game.map[p1.x][p1.y] = new CharacterCell(c1);
		Game.map[p2.x][p2.y] = new CharacterCell(c2);
		c1.setLocation(p1);
		c2.setLocation(p2);

		if (c1 instanceof Hero) {
			Game.heroes.add((Hero) c1);
		} else if (c1 instanceof Zombie) {
			Game.zombies.add((Zombie) c1);
		}else
		{
			agents.add(c1);
		}

		if (c2 instanceof Hero) {
			Game.heroes.add((Hero) c2);
		} else if (c2 instanceof Zombie) {
			Game.zombies.add((Zombie) c2);
		}else
		{
			agents.add(c2);
		}
	}


	@Test(timeout = 100)
	public void testClassMilitantExistsAndIsSubclassOfAgent() {
		Class militantClass = null;
		try {
			militantClass = Class.forName(militantPath);
		} catch (ClassNotFoundException e) {
			assertFalse("Class Militant not found.", true);
		}
		Class agentClass = null;
		try {
			agentClass = Class.forName(agentPath);
		} catch (ClassNotFoundException e) {
			assertFalse("Class Agent not found.", true);
		}
		assertTrue("Class Militant should be a subclass of Agent.", agentClass.isAssignableFrom(militantClass));
	}

	
	
	@Test(timeout = 100)
	public void testMilitantInitializedCorrectly() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Class militantClass = null;
		try {
			militantClass = Class.forName(militantPath);
		} catch (ClassNotFoundException e) {
			assertFalse("Class Militant not found.", true);
		}

		Constructor constructor = null;
		try {
			constructor = militantClass.getConstructor();
		} catch (NoSuchMethodException e) {
			assertTrue("Constructor of Militant not found.", false);
		} catch (SecurityException e) {
			assertTrue("Constructor of Militant not found.", false);
		}

		Character m = (Character) constructor.newInstance();
		assertEquals("Militant's health initialized incorrectly.", 120, m.getCurrentHp());
		assertEquals("Militant's attack initialized incorrectly.", 20, m.getAttackDmg());
		assertEquals("Militant's name initialized incorrectly.", "Eisenhower", m.getName());
	}

	@Test(timeout = 100)
	public void testGameStartAddsAgentsCorrectly() throws IllegalArgumentException, IllegalAccessException
	{
		Hero h = new Medic("Medic", 100, 10, 2);
		Game.agents.clear();
		Game.startGame(h);
		
		Field f = null;

		try {
			f = Game.class.getDeclaredField("agents");
		} catch (NoSuchFieldException e) {
			assertFalse("Attribute agents not found.", true);
		} catch (SecurityException e) {
			assertFalse("Attribute agents not found.", true);
		}

		f.setAccessible(true);

		ArrayList<Object> agents = (ArrayList<Object>)f.get(null);
		
		assertEquals("Game should have 2 agents.", 2, agents.size());

		int countMilitant = 0;
		int countRevolutionary = 0;
		boolean foundFriendly = false;
		boolean foundUnfriendly = false;

		for (int i = 0; i< 15; i++) {
			for (int j = 0; j < 15; j++){
				if (!(Game.map[i][j] instanceof CharacterCell) || ((CharacterCell)Game.map[i][j]).getCharacter()== null)
				continue;
				if (((CharacterCell)Game.map[i][j]).getCharacter().getClass().getName().equals(militantPath)) {
					countMilitant++;
				}
		}
		}
		assertEquals("Game map should have 2 Militants.", 2, countMilitant);
	}


	@Test(timeout = 100)
	public void testMilitantShouldAttackHero() throws IllegalArgumentException, IllegalAccessException, NotEnoughActionsException, InvalidTargetException, InstantiationException, InvocationTargetException, SecurityException, ClassNotFoundException
	{
		Class militantClass = null;
		try {
			militantClass = Class.forName(militantPath);
		} catch (ClassNotFoundException e) {
			assertFalse("Class Militant not found.", true);
		}

		Constructor constructor = null;
		try {
			constructor = militantClass.getConstructor();
		} catch (NoSuchMethodException e) {
			assertTrue("Constructor of Militant not found.", false);
		} catch (SecurityException e) {
			assertTrue("Constructor of Militant not found.", false);
		}

		Character m = (Character) constructor.newInstance();
		Character h = new Medic("Medic", 100, 10, 2);
		generateGameWithCharacter(h, m, new Point(0, 0), new Point(0, 1));
		Method mf = null;
		try{
			mf = Class.forName(militantPath).getDeclaredMethod("attack");
		}
		catch(NoSuchMethodException e){
			assertFalse("Method attack not found.", true);
		}
		mf.setAccessible(true);
		mf.invoke(m);
		assertEquals("Militant should attack hero.", 80, h.getCurrentHp());
	}

	@Test(timeout = 100)
	public void testMilitantShouldAttackHeroAtDistance() throws IllegalArgumentException, IllegalAccessException, NotEnoughActionsException, InvalidTargetException, InstantiationException, InvocationTargetException, SecurityException, ClassNotFoundException
	{
		Class militantClass = null;
		try {
			militantClass = Class.forName(militantPath);
		} catch (ClassNotFoundException e) {
			assertFalse("Class Militant not found.", true);
		}

		Constructor constructor = null;
		try {
			constructor = militantClass.getConstructor();
		} catch (NoSuchMethodException e) {
			assertTrue("Constructor of Militant not found.", false);
		} catch (SecurityException e) {
			assertTrue("Constructor of Militant not found.", false);
		}

		Character m = (Character) constructor.newInstance();
		Character h = new Medic("Medic", 100, 10, 2);
		generateGameWithCharacter(h, m, new Point(0, 0), new Point(0, 3));
		Method mf = null;
		try{
			mf = Class.forName(militantPath).getDeclaredMethod("attack");
		}
		catch(NoSuchMethodException e){
			assertFalse("Method attack not found.", true);
		}
		mf.setAccessible(true);
		mf.invoke(m);
		assertEquals("Militant should attack hero.", 80, h.getCurrentHp());
		h = new Medic("Medic", 100, 10, 2);
		generateGameWithCharacter(h, m, new Point(0, 0), new Point(0, 4));
		mf = null;
		try{
			mf = Class.forName(militantPath).getDeclaredMethod("attack");
		}
		catch(NoSuchMethodException e){
			assertFalse("Method attack not found.", true);
		}
		mf.setAccessible(true);
		mf.invoke(m);
		assertEquals("Militant shouldn't attack hero if far.", 100, h.getCurrentHp());
	}

	@Test(timeout = 100)
	public void testMilitantsShouldNotAttackEachother() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotEnoughActionsException, InvalidTargetException
	{
		Class militantClass = null;
		try {
			militantClass = Class.forName(militantPath);
		} catch (ClassNotFoundException e) {
			assertFalse("Class Militant not found.", true);
		}

		Constructor constructorm = null;
		try {
			constructorm = militantClass.getConstructor();
		} catch (NoSuchMethodException e) {
			assertTrue("Constructor of Militant not found.", false);
		} catch (SecurityException e) {
			assertTrue("Constructor of Militant not found.", false);
		}
		Character m1 = (Character) constructorm.newInstance();
		Character m2 = (Character) constructorm.newInstance();
		generateGameWithCharacter(m1, m2, new Point(0, 0), new Point(0, 1));
		Game.endTurn();
		assertEquals("Militant should not attack Militant.", 120, m1.getCurrentHp());
		assertEquals("Militant should not attack Militant.", 120, m2.getCurrentHp());
	}

	@Test(timeout = 100)
	public void testUpdatedCheckWin() throws IllegalArgumentException, IllegalAccessException
	{
		Hero h = new Medic("Medic", 100, 10, 2);
		Game.startGame(h);
		for (int i = 0; i < Game.map.length; i++) {
			for (int j = 0; j < Game.map[i].length; j++) {
				if (Game.map[i][j] instanceof CollectibleCell
						&& ((CollectibleCell) Game.map[i][j]).getCollectible() instanceof Vaccine)
						Game.map[i][j] = new CharacterCell(null);
			}
		}
		for (int k = 0; k < 4; k++)
			{
				for (int i = 0; i < Game.map.length; i++) {
				for (int j = 0; j < Game.map[i].length; j++) {
					if (Game.map[i][j] instanceof CharacterCell
							&& ((CharacterCell) Game.map[i][j]).getCharacter() == null)
							{
								Hero hh = new Medic("Medic", 100, 10, 2);
							((CharacterCell) Game.map[i][j]).setCharacter(hh);
							Game.heroes.add(hh);
							}
				}
			}
		}
		assertFalse("Game should not end while there is still militants alive.", Game.checkWin());
		for (int i = 0; i < Game.map.length; i++) {
			for (int j = 0; j < Game.map[i].length; j++) {
				if (Game.map[i][j] instanceof CharacterCell
						&& ((CharacterCell) Game.map[i][j]).getCharacter() != null)
						if(((CharacterCell) Game.map[i][j]).getCharacter().getClass().getName().equals(militantPath))
						{
							((CharacterCell) Game.map[i][j]).setCharacter(null);
						}
			}
		}

		Field f = null;

		try {
			f = Game.class.getDeclaredField("agents");
		} catch (NoSuchFieldException e) {
			assertFalse("Attribute agents not found.", true);
		} catch (SecurityException e) {
			assertFalse("Attribute agents not found.", true);
		}

		f.setAccessible(true);
		f.set(null, new ArrayList<>());
		assertTrue("Game should end when there is no militants alive.", Game.checkWin());
	}
}