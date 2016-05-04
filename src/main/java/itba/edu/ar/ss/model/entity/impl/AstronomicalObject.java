package itba.edu.ar.ss.model.entity.impl;

import itba.edu.ar.cellIndexMethod.data.particle.FloatPoint;
import itba.edu.ar.cellIndexMethod.data.particle.Particle;
import itba.edu.ar.ss.model.entity.Entity;
import itba.edu.ar.ss.model.force.Force;

public class AstronomicalObject implements Entity<FloatPoint> {

	private Particle particle;
	private FloatPoint previousPosition;
	private double angularMoment;

	public AstronomicalObject(Particle particle, double angularMoment, double deltaTime) {
		super();
		this.particle = particle;
		this.setAngularMoment(angularMoment);
		this.previousPosition = particle.getPosition().minus(particle.getVelocity().multiply(deltaTime));

	}

	public AstronomicalObject(Particle particle, double angularMoment, FloatPoint previousPosition) {
		super();
		this.particle = particle;
		this.setAngularMoment(angularMoment);
		this.previousPosition = previousPosition;

	}

	private void setAngularMoment(double angularMoment) {
		this.angularMoment = angularMoment;
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

		return Math.hypot(position.getX() - ao2.getPosition().getX(), position.getY() - ao2.getPosition().getY());
	}

	public Particle getParticle() {
		return particle;
	}

	public double getAngularMoment() {
		return angularMoment;
	}

}
