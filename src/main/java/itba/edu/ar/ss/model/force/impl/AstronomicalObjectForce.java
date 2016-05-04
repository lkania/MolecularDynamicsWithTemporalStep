package itba.edu.ar.ss.model.force.impl;

import itba.edu.ar.cellIndexMethod.data.particle.FloatPoint;
import itba.edu.ar.ss.model.entity.impl.AstronomicalObject;
import itba.edu.ar.ss.model.force.Force;

public class AstronomicalObjectForce implements Force<FloatPoint> {

	private AstronomicalObject ao1;
	private AstronomicalObject sun;
	public static double G = 6.693 * Math.pow(10, -11);
	
	public AstronomicalObjectForce(AstronomicalObject ao1, AstronomicalObject sun) {
		super();
		this.ao1 = ao1;
		this.sun = sun;
	}

	public FloatPoint getCurrentForce() {
		FloatPoint vectorDistance = sun.getPosition().minus(ao1.getPosition());
		double absDistance = vectorDistance.abs();

		double absForce = ao1.getMass() * sun.getMass() * G / Math.pow(absDistance, 2);

		FloatPoint force = vectorDistance.divide(absDistance).multiply(absForce);

		return force;
	}

	public FloatPoint getPreviousForce() {
		throw new IllegalAccessError();
	}

}
