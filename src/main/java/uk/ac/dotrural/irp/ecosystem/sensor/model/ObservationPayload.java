package uk.ac.dotrural.irp.ecosystem.sensor.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ObservationPayload {

	private String featureOfInterest, observationResult, observedBy,
			observedProperty, sensingMethodUsed;
	private Long observationResultTime, observationSamplingTime;

	@XmlElement(name = "observedBy")
	public String getObservedBy() {
		return observedBy;
	}

	public void setObservedBy(String observedBy) {
		this.observedBy = observedBy;
	}

	@XmlElement(name = "sensingMethodUsed")
	public String getSensingMethodUsed() {
		return sensingMethodUsed;
	}

	public void setSensingMethodUsed(String sensingMethodUsed) {
		this.sensingMethodUsed = sensingMethodUsed;
	}

	@XmlElement(name = "featureOfInterest")
	public String getFeatureOfInterest() {
		return featureOfInterest;
	}

	public void setFeatureOfInterest(String featureOfInterest) {
		this.featureOfInterest = featureOfInterest;
	}

	@XmlElement(name = "observationResult")
	public String getObservationResult() {
		return observationResult;
	}

	public void setObservationResult(String observationResult) {
		this.observationResult = observationResult;
	}

	@XmlElement(name = "observedProperty")
	public String getObservedProperty() {
		return observedProperty;
	}

	public void setObservedProperty(String observedProperty) {
		this.observedProperty = observedProperty;
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
}
