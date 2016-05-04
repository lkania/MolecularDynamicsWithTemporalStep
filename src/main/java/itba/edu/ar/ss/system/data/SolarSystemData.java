package itba.edu.ar.ss.system.data;

import itba.edu.ar.cellIndexMethod.data.particle.FloatPoint;
import itba.edu.ar.cellIndexMethod.data.particle.Particle;
import itba.edu.ar.ss.model.entity.impl.AstronomicalObject;
import itba.edu.ar.ss.system.AstronomicalObjectData;

public abstract class SolarSystemData {

	private AstronomicalObject sun;

	public abstract double getSpaceLength();

	public abstract double getSunMass();

	public abstract int getAstronomicalObjectsQuantity();

	public abstract double getAngularMoment();

	public abstract double getMaxDistanceBetweenAstronomicalObjects();

	public abstract double getDeltaTime();

	public abstract double getSimulationTime();

	public AstronomicalObject getSun() {
		if (sun == null) {
			sun = new AstronomicalObject(new Particle(getSunMass(),
					new FloatPoint(getSpaceLength() / 2, getSpaceLength() / 2), new FloatPoint(0, 0)), 0,getDeltaTime());
		}

		return sun;
	}

}
