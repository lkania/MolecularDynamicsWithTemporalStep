package itba.edu.ar.ss.algorithm.impl;

import itba.edu.ar.ss.model.entity.Entity;
import itba.edu.ar.ss.model.entity.impl.Spring;
import itba.edu.ar.ss.model.force.Force;

public class VerletVelocityForSpring extends AbstractAlgorithm<Double> {

	private Spring spring;
	private double powDeltaTime2;
	private double deltaTimeOverTwiceMass;

	public VerletVelocityForSpring(double deltaTime, Spring spring) {
		super(deltaTime);
		this.spring = spring;
		this.deltaTimeOverTwiceMass = deltaTime / (2 * spring.getMass());
		this.powDeltaTime2 = Math.pow(deltaTime, 2);

	}

	public void evolveEntity(Entity<Double> entity, Force<Double> force) {

		double newPosition = entity.getPosition() + deltaTime * entity.getVelocity()
				+ powDeltaTime2 * force.getCurrentForce() / entity.getMass();

		double newVelocity = (entity.getVelocity()
				+ deltaTimeOverTwiceMass * (force.getCurrentForce() - spring.getElasticConst() * newPosition))
				/ (1 + spring.getAbsortionConst() * deltaTimeOverTwiceMass);

		setNewValues(entity,newPosition,newVelocity);
		
	}


}
