package uk.ac.dotrural.irp.ecosystem.sensor;

import java.util.UUID;

import uk.ac.dotrural.irp.ecosystem.sensor.model.PropertyType;
import uk.ac.dotrural.irp.ecosystem.sensor.model.PropertyValue;
import uk.ac.dotrural.irp.ecosystem.sensor.queries.ObservationQueries;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class Util {

	public static PropertyValue getPropertyValue(String property, RDFNode n) {
		PropertyValue p = new PropertyValue();
		p.setProperty(property);
		if (n.isResource() || n.isURIResource()) {
			p.setType(PropertyType.URI);
			p.setValue(n.asResource().getURI());
		} else if (n.isLiteral()) {
			Literal l = n.asLiteral();
			RDFDatatype type = l.getDatatype();
			if (XSDDatatype.XSDinteger == type) {
				p.setValue(Integer.toString(l.getInt()));
				p.setType(PropertyType.INTEGER);
			} else if (XSDDatatype.XSDdouble == type) {
				p.setValue(Double.toString(l.getDouble()));
				p.setType(PropertyType.DOUBLE);
			} else if (XSDDatatype.XSDlong == type) {
				p.setValue(Long.toString(l.getLong()));
				p.setType(PropertyType.LONG);
			} else if (XSDDatatype.XSDstring == type) {
				p.setValue(l.getString());
				p.setType(PropertyType.STRING);
			}
		}
		return p;
	}
	

	public static String createIndividualUri(String uri) {
		if (uri.charAt(uri.length() - 1) != '/') {
			uri += '/';
		}
		uri += UUID.randomUUID().toString();
		return uri;
	}
}
