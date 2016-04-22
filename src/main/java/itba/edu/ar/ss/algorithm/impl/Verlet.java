package itba.edu.ar.ss.algorithm.impl;

import itba.edu.ar.cellIndexMethod.data.particle.FloatPoint;
import itba.edu.ar.ss.data.entity.Entity;
import itba.edu.ar.ss.data.force.Force;

public class Verlet extends AbstractAlgorithm<FloatPoint> {

	private double powDeltaTime2;

	public Verlet(double deltaTime) {
		super(deltaTime);
		powDeltaTime2 = Math.pow(deltaTime, 2);
	}

	public void evolveEntity(Entity<FloatPoint> entity, Force<FloatPoint> force) {

		FloatPoint newPosition = entity.getPosition().multiply(2).minus(entity.getPreviousPosition())
				.plus(force.getCurrentForce().multiply(powDeltaTime2 / entity.getMass()));

		FloatPoint newVelocity = newPosition.minus(entity.getPosition()).divide(deltaTime);
		
		setNewValues(entity, newPosition, newVelocity);
		
	}


}
