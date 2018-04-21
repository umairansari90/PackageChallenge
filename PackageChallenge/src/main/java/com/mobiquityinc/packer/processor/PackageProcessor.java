package com.mobiquityinc.packer.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import com.mobiquityinc.packer.info.Item;
import com.mobiquityinc.packer.info.PackageInfo;

/**
* This class provides facility to process package info and identify
* optimal package to use based on these conditions
* 1) Total weight is less than or equal to the package limit.
* 2) Total cost is as large as possible
* 3) Returns "-" in case of no result
*
* @author UmairZ
* @since Apr 20, 2018
*
*/
public class PackageProcessor {

    static final String DEFAULT_CHAR = "-";
    static final String ITEM_SEPARATOR_CHAR = ",";

	public String processPackage(Optional<PackageInfo> packageInfo) {
		String response = null;
		if (!packageInfo.isPresent() || packageInfo.get().getItems().size() < 1) {
			response = DEFAULT_CHAR;
		} else {

			/*
			 *  Get all possible combinations of available items in the form of list
			 *  Each combination has already calculated total weight and total cost
			 *  of that combination
			 */
			List<Combination> combinations = createCombinations(packageInfo.get().getItems());

			/*
			 * Stream operations are used here to first filter combinations which has weight
			 * greater than allowed package weight. Then the list is sorted. 
			 * 
			 * As Combination class implements Comparable and sorts combinations based on 
			 * Cost in descending order then on Weight in ascending order if cost is same.
			 * 
			 * Once the sorting is done, first combination is picked because that has the
			 * maximum cost with weight within limit.  
			 */
			Optional<Combination> foundCombination = combinations.stream()
					.filter(cmb -> cmb.combinationWeight <= packageInfo.get().getMaxWeight()).sorted().findFirst();

			if (foundCombination.isPresent() && foundCombination.get().items.size() > 0) {
				
				/*
				 *  Stream operation are used here to generate result in required format which
				 *  is comma separated index of selected items
				 */
				response = foundCombination.get().items.stream().map(item -> item.getIndex())
						.collect(Collectors.joining(ITEM_SEPARATOR_CHAR));
			} else {
				response = DEFAULT_CHAR;
			}
		}
		return response;
	}
	
	/**
	 * 
	 * This function returns all possible combinations of items given in input.
	 * Each combination class also has combined cost and weight
	 * 
	 * @throws NullPointerException if list is empty
	 */
	private List<Combination> createCombinations(List<Item> itemList) {
		/*
		 *  Combination list is being updated while iterating stream so used
		 *  CopyOnWriteArrayList implementation of list to avoid concurrency issues
		 */
		CopyOnWriteArrayList<Combination> combinations = new CopyOnWriteArrayList<>();

		/*
		 * This piece of code iterates and adds each item to the combination list
		 * and also the combination of that item with already available combinations.
		 */
		itemList.stream().forEach(item -> {
			combinations.stream().forEach(cmb -> {
				Combination newCmb = new Combination(cmb);
				newCmb.addItem(item);
				combinations.add(newCmb);
			});

			Combination currentItem = new Combination();
			currentItem.addItem(item);
			combinations.add(currentItem);
		});

		return combinations;
	}
	
	// Used nested class because this is only going to be used within this Package Processor
	private class Combination implements Comparable<Combination> {
		List<Item> items;
		double combinationWeight;
		double combinationCost;

		Combination() {
			items = new ArrayList<Item>();
		}

		Combination(Combination combination) {
			this.combinationWeight = combination.combinationWeight;
			this.combinationCost = combination.combinationCost;
			items = new ArrayList<Item>(combination.items);
		}

		void addItem(Item item) {
			items.add(item);
			combinationWeight += item.getWeight();
			combinationCost += item.getCost();
		}

		/**
		 * This compare method first sorts combinations based on Cost in descending order
		 * then on Weight in ascending order if cost is same
		 */
		@Override
		public int compareTo(Combination other) {

			if (other == null) {
				return 1;
			}

			final int comp = Double.compare(other.combinationCost, this.combinationCost);
			return comp == 0 ? Double.compare(this.combinationWeight, other.combinationWeight) : comp;
		}
	}
}
