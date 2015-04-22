package mjk.jaxb;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class OffsetTimeAdapter extends XmlAdapter<String, OffsetDateTime> {

	/***
	 * xs:dateTime into OffsetDateTime
	 */
	@Override
	public OffsetDateTime unmarshal(String str) throws Exception {
		return OffsetDateTime.parse(str, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	}

	@Override
	public String marshal(OffsetDateTime odt) throws Exception {
		return odt.toString();
	}

}
