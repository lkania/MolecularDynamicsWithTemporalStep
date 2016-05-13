package itba.edu.ar.ss.output;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import itba.edu.ar.cellIndexMethod.data.particle.FloatPoint;
import itba.edu.ar.cellIndexMethod.data.particle.Particle;
import itba.edu.ar.ss.algorithm.AlgorithmObserver;
import itba.edu.ar.ss.model.entity.Entity;
import itba.edu.ar.ss.model.entity.impl.AstronomicalObject;
import itba.edu.ar.ss.system.data.SolarSystemData;

public class SolarSystemPositions implements AlgorithmObserver<FloatPoint> {

	private static double scalation = Math.pow(10, 9);
	private static final String COLUMNS_FILE = "Properties=id:I:1:pos:R:2:velocity:R:2:color:R:3";
	private List<ParticleData> data = new LinkedList<ParticleData>();

	private String path;
	private static String _SEPARTOR_ = " ";
	private String sunData;
	private int particleQuantity;
	private double length;

	public SolarSystemPositions(String path, SolarSystemData data) throws IOException {
		this.path = path;
		AstronomicalObject sun = data.getSun();
		FloatPoint position = sun.getPosition().divide(scalation);
		FloatPoint velocity = sun.getVelocity().divide(scalation);
		this.particleQuantity = data.getAstronomicalObjectsQuantity() + 1;
		this.length = data.getSpaceLength() / scalation;

		StringBuilder sb = new StringBuilder();
		sb.append(particleQuantity).append(_SEPARTOR_).append(position.getX()).append(_SEPARTOR_)
				.append(position.getY()).append(_SEPARTOR_).append(velocity.getX()).append(_SEPARTOR_)
				.append(velocity.getY()).append(_SEPARTOR_).append("1 0 0");
		sunData = sb.toString();

		Files.write(Paths.get(path + "SolarSystemPositions.csv"), new LinkedList<String>(), Charset.forName("UTF-8"),
				StandardOpenOption.TRUNCATE_EXISTING,StandardOpenOption.CREATE);

	}

	
	public void deltaTimeStepDone(Entity<FloatPoint> entity, double time) {

		ParticleData pd = new ParticleData(((AstronomicalObject) entity).getParticle());

		data.add(pd);
		System.out.println(pd.toString());
	}

	private int analizedParticles = 0;

	
	public void simulationEnded() {
		analizedParticles++;
		if (analizedParticles == particleQuantity - 1) {
			analizedParticles = 0;
			print();
			data.clear();
		}
	}

	int frame = 0;

	public void print() {

		List<String> fileContent = new ArrayList<String>();

		fileContent.add(particleQuantity + "");
		fileContent.add("Time=" + frame + " " + sizeBox(length) + " " + COLUMNS_FILE);

		fileContent.add(sunData);

		int id = 1;
		for (ParticleData particleData : data) {

			fileContent.add(id + particleData.toString());
			id++;
		}

		try {
			Files.write(Paths.get(path + "SolarSystemPositions.csv"), fileContent, Charset.forName("UTF-8"),
					StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			throw new IllegalAccessError();
		}

	}

	private String sizeBox(double length) {
		String sizeX = length + " 0.00000000 0.00000000";
		String sizeY = "0.00000000 " + length + " 0.00000000";
		String sizeZ = "0.00000000 0.00000000 0.000000000000000001"; // sizeZ!=(0,0,0)
																		// for
																		// Ovito
																		// recognize
																		// the
																		// box
																		// size
		String sizeBox = "Lattice=\"" + sizeX + " " + sizeY + " " + sizeZ + "\"";
		return sizeBox;
	}

	private static class ParticleData {
		private int id;
		private FloatPoint position;
		private FloatPoint velocity;

		public ParticleData(Particle particle) {
			this.id = particle.getId();
			this.position = particle.getPosition().divide(scalation);
			this.velocity = particle.getVelocity().divide(scalation);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + id;
			result = prime * result + ((position == null) ? 0 : position.hashCode());
			result = prime * result + ((velocity == null) ? 0 : velocity.hashCode());
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
			ParticleData other = (ParticleData) obj;
			if (id != other.id)
				return false;
			if (position == null) {
				if (other.position != null)
					return false;
			} else if (!position.equals(other.position))
				return false;
			if (velocity == null) {
				if (other.velocity != null)
					return false;
			} else if (!velocity.equals(other.velocity))
				return false;
			return true;
		}

		private String data;

		@Override
		public String toString() {
			if (data == null) {
				StringBuilder sb = new StringBuilder();
				sb.append(_SEPARTOR_).append(position.getX()).append(_SEPARTOR_).append(position.getY())
						.append(_SEPARTOR_).append(velocity.getX()).append(_SEPARTOR_).append(velocity.getY())
						.append(_SEPARTOR_).append("0.4 0.8 0.9");
				data = sb.toString();
			}
			return data;
		}

	}

}
