package uk.ac.dotrural.irp.ecosystem.sensor.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Observation {

	private String uri;
	private Long observationResultTime, observationSamplingTime;
	private SensorOutput observationResult;
	private FeatureOfInterest featureOfInterest;
	private Sensor observedBy;
	private Sensing sensingMethodUsed;
	private Property observedProperty;

	private List<String> additionalTypes;
	private List<PropertyValue> additionalProperties;

	public Observation() {
		super();
	}

	public Observation(String observationUri) {
		super();
		setUri(observationUri);
	}

	@XmlElement(name = "uri")
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@XmlElement(name = "observationResult")
	public SensorOutput getObservationResult() {
		return observationResult;
	}

	public void setObservationResult(SensorOutput observationResult) {
		this.observationResult = observationResult;
	}

	@XmlElement(name = "featureOfInterest")
	public FeatureOfInterest getFeatureOfInterest() {
		return featureOfInterest;
	}

	public void setFeatureOfInterest(FeatureOfInterest featureOfInterest) {
		this.featureOfInterest = featureOfInterest;
	}

	@XmlElement(name = "observedBy")
	public Sensor getObservedBy() {
		return observedBy;
	}

	public void setObservedBy(Sensor observedBy) {
		this.observedBy = observedBy;
	}

	@XmlElement(name = "sensingMethodUsed")
	public Sensing getSensingMethodUsed() {
		return sensingMethodUsed;
	}

	public void setSensingMethodUsed(Sensing sensingMethodUsed) {
		this.sensingMethodUsed = sensingMethodUsed;
	}

	@XmlElement(name = "observationResultTime")
	public Long getObservationResultTime() {
		return observationResultTime;
	}

	public void setObservationResultTime(Long observationResultTime) {
		this.observationResultTime = observationResultTime;
	}

	@XmlElement(name = "observationSamplingTime")
	public Long getObservationSamplingTime() {
		return observationSamplingTime;
	}

	public void setObservationSamplingTime(Long observationSamplingTime) {
		this.observationSamplingTime = observationSamplingTime;
	}

	@XmlElement(name = "additionalProperties")
	public List<PropertyValue> getAdditionalProperties() {
		return additionalProperties;
	}

	public void setAdditionalProperties(List<PropertyValue> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}

	@XmlElement(name="observedProperty")
	public Property getObservedProperty() {
		return observedProperty;
	}

	public void setObservedProperty(Property observedProperty) {
		this.observedProperty = observedProperty;
	}

	@XmlElement(name="additionalTypes")
	public List<String> getAdditionalTypes() {
		return additionalTypes;
	}

	public void setAdditionalTypes(List<String> additionalTypes) {
		this.additionalTypes = additionalTypes;
	}
	
	
}
