package me.anichakra.poc.simple.rest.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IncomeData {

	private String account;
	private String incomeDetails;
	private Long time;
	private double salary;
}
