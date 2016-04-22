package itba.edu.ar.ss.data.force.impl;

import itba.edu.ar.ss.data.entity.impl.Spring;
import itba.edu.ar.ss.data.force.Force;

public class SpringForce implements Force<Double> {

	private Spring spring;
	private double force;

	public SpringForce(Spring spring) {
		super();
		this.spring = spring;

	}

	// Beeman Constructor
	public SpringForce(Spring spring, double deltaTime) {
		super();
		this.spring = spring;
		double currentForce = getCurrentForce();
	
		double previousPosition = spring.getPosition() - spring.getVelocity() * deltaTime
				+ currentForce * Math.pow(deltaTime, 2) / (2 * spring.getMass());
		double previousVelocity = spring.getVelocity() - deltaTime * currentForce / spring.getMass();

		force = getForce(previousPosition, previousVelocity);
	}

	public Double getCurrentForce() {
		force = getForce(spring.getPosition(), spring.getVelocity());
		return force;
	}

	private double getForce(double position, double velocity) {
		return -spring.getElasticConst() * position - spring.getAbsortionConst() * velocity;
	}

	public Double getPreviousForce() {
		return force;
	}

}
