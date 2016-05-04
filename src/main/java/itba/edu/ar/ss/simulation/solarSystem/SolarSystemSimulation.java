package itba.edu.ar.ss.simulation.solarSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.omg.CORBA.IdentifierHelper;

import itba.edu.ar.cellIndexMethod.CellIndexMethod;
import itba.edu.ar.cellIndexMethod.IndexMatrix;
import itba.edu.ar.cellIndexMethod.IndexMatrixBuilder;
import itba.edu.ar.cellIndexMethod.data.particle.FloatPoint;
import itba.edu.ar.cellIndexMethod.data.particle.Particle;
import itba.edu.ar.cellIndexMethod.route.Route;
import itba.edu.ar.cellIndexMethod.route.routeImpl.OptimizedRoute;
import itba.edu.ar.ss.algorithm.Algorithm;
import itba.edu.ar.ss.model.entity.Entity;
import itba.edu.ar.ss.model.entity.impl.AstronomicalObject;
import itba.edu.ar.ss.model.force.Force;
import itba.edu.ar.ss.model.force.impl.AstronomicalObjectForce;
import itba.edu.ar.ss.system.SolarSystemGenerator;
import itba.edu.ar.ss.system.data.SolarSystemData;

public class SolarSystemSimulation {

	private List<SolarSystemSimulationObserver> subscribers = new LinkedList<>();

	public void subscribe(SolarSystemSimulationObserver ssso) {
		this.subscribers.add(ssso);
	}

	private List<Particle> particles;

	public void simulate(SolarSystemData data, String path, Algorithm<FloatPoint> algorithm)
			throws InstantiationException, IllegalAccessException, IOException {

		List<String> staticPath = new ArrayList<String>();
		List<String> dynamicPath = new ArrayList<String>();

		SolarSystemGenerator.generate(data, path, staticPath, dynamicPath);

		IndexMatrix indexMatrix = IndexMatrixBuilder.getIndexMatrix(staticPath.get(0), dynamicPath.get(0),
				getCellQuantity(data), data.getDeltaTime());

		Map<Integer, Entity<FloatPoint>> astronomicalObjectsMap = getAstronomicalObjects(indexMatrix.getParticles(),
				data);

		particles = indexMatrix.getParticles();
		
		for (double time = 0; time < data.getSimulationTime(); time += data.getDeltaTime()) {

			do {
				indexMatrix.clear();
				indexMatrix.addParticles(particles);

				CellIndexMethod cellIndexMethod = new CellIndexMethod(indexMatrix, getRoute(data),
						data.getMaxDistanceBetweenAstronomicalObjects());

				cellIndexMethod.execute();

			} while (hasCollided(astronomicalObjectsMap, data));

			Collection<Entity<FloatPoint>> astronomicalObjects = (Collection<Entity<FloatPoint>>) astronomicalObjectsMap
					.values();

			algorithm.evolveSystem(astronomicalObjects, getForces(astronomicalObjects, data), data.getDeltaTime());

			notifyStepEnded(particles, time);

		}

		notifySimulationEnded();

	}

	private void notifySimulationEnded() throws IOException {
		for (SolarSystemSimulationObserver ssso : subscribers) {
			ssso.simulationEnded();
		}
	}

	private void notifyStepEnded(List<Particle> particles, double time) {
		for (SolarSystemSimulationObserver ssso : subscribers) {
			ssso.stepEnded(particles, time);
		}
	}

	private static List<Force<FloatPoint>> getForces(Collection<Entity<FloatPoint>> astronomicalObjects,
			SolarSystemData data) {

		Iterator<Entity<FloatPoint>> iterator = astronomicalObjects.iterator();
		List<Force<FloatPoint>> ans = new LinkedList<>();

		while (iterator.hasNext()) {
			AstronomicalObject ao = (AstronomicalObject) iterator.next();
			Force<FloatPoint> force = new AstronomicalObjectForce(ao, data.getSun());
			ans.add(force);
		}

		return ans;
	}

