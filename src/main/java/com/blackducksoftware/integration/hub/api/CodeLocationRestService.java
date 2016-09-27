/*******************************************************************************
 * Copyright (C) 2016 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package com.blackducksoftware.integration.hub.api;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.restlet.data.Method;

import com.blackducksoftware.integration.hub.api.codelocation.CodeLocationItem;
import com.blackducksoftware.integration.hub.api.codelocation.CodeLocationTypeEnum;
import com.blackducksoftware.integration.hub.exception.BDRestException;
import com.blackducksoftware.integration.hub.rest.RestConnection;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class CodeLocationRestService extends HubRestService<CodeLocationItem> {
	private final List<String> getCodeLocationsSegments = Arrays.asList(UrlConstants.SEGMENT_API,
			UrlConstants.SEGMENT_CODE_LOCATIONS);

	public CodeLocationRestService(final RestConnection restConnection, final Gson gson, final JsonParser jsonParser) {
		super(restConnection, gson, jsonParser, new TypeToken<CodeLocationItem>() {
		}.getType(), new TypeToken<List<CodeLocationItem>>() {
		}.getType());
	}

	public List<CodeLocationItem> getAllCodeLocations() throws IOException, BDRestException, URISyntaxException {
		final HubRequest codeLocationItemRequest = new HubRequest(getRestConnection(), getJsonParser());
		codeLocationItemRequest.setMethod(Method.GET);
		codeLocationItemRequest.setLimit(100);
		codeLocationItemRequest.addUrlSegments(getCodeLocationsSegments);

		final JsonObject jsonObject = codeLocationItemRequest.executeForResponseJson();
		final List<CodeLocationItem> allCodeLocations = getAll(jsonObject, codeLocationItemRequest);
		return allCodeLocations;
	}

	public List<CodeLocationItem> getAllCodeLocationsForCodeLocationType(final CodeLocationTypeEnum codeLocationType)
			throws IOException, BDRestException, URISyntaxException {
		final HubRequest codeLocationItemRequest = new HubRequest(getRestConnection(), getJsonParser());
		codeLocationItemRequest.setMethod(Method.GET);
		codeLocationItemRequest.setLimit(100);
		codeLocationItemRequest.addQueryParameter("codeLocationType", codeLocationType.toString());
		codeLocationItemRequest.addUrlSegments(getCodeLocationsSegments);

		final JsonObject jsonObject = codeLocationItemRequest.executeForResponseJson();
		final List<CodeLocationItem> allCodeLocations = getAll(jsonObject, codeLocationItemRequest);
		return allCodeLocations;
	}

}
