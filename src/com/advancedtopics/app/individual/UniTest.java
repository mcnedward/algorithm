package com.advancedtopics.app.individual;

import java.util.ArrayList;
import java.util.List;

public class UniTest {
	private String name;
	private List<TestCase> testCases;

	public UniTest() {
		testCases = new ArrayList<TestCase>();
	}

	public UniTest(String n) {
		this();
		name = n;
	}

	public UniTest(String name, List<TestCase> cases) {
		this.name = name;
		this.testCases = cases;
	}

	public void addTestCase(TestCase ob) {
		testCases.add(ob);

	}

	public int getNumberOfFaults() {
		int number = 0;

		for (TestCase test : testCases) {
			if (test.isFaulty())
				number++;
		}
		return number;
	}

	public UniTest compareTests(UniTest uniTest) {
		int case1Count = 0, case2Count = 0;

		for (int x = 0; x < testCases.size(); x++) {
			TestCase case1 = testCases.get(x);
			for (int y = 0; y < uniTest.getTestCases().size(); y++) {
				TestCase case2 = uniTest.getTestCases().get(y);
				if (case1.getName().equals(case2.getName())) {
					if (case1.isFaulty() && !case2.isFaulty())
						case1Count++;
					else if (!case1.isFaulty() && case2.isFaulty())
						case2Count++;
					break;
				}
			}
		}
		if (case1Count > case2Count)
			return this;
		else
			return uniTest;
	}

	public List<TestCase> getTestCasesAtRange(int start) {
		return getTestCasesAtRange(start, testCases.size());
	}

	public List<TestCase> getTestCasesAtRange(int start, int end) {
		List<TestCase> cases = new ArrayList<TestCase>();
		for (int x = start; x < end; x++) {
			cases.add(testCases.get(x));
		}
		return cases;
	}

	public int getNumberOfTestCases() {
		return testCases.size();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TestCase> getTestCases() {
		return testCases;
	}

	public void setTestCases(List<TestCase> testCases) {
		this.testCases = testCases;
	}

	@Override
	public String toString() {
		String output = name + "\n";
		for (TestCase testCase : testCases) {
			output += testCase.getName() + " Has Fault? " + testCase.isFaulty() + "\n";
		}
		return output;

	}

}