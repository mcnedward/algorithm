package com.advancedtopics.app.algorithm;

import java.util.List;

import com.advancedtopics.app.individual.Individual;
import com.advancedtopics.app.individual.UniTest;

/**
 * @author Edward McNealy <edwardmcn64@gmail.com> - Oct 15, 2015
 *
 */
public class RandomSearch extends Algorithm {

	public RandomSearch(List<UniTest> tests) {
		super(tests);
	}

	@Override
	protected Individual algorithm() {
		return randomSearch();
	}

	private Individual randomSearch() {
		Individual individual = getRandomSelection(5);
		return individual;
	}

}