	private static Map<Integer, Entity<FloatPoint>> getAstronomicalObjects(List<Particle> particles,
			SolarSystemData data) {
		Map<Integer, Entity<FloatPoint>> astronomicalObjectMap = new HashMap<>();

		for (Particle ao : particles) {
			astronomicalObjectMap.put(ao.getId(),
					new AstronomicalObject(ao, data.getAngularMoment(), data.getDeltaTime()));
		}

		return astronomicalObjectMap;
	}

	private static List<Particle> getParticles(Collection<Entity<FloatPoint>> collection) {
		List<Particle> particles = new LinkedList<>();
		for (Entity<FloatPoint> ao : collection) {
			particles.add(((AstronomicalObject) ao).getParticle());
		}
		return particles;
	}

	private static Route getRoute(SolarSystemData data) {
		return new OptimizedRoute(getCellQuantity(data), false, data.getSpaceLength());
	}

	private static int getCellQuantity(SolarSystemData data) {
		if (data.getAstronomicalObjectsQuantity() / Math.pow(data.getSpaceLength(), 2) < 0.05) {
			return (int) Math.floor(Math.sqrt(data.getAstronomicalObjectsQuantity()));
		}

		return (int) Math.ceil(data.getSpaceLength() / data.getMaxDistanceBetweenAstronomicalObjects()) - 1;
	}

	private boolean hasCollided(Map<Integer, Entity<FloatPoint>> astronomicalObjectsMap, SolarSystemData data) {

		boolean collision = false;

		for (Particle particle : particles) {

			for (Particle neightbour : particle.getNeightbours()) {
				if (particle.distance(neightbour) < data.getMaxDistanceBetweenAstronomicalObjects()) {
					collision = true;

					collide(particle, neightbour, astronomicalObjectsMap, data.getSun());

				}
			}
		}

		particles.clear();
		particles.addAll(getParticles(astronomicalObjectsMap.values()));

		return collision;
	}

	private static void collide(Particle particle, Particle neightbour,
			Map<Integer, Entity<FloatPoint>> astronomicalObjectsMap, AstronomicalObject sun) {

		System.out.println("hiyyy");
		AstronomicalObject ao1 = (AstronomicalObject) astronomicalObjectsMap.get(particle.getId());
		AstronomicalObject ao2 = (AstronomicalObject) astronomicalObjectsMap.get(neightbour.getId());

		// The collision is ignored if one of the particles has already collided
		// with other particle
		if (ao1 == null || ao2 == null)
			return;

		double newMass = ao1.getMass() + ao2.getMass();

		FloatPoint newPosition = ao1.getPosition().multiply(ao1.getMass())
				.plus(ao2.getPosition().multiply(ao2.getMass())).divide(newMass);

		double newAngularMoment = ao1.getAngularMoment() + ao2.getAngularMoment();

		double tanVelocityAbs = (newAngularMoment) / (newMass * newPosition.abs());

		FloatPoint tanVelocity = sun.getPosition().minus(newPosition).getVersor().multiply(tanVelocityAbs)
				.rotateRadiants(Math.PI / 2);

		FloatPoint radialVelocity1 = getRadialVelocity(ao1, sun);
		FloatPoint radialVelocity2 = getRadialVelocity(ao2, sun);

		FloatPoint radialVelocity = radialVelocity1.multiply(ao1.getMass())
				.plus(radialVelocity2.multiply(ao2.getMass())).divide(ao1.getMass() + ao2.getMass());

		FloatPoint newVelocity = tanVelocity.plus(radialVelocity);

		AstronomicalObject newAo = new AstronomicalObject(new Particle(newMass, newPosition, newVelocity),
				newAngularMoment, ao1.getPreviousPosition().plus(ao2.getPreviousPosition()).divide(2));

		astronomicalObjectsMap.remove(ao1.getParticle().getId());
		astronomicalObjectsMap.remove(ao2.getParticle().getId());
		astronomicalObjectsMap.put(newAo.getParticle().getId(), newAo);

	}

	private static FloatPoint getRadialVelocity(AstronomicalObject ao1, AstronomicalObject sun) {
		FloatPoint radialVersor = sun.getPosition().minus(ao1.getPosition()).getVersor();
		return radialVersor.multiply(ao1.getVelocity().multiply(radialVersor));
	}

}
