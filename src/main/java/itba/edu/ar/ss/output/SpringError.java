package itba.edu.ar.ss.output;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import itba.edu.ar.ss.algorithm.AlgorithmObserver;
import itba.edu.ar.ss.model.entity.Entity;
import itba.edu.ar.ss.model.entity.impl.Spring;

public class SpringError implements AlgorithmObserver<Double> {

	private static final String SEPARATOR = " ";
	private double[] positions;
	private double[] errors;
	private double deltaTime;

	private int currentTimeStep = 0;
	private String path;
	private String method;

	public SpringError(String path, String method, Spring spring, double deltaTime, double finalTime) {
		this.path = path;
		this.deltaTime = deltaTime;
		this.method = method;
		generatePositions(spring, finalTime);

	}

	private void generatePositions(Spring spring, double finalTime) {

		int timeSteps = (int) (finalTime / deltaTime);
System.out.println(timeSteps);
		
		positions = new double[timeSteps];
		errors = new double[timeSteps];

		for (int i = 0; i < timeSteps; i++) {
			double time = i * deltaTime;
			positions[i] = Math.exp(-spring.getAbsortionConst() * time / (2 * spring.getMass()))
					* Math.cos(time * Math.sqrt(spring.getElasticConst() / spring.getMass()
							- Math.pow(spring.getAbsortionConst(), 2) / (4 * Math.pow(spring.getMass(), 2))));
		}

	}

	public void deltaTimeStepDone(Entity<Double> entity, double time) {
		errors[currentTimeStep] = Math.pow(positions[currentTimeStep] - entity.getPosition(), 2);
		currentTimeStep++;
	}

	public void simulationEnded() {
		List<String> fileContent = new LinkedList<String>();

		double sum = 0;
		for (int step = 0; step < currentTimeStep; step++) {
			sum += errors[step];
		}

		sum /= currentTimeStep;

		fileContent.add(method + SEPARATOR + sum);

		try {
			Files.write(Paths.get(path + "SpringErrorWith" + method + ".csv"), fileContent, Charset.forName("UTF-8"));
		} catch (IOException e) {
			throw new IllegalAccessError();
		}
	}

}
