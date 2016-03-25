package fi.jyu.ties454.jiayang.assignment2.task1;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.http.client.utils.URIBuilder;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class openWeather {

	URL url;
	URIBuilder uri;
	InputStream inputstream;
	String weather;

	public String getWeather(String city) throws IOException {

		try {
			uri = new URIBuilder(
					"http://api.openweathermap.org/data/2.5/weather");
		} catch (URISyntaxException e2) {
			
			e2.printStackTrace();
		}

		uri.addParameter("q", city);
		uri.addParameter("mode", "xml");
		uri.addParameter("appid", "1b896bb34b22be72ca0fe7e467424ba4");

		try {
			url = uri.build().toURL();
		} catch (URISyntaxException e1) {
			
			e1.printStackTrace();
		}

		try {
			inputstream = url.openStream();
		} catch (IOException e) {
			throw new Error(e);
		}

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new Error(e);
		}
		Document document;
		try {
			document = db.parse(inputstream);
		} catch (SAXException e) {
			throw new Error(e);
		} catch (IOException e) {
			throw new Error(e);
		}
		XPathFactory getCC = XPathFactory.newInstance();
		XPath ccPath = getCC.newXPath();
		try {
			weather = ccPath.evaluate("/current/temperature/@value", document);
		} catch (XPathExpressionException e) {
			throw new Error(e);
		}
		System.out.println("weather of" + city + ":" + weather);

		
		return weather;

	}

}
