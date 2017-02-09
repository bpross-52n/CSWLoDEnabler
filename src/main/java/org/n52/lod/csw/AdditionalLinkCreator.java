/**
 * ﻿Copyright (C) 2013-2016 52°North Initiative for Geospatial Open Source
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
package org.n52.lod.csw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.n52.lod.Configuration;
import org.n52.lod.csw.mapping.CSWtoRDFMapper;
import org.n52.lod.triplestore.AbstractTripleSink;
import org.n52.lod.triplestore.FileTripleSink;
import org.n52.lod.triplestore.VirtuosoServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.OWL;

public class AdditionalLinkCreator {

    private static final Logger log = LoggerFactory.getLogger(AdditionalLinkCreator.class);

    public AdditionalLinkCreator(File csvFile, String configFile) {
        
        Configuration configuration = new Configuration(configFile);
        
        FileReader in;
        
        BufferedReader bufferedReader = null;
        
        AbstractTripleSink tripleSink;
        
        CSWtoRDFMapper mapper = new CSWtoRDFMapper(configuration);
        
        if(configuration.isAddToServer()){
            tripleSink = new VirtuosoServer(configuration,  mapper);
        }else if(configuration.isSaveToFile()){
            tripleSink = new FileTripleSink( mapper);
        }else{
            log.error("Neither addToServer nor saveToFile chosen, aborting.");
            return;
        }
                
        Model m = tripleSink.getModel();
        
        Map<String, Resource> datasetIdResourceMap = new HashMap<String, Resource>();
        
        try {
            in = new FileReader(csvFile);

            bufferedReader = new BufferedReader(in);

            String line = "";

            while ((line = bufferedReader.readLine()) != null) {

                String[] splitStrings = line.split(";");    
                
                String linkURL = splitStrings[0].trim();
                String name = splitStrings[1].replace(" ", "")
                        //.replace("@en", "")
                        .trim();
                
                log.trace("Adding new entity for " + name);
                
                name = mapper.getUriBase_keyword() + name;
                
                Resource linkResource = m.createResource(name); 
                
                linkResource.addProperty(OWL.sameAs, m.createResource(linkURL));
                
                for (int i = 3; i < splitStrings.length; i++) {
                    
                    String datasetId = splitStrings[i].trim();
                    
                    if(datasetId.isEmpty()){
                        break;
                    }
                    datasetIdResourceMap.put(datasetId, linkResource);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        tripleSink.addAdditionalLinks(datasetIdResourceMap);
        
        try {
            tripleSink.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
        
        if(args.length < 1){
            log.error("No csv file with additional links was specified.");
        }
        
        File csvFile = new File(args[0]);

        FileReader in = new FileReader(csvFile);

        BufferedReader bufferedReader = new BufferedReader(in);

        String line = "";

        while ((line = bufferedReader.readLine()) != null) {

            String[] splitStrings = line.split(";");    
            
            String linkURL = splitStrings[0].trim();
            String name = splitStrings[1].replace(" ", "");
        }
        
        String configFilePath = Configuration.DEFAULT_CONFIG_FILE;
        
        try{
            configFilePath = args[1];
        }catch(Exception e){
            log.info("No config file path specified, using default path: " + configFilePath);
        }
        
        new AdditionalLinkCreator(csvFile, configFilePath);
    }

}
