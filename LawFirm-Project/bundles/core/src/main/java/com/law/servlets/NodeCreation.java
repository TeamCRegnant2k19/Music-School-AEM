package com.law.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

@Component(service = Servlet.class, property = { "sling.servlet.paths=/bin/node",
"sling.servlet.methods={GET,POST}" }, name = "Node Creation Demo Servlet")
public class NodeCreation extends SlingSafeMethodsServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		/*Node Creation can be done by using Session or ResourceResolver*/
		String path = "/content/dam/we-retail/en/activities/hiking/hiking_2.jpg";
		Resource resource = request.getResourceResolver().getResource(path);
		Node node = resource.adaptTo(Node.class);
		
		PrintWriter writer = response.getWriter();
		
		writer.println("Servlet Called - "+getServletInfo());
		try {
			
			// Adding Child Node
			Node child = node.addNode("sri", "nt:unstructured");
			child.setProperty("prop1", "value1");
			child.setProperty("prop2", "value2");
			child.getSession().save();
			
			writer.println("\n Child Node: \n"+child.toString());			
			
			// Adding Properties to Already Exixting Node
			Resource related = resource.getChild(path.concat("/jcr:content/related"));
			ModifiableValueMap map = related.adaptTo(ModifiableValueMap.class);
			map.put("prop", "my value");
			resource.getResourceResolver().commit();
			
			writer.println("\n Related Node: \n"+related.adaptTo(Node.class).toString());			
			
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
