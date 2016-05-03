package itba.edu.ar.ss.test.spring;

import java.io.IOException;

import itba.edu.ar.cellIndexMethod.data.particle.FloatPoint;
import itba.edu.ar.ss.algorithm.Algorithm;
import itba.edu.ar.ss.algorithm.impl.Verlet;
import itba.edu.ar.ss.output.SolarSystemPositions;
import itba.edu.ar.ss.simulation.solarSystem.SolarSystemSimulation;
import itba.edu.ar.ss.system.data.SolarSystemData;

public class SolarSystemTest {


	private static SolarSystemData data = new SolarSystemData() {

		private double sunMass = 2*Math.pow(10,30);
		private double spaceLength = 2*Math.pow(10,10)*2;
		private double maxDistanceBetweenAstronomicalObjects = Math.pow(10,6);
		private double deltaTime = 10;
		
		private double angularMovement = 0.7*7.307*Math.pow(10,40);
		
		private double scalationFactor = Math.pow(10,3);
				
		@Override
		public double getSunMass() {
			return sunMass;
		}
		
		@Override
		public double getSpaceLength() {
			return spaceLength ;
		}
		
		@Override
		public double getSimulationTime() {
			return deltaTime*1000000;
		}
		
		@Override
		public double getMaxDistanceBetweenAstronomicalObjects() {
			return maxDistanceBetweenAstronomicalObjects ;
		}
		
		@Override
		public double getDeltaTime() {
			return deltaTime ;
		}
		
		@Override
		public int getAstronomicalObjectsQuantity() {
			return 100;
		}
		
		@Override
		public double getAngularMoment() {
			return angularMovement ;
		}
	};
	
	private static String path = System.getProperty("user.dir")+"/";
	
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, IOException {

		Algorithm<FloatPoint> algorithm = new Verlet(data.getDeltaTime());
		SolarSystemPositions ssp = new SolarSystemPositions(path, data);
		
		algorithm.subscribe(ssp);
		
		SolarSystemSimulation sss = new SolarSystemSimulation();
		sss.simulate(data, path,algorithm);
		
	}
	
}
