package com.smband.soap.configure;

import java.util.List;
import java.util.Properties;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

	@Bean
	public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean<>(servlet, "/ws/*");
	}
	
	
	@Override
	public void addInterceptors(List<EndpointInterceptor> interceptors) {
		// TODO Auto-generated method stub
		super.addInterceptors(interceptors);
	}


	@Bean(name = "countries")
	public Wsdl11Definition defaultWsdl11Definition() {
		SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
	    wsdl11Definition.setWsdl(new ClassPathResource("countries.wsdl"));
	    return wsdl11Definition;
		/*
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("CountriesPort");
		wsdl11Definition.setLocationUri("/ws");
		wsdl11Definition.setTargetNamespace("http://www.smband.com/countries");
		wsdl11Definition.setSchema(countriesSchema);
//		wsdl11Definition.setRequestSuffix("");
		Properties properties = new Properties();
		properties.put("getCountry", "http://www.smband.com/countries/country");
//		wsdl11Definition.setSoapActions(properties);
		return wsdl11Definition;
		*/
	    
	}
	
//	@Bean
//	public XsdSchema countriesSchema() {
//		return new SimpleXsdSchema(new ClassPathResource("countries.xsd"));
//	}
}
