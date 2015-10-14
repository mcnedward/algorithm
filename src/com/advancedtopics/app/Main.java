package com.advancedtopics.app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.advancedtopics.app.algorithm.Algorithm;
import com.advancedtopics.app.algorithm.GeneticAlgorithm;
import com.advancedtopics.app.algorithm.HillClimbingAlgorithm;
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

		Individual bestGAInd = null;
		Individual bestHCInd = null;

		GeneticAlgorithm ga = new GeneticAlgorithm(tests);
		HillClimbingAlgorithm hc = new HillClimbingAlgorithm(tests);

		int hcWins = 0, gaWins = 0, ties = 0;
		int highestGAFitness = 0, highestHCFitness = 0;
		for (int x = 0; x < ITERATIONS; x++) {
			// Run each of the algorithms and find the individual with the highest fitness
			Individual gaIndividual = ga.runAlgorithm();
			Individual hcIndividual = hc.runAlgorithm();

			// Determine if the GA of HC algorithm had the highest fitness for this round
			int gaFitness = Algorithm.getFitness(gaIndividual);
			int hcFitness = Algorithm.getFitness(hcIndividual);
			if (gaFitness > hcFitness)
				gaWins++;
			if (hcFitness > gaFitness)
				hcWins++;
			if (hcFitness == gaFitness)
				ties++;

			// Find the overall highest individual for each algorithm
			if (gaFitness > highestGAFitness)
				bestGAInd = gaIndividual;
			if (hcFitness > highestHCFitness)
				bestHCInd = hcIndividual;
		}

		long finishingTime = System.currentTimeMillis();
		long testTime = (finishingTime - startingTime);
		System.out.print("\n\n");
		System.out.println("FINISHED - Time to complete was: " + testTime + " ms");
		System.out.print("\n");
		System.out.println("Number of times Genetic Algorithm was better: " + gaWins);
		System.out.println("Number of times Hill Climbing Algorithm was better: " + hcWins);
		System.out.println("Number of times both algorithms had same fitness: " + ties);
		System.out.println("***** Best from Genetic Algorithm *****");
		ga.printOutIndividual(bestGAInd);
		System.out.println("***** Best from Hill Climbing Algorithm *****");
		ga.printOutIndividual(bestHCInd);
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