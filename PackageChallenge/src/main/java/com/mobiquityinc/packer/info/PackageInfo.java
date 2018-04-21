package com.mobiquityinc.packer.info;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
* 
* This class holds package information
* 
* @author UmairZ
* @since Apr 20, 2018
*
*/
@Getter
@AllArgsConstructor
@ToString
public class PackageInfo {
    private double maxWeight;
    private List<Item> items;
}
