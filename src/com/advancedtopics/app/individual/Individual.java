package com.advancedtopics.app.individual;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Edward McNealy <edwardmcn64@gmail.com> - Oct 12, 2015
 *
 */
public class Individual {

	private int id;
	private List<UniTest> population;

	public Individual(int id) {
		this.id = id;
		population = new ArrayList<UniTest>();
	}

	public Individual(int id, List<UniTest> population) {
		this.id = id;
		this.population = population;
	}

	/**
	 * Finds a UniTest from the individual's population.
	 * 
	 * @param index
	 *            The index of the UniTest to find.
	 * @return The UniTest.
	 */
	public UniTest getTestAt(int index) {
		return population.get(index);
	}

	/**
	 * Replaces the UniTest at the specified index with the provided UniTest.
	 * 
	 * @param index
	 *            The index to set the replacement to.
	 * @param test
	 *            The test to use as the replacement.
	 */
	public void replaceTest(int index, UniTest test) {
		population.set(index, test);
	}

	/**
	 * Determine if this individual already contains a certain UniTest.
	 * 
	 * @param test
	 *            The UniTest to check for.
	 * @return True if this individual contains the UniTest, false otherwise.
	 */
	public boolean hasTest(UniTest test) {
		for (UniTest pop : population) {
			if (test.getName().equals(pop.getName()))
				return true;
		}
		return false;
	}

	public void setPopulation(List<UniTest> population) {
		this.population = population;
	}

	public void addNewPopulation(List<UniTest> population) {
		this.population.addAll(population);
	}

	public List<UniTest> getPopulation() {
		return population;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Individual " + id;
	}

}
