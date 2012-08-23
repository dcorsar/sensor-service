package uk.ac.dotrural.irp.ecosystem.sensor.queries;

import java.util.List;

import uk.ac.dotrural.irp.ecosystem.sensor.model.Observation;
import uk.ac.dotrural.irp.ecosystem.sensor.model.ObservationValue;
import uk.ac.dotrural.irp.ecosystem.sensor.model.PropertyType;
import uk.ac.dotrural.irp.ecosystem.sensor.model.PropertyValue;
import uk.ac.dotrural.irp.ecosystem.sensor.model.SensorOutput;

public class ObservationQueries {

	public static String getBaseNS() {
		return QueryReader.getString("ObservationQueries.baseNS");
	}

	public static String getObservationCreateUpdate(Observation obs) {
		StringBuilder update = getObservationCreateUpdateBody(obs);

		return String.format(QueryReader
				.getString("ObservationQueries.update.insert.template"), update
				.toString());
	}

	public static StringBuilder getObservationCreateUpdateBody(Observation obs) {
		StringBuilder update = new StringBuilder();
		String uri = obs.getUri();
		update.append(String.format(
				QueryReader.getString("ObservationQueries.update.observation"),
				uri));
		
		if (obs.getAdditionalTypes()!=null){
			appendAdditionalTypes(obs.getAdditionalTypes(), update, uri);
		}
		
		if (obs.getObservationResultTime() != null) {
			update.append(String.format(
					QueryReader
							.getString("ObservationQueries.update.observation.resultTime"),
					uri, Long.toString(obs.getObservationResultTime())));
		}
		if (obs.getObservationSamplingTime() != null) {
			update.append(String.format(
					QueryReader
							.getString("ObservationQueries.update.observation.samplingTime"),
					uri, Long.toString(obs.getObservationSamplingTime())));
		}
		if (obs.getObservationResult() != null
				&& obs.getObservationResult().getUri() != null) {
			update.append(String.format(QueryReader
					.getString("ObservationQueries.update.observation.result"),
					uri, obs.getObservationResult().getUri()));
		}
		if (obs.getFeatureOfInterest() != null
				&& obs.getFeatureOfInterest().getUri() != null) {
			update.append(String.format(QueryReader
					.getString("ObservationQueries.update.observation.foi"),
					uri, obs.getFeatureOfInterest().getUri()));
		}
		if (obs.getObservedBy() != null && obs.getObservedBy().getUri() != null) {
			update.append(String.format(
					QueryReader
							.getString("ObservationQueries.update.observation.observedBy"),
					uri, obs.getObservedBy().getUri()));
		}
		if (obs.getObservedProperty() != null
				&& obs.getObservedProperty().getUri() != null) {
			update.append(String.format(
					QueryReader
							.getString("ObservationQueries.update.observation.observedProperty"),
					uri, obs.getObservedProperty().getUri()));
		}
		if (obs.getSensingMethodUsed() != null
				&& obs.getSensingMethodUsed().getUri() != null) {
			update.append(String.format(
					QueryReader
							.getString("ObservationQueries.update.observation.sensingMethodUsed"),
					uri, obs.getSensingMethodUsed().getUri()));
		}
		
		appendPropertyValues(obs.getAdditionalProperties(), update, uri);
		return update;
	}
	
	private static void appendAdditionalTypes(List<String> types, StringBuilder update, String uri){
		for (String type: types) {
			update.append(String.format(
					QueryReader.getString("ObservationQueries.triple.type"), uri,
					type));
		}
	}

	private static void appendPropertyValues(List<PropertyValue> list,
			StringBuilder update, String uri) {
		for (PropertyValue pv : list) {
			update.append(String.format(
					QueryReader.getString("ObservationQueries.triple"), uri,
					pv.getProperty(), getObject(pv)));
		}
	}

	public static String getSensorOutputCreateUpdate(SensorOutput so) {
		StringBuilder update = getSensorOutputCreateUpdateBody(so);

		return String.format(QueryReader
				.getString("ObservationQueries.update.insert.template"), update
				.toString());
	}

	public static StringBuilder getSensorOutputCreateUpdateBody(SensorOutput so) {
		StringBuilder update = new StringBuilder();
		String uri = so.getUri();
		update.append(String.format(
				QueryReader.getString("ObservationQueries.update.sensorOutput"),
				uri));
		if (so.getAdditionalTypes()!=null){
			appendAdditionalTypes(so.getAdditionalTypes(), update, uri);
		}
		
		if (so.getHasValue() != null && so.getHasValue().getUri() != null) {
			update.append(String.format(
					QueryReader
							.getString("ObservationQueries.update.sensorOutput.hasValue"),
					uri, so.getHasValue().getUri()));
		}
		appendPropertyValues(so.getAdditionalProperties(), update, uri);
		return update;
	}

	public static String getObservationValueCreateUpdate(ObservationValue ov) {
		StringBuilder update = getObservationValueCreateUpdateBody(ov);

		return String.format(QueryReader
				.getString("ObservationQueries.update.insert.template"), update
				.toString());
	}

	public static StringBuilder getObservationValueCreateUpdateBody(
			ObservationValue ov) {
		StringBuilder update = new StringBuilder();
		String uri = ov.getUri();
		
		if (ov.getAdditionalTypes()!=null){
			appendAdditionalTypes(ov.getAdditionalTypes(), update, uri);
		}
		update.append(String.format(QueryReader
				.getString("ObservationQueries.update.observationValue"), uri));

		appendPropertyValues(ov.getAdditionalProperties(), update, uri);
		return update;
	}

	private static String getObject(PropertyValue pv) {
		if (pv.getType() == PropertyType.URI) {
			return String.format(
					QueryReader.getString("ObservationQueries.update.uri"),
					pv.getValue().toString());
		}
		if (pv.getType() == PropertyType.DOUBLE) {
			return String.format(
					QueryReader.getString("ObservationQueries.update.double"),
					pv.getValue().toString());
		}
		if (pv.getType() == PropertyType.LONG) {
			return String.format(
					QueryReader.getString("ObservationQueries.update.long"),
					pv.getValue());
		}
		if (pv.getType() == PropertyType.STRING) {
			return String.format(
					QueryReader.getString("ObservationQueries.update.string"),
					pv.getValue());
		}
		if (pv.getType() == PropertyType.INTEGER) {
			return String.format(
					QueryReader.getString("ObservationQueries.update.integer"),
					pv.getValue());
		}
		return pv.getValue().toString();
	}
	
	public static String getInsertUpdate(String body){
		return String.format(QueryReader.getString("ObservationQueries.update.insert.template"), body);
	}

	public static String getPredObj(String subjectUri) {
		return String.format(QueryReader.getString("ObservationQueries.get"), subjectUri);
	}
	
	public static String getDelete(String observationUri) {
		return String.format(QueryReader.getString("ObservationQueries.delete"), observationUri, observationUri);
	}

}
