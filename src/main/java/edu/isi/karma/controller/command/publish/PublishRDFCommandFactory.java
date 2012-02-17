package edu.isi.karma.controller.command.publish;

import javax.servlet.http.HttpServletRequest;

import edu.isi.karma.controller.command.Command;
import edu.isi.karma.controller.command.CommandFactory;
import edu.isi.karma.view.VWorkspace;
import edu.isi.karma.webserver.ServletContextParameterMap;
import edu.isi.karma.webserver.ServletContextParameterMap.ContextParameter;

public class PublishRDFCommandFactory extends CommandFactory {
	private enum Arguments {
		vWorksheetId, addInverseProperties, rdfPrefix, saveToStore,hostName,dbName,userName,password,modelName
	}

	@Override
	public Command createCommand(HttpServletRequest request,
			VWorkspace vWorkspace) {
		String vWorksheetId = request.getParameter(Arguments.vWorksheetId
				.name());
		String addInverseProperties = request.getParameter(Arguments.addInverseProperties
				.name());
		String rdfPrefix = request.getParameter(Arguments.rdfPrefix
				.name());
		return new PublishRDFCommand(getNewId(vWorkspace), vWorksheetId,
				ServletContextParameterMap
				.getParameterValue(ContextParameter.PUBLIC_RDF_ADDRESS),
				rdfPrefix, addInverseProperties,
				request.getParameter(Arguments.saveToStore.name()),
				request.getParameter(Arguments.hostName.name()),
				request.getParameter(Arguments.dbName.name()),
				request.getParameter(Arguments.userName.name()),
				request.getParameter(Arguments.password.name()),
				request.getParameter(Arguments.modelName.name()));
	}

}