/**
 * ﻿Copyright (C) 2013-2014 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.wps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.n52.wps.webapp.api.AlgorithmEntry;
import org.n52.wps.webapp.api.ConfigurationCategory;
import org.n52.wps.webapp.api.ConfigurationKey;
import org.n52.wps.webapp.api.ConfigurationModule;
import org.n52.wps.webapp.api.FormatEntry;
import org.n52.wps.webapp.api.types.ConfigurationEntry;
import org.n52.wps.webapp.api.types.IntegerConfigurationEntry;
import org.n52.wps.webapp.api.types.StringConfigurationEntry;

public class CSWLoDAlgorithmCM implements ConfigurationModule{

	private boolean isActive = true;

	private List<AlgorithmEntry> algorithmEntries;
	       
	public final static String virtuosoUserKey = "virtuoso.user";
        
        private String virtuosoUser;
        
        private ConfigurationEntry<String> virtuosoUserEntry = new StringConfigurationEntry(virtuosoUserKey, "Virtuoso username", "",
                true, "dba");
        
        public final static String virtuosoPwdKey = "virtuoso.pwd";
        
        private String virtuosoPwd;
        
        private ConfigurationEntry<String> virtuosoPwdEntry = new StringConfigurationEntry(virtuosoPwdKey, "Virtuoso password", "",
                true, "dba");
        
        public final static String virtuosoJDBCUrlKey = "virtuoso.jdbcurl";
        
        private String virtuosoJDBCUrl;
        
        private ConfigurationEntry<String> virtuosoJDBCUrlEntry = new StringConfigurationEntry(virtuosoJDBCUrlKey, "Virtuoso JDBC URL", "",
                true, "jdbc:virtuoso://localhost:1111");
        
        public final static String startPosKey = "start.pos";
        
        private int startPos;
        
        private ConfigurationEntry<Integer> startPosEntry = new IntegerConfigurationEntry(startPosKey, "Catalog harvesting start position", "",
                false, 1);
	
	private List<? extends ConfigurationEntry<?>> configurationEntries = Arrays.asList(virtuosoJDBCUrlEntry, virtuosoUserEntry, virtuosoPwdEntry, startPosEntry);
	
	public CSWLoDAlgorithmCM() {
		algorithmEntries = new ArrayList<>();
	}
	
	@Override
	public String getModuleName() {
		return "CSWLoDEnablerStarter Configuration Module";
	}

	@Override
	public boolean isActive() {
		return isActive;
	}

	@Override
	public void setActive(boolean active) {
		this.isActive = active;
	}

	@Override
	public ConfigurationCategory getCategory() {
		return ConfigurationCategory.REPOSITORY;
	}

	@Override
	public List<? extends ConfigurationEntry<?>> getConfigurationEntries() {
		return configurationEntries;
	}

	@Override
	public List<AlgorithmEntry> getAlgorithmEntries() {
		return algorithmEntries;
	}

	@Override
	public List<FormatEntry> getFormatEntries() {
		return null;
	}

    public String getVirtuosoUser() {
        return virtuosoUser;
    }

    @ConfigurationKey(key = virtuosoUserKey)
    public void setVirtuosoUser(String virtuosoUser) {
        this.virtuosoUser = virtuosoUser;
    }

    public String getVirtuosoPwd() {
        return virtuosoPwd;
    }

    @ConfigurationKey(key = virtuosoPwdKey)
    public void setVirtuosoPwd(String virtuosoPwd) {
        this.virtuosoPwd = virtuosoPwd;
    }

    public String getVirtuosoJDBCUrl() {
        return virtuosoJDBCUrl;
    }

    @ConfigurationKey(key = virtuosoJDBCUrlKey)
    public void setVirtuosoJDBCUrl(String virtuosoJDBCUrl) {
        this.virtuosoJDBCUrl = virtuosoJDBCUrl;
    }

    public int getStartPos() {
        return startPos;
    }

    @ConfigurationKey(key = startPosKey)
    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

}
