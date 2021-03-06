/*******************************************************************************
 * Copyright (c) 2012, Institute for Pervasive Computing, ETH Zurich.
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the Institute nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE INSTITUTE AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE INSTITUTE OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * 
 * This file is part of the Californium (Cf) CoAP framework.
 ******************************************************************************/
package ch.ethz.inf.vs.californium.examples.plugtest;

import ch.ethz.inf.vs.californium.coap.GETRequest;
import ch.ethz.inf.vs.californium.coap.Response;
import ch.ethz.inf.vs.californium.coap.registries.CodeRegistry;
import ch.ethz.inf.vs.californium.coap.registries.MediaTypeRegistry;
import ch.ethz.inf.vs.californium.endpoint.resources.LocalResource;

/**
 * This resource implements a test of specification for the ETSI IoT CoAP Plugtests, Paris, France, 24 - 25 March 2012.
 * 
 * @author Matthias Kovatsch
 */
public class MultiFormat extends LocalResource {

	public MultiFormat() {
		super("multi-format");
		setTitle("Resource that exists in different content formats (text/plain utf8 and application/xml)");
		setContentTypeCode(0);
		setContentTypeCode(41);
	}

	@Override
	public void performGET(GETRequest request) {
		Response response = new Response(CodeRegistry.RESP_CONTENT); // 2.05 content

		String format = "";
		switch (request.getFirstAccept()) {
		case MediaTypeRegistry.UNDEFINED:
		case MediaTypeRegistry.TEXT_PLAIN:
			response.setContentType(MediaTypeRegistry.TEXT_PLAIN);
			format = "Status type: \"%s\"\nCode: \"%s\"\nMID: \"%s\"\nAccept: \"%s\"";

			break;

		case MediaTypeRegistry.APPLICATION_XML:
			response.setContentType(MediaTypeRegistry.APPLICATION_XML);
			format = "<status type=\"%s\" code=\"%s\" mid=\"%s\" accept=\"%s\"/>";

			break;

		default:
			response.setCode(CodeRegistry.RESP_NOT_ACCEPTABLE);
			format = "text/plain or application/xml only";
			break;
		}
		
		response.setPayload( String.format(format, request.typeString(), request.getCode(), request.getMID(), MediaTypeRegistry.toString(request.getFirstAccept())) );

		request.respond(response);
	}

}
