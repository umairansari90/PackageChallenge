package com.mobiquityinc.packer.info;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
* 
* This class holds item information
*
* @author UmairZ
* @since Apr 20, 2018
*
*/
@Getter
@Builder
@ToString
public class Item {

    private String index;
    private double weight;
    private double cost;
    
}
