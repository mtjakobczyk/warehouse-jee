package pl.mtjakobczyk.contracts.dictionaryfrontend;

import java.util.concurrent.Future;
import java.util.logging.Logger;

import javax.jws.WebService;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.BindingType;
import javax.xml.ws.Holder;
import javax.xml.ws.Response;

import pl.mtjakobczyk.schemas.dictionaryfrontend.GetStationResponse;

@WebService(serviceName = "DictionaryService", targetNamespace = "http://www.mtjakobczyk.pl/contracts/DictionaryFrontend/")
@BindingType(value=javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class DictionaryServiceImplementation implements IDictionary {
	//private final Logger log = Logger.getLogger("DictionaryServiceImplementation");
	
	@Override
	public void getStation(Holder<String> stationCode, Holder<String> stationName) throws GetStationFault {
		
		if(stationCode==null || stationCode.value == null || stationCode.value.trim().isEmpty()) {
			pl.mtjakobczyk.schemas.dictionaryfrontend.GetStationFault f = new pl.mtjakobczyk.schemas.dictionaryfrontend.GetStationFault();
			f.setFaultcode("");
			f.setFaultinfo("Empty StationCode");
			throw new GetStationFault("Empty StationCode", f);
		}
		
		switch(stationCode.value) {
		case "WAW":
			stationName.value = "Warszawa (Poland)";	
			break;
		case "FRA":
			stationName.value = "Frankfurt (Germany)";	
			break;
		case "KRK":
			stationName.value = "Krakow (Poland)";	
			break;
		case "GDA":
			stationName.value = "Gdansk (Poland)";	
			break;
		default:
			stationName.value = stationCode.value+" - Unknown";	
		}

	}

	@Override
	public Response<GetStationResponse> getStationAsync(String stationCode) {
		throw new IllegalStateException(); // generated in interface only for async client purpose
	}

	@Override
	public Future<?> getStationAsync(String stationCode, AsyncHandler<GetStationResponse> asyncHandler) {
		throw new IllegalStateException(); // generated in interface only for async client purpose
	}

}
