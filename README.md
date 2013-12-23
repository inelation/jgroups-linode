<h3>JGroups Discovery implementation for Linode (http://linode.com)</h3>

Use this implementation to overcome Linode multicasting limitations.  This simple project uses the Linode API implementation by theo: https://github.com/theo/linode-api

Only nodes with private IP addresses are returned by this discovery implementation.  At the moment Linode does not support tagging or grouping of nodes.

<h3>How to use</h3>

Replace TCPING section in your JGroups configuration file with:

<com.levisoft.jgroups.linode.LinodePing 
   timeout="TIMEOUT_IN_MILLISECONDS"
   port_number="PORT_NUMBER_FOR_MEMBERSHIP"
   linode_api_key="YOUR_LINODE_API_KEY"

/>




