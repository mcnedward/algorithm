package com.advancedtopics.app.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.advancedtopics.app.individual.Individual;
import com.advancedtopics.app.individual.TestCase;
import com.advancedtopics.app.individual.UniTest;

/**
 * @author Edward McNealy <edwardmcn64@gmail.com> - Oct 9, 2015
 *
 */
public class GeneticAlgorithm extends Algorithm {

	public GeneticAlgorithm() {
		super(new ArrayList<UniTest>());
	}

	public GeneticAlgorithm(List<UniTest> tests) {
		super(tests);
	}

	@Override
	public Individual algorithm() {
		return geneticAlgorithm();
	}

	/**
	 * Select two random individuals from the data set. Then crossover those two (as the parents) and find the two
	 * children. There will be a small chance for mutation, then select the two individuals with the highest function,
	 * from the parents and the children. Those two best become the new parents, and are the crossover occurs again, for
	 * the specified amount of iterations. Once the iterations are complete, the best individual is selected.
	 * 
	 * @return The best individual from the genetic algorithm.
	 */
	private Individual geneticAlgorithm() {

		List<Individual> population = new ArrayList<Individual>();

		Individual parent_1 = getRandomSelection(5);
		Individual parent_2 = getRandomSelection(5);

		for (int x = 0; x < 50; x++) {

			Map<String, Individual> children = crossover(parent_1, parent_2);

			Individual child_1 = children.get("child1");
			Individual child_2 = children.get("child2");

			population.add(parent_1);
			population.add(parent_2);
			population.add(child_1);
			population.add(child_2);

			System.out.println("********** New Population **********");
			for (Individual i : population) {
				printOut(i);
			}

			// Mutation
			mutate(population);

			Individual bestIndividual_1 = findBest(population);
			Individual bestIndividual_2 = findBest(population);

			System.out.println("Found best 2 individuals: " + bestIndividual_1.toString() + " and " + bestIndividual_2.toString());
			printOut(bestIndividual_1);
			printOut(bestIndividual_2);

			population.clear();
			parent_1 = bestIndividual_1;
			parent_2 = bestIndividual_2;
		}

		int fitness1 = getFitness(parent_1);
		int fitness2 = getFitness(parent_2);
		Individual overallBest = null;
		if (fitness1 >= fitness2)
			overallBest = parent_1;
		else
			overallBest = parent_2;
		System.out.println("***** Found the overall best individual - " + overallBest.getId() + "/" + id + " *****");
		printOut(overallBest);

		return overallBest;
	}

	/**
	 * Finds the best individual out of the population.
	 * 
	 * @param population
	 *            A list of individuals.
	 * @return The best individual.
	 */
	private Individual findBest(List<Individual> population) {
		int fitness = 0;
		// Get the highest
		Individual individual = null;
		for (Individual i : population) {
			int f = getFitness(i);
			if (f > fitness) {
				fitness = f;
				individual = i;
			}
		}
		population.remove(individual);
		return individual;
	}

	/**
	 * Performs a crossover between two parent individuals.
	 * 
	 * @param parent1
	 *            The first parent individual.
	 * @param parent2
	 *            The second parent individual.
	 * @return A map containing the two children from the crossover. Can be found with the keys: "child1" and "child2".
	 */
	private Map<String, Individual> crossover(Individual parent1, Individual parent2) {
		int offset = getRandomOffset();

		Map<String, Individual> result = new HashMap<String, Individual>();

		Individual child1 = new Individual(++id);
		Individual child2 = new Individual(++id);
		child1.addNewPopulation(parent1.getPopulation());
		child2.addNewPopulation(parent2.getPopulation());

		// Do the crossover
		for (int x = 0; x <= offset; x++) {
			int y = (child2.getPopulation().size() - 1) - x;
			UniTest test1 = child1.getTestAt(x);
			if (!child2.hasTest(test1)) {
				UniTest test2 = child2.getTestAt(y);

				child1.replaceTest(x, test2);
				child2.replaceTest(y, test1);
			}
		}

		result.put("child1", child1);
		result.put("child2", child2);

		return result;
	}

	/**
	 * Prints out the individual with each of the faults for every UniTest marked with an "X".
	 * 
	 * @param individual
	 *            The individual to print.
	 */
	public void printOutIndividual(Individual individual) {
		new GeneticAlgorithm().printOut(individual);
	}

	@Override
	protected void printOut(Individual individual) {
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
