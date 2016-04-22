package itba.edu.ar.ss.data.force.impl;

import itba.edu.ar.cellIndexMethod.data.particle.FloatPoint;
import itba.edu.ar.ss.data.entity.impl.AstronomicalObject;
import itba.edu.ar.ss.data.force.Force;

public class AstronomicalObjectForce implements Force<FloatPoint> {

	private AstronomicalObject ao1;
	private AstronomicalObject ao2;
	private static double G = 6.693 * Math.pow(10, -11);

	public FloatPoint getCurrentForce() {
		FloatPoint vectorDistance = ao2.getPosition().minus(ao1.getPosition());
		double absDistance = vectorDistance.abs();

		double absForce = ao1.getMass() * ao2.getMass() * G / Math.pow(absDistance, 2);

		FloatPoint force = vectorDistance.divide(absDistance).multiply(absForce);

		return force;
	}

	public FloatPoint getPreviousForce() {
		throw new IllegalAccessError();
	}

}
