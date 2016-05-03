package itba.edu.ar.ss.system;

import itba.edu.ar.cellIndexMethod.data.particle.FloatPoint;
import itba.edu.ar.input.file.data.Data;

public class AstronomicalObjectData extends Data {

	private FloatPoint sunPosition;
	private double angularMoment;

	public AstronomicalObjectData(int particleQuantity, double mass, FloatPoint sunPosition, double angularMoment) {
		super(particleQuantity, mass, 0);
		this.sunPosition = sunPosition;
		this.angularMoment = angularMoment;

	}

	@Override
	public FloatPoint getVelocity(double positionX, double positionY) {
		return getVelocity(new FloatPoint(positionX, positionY));
	}

	public FloatPoint getVelocity(FloatPoint position) {
		FloatPoint distance = position.minus(sunPosition);

		double velocityAbs = angularMoment / (distance.abs() * getMass());

		FloatPoint velocity = distance.rotateRadiants(Math.PI / 2).divide(distance.abs()).multiply(velocityAbs);

		return velocity;

	}
	
	private static double A = Math.pow(10, 10) - Math.pow(10, 9);
	private static double B = Math.pow(10, 9);
	
	@Override
	public double getPosition(double random) {
		return sunPosition.getX()+(random*A+B)*((Math.random()<0.5) ? 1:-1);
	}

}