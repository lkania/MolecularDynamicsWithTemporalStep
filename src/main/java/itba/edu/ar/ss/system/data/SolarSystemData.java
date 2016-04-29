package itba.edu.ar.ss.system.data;

import itba.edu.ar.ss.model.entity.impl.AstronomicalObject;
import itba.edu.ar.ss.system.AstronomicalObjectData;

public interface SolarSystemData {

	public double getSpaceLength();

	public int getSunMass();

	public int getAstronomicalObjectsQuantity();

	public double getAngularMoment();
	
	public double getMaxDistanceBetweenAstronomicalObjects();

	public double getDeltaTime();

	public double getSimulationTime();

	public AstronomicalObject getSun();

	public void setSunData(AstronomicalObjectData sun);
	
	

	
}
