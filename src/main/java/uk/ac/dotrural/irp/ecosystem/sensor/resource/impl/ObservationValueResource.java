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

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import uk.ac.dotrural.irp.ecosystem.core.models.jaxb.system.EndpointInfo;
import uk.ac.dotrural.irp.ecosystem.core.models.jaxb.system.Query;
import uk.ac.dotrural.irp.ecosystem.core.models.jaxb.system.ServiceInitialiser;
import uk.ac.dotrural.irp.ecosystem.core.models.jaxb.system.SystemMessage;
import uk.ac.dotrural.irp.ecosystem.core.services.SPARQLEndpoint;
import uk.ac.dotrural.irp.ecosystem.core.util.Util;
import uk.ac.dotrural.irp.ecosystem.sensor.model.Observation;
import uk.ac.dotrural.irp.ecosystem.sensor.model.ObservationValue;
import uk.ac.dotrural.irp.ecosystem.sensor.model.PropertyValue;
import uk.ac.dotrural.irp.ecosystem.sensor.model.SensorOutput;
import uk.ac.dotrural.irp.ecosystem.sensor.queries.ObservationQueries;

public class ObservationValueResource {

	@Context
	private UriInfo uriInfo;

	private SPARQLEndpoint observationValueEndpoint;

	public void setObservationValueEndpoint(
			SPARQLEndpoint observationValueEndpoint) {
		this.observationValueEndpoint = observationValueEndpoint;
	}

	public SystemMessage init(ServiceInitialiser si) {
		return observationValueEndpoint.init(uriInfo, si);
	}

	public void update(Query query) {
		observationValueEndpoint.update(query);
	}

	public String query(Query query) {
		return Util.resultsetToString(observationValueEndpoint.query(query));
	}

	public EndpointInfo info() {
		return observationValueEndpoint.info();
	}

	@POST
	@Path("create")
	public String create(ObservationValue value) {
		StringBuilder updateBody = new StringBuilder();

		value.setUri(uk.ac.dotrural.irp.ecosystem.sensor.Util
				.createIndividualUri(ObservationQueries.getBaseNS()));

		updateBody.append(ObservationQueries
				.getObservationValueCreateUpdateBody(value));

		String queryStr = ObservationQueries.getInsertUpdate(updateBody
				.toString());

		// perform update
		Query query = new Query(queryStr);
		observationValueEndpoint.update(query);

		return value.getUri();
	}

	@GET
	@Path("get")
	public ObservationValue get(
			@QueryParam("observationValueUri") String observationValueUri) {
		ObservationValue so = new ObservationValue(observationValueUri);
		List<PropertyValue> additionalProperties = new ArrayList<PropertyValue>();
		List<String> additionalTypes = new ArrayList<String>();
		
		String queryStr = ObservationQueries.getPredObj(observationValueUri);
		Query query = new Query(queryStr);

		ResultSet rs = this.observationValueEndpoint.query(query);
		for (QuerySolution qs = rs.next(); rs.hasNext(); qs = rs.next()) {
			String p = Util.getNodeValue(qs.get("p"));
			if ("http://www.w3.org/1999/02/22-rdf-syntax-ns#type".equals(p)) {
				additionalTypes.add(Util.getNodeValue(qs.get("o")));
			} else {
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
	public void delete(
			@QueryParam("observationValueUri") String observationValueUri) {
		String queryStr = ObservationQueries.getDelete(observationValueUri);
		Query query = new Query(queryStr);
		observationValueEndpoint.update(query);
	}

	@PUT
	@Path("update")
	public void update(@QueryParam("observationUri") String observationUri) {
		// TODO incomplete method
	}

}
