package com.levisoft.jgroups.linode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.jgroups.PhysicalAddress;
import org.jgroups.annotations.Property;
import org.jgroups.conf.ClassConfigurator;
import org.jgroups.protocols.Discovery;
import org.jgroups.stack.IpAddress;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.notedpath.linode.API_ACTION;
import com.notedpath.linode.Linode;
import com.notedpath.linode.LinodeResponse;

public class LinodePing extends Discovery {

	private Linode linode;
	@Property(description = "Linode API KEY")
	private String linode_api_key;
	@Property(description = "The port number for each node for membership. Default port is 7800")
	private int port_number;
	
	
	 static {
		    ClassConfigurator.addProtocol((short) 600, LinodePing.class); // ID needs to
		                                                                // be unique
		  }
	 
	@Override
	public Collection<PhysicalAddress> fetchClusterMembers(String clusterName) {
		List<PhysicalAddress> clusterMembers = new ArrayList<PhysicalAddress>();
		
		try {
			LinodeResponse response = linode.execute(API_ACTION.LINODE_LIST);
			JSONArray jarray = response.getDataAsJSONArray();
			JSONObject jo;
			JSONArray ipArray;
			JSONObject ipObj;
			LinodeResponse ipResponse;
			for (int i=0; i < jarray.length(); i++) {
				jo = jarray.getJSONObject(i);
		
				ipResponse = linode.execute(API_ACTION.LINODE_IP_LIST, "LinodeID", jo.get("LINODEID").toString());
				ipArray = ipResponse.getDataAsJSONArray();
				for (int j=0; j < ipArray.length(); j++) {
					ipObj = ipArray.getJSONObject(j);
					if (ipObj.getInt("ISPUBLIC") == 0) {
						clusterMembers.add(new IpAddress(ipObj.getString("IPADDRESS"), port_number));
					}
					
				}
				
			}
			
		} catch (HttpException e) {
			log.warn("Problem communicating with Linode API while retrieving cluster members", e);
		} catch (JSONException e) {
			log.warn("Problem parsing JSON response from Linode API while retrieving cluster members", e);
		} catch (IOException e) {
			log.warn("Problem communicating with Linode API while retrieving cluster members", e);
		}
		return clusterMembers;
	}

	@Override
	public void init() throws Exception {
		super.init();
		linode = new Linode(linode_api_key);
		
	}

	@Override
	public boolean isDynamic() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean sendDiscoveryRequestsInParallel() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub
		super.start();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		super.stop();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "LinodePing";
	}
	
}
