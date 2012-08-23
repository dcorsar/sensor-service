package uk.ac.dotrural.irp.ecosystem.sensor.resource.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.springframework.context.annotation.Scope;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import uk.ac.dotrural.irp.ecosystem.core.models.jaxb.system.EndpointInfo;
import uk.ac.dotrural.irp.ecosystem.core.models.jaxb.system.Query;
import uk.ac.dotrural.irp.ecosystem.core.models.jaxb.system.ServiceInitialiser;
import uk.ac.dotrural.irp.ecosystem.core.models.jaxb.system.SystemMessage;
import uk.ac.dotrural.irp.ecosystem.core.resources.RESTFulSPARQL;
import uk.ac.dotrural.irp.ecosystem.core.resources.support.reporters.ExceptionReporter;
import uk.ac.dotrural.irp.ecosystem.core.services.SPARQLEndpoint;
import uk.ac.dotrural.irp.ecosystem.core.util.Util;
import uk.ac.dotrural.irp.ecosystem.sensor.model.FeatureOfInterest;
import uk.ac.dotrural.irp.ecosystem.sensor.model.Observation;
import uk.ac.dotrural.irp.ecosystem.sensor.model.ObservationValue;
import uk.ac.dotrural.irp.ecosystem.sensor.model.PropertyValue;
import uk.ac.dotrural.irp.ecosystem.sensor.model.SensorOutput;
import uk.ac.dotrural.irp.ecosystem.sensor.queries.ObservationQueries;

@Path("/sensoroutputs")
@Scope("request")
public class SensorOutputResource implements RESTFulSPARQL {

	@Context
	private UriInfo uriInfo;

	private SPARQLEndpoint sensorOutputEndpoint;

	public void setSensorOutputEndpointEndpoint(
			SPARQLEndpoint sensorOutputEndpoint) {
		this.sensorOutputEndpoint = sensorOutputEndpoint;
	}

	public SystemMessage init(ServiceInitialiser si) {
		return sensorOutputEndpoint.init(uriInfo, si);
	}

	public void update(Query query) {
		sensorOutputEndpoint.update(query);
	}

	public String query(Query query) {
		return Util.resultsetToString(sensorOutputEndpoint.query(query));
	}

	public EndpointInfo info() {
		return sensorOutputEndpoint.info();
	}

	@POST
	@Path("create")
	public String create(SensorOutput so) {

		StringBuilder updateBody = new StringBuilder();
		so.setUri(uk.ac.dotrural.irp.ecosystem.sensor.Util
				.createIndividualUri(ObservationQueries.getBaseNS()));

		// check the sensor output/value
		if (so.getHasValue() != null && so.getHasValue().getUri() == null) {
			// the observation value exists, but doesn't have a URI, so
			// assume its not in the triplestore
			ObservationValue ov = so.getHasValue();
			ov.setUri(uk.ac.dotrural.irp.ecosystem.sensor.Util
					.createIndividualUri(ObservationQueries.getBaseNS()));
			updateBody.append(ObservationQueries
					.getObservationValueCreateUpdateBody(ov));
		}

		updateBody.append(ObservationQueries
				.getSensorOutputCreateUpdateBody(so));

		String queryStr = ObservationQueries.getInsertUpdate(updateBody
				.toString());

		// perform update
		Query query = new Query(queryStr);
		sensorOutputEndpoint.update(query);

		return so.getUri();
	}

	@GET
	@Path("get")
	public SensorOutput get(
			@QueryParam("sensorOutputUri") String sensorOutputUri) {
		SensorOutput so = new SensorOutput(sensorOutputUri);
		List<PropertyValue> additionalProperties = new ArrayList<PropertyValue>();
		List<String> additionalTypes = new ArrayList<String>();
		String queryStr = ObservationQueries.getPredObj(sensorOutputUri);
		Query query = new Query(queryStr);
		ResultSet rs = this.sensorOutputEndpoint.query(query);
		for (QuerySolution qs = rs.next(); rs.hasNext(); qs = rs.next()) {
			String p = Util.getNodeValue(qs.get("p"));
			if ("http://purl.oclc.org/NET/ssnx/ssn#hasValue".equals(p)) {
				ObservationValue ov = new ObservationValue(Util.getNodeValue(qs
						.get("o")));
				so.setHasValue(ov);
			} else if ("http://www.w3.org/1999/02/22-rdf-syntax-ns#type".equals(p)){
				additionalTypes.add(Util.getNodeValue(qs.get("o")));
			}	else {
				additionalProperties.add(uk.ac.dotrural.irp.ecosystem.sensor.Util
						.getPropertyValue(p, qs.get("o")));
			}
		}

		so.setAdditionalTypes(additionalTypes);
		so.setAdditionalProperties(additionalProperties);
		return so;
	}

	@DELETE
	@Path("delele")
	public void delete(@QueryParam("sensorOutputUri") String sensorOutputUri) {
		String queryStr = ObservationQueries.getDelete(sensorOutputUri);
		Query query = new Query(queryStr);
		sensorOutputEndpoint.update(query);
	}

	@PUT
	@Path("update")
	public void update(@QueryParam("observationUri") String observationUri) {
		// TODO incomplete method
	}
}
