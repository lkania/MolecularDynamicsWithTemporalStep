package itba.edu.ar.ss.simulation.solarSystem.data;

import itba.edu.ar.cellIndexMethod.data.particle.Particle;

public class Collision {

	private Particle p1;
	private Particle p2;

	public Collision(Particle p1, Particle p2) {
		this.p1 = p1;
		this.p2 = p2;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((p1 == null) ? 0 : p1.hashCode());
		result = prime * result + ((p2 == null) ? 0 : p2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Collision other = (Collision) obj;

		return ((p1.equals(other.getP1()) && p2.equals(other.getP2()))
				|| (p1.equals(other.getP2()) && p2.equals(other.getP1())));

	}

	public Particle getP1() {
		return p1;
	}

	public Particle getP2() {
		return p2;
	}

}
