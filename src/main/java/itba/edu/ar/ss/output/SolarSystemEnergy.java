package itba.edu.ar.ss.output;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;

import itba.edu.ar.cellIndexMethod.data.particle.Particle;
import itba.edu.ar.ss.model.entity.impl.AstronomicalObject;
import itba.edu.ar.ss.model.force.impl.AstronomicalObjectForce;
import itba.edu.ar.ss.simulation.solarSystem.SolarSystemSimulationObserver;
import itba.edu.ar.ss.system.data.SolarSystemData;

public class SolarSystemEnergy implements SolarSystemSimulationObserver {

	private List<String> fileContent = new LinkedList<String>();
	private static String _SEPARATOR_ = " ";
	private String path;
	private SolarSystemData data;
	
	public SolarSystemEnergy(String path,SolarSystemData data) {
		this.path=path;
		this.data=data;
	}
	
	@Override
	public void stepEnded(List<Particle> particles, double time) {
		StringBuilder sb = new StringBuilder();
		double totalPotentialEnergy = 0;
		double totalKineticEnergy = 0;

		for (Particle particle : particles) {
			totalKineticEnergy += getKineticEnergy(particle);
			totalPotentialEnergy += getPotentialEnergy(particle);
		}

		sb.append(time).append(_SEPARATOR_).append(totalKineticEnergy).append(_SEPARATOR_).append(totalPotentialEnergy)
				.append(_SEPARATOR_).append(totalKineticEnergy + totalPotentialEnergy);
		fileContent.add(sb.toString());
	}

	public double getKineticEnergy(Particle particle) {
		return 0.5 * particle.getMass() * Math.pow(particle.getVelocityAbs(), 2);
	}

	public double getPotentialEnergy(Particle particle) {
		AstronomicalObject sun = data.getSun();
		return -AstronomicalObjectForce.G * particle.getMass() * sun.getMass()
				/ particle.getPosition().distance(sun.getPosition());
	}

	@Override
	public void simulationEnded() throws IOException {
		Files.write(Paths.get(path + "SolarSystemEnergy"), fileContent, Charset.forName("UTF-8"));
	}

}
