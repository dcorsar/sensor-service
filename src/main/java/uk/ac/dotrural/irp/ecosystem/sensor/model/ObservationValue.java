package uk.ac.dotrural.irp.ecosystem.sensor.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ObservationValue {

	private String uri;

	private List<PropertyValue> additionalProperties;

	private List<String> additionalTypes;
	
	public ObservationValue() {
		super();
	}
	public ObservationValue(String uri) {
		this();
		this.uri = uri;
	}

	@XmlElement(name = "uri")
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@XmlElement(name = "additionalProperties")
	public List<PropertyValue> getAdditionalProperties() {
		return additionalProperties;
	}

	public void setAdditionalProperties(List<PropertyValue> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}
	@XmlElement(name="additionalTypes")
	public List<String> getAdditionalTypes() {
		return additionalTypes;
	}

	public void setAdditionalTypes(List<String> additionalTypes) {
		this.additionalTypes = additionalTypes;
	}

}
