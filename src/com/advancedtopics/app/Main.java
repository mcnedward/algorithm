package com.advancedtopics.app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.advancedtopics.app.algorithm.Algorithm;
import com.advancedtopics.app.algorithm.GeneticAlgorithm;
import com.advancedtopics.app.algorithm.HillClimberAlgorithm;
import com.advancedtopics.app.algorithm.RandomSearch;
import com.advancedtopics.app.individual.Individual;
import com.advancedtopics.app.individual.TestCase;
import com.advancedtopics.app.individual.UniTest;

public class Main {

	private static String SMALL_DATA_SET = "nanoxmltestfaultmatrix.txt";
	private static String BIG_DATA_SET = "fault-matrix-1000.dat";

	private static int ITERATIONS = 100;

	protected static int GA_WINS = 0;
	protected static int HC_WINS = 0;
	protected static int TIES = 0;

	protected static Individual GA_INDIVIDUAL;
	protected static Individual HC_INDIVIDUAL;

	protected static int HIGEST_GA_FITNESS = 0, HIGHEST_HC_FITNESS = 0;

	public static void main(String[] args) {
		List<UniTest> tests = createTestCases(BIG_DATA_SET);

		long startingTime = System.currentTimeMillis();

		GeneticAlgorithm ga = new GeneticAlgorithm(tests, false);
		HillClimberAlgorithm hc = new HillClimberAlgorithm(tests, false);
		RandomSearch rs = new RandomSearch(tests);

		Individual bestGAInd = null;
		Individual bestHCInd = null;
		Individual bestRSInd = null;

		Map<String, List<Individual>> testResults = new HashMap<String, List<Individual>>();
		testResults.put("ga", new ArrayList<Individual>());
		testResults.put("hc", new ArrayList<Individual>());
		testResults.put("rs", new ArrayList<Individual>());

		List<Map<String, Integer>> testWins = new ArrayList<Map<String, Integer>>();

		for (int i = 0; i < 10; i++) {
			int hcWins = 0, gaWins = 0, rsWins = 0, ties = 0;
			int highestGAFitness = 0, highestHCFitness = 0, highestRSFitness = 0;
			for (int x = 0; x < ITERATIONS; x++) {
				// Run each of the algorithms and find the individual with the highest fitness
				Individual gaIndividual = ga.runAlgorithm();
				Individual hcIndividual = hc.runAlgorithm();
				Individual rsIndividual = rs.runAlgorithm();

				// Determine if the GA or HC algorithm or the random search had the highest fitness for this round
				int gaFitness = Algorithm.getFitness(gaIndividual);
				int hcFitness = Algorithm.getFitness(hcIndividual);
				int rsFitness = Algorithm.getFitness(rsIndividual);
				if (hcFitness == gaFitness && hcFitness == rsFitness && gaFitness == rsFitness)
					ties++;
				else if (gaFitness > hcFitness) {
					if (gaFitness > rsFitness) {
						gaWins++;
						testResults.get("ga").add(gaIndividual);
					} else {
						rsWins++;
						testResults.get("rs").add(rsIndividual);
					}
				} else if (hcFitness > gaFitness) {
					if (hcFitness > rsFitness) {
						hcWins++;
						testResults.get("hc").add(hcIndividual);
					} else {
						rsWins++;
						testResults.get("rs").add(rsIndividual);
					}
				} else if (rsFitness > gaFitness) {
					if (rsFitness > hcFitness) {
						rsWins++;
						testResults.get("rs").add(rsIndividual);
					} else {
						hcWins++;
						testResults.get("hc").add(hcIndividual);
					}
				} else if (rsFitness > hcFitness) {
					if (rsFitness > gaFitness) {
						rsWins++;
						testResults.get("rs").add(rsIndividual);
					} else {
						gaWins++;
						testResults.get("ga").add(gaIndividual);
					}
				} else if (gaFitness == hcFitness) {
					ties++;
				}

				// Find the overall highest individual for each algorithm
				if (gaFitness > highestGAFitness) {
					bestGAInd = gaIndividual;
				}
				if (hcFitness > highestHCFitness) {
					bestHCInd = hcIndividual;
				}
				if (rsFitness > highestRSFitness) {
					bestRSInd = rsIndividual;
				}
			}

			long finishingTime = System.currentTimeMillis();
			long testTime = (finishingTime - startingTime);

			System.out.println("FINISHED - Time to complete " + ITERATIONS + " iterations was: " + testTime + " ms");
			System.out.print("\n");
			System.out.println("Number of times Genetic Algorithm was better: " + gaWins);
			System.out.println("Number of times Hill Climbing Algorithm was better: " + hcWins);
			System.out.println("Number of times random search was better: " + rsWins);
			System.out.println("Number of times both algorithms had same fitness: " + ties);
			System.out.println();
			System.out.println("***** Best from Genetic Algorithm *****");
			Algorithm.printOut(bestGAInd);
			System.out.println("***** Best from Hill Climbing Algorithm *****");
			Algorithm.printOut(bestHCInd);
			System.out.println("***** Best from Random Search *****");
			Algorithm.printOut(bestRSInd);

			// Store the wins
			Map<String, Integer> wins = new HashMap<String, Integer>();
			wins.put("gaWins", gaWins);
			wins.put("hcWins", hcWins);
			wins.put("rsWins", rsWins);
			wins.put("ties", ties);
			testWins.add(wins);
		}

		System.out.println("***** Genetic Algorithm [" + testResults.get("ga").size() + "] *****");
		for (Individual i : testResults.get("ga")) {
			System.out.println(i.toString() + ": " + Algorithm.getFitness(i));
		}
		System.out.println("\n***** Hill Climber [" + testResults.get("hc").size() + "] *****");
		for (Individual i : testResults.get("hc")) {
			System.out.println(i.toString() + ": " + Algorithm.getFitness(i));
		}
		System.out.println("\n***** Random Search [" + testResults.get("rs").size() + "] *****");
		for (Individual i : testResults.get("rs")) {
			System.out.println(i.toString() + ": " + Algorithm.getFitness(i));
		}

		for (Map<String, Integer> wins : testWins) {
			Integer gaWins = wins.get("gaWins");
			Integer hcWins = wins.get("hcWins");
			Integer rsWins = wins.get("rsWins");
			Integer ties = wins.get("ties");
			System.out.println("\nGenetic Algorithm Wins: " + gaWins);
			System.out.println("Hill Climber Wins: " + hcWins);
			System.out.println("Random Search Wins: " + rsWins);
			System.out.println("Ties: " + ties);
		}

	}

