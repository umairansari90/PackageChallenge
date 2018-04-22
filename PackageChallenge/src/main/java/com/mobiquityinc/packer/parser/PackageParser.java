package com.mobiquityinc.packer.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.mobiquityinc.packer.info.Item;
import com.mobiquityinc.packer.info.PackageInfo;

/**
 * This class provides facility to parse data in the form of 81 : (1,53.38,€45)
 * (2,88.62,€98) and return Package info enclosed in optional
 *
 * @author UmairZ
 * @since Apr 20, 2018
 *
 */
public class PackageParser {

	static final int MAX_PACKAGE_WEIGHT = 100;

	/**
	 * This function take input data in the form of 81 : (1,53.38,€45) (2,88.62,€98)
	 * where 81 is the max weight and remaining part after column is the list of
	 * items.
	 * 
	 * Note: If package max weight is greater then allowed max weight then this
	 * class returns Optional with null.
	 * 
	 * @param data
	 * @return Optional<PackageInfo>
	 */
	public Optional<PackageInfo> parsePackage(String data) {
		String[] splittedByColumn = data.split(":");
		int maxWeight = Integer.parseInt(splittedByColumn[0].trim());

		/*
		 * Checks if weight provided in data is less than or equal to the provided
		 * maximum limit. If weight is valid, package info else null is returned
		 * enclosed in optional
		 */
		return Optional.ofNullable(maxWeight <= MAX_PACKAGE_WEIGHT
				? new PackageInfo(maxWeight, getItems(splittedByColumn[1].trim(), maxWeight))
				: null);
	}

	/**
	 * 
	 * This method parses items and returns list of Item class
	 * 
	 */
	private List<Item> getItems(String items, int maxWeight) {

		List<Item> itemList = new ArrayList<>();
		String[] splittedItems = items.split(" ");

		if (splittedItems.length > 0) {

			Arrays.asList(splittedItems).stream().forEach(item -> {

				String[] splittedByCommas = item.substring(1, item.length() - 1).split(",");
				Item itemInfo = Item.builder().index(splittedByCommas[0])
						.weight(Double.parseDouble(splittedByCommas[1]))
						.cost(Integer.parseInt(splittedByCommas[2].substring(1))).build();

				// This check skips items that has weight greater than allowed maximum weight
				if (itemInfo.getWeight() <= maxWeight) {
					itemList.add(itemInfo);
				}
			});
		}
		return itemList;
	}

}
