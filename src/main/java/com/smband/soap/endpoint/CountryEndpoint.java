package com.smband.soap.endpoint;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.soap.server.endpoint.annotation.SoapAction;
import org.springframework.ws.soap.server.endpoint.annotation.SoapHeader;

import com.smband.soap.model.CommonHeader;
import com.smband.soap.model.GetCountryRequest;
import com.smband.soap.model.GetCountryResponse;
import com.smband.soap.model.ObjectFactory;
import com.smband.soap.repo.CountryRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Endpoint
public class CountryEndpoint {
	private static final String NAMESPACE_URI = "http://www.smband.com/countries";

	private CountryRepository countryRepository;
	@Autowired
	public CountryEndpoint(CountryRepository countryRepository) {
		this.countryRepository = countryRepository;
	}

	//@PayloadRoot(namespace = NAMESPACE_URI, localPart = "countryRequest")
	//@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountryRequest")
	//@SoapAction(NAMESPACE_URI+"/country")
	//@ResponsePayload
//	public GetCountryResponse findCountry(@RequestPayload GetCountryRequest request,
//			@SoapHeader("{"+NAMESPACE_URI+"}commonHeader") CommonHeader commonHeader) {
//
//		log.info("request name: {}, appName: {}", request.getName(), commonHeader.getAppName());
//
//		GetCountryResponse response = new GetCountryResponse();
//		response.setCountry(countryRepository.findCountry(request.getName()));
//
//		return response;
//	}


	//@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountryRequest")
	@SoapAction("http://www.smband.com/countries/countryRequest")
	@ResponsePayload
	public GetCountryResponse findCountry2(
			@RequestPayload GetCountryRequest request, 
			@SoapHeader("{"+NAMESPACE_URI+"}commonHeader") SoapHeaderElement soapHeaderElement,
			MessageContext msgContext
			) throws JAXBException{
		//Create Response Body and Header
        SaajSoapMessage soapResponse = (SaajSoapMessage) msgContext.getResponse();
        org.springframework.ws.soap.SoapHeader soapResponseHeader = soapResponse.getSoapHeader();
        
		
        SoapMessage soapMsg =  (SoapMessage)msgContext.getRequest();
        String soapAction = soapMsg.getSoapAction();
        log.info("request soapAction: {}", soapAction);
		CommonHeader requestHeader = null;
		try {
			// create an unmarshaller
			JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();

			// unmarshal the header from the specified source
			JAXBElement<CommonHeader> header = (JAXBElement<CommonHeader>)
			unmarshaller.unmarshal(soapHeaderElement.getSource());

			// get the header values
			requestHeader = header.getValue();
		}catch(Exception e) {
			log.error("", e);
		}

		//GetCountryRequest countryRequest = request.getValue();
		log.info("request header appName: {}, name: {}", requestHeader.getAppName(), request.getName());
		// response 구성.
		ObjectFactory factory = new ObjectFactory();
		GetCountryResponse countryResponse = factory.createGetCountryResponse();
		countryResponse.setCountry(countryRepository.findCountry(request.getName()));
		
		//Send Response back (Classes marshalled)
		JAXBContext jaxbContext = JAXBContext.newInstance(CommonHeader.class);
		CommonHeader resHeader = factory.createCommonHeader();
		resHeader.setAppName("res app name");
		resHeader.setSvcName("res svc name");
		resHeader.setFnCd("res fn cd");
		
		JAXBElement<CommonHeader> jaxHeader = factory.createCommonHeader(resHeader);
		jaxbContext.createMarshaller().marshal(jaxHeader, soapResponseHeader.getResult());
		
		return countryResponse;
	}

}
