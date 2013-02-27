package uk.ac.dotrural.irp.ecosystem.sensor.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Property {

	private String uri;

	public Property(){
		super();
	}
	
	public Property(String uri){
		this();
		this.uri = uri;
	}
	
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

        @Override public String toString() {
            return "uri: " + uri;
        }

}
