package com.njm.yaho.domain.oracle.info;

import lombok.Data;

@Data
public class ChartData {
	private String label;
	private int value;
	
	public ChartData(String item,int cell) {
		this.label=item;
		this.value=cell;
	}
}
