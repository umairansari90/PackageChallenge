package com.mobiquityinc.packer.parser;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Optional;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.mobiquityinc.packer.info.PackageInfo;

public class PackageParserTest {

	private PackageParser packageParcer = new PackageParser();

	@Test
	public void testInvalidWeight() {
		assertFalse(packageParcer.parsePackage("101 : (1,15.3,�34)").isPresent());
	}
	
	@DataProvider(name = "testData")
	private Object[][] dataProvider() {
		String testString = "56 : (1,90.72,�13) (2,33.80,�40) (3,43.15,�10) (4,37.97,�16) (5,46.81,�36) (6,48.77,�79) (7,81.80,�45) (8,19.36,�79) (9,6.76,�64)";
		return new Object[][] { 
			{ testString, "2", 56, 40, 33.80, 0 },
			{ testString, "3", 56, 10, 43.15, 1 },
			{ testString, "4", 56, 16, 37.97, 2 },
			{ testString, "5", 56, 36, 46.81, 3 },
			{ testString, "6", 56, 79, 48.77, 4 },
			{ testString, "8", 56, 79, 19.36, 5 },
			{ testString, "9", 56, 64, 6.76, 6 }};
	}
	
	@Test(dataProvider = "testData")
	public void testParcer(String testString, String index, double maxWeight, double cost, double weight, int listIndex) {

		Optional<PackageInfo> packageInfo = packageParcer.parsePackage(testString);

		assertTrue(packageInfo.isPresent());
		assertTrue(packageInfo.get().getMaxWeight() == maxWeight);
		assertTrue(packageInfo.get().getItems().get(listIndex).getWeight() == weight);
		assertTrue(packageInfo.get().getItems().get(listIndex).getCost() == cost);
		assertEquals(packageInfo.get().getItems().get(listIndex).getIndex(),index);

	}

}
