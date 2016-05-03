package itba.edu.ar.ss.simulation.solarSystem;

import java.io.IOException;
import java.util.List;

import itba.edu.ar.cellIndexMethod.data.particle.Particle;

public interface SolarSystemSimulationObserver {

	public void stepEnded(List<Particle> particles,double time);
	public void simulationEnded() throws IOException;
	
}
