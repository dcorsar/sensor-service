package uk.ac.dotrural.irp.ecosystem.sensor.resource.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.springframework.context.annotation.Scope;

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
import uk.ac.dotrural.irp.ecosystem.sensor.model.ObservationPayload;
import uk.ac.dotrural.irp.ecosystem.sensor.model.ObservationValue;
import uk.ac.dotrural.irp.ecosystem.sensor.model.Property;
import uk.ac.dotrural.irp.ecosystem.sensor.model.PropertyValue;
import uk.ac.dotrural.irp.ecosystem.sensor.model.Sensing;
import uk.ac.dotrural.irp.ecosystem.sensor.model.Sensor;
import uk.ac.dotrural.irp.ecosystem.sensor.model.SensorOutput;
import uk.ac.dotrural.irp.ecosystem.sensor.queries.ObservationQueries;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

@Path("/observations")
@Scope("request")
public class ObservationResource implements RESTFulSPARQL {

	@Context
	private UriInfo uriInfo;

	private SPARQLEndpoint observationEndpoint;

	public void setObservationEndpoint(SPARQLEndpoint observationEndpoint) {
		this.observationEndpoint = observationEndpoint;
	}

	public SystemMessage init(ServiceInitialiser si) {
		return observationEndpoint.init(uriInfo, si);
	}

	public void update(Query query) {
		observationEndpoint.update(query);
	}

	public String query(Query query) {
		return Util.resultsetToString(observationEndpoint.query(query));
	}

	public EndpointInfo info() {
		return observationEndpoint.info();
	}

	@POST
	@Path("createPayload")
	public String create(ObservationPayload op) {
		Observation o = new Observation();
		if (op.getFeatureOfInterest() != null) {
			o.setFeatureOfInterest(new FeatureOfInterest(op
					.getFeatureOfInterest()));
		}
		if (op.getObservationResult() != null) {
			o.setObservationResult(new SensorOutput(op.getObservationResult()));
		}
		if (op.getObservationResultTime() != null) {
			o.setObservationResultTime(op.getObservationResultTime());
		}
		if (op.getObservationSamplingTime() != null) {
			o.setObservationSamplingTime(op.getObservationSamplingTime());
		}
		if (op.getObservedBy() != null) {
			o.setObservedBy(new Sensor(op.getObservedBy()));
		}
		if (op.getObservedProperty() != null) {
			o.setObservedProperty(new Property(op.getObservedProperty()));
		}
		if (op.getSensingMethodUsed() != null) {
			o.setSensingMethodUsed(new Sensing(op.getSensingMethodUsed()));
		}

		return create(o);
	}

	@POST
	@Path("create")
	public String create(Observation observation) {
		if (observation.getObservedBy() == null
				|| observation.getObservedBy().getUri() == null) {
			throw new ExceptionReporter(
					new NullPointerException(
							"observedBy with a URI is required to create an observation"));
		}
		if (observation.getFeatureOfInterest() == null
				|| observation.getFeatureOfInterest().getUri() == null) {
			throw new ExceptionReporter(
					new NullPointerException(
							"featureOfIntererest with a URI is required to create an observation"));
		}
		if (observation.getObservedProperty() == null
				|| observation.getObservedProperty().getUri() == null) {
			throw new ExceptionReporter(
					new IllegalArgumentException(
							"observedProperty with a URI is required to create an observation"));
		}

		StringBuilder updateBody = new StringBuilder();
		if (observation.getObservationResult() != null
				&& observation.getObservationResult().getUri() == null) {
			// the result exists, but it doesn't have a URI, so assume its not
			// in the triplestore
			SensorOutput so = observation.getObservationResult();

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

			so.setUri(uk.ac.dotrural.irp.ecosystem.sensor.Util
					.createIndividualUri(ObservationQueries.getBaseNS()));
			updateBody.append(ObservationQueries
					.getSensorOutputCreateUpdateBody(so));

		}
		observation.setUri(uk.ac.dotrural.irp.ecosystem.sensor.Util
				.createIndividualUri(ObservationQueries.getBaseNS()));

		updateBody.append(ObservationQueries
				.getObservationCreateUpdateBody(observation));

		String queryStr = ObservationQueries.getInsertUpdate(updateBody
				.toString());

		// perform update
		Query query = new Query(queryStr);
		observationEndpoint.update(query);

		return observation.getUri();
	}

	@GET
	@Path("get")
	public Observation get(@QueryParam("observationUri") String observationUri) {
		Observation obs = new Observation(observationUri);
		List<PropertyValue> additionalProperties = new ArrayList<PropertyValue>();
		List<String> additionalTypes = new ArrayList<String>();

		String queryStr = ObservationQueries.getPredObj(observationUri);
		Query query = new Query(queryStr);
		ResultSet rs = this.observationEndpoint.query(query);
		for (QuerySolution qs = rs.next(); rs.hasNext(); qs = rs.next()) {
			String p = Util.getNodeValue(qs.get("p"));
			if ("http://purl.oclc.org/NET/ssnx/ssn#featureOfInterest".equals(p)) {
				FeatureOfInterest foi = new FeatureOfInterest(
						Util.getNodeValue(qs.get("o")));
				obs.setFeatureOfInterest(foi);
			} else if ("http://purl.oclc.org/NET/ssnx/ssn#observationResult"
					.equals(p)) {
				SensorOutput so = new SensorOutput(Util.getNodeValue(qs
						.get("o")));
				obs.setObservationResult(so);
			} else if ("http://purl.oclc.org/NET/ssnx/ssn#observationResultTime"
					.equals(p)) {
				obs.setObservationResultTime(Util.getNodeLongValue(qs.get("o")));
			} else if ("http://purl.oclc.org/NET/ssnx/ssn#observationSamplingTime"
					.equals(p)) {
				obs.setObservationSamplingTime(Util.getNodeLongValue(qs
						.get("o")));
			} else if ("http://purl.oclc.org/NET/ssnx/ssn#observedBy".equals(p)) {
				Sensor s = new Sensor(Util.getNodeValue(qs.get("o")));
				obs.setObservedBy(s);
			} else if ("http://purl.oclc.org/NET/ssnx/ssn#observedProperty"
					.equals(p)) {
				Property prop = new Property(Util.getNodeValue(qs.get("o")));
				obs.setObservedProperty(prop);
			} else if ("http://purl.oclc.org/NET/ssnx/ssn#sensingMethodUsed"
					.equals(p)) {
				Sensing s = new Sensing(Util.getNodeValue(qs.get("o")));
				obs.setSensingMethodUsed(s);
			} else if ("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
					.equals(p)) {
				additionalTypes.add(Util.getNodeValue(qs.get("o")));
			} else {
				additionalProperties
						.add(uk.ac.dotrural.irp.ecosystem.sensor.Util
								.getPropertyValue(p, qs.get("o")));
			}
		}

		obs.setAdditionalTypes(additionalTypes);
		obs.setAdditionalProperties(additionalProperties);
		return obs;
	}

	@DELETE
	@Path("delele")
	public void delete(@QueryParam("observationUri") String observationUri) {
		String queryStr = ObservationQueries.getDelete(observationUri);
		Query query = new Query(queryStr);
		observationEndpoint.update(query);
	}

	@PUT
	@Path("update")
	public void update(@QueryParam("observationUri") String observationUri) {
		// TODO incomplete method
	}

}
