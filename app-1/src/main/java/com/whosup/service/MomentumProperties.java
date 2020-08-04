package com.whosup.service;

import java.util.Date;

import org.joda.time.Duration;

import com.whosup.model.Event;

;

public class MomentumProperties {

	private Double mass;
	private Double momentum;
	private Double compoundEffect;
	private Double lastUpdateTime;
	private static final Double eventFrictionExponent = 4.0;
	private static final Double eventFrictionFactor = 0.000001;
	private static final Double eventPercentageRefillOnBuzz = 0.2;
	private static final Double ideaFrictionExponent = 4.0;
	private static final Double ideaFrictionFactor = 0.000001;
	private static final Double ideaPercentageRefillOnBuzz = 0.2;
	private static final Double ideaDomainFrictionExponent = 4.0;
	private static final Double ideaDomainFrictionFactor = 0.001;
	private static final Double ideaDomainPercentageRefillOnBuzz = 0.2;
	private static final Double ideaLocationFrictionExponent = 4.0;
	private static final Double ideaLocationFrictionFactor = 0.001;
	private static final Double ideaLocationPercentageRefillOnBuzz = 0.2;

	public MomentumProperties(Double mass, Double momentum,
			Double compoundEffect, Double lastUpdateTime) {
		this.mass = mass;
		this.momentum = momentum;
		this.compoundEffect = compoundEffect;
		this.lastUpdateTime = lastUpdateTime;
	}

	public Double getMass() {
		return mass;
	}

	public void setMass(Double mass) {
		this.mass = mass;
	}

	public Double getMomentum() {
		return momentum;
	}

	public void setMomentum(Double momentum) {
		this.momentum = momentum;
	}

	public Double getCompoundEffect() {
		return compoundEffect;
	}

	public void setCompoundEffect(Double compoundEffect) {
		this.compoundEffect = compoundEffect;
	}

	public Double getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Double lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public MomentumProperties recalculateForTime(Double time) {
		Double alpha = eventFrictionFactor / mass;
		Double exp1 = Math.exp(-alpha
				* Math.pow(time - compoundEffect, eventFrictionExponent + 1));

		Double exp2 = Math.exp(-alpha
				* Math.pow(lastUpdateTime - compoundEffect,
						eventFrictionExponent + 1));

		Double oldVelocity = momentum / mass;

		Double newVelocity = (oldVelocity / exp1) * exp2;

		return new MomentumProperties(mass, newVelocity * mass, compoundEffect,
				time);
	}

	public MomentumProperties updateMomentum(Double time) {
		Double alpha = eventFrictionFactor / mass;
		Double newCompoundEffect = compoundEffect + eventPercentageRefillOnBuzz
				* (time - compoundEffect);
		Double exp1 = Math
				.exp(-alpha
						* Math.pow(time - newCompoundEffect,
								eventFrictionExponent + 1));

		Double exp2 = Math.exp(-alpha
				* Math.pow(lastUpdateTime - compoundEffect,
						eventFrictionExponent + 1));

		Double oldVelocity = momentum / mass;

		Double newVelocity = (oldVelocity / exp1) * exp2;

		return new MomentumProperties(mass, newVelocity * mass,
				newCompoundEffect, time);
	}

	public Event updateEvent(Event event) {
		event.setMass(1.0);
		event.setMomentum(momentum);
		event.setCompoundEffect(compoundEffect);
		event.setMomentumLastUpdated(new Date());
		return event;
	}

	public static Double dateDiff(Date fromDate, Date toDate) {
		long d1 = fromDate.getTime();
		long d2 = toDate.getTime();
		if (d1 == d2)
			return 1.0;
		Duration duration = new Duration(d1, d2);
		return ((Long) duration.getStandardSeconds()).doubleValue();
	}

	public MomentumProperties updateStarredIdeaMomentum(Double time) {
		Double alpha = ideaFrictionFactor / mass;
		Double newCompoundEffect = compoundEffect + ideaPercentageRefillOnBuzz
				* (time - compoundEffect);
		
		Double exp1 = Math.exp(-alpha
				* Math.pow(time - newCompoundEffect, ideaFrictionExponent + 1));
		
//		Double exp1 = Math.exp(-alpha * (time - newCompoundEffect));
//
//		Double exp2 = Math.exp(-alpha * (lastUpdateTime - compoundEffect));
		
		Double exp2 = Math.exp(-alpha
				* Math.pow(lastUpdateTime - compoundEffect,
						ideaFrictionExponent + 1));
		
		Double oldVelocity = momentum / mass;
		
//		Double newVelocity = (oldVelocity / exp1) * exp2;
		Double newVelocity = (oldVelocity / exp2) * exp1;
		
		return new MomentumProperties(mass, newVelocity * mass,
				newCompoundEffect, time);
	}

	public MomentumProperties updateIdeaDomainMomentum(Double time) {
		Double alpha = ideaDomainFrictionFactor / mass;
		Double newCompoundEffect = compoundEffect
				+ ideaDomainPercentageRefillOnBuzz * (time - compoundEffect);
		Double exp1 = Math.exp(-alpha
				* Math.pow(time - newCompoundEffect,
						ideaDomainFrictionExponent + 1));

		Double exp2 = Math.exp(-alpha
				* Math.pow(lastUpdateTime - compoundEffect,
						ideaDomainFrictionExponent + 1));

		Double oldVelocity = momentum / mass;

		Double newVelocity = (oldVelocity / exp1) * exp2;

		return new MomentumProperties(mass, newVelocity * mass,
				newCompoundEffect, time);
	}

	public MomentumProperties updateIdeaLocationMomentum(Double time) {
		Double alpha = ideaLocationFrictionFactor / mass;
		Double newCompoundEffect = compoundEffect
				+ ideaLocationPercentageRefillOnBuzz * (time - compoundEffect);
		Double exp1 = Math.exp(-alpha
				* Math.pow(time - newCompoundEffect,
						ideaLocationFrictionExponent + 1));

		Double exp2 = Math.exp(-alpha
				* Math.pow(lastUpdateTime - compoundEffect,
						ideaLocationFrictionExponent + 1));

		Double oldVelocity = momentum / mass;

		Double newVelocity = (oldVelocity / exp1) * exp2;

		return new MomentumProperties(mass, newVelocity * mass,
				newCompoundEffect, time);
	}

}
