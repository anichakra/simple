package me.anichakra.poc.simple.rest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.github.javafaker.Faker;

import lombok.extern.slf4j.Slf4j;
import me.anichakra.poc.simple.rest.domain.AssetData;
import me.anichakra.poc.simple.rest.domain.IncomeData;
import me.anichakra.poc.simple.rest.domain.PropertyData;

@Service
@Slf4j
@XRayEnabled
public class DataService {
	public List<AssetData> getAsset(String account) {
		List<AssetData> assetDataList = new ArrayList<AssetData>();
		String node = System.getProperty("node.id");
		assetDataList.add(new AssetData(account, node, System.currentTimeMillis()));
		String value;
		value = new Faker(Locale.ENGLISH).artist().name();
		assetDataList.add(new AssetData(account, value, System.currentTimeMillis()));
		value = new Faker(Locale.ENGLISH).book().author();
		assetDataList.add(new AssetData(account, value, System.currentTimeMillis()));
		value = new Faker(Locale.ENGLISH).shakespeare().asYouLikeItQuote();
		assetDataList.add(new AssetData(account, value, System.currentTimeMillis()));
		return assetDataList;
	}

	public IncomeData getIncome(String account) {
		log.debug(account);
		return new IncomeData(account,
				new Faker(Locale.ENGLISH).business().creditCardNumber() + ":" + new Faker(Locale.ENGLISH).currency()
						+ ":" + new Faker(Locale.ENGLISH).company().name() + ":"
						+ new Faker(Locale.ENGLISH).commerce().price() + ":"
						+ new Faker(Locale.ENGLISH).commerce().productName(),

				System.currentTimeMillis(), 1000.999d);
	}

	public PropertyData getProperty(String account) {
		return new PropertyData(account,
				new Faker(Locale.ENGLISH).address().fullAddress(),
				System.currentTimeMillis());
	}

}
