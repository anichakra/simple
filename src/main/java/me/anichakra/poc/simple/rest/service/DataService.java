package me.anichakra.poc.simple.rest.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import me.anichakra.poc.simple.rest.domain.AssetData;
import me.anichakra.poc.simple.rest.domain.IncomeData;
import me.anichakra.poc.simple.rest.domain.PropertyData;

@Service
@Slf4j
public class DataService {

	public List<AssetData> getAsset(String account) {
		List<AssetData> assetDataList = new ArrayList<AssetData>();
		assetDataList.add(new AssetData(account, "our asset1 for loan account " + account, System.currentTimeMillis()));
		assetDataList.add(new AssetData(account, "our asset2 for loan account " + account, System.currentTimeMillis()));
		assetDataList.add(new AssetData(account, "our asset3 for loan account " + account, System.currentTimeMillis()));
		return assetDataList;
	}

	public IncomeData getIncome(String account) {
	    log.debug(account);
		return new IncomeData(account, "my income for loan account " + account, System.currentTimeMillis(), 1000.999d);
	}

	public PropertyData getProperty(String account) {
		return new PropertyData(account, "my property for loan account " + account, System.currentTimeMillis());

	}

}
