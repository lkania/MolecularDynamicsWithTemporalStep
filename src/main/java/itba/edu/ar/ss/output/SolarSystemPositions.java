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

	private List<ParticleData> data = new LinkedList<ParticleData>();

	private String path;
	private static String _SEPARTOR_ = " ";
	private String sunData;
	private int particleQuantity;

	public SolarSystemPositions(String path, SolarSystemData data) {
		this.path = path;
		AstronomicalObject sun = data.getSun();
		FloatPoint position = sun.getPosition().divide(scalation);
		FloatPoint velocity = sun.getVelocity().divide(scalation);

		StringBuilder sb = new StringBuilder();
		sb.append(101).append(_SEPARTOR_).append(position.getX()).append(_SEPARTOR_).append(position.getY())
				.append(_SEPARTOR_).append(velocity.getX()).append(_SEPARTOR_).append(velocity.getY());
		sunData = sb.toString();
		this.particleQuantity = data.getAstronomicalObjectsQuantity() + 1;
	}

	@Override
	public void deltaTimeStepDone(Entity<FloatPoint> entity, double time) {

		ParticleData pd = new ParticleData(((AstronomicalObject) entity).getParticle());

		data.add(pd);
		System.out.println(pd.toString());
	}

	private int analizedParticles = 0;

	@Override
	public void simulationEnded() {
		analizedParticles++;
		if (analizedParticles == 100) {
			analizedParticles = 0;
			print();
			data.clear();
		}
	}

	int frame=0;
	public void print() {

		List<String> fileContent = new ArrayList<>();


			fileContent.add(particleQuantity + "");
			fileContent.add(frame+++"");
			fileContent.add(sunData);

			int id = 1;
			for (ParticleData particleData : data) {

				fileContent.add(id + particleData.toString());
				id++;
			}

	
		try {
			Files.write(Paths.get(path + "SolarSystemPositions"), fileContent, Charset.forName("UTF-8"),
					StandardOpenOption.CREATE,StandardOpenOption.WRITE,StandardOpenOption.APPEND);
		} catch (IOException e) {
			throw new IllegalAccessError();
		}

	}

	private static double scalation = Math.pow(10, 9);

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
						.append(_SEPARTOR_).append(velocity.getX()).append(_SEPARTOR_).append(velocity.getY());
				data = sb.toString();
			}
			return data;
		}

	}

}
