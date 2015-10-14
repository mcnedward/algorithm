package com.advancedtopics.app.individual;

/**
 * @author Edward McNealy <edwardmcn64@gmail.com> - Oct 14, 2015
 *
 */
public class TestCase {
	private String name;
	private boolean faulty;

	public TestCase(String name, boolean faulty) {
		this.name = name;
		this.faulty = faulty;
	}

	public TestCase(String line) {
		name = line;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isFaulty() {
		return faulty;
	}

	public void setFaulty(boolean faulty) {
		this.faulty = faulty;
	}
}
