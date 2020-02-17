package com.rest.app.helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculationHelper {

	public static String calculate(final BigDecimal followers, final BigDecimal publicRepos) {
		final BigDecimal division = new BigDecimal(6).divide(followers, 3, RoundingMode.HALF_EVEN);
		final BigDecimal multiplyFactor = new BigDecimal(2).add(publicRepos);
		final BigDecimal calulatedValue = division.multiply(multiplyFactor).setScale(2, RoundingMode.HALF_EVEN);

		return calulatedValue.toString();
	}
}
