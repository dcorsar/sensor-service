/**
 * 
 */
package uk.ac.dotrural.irp.ecosystem.sensor.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author david
 * 
 */
@XmlRootElement
public class SensorOutput {

	private String uri;

	private ObservationValue hasValue;

	private List<PropertyValue> additionalProperties;
	private List<String> additionalTypes;

	@XmlElement(name = "hasValue")
	public ObservationValue getHasValue() {
		return hasValue;
	}

	public void setHasValue(ObservationValue hasValue) {
		this.hasValue = hasValue;
	}

	public SensorOutput() {
		super();
	}

	public SensorOutput(String uri) {
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

	@XmlElement(name = "additionalProperties")
	public List<PropertyValue> getAdditionalProperties() {
		return additionalProperties;
	}

	public void setAdditionalProperties(List<PropertyValue> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}

	@XmlElement(name = "additionalTypes")
	public List<String> getAdditionalTypes() {
		return additionalTypes;
	}

	public void setAdditionalTypes(List<String> additionalTypes) {
		this.additionalTypes = additionalTypes;
	}

        @Override public String toString() {
            return "uri: " + uri +
               "; hasValue: [" + hasValue + "]" +
               "; additionalProperties: " + additionalProperties +
               "; additionalTypes: " + additionalTypes;
        }

}