	/**
	 * Reads the file containing the data set, then parses the data to a list of UniTests.
	 * 
	 * @param dataSetFile
	 *            The file name for the data set file.
	 * @return A list of UniTests parsed from the data set.
	 */
	private static List<UniTest> createTestCases(String dataSetFile) {
		List<UniTest> tests = new ArrayList<UniTest>();

		try {
			FileReader fp = new FileReader(dataSetFile);
			BufferedReader bf = new BufferedReader(fp);
			StringBuffer buffer = new StringBuffer();
			String line = "";

			UniTest uniTest = null;
			TestCase testCase = null;
			while ((line = bf.readLine()) != null) {
				if (line.contains("unitest")) {

					if (uniTest != null) {
						tests.add(uniTest);
					}

					uniTest = new UniTest(line.replace(":", ""));
					buffer.append("Test Title " + line + "\n");
					buffer.append("-----------------" + "\n");
					buffer.append("Test Case:  Has Fault?  \n");
				} else {
					if (line.contains("v")) {
						testCase = new TestCase(line.replace(":", ""));

						for (int x = 0; x < 10 - line.length(); x++) {
							buffer.append(" ");
						}
						buffer.append(line);
					} else if (!line.trim().equals("")) {
						if (!line.contains("0")) {
							testCase.setFaulty(true);
							buffer.append("  X");
						}
						buffer.append("\n");
						uniTest.addTestCase(testCase);
					}
				}

			}

			bf.close();
			fp.close();
		} catch (IOException e) {
			System.out.println("Error in file" + e.getMessage());
		}
		return tests;
	}
}