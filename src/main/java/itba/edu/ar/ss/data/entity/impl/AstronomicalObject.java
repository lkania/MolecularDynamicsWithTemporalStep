package itba.edu.ar.ss.data.entity.impl;

import itba.edu.ar.cellIndexMethod.data.particle.FloatPoint;
import itba.edu.ar.cellIndexMethod.data.particle.Particle;
import itba.edu.ar.ss.data.entity.Entity;

public class AstronomicalObject implements Entity<FloatPoint>{

	private Particle particle;
	private FloatPoint previousPosition;
	
	
	public AstronomicalObject(Particle particle) {
		super();
		this.particle = particle;
	}

	public FloatPoint getPosition() {
		return particle.getPosition();
	}

	public double getMass() {
		return particle.getMass();
	}

	public FloatPoint getVelocity() {
		return particle.getVelocity();
	}

	public void setPosition(FloatPoint newPosition) {
		previousPosition = particle.getPosition();
		particle.setPosition(newPosition);
		
	}

	public void setVelocity(FloatPoint velocity) {
		particle.setVelocity(velocity);
	}

	public FloatPoint getPreviousPosition() {
		return previousPosition;
	}

	public double distance(AstronomicalObject ao2) {
		FloatPoint position = particle.getPosition();
		
		return Math.hypot(position.getX()-ao2.getPosition().getX(),position.getY()-ao2.getPosition().getY());
	}

}
