package model.collectibles;

import engine.Game;
import exceptions.InvalidTargetException;
import exceptions.NoAvailableResourcesException;
import model.characters.Character;
import model.characters.Hero;
import model.characters.Zombie;

import java.awt.*;
import java.util.Random;

public class Vaccine implements Collectible {
	public Vaccine() {}

	@Override
	public void pickUp(Hero h) {
		h.getVaccineInventory().add(this);
	}

	@Override
	public void use(Hero h){

			h.getVaccineInventory().remove(this);

			Zombie zombieToCure = (Zombie) h.getTarget();
			Point zombieLocation = zombieToCure.getLocation();
			Game.zombies.remove(zombieToCure);

			Random r = new Random();

			Hero newHero = Game.availableHeroes.get(r.nextInt(Game.availableHeroes.size()));
			newHero.setLocation(zombieLocation);
			Game.heroes.add(newHero);
			Game.availableHeroes.remove(newHero);
		}
		
	}

