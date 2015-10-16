package com.advancedtopics.app.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.advancedtopics.app.individual.Individual;
import com.advancedtopics.app.individual.TestCase;
import com.advancedtopics.app.individual.UniTest;

/**
 * @author Edward McNealy <edwardmcn64@gmail.com> - Oct 14, 2015
 *
 */
public abstract class Algorithm {

	protected List<UniTest> tests;
	protected static Random random;
	protected static int id;

	protected boolean print;

	public Algorithm(List<UniTest> tests) {
		this(tests, true);
	}

	public Algorithm(List<UniTest> tests, boolean print) {
		this.tests = tests;
		this.print = print;
		random = new Random();
	}

	/**
	 * Run the algorithm.
	 * 
	 * @return The best individual from the algorithm's result.
	 */
	public Individual runAlgorithm() {
		return algorithm();
	}

	/**
	 * The algorithm.
	 * 
	 * @return The best individual from the algorithm's result.
	 */
	protected abstract Individual algorithm();

	/**
	 * Determine the fitness for an individual. If a fault is found in the first UniTest, then it will receive the
	 * maximum amount of points. For every UniTest after, the number of points for finding a new fault will be decreased
	 * by one.
	 * <p>
	 * The number of points is determined by the size of individual's population. For example, if there are 5 UniTests
	 * in the population, then the maximum points will be 5. If a new fault is found in the second UniTest, then the
	 * points for that new fault will be 4.
	 * </p>
	 * 
	 * @param individual
	 *            The individual to use for finding the fitness.
	 * @return The fitness of the individual.
	 */
	public static int getFitness(Individual individual) {
		int pointsToAdd = individual.getPopulation().size();
		int totalPoints = 0;

		List<String> previousCases = new ArrayList<String>();
		for (UniTest test : individual.getPopulation()) {
			for (TestCase testCase : test.getTestCases()) {
				if (testCase.isFaulty()) {
					if (!previousCases.contains(testCase.getName())) {
						totalPoints += pointsToAdd;
						previousCases.add(testCase.getName());
					}
				}
			}
			pointsToAdd--;
		}
		return totalPoints;
	}

	/**
	 * Small percentage that an individual from the population will be mutated. This will randomly select one of the
	 * individuals, then randomly select 2 of that individual's UniTests to switch places.
	 * 
	 * @param population
	 *            The population of individuals to mutate.
	 */
	protected void mutate(List<Individual> population) {
		int offset1 = getRandomOffset();
		int offset2 = getRandomOffset();

		if (random.nextDouble() <= 0.25) {
			int rand = random.nextInt(population.size());

			Individual mutatedInd = population.get(rand);

			UniTest t1 = mutatedInd.getTestAt(offset1);
			UniTest t2 = mutatedInd.getTestAt(offset2);
			mutatedInd.replaceTest(offset1, t2);
			mutatedInd.replaceTest(offset2, t1);

			if (print) {
				System.out.println("Mutating " + mutatedInd.toString());
				printOut(mutatedInd);
			}
		}
	}

	/**
	 * Find a random individual with a specified number of UniTests from the data set.
	 * 
	 * @param size
	 *            The size of the individual's population.
	 * @return The individual.
	 */
	protected Individual getRandomSelection(int size) {
		List<UniTest> newTests = new ArrayList<UniTest>();
		for (int x = 0; x < size; x++) {
			int randomNum = random.nextInt(tests.size());
			UniTest testToAdd = tests.get(randomNum);
			if (!newTests.contains(testToAdd))
				newTests.add(testToAdd);
			else
				x--;
		}
		Individual individual = new Individual(++id, newTests);
		return individual;
	}

	/**
	 * Find a random offset number to use in the crossover. The max for the number is 4, and the min is 0.
	 * 
	 * @return The random offset number.
	 */
	protected int getRandomOffset() {
		return random.nextInt(4);
	}

	public static void printOut(Individual individual) {
		int spaceSize = 14;
		StringBuilder builder = new StringBuilder();

		builder.append("--- " + individual.toString() + " ---\n");

		List<TestCase> testCases = individual.getPopulation().get(0).getTestCases();
		for (int x = 0; x < spaceSize; x++)
			builder.append(" ");
		for (TestCase testCase : testCases) {
			builder.append(testCase.getName() + " ");
			for (int x = 0; x < 4; x++) {
				if (x > testCase.getName().length()) {
					builder.append(" ");
				}
			}
		}
		builder.append("\n");
		for (UniTest test : individual.getPopulation()) {
			builder.append(test.getName());
			for (int x = 0; x <= spaceSize; x++) {
				if (x > test.getName().length()) {
					if (x < spaceSize)
						builder.append(" ");
					else
						builder.append("|");
				}
			}
			for (TestCase testCase : test.getTestCases()) {
				builder.append(testCase.isFaulty() ? " X  " : " O  ");
			}
			builder.append("\n");
		}
		int fitness = getFitness(individual);
		builder.append("Fitness for individual was: " + fitness + "\n");
		System.out.println(builder.toString());
	}
}
