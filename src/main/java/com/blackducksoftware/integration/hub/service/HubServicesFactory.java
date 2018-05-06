/**
 * hub-common
 *
 * Copyright (C) 2018 Black Duck Software, Inc.
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
 */
package com.blackducksoftware.integration.hub.service;

import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.blackducksoftware.integration.exception.IntegrationException;
import com.blackducksoftware.integration.hub.cli.CLIDownloadUtility;
import com.blackducksoftware.integration.hub.cli.SimpleScanUtility;
import com.blackducksoftware.integration.hub.configuration.HubScanConfig;
import com.blackducksoftware.integration.hub.configuration.HubServerConfig;
import com.blackducksoftware.integration.hub.rest.RestConnection;
import com.blackducksoftware.integration.hub.rest.UriCombiner;
import com.blackducksoftware.integration.hub.service.bucket.HubBucketService;
import com.blackducksoftware.integration.phonehome.PhoneHomeClient;
import com.blackducksoftware.integration.phonehome.google.analytics.GoogleAnalyticsConstants;
import com.blackducksoftware.integration.util.IntEnvironmentVariables;
import com.blackducksoftware.integration.util.IntegrationEscapeUtil;

public class HubServicesFactory {
    private final IntEnvironmentVariables ciEnvironmentVariables;
    private final RestConnection restConnection;

    public HubServicesFactory(final RestConnection restConnection) {
        this.ciEnvironmentVariables = new IntEnvironmentVariables();

        this.restConnection = restConnection;
    }

    public void addEnvironmentVariable(final String key, final String value) {
        ciEnvironmentVariables.put(key, value);
    }

    public void addEnvironmentVariables(final Map<String, String> environmentVariables) {
        ciEnvironmentVariables.putAll(environmentVariables);
    }

    public SignatureScannerService createSignatureScannerService() {
        return createSignatureScannerService(120000l);
    }

    public SignatureScannerService createSignatureScannerService(final long timeoutInMilliseconds) {
        return new SignatureScannerService(createHubService(), ciEnvironmentVariables, createCliDownloadUtility(), createProjectService(), createCodeLocationService(), createScanStatusService(timeoutInMilliseconds));
    }

    public PhoneHomeService createPhoneHomeService() {
        return new PhoneHomeService(createHubService(), createPhoneHomeClient(), createHubRegistrationService(), ciEnvironmentVariables);
    }

    public PhoneHomeClient createPhoneHomeClient() {
        return new PhoneHomeClient(GoogleAnalyticsConstants.PRODUCTION_INTEGRATIONS_TRACKING_ID, restConnection.timeout, restConnection.getProxyInfo(), restConnection.alwaysTrustServerCertificate,
                restConnection.logger, restConnection.gson);
    }

    public ReportService createReportService(final long timeoutInMilliseconds) throws IntegrationException {
        return new ReportService(createHubService(), createProjectService(), createIntegrationEscapeUtil(), timeoutInMilliseconds);
    }

    public PolicyRuleService createPolicyRuleService() {
        return new PolicyRuleService(createHubService());
    }

    public ScanStatusService createScanStatusService(final long timeoutInMilliseconds) {
        return new ScanStatusService(createHubService(), createProjectService(), createCodeLocationService(), timeoutInMilliseconds);
    }

    public NotificationService createNotificationService() {
        return new NotificationService(createHubService(), createHubBucketService());
    }

    public NotificationService createNotificationService(final ExecutorService executorService) {
        return new NotificationService(createHubService(), createHubBucketService(executorService));
    }

    public ExtensionConfigService createExtensionConfigService() {
        return new ExtensionConfigService(createHubService());
    }

    public LicenseService createLicenseService() {
        return new LicenseService(createHubService(), createComponentService());
    }

    public CodeLocationService createCodeLocationService() {
        return new CodeLocationService(createHubService());
    }

    public CLIDownloadUtility createCliDownloadUtility() {
        return new CLIDownloadUtility(restConnection.logger, restConnection);
    }

    public IntegrationEscapeUtil createIntegrationEscapeUtil() {
        return new IntegrationEscapeUtil();
    }

    public SimpleScanUtility createSimpleScanUtility(final HubServerConfig hubServerConfig, final HubScanConfig hubScanConfig, final String projectName, final String versionName) {
        return new SimpleScanUtility(restConnection.logger, restConnection.gson, hubServerConfig, ciEnvironmentVariables, hubScanConfig, projectName, versionName);
    }

    public HubRegistrationService createHubRegistrationService() {
        return new HubRegistrationService(createHubService());
    }

    public HubService createHubService() {
        return new HubService(restConnection);
    }

    public HubService createHubService(final UriCombiner uriCombiner) {
        return new HubService(restConnection, uriCombiner);
    }

    public RestConnection getRestConnection() {
        return restConnection;
    }

    public ComponentService createComponentService() {
        return new ComponentService(createHubService());
    }

    public IssueService createIssueService() {
        return new IssueService(createHubService());
    }

    public ProjectService createProjectService() {
        return new ProjectService(createHubService(), createComponentService());
    }

    public UserGroupService createUserGroupService() {
        return new UserGroupService(createHubService());
    }

    public HubBucketService createHubBucketService() {
        return new HubBucketService(createHubService());
    }

    public HubBucketService createHubBucketService(final ExecutorService executorService) {
        return new HubBucketService(createHubService(), executorService);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, RecursiveToStringStyle.JSON_STYLE);
    }

}
