package itba.edu.ar.ss.output;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import itba.edu.ar.ss.algorithm.AlgorithmObserver;
import itba.edu.ar.ss.model.entity.Entity;

public class SpringPositionVsTime implements AlgorithmObserver<Double> {

	private static final String SEPARATOR = ",";
	private List<String> fileContent = new LinkedList<String>();
	private String path;

	public SpringPositionVsTime(String path) {
		super();
		this.path = path;
		fileContent.add("Time,Position");
	}

	public void deltaTimeStepDone(Entity entity, double time) {
		StringBuilder sb = new StringBuilder();
		sb.append(time).append(SEPARATOR).append(entity.getPosition());
		fileContent.add(sb.toString());
	}

	public void simulationEnded() {
		try {
			Files.write(Paths.get(path + "SpringPositionVsTime.csv"), fileContent, Charset.forName("UTF-8"));
		} catch (IOException e) {
			throw new IllegalAccessError();
		}
	}

}
