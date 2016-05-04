package itba.edu.ar.ss.test.spring;

import java.io.IOException;

import itba.edu.ar.cellIndexMethod.data.particle.FloatPoint;
import itba.edu.ar.ss.algorithm.Algorithm;
import itba.edu.ar.ss.algorithm.impl.Verlet;
import itba.edu.ar.ss.model.entity.impl.AstronomicalObject;
import itba.edu.ar.ss.model.force.impl.AstronomicalObjectForce;
import itba.edu.ar.ss.output.SolarSystemEnergy;
import itba.edu.ar.ss.output.SolarSystemPositions;
import itba.edu.ar.ss.simulation.solarSystem.SolarSystemSimulation;
import itba.edu.ar.ss.system.data.SolarSystemData;

public class SolarSystemTest {

	private static double deltaTime = 1000;
	private static int aoQuanitity = 10;
	private static double sunMass = 2 * Math.pow(10, 30);

	private static SolarSystemData data = new SolarSystemData() {

		private double spaceLength = 2 * Math.pow(10, 10) * 10;
		private double maxDistanceBetweenAstronomicalObjects = Math.pow(10,6);

		private double angularMovement = Math
				.sqrt(AstronomicalObjectForce.G * Math.pow(sunMass, 3) * Math.pow(10, 9)) / aoQuanitity;

		@Override
		public double getSunMass() {
			return sunMass;
		}

		@Override
		public double getSpaceLength() {
			return spaceLength;
		}

		@Override
		public double getSimulationTime() {
			return deltaTime * 500;
		}

		@Override
		public double getMaxDistanceBetweenAstronomicalObjects() {
			return maxDistanceBetweenAstronomicalObjects;
		}

		@Override
		public double getDeltaTime() {
			return deltaTime;
		}

		@Override
		public int getAstronomicalObjectsQuantity() {
			return aoQuanitity;
		}

		@Override
		public double getAngularMoment() {
			return angularMovement;
		}
	};

	private static String path = System.getProperty("user.dir") + "/";

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, IOException {

		Algorithm<FloatPoint> algorithm = new Verlet(data.getDeltaTime());
		SolarSystemPositions ssp = new SolarSystemPositions(path, data);

		SolarSystemEnergy sse = new SolarSystemEnergy(path, data);
		
		algorithm.subscribe(ssp);
		
		SolarSystemSimulation sss = new SolarSystemSimulation();
		sss.subscribe(sse);
		
		sss.simulate(data, path, algorithm);

	}

}
