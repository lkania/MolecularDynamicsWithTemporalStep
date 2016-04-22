package itba.edu.ar.ss.algorithm.impl;

import itba.edu.ar.ss.data.entity.Entity;
import itba.edu.ar.ss.data.entity.impl.Spring;
import itba.edu.ar.ss.data.force.Force;

public class BeemanForSpring extends AbstractAlgorithm<Double> {

	private double powDeltaTime2;
	private Spring spring;
	private boolean first=true;
	
	public BeemanForSpring(double deltaTime,Spring spring) {
		super(deltaTime);
		this.spring=spring;
		this.powDeltaTime2 = Math.pow(deltaTime, 2);

	}

	public void evolveEntity(Entity<Double> entity, Force<Double> force) {

		double previousAceleration = (force.getPreviousForce() / entity.getMass());
		double currentAceleration = (force.getCurrentForce() / entity.getMass());

		double newPosition = entity.getPosition() + entity.getVelocity() * deltaTime
				+ (2 / 3.0) * currentAceleration * powDeltaTime2 - (1 / 6.0) * previousAceleration * powDeltaTime2;

		double newVelocity = (entity.getVelocity() + (5 / 6.0) * deltaTime * currentAceleration
				- (1 / 6.0) * previousAceleration * deltaTime
				+ (1 / 3.0) * deltaTime * (-spring.getElasticConst() * newPosition / entity.getMass()))
				/ (1 + (spring.getAbsortionConst() * deltaTime / (3 * entity.getMass())));

		setNewValues(entity, newPosition, newVelocity);
	}
	
	@Override
	public String getName() {
		return "Beeman";
	}

}
