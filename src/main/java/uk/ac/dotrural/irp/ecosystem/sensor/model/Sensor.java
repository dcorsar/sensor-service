package uk.ac.dotrural.irp.ecosystem.sensor.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Sensor {

	private String uri;
	
	private Property observes;
	
	private List<Sensing> implementsSensing;

	public Sensor(){
		super();
	}
	
	public Sensor(String uri) {
		super();
		setUri(uri);
	}

	@XmlElement(name = "uri")
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}


	@XmlElement(name = "observes")
	public Property getObserves() {
		return observes;
	}

	public void setObserves(Property observes) {
		this.observes = observes;
	}


	@XmlElement(name = "implements")
	public List<Sensing> getImplements() {
		return implementsSensing;
	}

	public void setImplements(List<Sensing> implementsSensing) {
		this.implementsSensing = implementsSensing;
	}

	
	
}
