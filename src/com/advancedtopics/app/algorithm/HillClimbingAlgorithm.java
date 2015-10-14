package com.advancedtopics.app.algorithm;

import java.util.ArrayList;
import java.util.List;

import com.advancedtopics.app.individual.Individual;
import com.advancedtopics.app.individual.UniTest;

/**
 * @author Edward McNealy <edwardmcn64@gmail.com> - Oct 13, 2015
 *
 */
public class HillClimbingAlgorithm extends Algorithm {

	List<Individual> population;

	public HillClimbingAlgorithm(List<UniTest> tests) {
		super(tests);
	}

	@Override
	public Individual algorithm() {
		return hillClimbAlgorithm();
	}

	/**
	 * Find all the individuals in the data set, then select a random individual to start at. Start moving towards the
	 * right, and continue until an individual with a lower fitness is found. Then return to the starting individual,
	 * and start moving to the left, continuing until a lower fitness is found again. Then return the individual with
	 * the highest fitness.
	 * 
	 * @return The individual with the highest fitness.
	 */
	private Individual hillClimbAlgorithm() {
		population = new ArrayList<Individual>();

		for (int x = 0; x < tests.size(); x++) {
			Individual i = getRandomSelection(5);
			population.add(i);
		}
		for (Individual i : population)
			printOut(i);

		int rand = random.nextInt(population.size());

		Individual starting = population.get(rand);
		System.out.println("\nFirst test is " + starting.toString() + " found at position: " + (rand + 1) + "\n");

		int highestFitness = 0;
		int index = 0;
		boolean switchSides = false;

		for (int x = rand; x <= population.size(); x++) {
			if (x == population.size()) {
				switchSides = true;
				break;
			}
			printOut(population.get(x));
			int fitness = getFitness(population.get(x));
			if (fitness > highestFitness) {
				highestFitness = fitness;
				index = x;
			} else if (fitness < highestFitness) {
				switchSides = true;
				break;
			}
		}

		if (switchSides) {
			System.out.println("Switching sides");
			for (int x = rand - 1; x >= 0; x--) {
				printOut(population.get(x));

				int fitness = getFitness(population.get(x));
				if (fitness > highestFitness) {
					highestFitness = fitness;
					index = x;
				} else if (fitness < highestFitness) {
					break;
				}
			}
		}
		System.out.println("\nFound the best.");
		printOut(population.get(index));

		return population.get(index);
	}

	@Override
	protected void printOut(Individual individual) {
		int fitness = getFitness(individual);
		System.out.println(individual.toString() + " [" + (population.indexOf(individual) + 1) + "/" + population.size() + "]: " + fitness);
	}
}
