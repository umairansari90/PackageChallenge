package com.mobiquityinc.packer.processor;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.mobiquityinc.packer.info.Item;
import com.mobiquityinc.packer.info.PackageInfo;

public class PackageProcessorTest {

	private PackageProcessor packageProcessor = new PackageProcessor();

	@DataProvider(name = "testData")
	private Object[][] dataProvider() {
		List<Item> items1 = new ArrayList<>();
		items1.add(Item.builder().weight(3).cost(100).index("1").build());
		items1.add(Item.builder().weight(65).cost(37).index("2").build());
		items1.add(Item.builder().weight(10).cost(46).index("3").build());
		items1.add(Item.builder().weight(55).cost(57).index("4").build());
		
		
		List<Item> items2 = new ArrayList<>();
		items2.add(Item.builder().weight(1).cost(300).index("1").build());
		items2.add(Item.builder().weight(3).cost(600).index("2").build());
		
		return new Object[][] {
				{ Optional.of(new PackageInfo(57, items1)), "1,3" },
				{ Optional.of(new PackageInfo(2, items2)), "1" }};
	}
	
	@Test(dataProvider = "testData")
	public void testProcessor(Optional<PackageInfo> packageInfo, String result) {

		assertEquals(packageProcessor.processPackage(packageInfo), result);
	}
	
}
