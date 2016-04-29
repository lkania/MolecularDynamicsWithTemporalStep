package itba.edu.ar.ss.simulation.solarSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import itba.edu.ar.cellIndexMethod.CellIndexMethod;
import itba.edu.ar.cellIndexMethod.IndexMatrix;
import itba.edu.ar.cellIndexMethod.IndexMatrixBuilder;
import itba.edu.ar.cellIndexMethod.data.particle.FloatPoint;
import itba.edu.ar.cellIndexMethod.data.particle.Particle;
import itba.edu.ar.cellIndexMethod.route.Route;
import itba.edu.ar.cellIndexMethod.route.routeImpl.OptimizedRoute;
import itba.edu.ar.ss.model.entity.impl.AstronomicalObject;
import itba.edu.ar.ss.system.SolarSystemGenerator;
import itba.edu.ar.ss.system.data.SolarSystemData;

public class SolarSystemSimulation {

	public static void simulate(SolarSystemData data, String path)
			throws InstantiationException, IllegalAccessException, IOException {

		List<String> staticPath = new ArrayList<String>();
		List<String> dynamicPath = new ArrayList<String>();

		SolarSystemGenerator.generate(data, path, staticPath, dynamicPath);

		IndexMatrix indexMatrix = IndexMatrixBuilder.getIndexMatrix(staticPath.get(0), dynamicPath.get(0),
				getCellQuantity(data), data.getDeltaTime());

		
		for (double time = 0; time < data.getSimulationTime(); time += data.getDeltaTime()) {
			
			Map<Integer, AstronomicalObject> astronomicalObjectsMap = getAstronomicalObjects(indexMatrix.getParticles(),data);
			
			CellIndexMethod cellIndexMethod = new CellIndexMethod(indexMatrix, getRoute(data),
					data.getMaxDistanceBetweenAstronomicalObjects());

			cellIndexMethod.execute();

			List<Particle>  particles = getAstronomicalObjectsWithoutCollisions(indexMatrix.getParticles(), astronomicalObjectsMap, data);

			indexMatrix.clear();
			indexMatrix.addParticles(particles);
			
		}

	}

	private static Map<Integer, AstronomicalObject> getAstronomicalObjects(
			List<Particle> particles,SolarSystemData data) {
		Map<Integer, AstronomicalObject> astronomicalObjectMap = new HashMap<>();

		for (Particle ao : particles) {
			astronomicalObjectMap.put(ao.getId(), new AstronomicalObject(ao, data.getAngularMoment()));
		}

		return astronomicalObjectMap;
	}

	private static List<Particle> getParticles(Collection<AstronomicalObject> collection) {
		List<Particle> particles = new LinkedList<>();
		for (AstronomicalObject ao : collection) {
			particles.add(ao.getParticle());
		}
		return particles;
	}

	private static Route getRoute(SolarSystemData data) {
		return new OptimizedRoute(getCellQuantity(data), false, data.getSpaceLength());
	}

	private static int getCellQuantity(SolarSystemData data) {
		return (int) Math.ceil(data.getSpaceLength() / data.getMaxDistanceBetweenAstronomicalObjects()) - 1;
	}

	private static List<Particle> getAstronomicalObjectsWithoutCollisions(List<Particle> particles,
			Map<Integer, AstronomicalObject> astronomicalObjectsMap, SolarSystemData data) {

		boolean collision = false;

		for (Particle particle : particles) {

			for (Particle neightbour : particle.getNeightbours()) {
				if (particle.distance(neightbour) < data.getMaxDistanceBetweenAstronomicalObjects()) {
					collision = true;

					collide(particle, neightbour, astronomicalObjectsMap, data.getSun());

				}
			}

		}
		
		if(collision)
		{
			return getAstronomicalObjectsWithoutCollisions(particles, astronomicalObjectsMap, data);
		}
		
		List<Particle> ans = new LinkedList<Particle>();
		ans.addAll(getParticles(astronomicalObjectsMap.values()));
		return ans;
	}

	private static void collide(Particle particle, Particle neightbour, Map<Integer, AstronomicalObject> astronomicalObjectsMap,AstronomicalObject sun) {
		AstronomicalObject ao1 =astronomicalObjectsMap.get(particle.getId());
		AstronomicalObject ao2 =astronomicalObjectsMap.get(neightbour.getId());

		//The collision is ignored if one of the particles has already collided with other particle
		if(ao1==null || ao2==null)
			return;
		
		double newMass = ao1.getMass()+ao2.getMass();
		
		FloatPoint newPosition = ao1.getPosition().multiply(ao1.getMass()).plus(ao2.getPosition().multiply(ao2.getMass())).divide(newMass);

		double newAngularMoment = ao1.getAngularMoment()+ao2.getAngularMoment();
		
		double tanVelocityAbs = (newAngularMoment)/(newMass*newPosition.abs());
		
		FloatPoint tanVelocity = sun.getPosition().minus(newPosition).getVersor().multiply(tanVelocityAbs);
		
		FloatPoint radialVelocity1 = getRadialVelocity(ao1,sun);
		FloatPoint radialVelocity2 = getRadialVelocity(ao2,sun);

		
		FloatPoint radialVelocity = radialVelocity1.multiply(ao1.getMass()).plus(radialVelocity2.multiply(ao2.getMass())).divide(ao1.getMass()+ao2.getMass());
		
		FloatPoint newVelocity =  tanVelocity.plus(radialVelocity);
		
		AstronomicalObject newAo = new AstronomicalObject(new Particle(newMass,newPosition,newVelocity),newAngularMoment);
		
		astronomicalObjectsMap.remove(ao1.getParticle().getId());
		astronomicalObjectsMap.remove(ao2.getParticle().getId());
		astronomicalObjectsMap.put(newAo.getParticle().getId(),newAo);
		
	}

	private static FloatPoint getRadialVelocity(AstronomicalObject ao1, AstronomicalObject sun) {
		FloatPoint radialVersor = sun.getPosition().minus(ao1.getPosition()).getVersor();
		return radialVersor.multiply(ao1.getVelocity().multiply(radialVersor));
	}

}
