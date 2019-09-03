package me.anichakra.poc.simple.rest.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AssetData {
	private String account;
	private String assetDetails;
	private Long time;
}
