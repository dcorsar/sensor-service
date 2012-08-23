package uk.ac.dotrural.irp.ecosystem.sensor.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "type")
@XmlEnum
public enum PropertyType {

	URI("uri"), STRING("string"), LONG("long"), DOUBLE("double"), INTEGER(
			"integer");

	private final String value;

	private PropertyType(String v) {
		value = v;
	}

	public String value() {
		return value;
	}

	public static PropertyType fromValue(String v) {
		for (PropertyType c : PropertyType.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}

}
