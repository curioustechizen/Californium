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
package ch.ethz.inf.vs.californium.examples.ipso;

import java.util.Timer;
import java.util.TimerTask;

import ch.ethz.inf.vs.californium.coap.GETRequest;
import ch.ethz.inf.vs.californium.coap.registries.CodeRegistry;
import ch.ethz.inf.vs.californium.coap.registries.MediaTypeRegistry;
import ch.ethz.inf.vs.californium.endpoint.resources.LocalResource;

/**
 * This resource implements a part of the IPSO profile.
 * 
 * @author Matthias Kovatsch
 */
public class DeviceBattery extends LocalResource {
	
	private double power = 3.6d;

	public DeviceBattery() {
		super("dev/bat");
		setTitle("Battery");
		setResourceType("ipso:dev-bat");
		// second rt not supported by current SensiNode RD demo
		//setResourceType("ucum:V");
		setInterfaceDescription("core#s");
		
		isObservable(true);

		// Set timer task scheduling
		Timer timer = new Timer();
		timer.schedule(new TimeTask(), 0, 1000);
	}

	private class TimeTask extends TimerTask {

		@Override
		public void run() {
			power -= 0.001d*Math.random();

			// Call changed to notify subscribers
			changed();
		}
	}

	@Override
	public void performGET(GETRequest request) {
		
		// complete the request
		request.respond(CodeRegistry.RESP_CONTENT, Double.toString(Math.round(power*1000d)/1000d), MediaTypeRegistry.TEXT_PLAIN);
	}
}
