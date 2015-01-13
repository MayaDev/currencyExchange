package com.mayacarmi.wordpress.restfulasynctaskexample;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivityAsynctask extends Activity {

	final TextView text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_asynctask);
		
		// creating simple user interface
		// = setContentView(R.id.textView1);//new TextView(this);
		setContentView(R.id.textView1);

		class DownloadTask extends AsyncTask<String, Integer, String> {

			@Override
			protected String doInBackground(String... url) {
				
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet("http://www.boi.org.il/currency.xml");
				HttpResponse response = null;
				InputStream is = null;
				try {
					response = client.execute(request);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					is = response.getEntity().getContent();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				final StringBuffer sb = new StringBuffer();

				// getting specific tags from xml
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = null;
				try {
					builder = factory.newDocumentBuilder();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Document doc = null;
				try {
					doc = builder.parse(is);
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				NodeList list = doc.getElementsByTagName("CURRENCYCODE");
				int length = list.getLength();
				for (int i = 0; i < length; i++) {
					sb.append(list.item(i).getFirstChild().getNodeValue());
					sb.append(" ");
				}
				return sb.toString();
			}

			protected void onPostExecute(String result) {
				text.setText(result);
//				setListAdapter(new ArrayAdapter<String>(this,
//						android.R.layout.simple_list_item_1 , ));
			}

		}
		DownloadTask downloadTask = new DownloadTask();
		downloadTask.execute("http://www.boi.org.il/currency.xml");
	}
}
