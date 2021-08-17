/*
 *  =============================================================================
 *  ===  Copyright (C) 2021 Food and Agriculture Organization of the
 *  ===  United Nations (FAO-UN), United Nations World Food Programme (WFP)
 *  ===  and United Nations Environment Programme (UNEP)
 *  ===
 *  ===  This program is free software; you can redistribute it and/or modify
 *  ===  it under the terms of the GNU General Public License as published by
 *  ===  the Free Software Foundation; either version 2 of the License, or (at
 *  ===  your option) any later version.
 *  ===
 *  ===  This program is distributed in the hope that it will be useful, but
 *  ===  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  ===  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  ===  General Public License for more details.
 *  ===
 *  ===  You should have received a copy of the GNU General Public License
 *  ===  along with this program; if not, write to the Free Software
 *  ===  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 *  ===
 *  ===  Contact: Jeroen Ticheler - FAO - Viale delle Terme di Caracalla 2,
 *  ===  Rome - Italy. email: geonetwork@osgeo.org
 *  ===
 *  ===  Development of this program was financed by the European Union within
 *  ===  Service Contract NUMBER – 941143 – IPR – 2021 with subject matter
 *  ===  "Facilitating a sustainable evolution and maintenance of the INSPIRE
 *  ===  Geoportal", performed in the period 2021-2023.
 *  ===
 *  ===  Contact: JRC Unit B.6 Digital Economy, Via Enrico Fermi 2749,
 *  ===  21027 Ispra, Italy. email: JRC-INSPIRE-SUPPORT@ec.europa.eu
 *  ==============================================================================
 */

package net.geocat.database.linkchecker.entities.helper;


import net.geocat.database.linkchecker.entities.DatasetDocumentLink;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dataset_record_type",
        discriminatorType = DiscriminatorType.STRING)
public class DatasetMetadataRecord extends MetadataRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long datasetMetadataDocumentId;

    private String datasetIdentifier;

    private Integer numberOfLinksFound;

    @OneToMany(mappedBy = "datasetMetadataRecord",
            cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<DatasetDocumentLink> documentLinks;



    //---------------------------------------------------------------------------

    public DatasetMetadataRecord() {
        super();
        documentLinks = new ArrayList<>();
    }

    public List<DatasetDocumentLink> getDocumentLinks() {
        return documentLinks;
    }

    public void setDocumentLinks(List<DatasetDocumentLink> documentLinks) {
        this.documentLinks = documentLinks;
    }

    public Integer getNumberOfLinksFound() {
        return numberOfLinksFound;
    }

    public void setNumberOfLinksFound(Integer numberOfLinksFound) {
        this.numberOfLinksFound = numberOfLinksFound;
    }

    public long getDatasetMetadataDocumentId() {
        return datasetMetadataDocumentId;
    }

    public void setDatasetMetadataDocumentId(long datasetMetadataDocumentId) {
        this.datasetMetadataDocumentId = datasetMetadataDocumentId;
    }

    public String getDatasetIdentifier() {
        return datasetIdentifier;
    }

    public void setDatasetIdentifier(String datasetIdentifier) {
        this.datasetIdentifier = datasetIdentifier;
    }


    //---------------------------------------------------------------------------


    protected void onUpdate() {
        super.onUpdate();
        update();
    }


    protected void onInsert() {
        super.onInsert();
        update();
    }

    protected void update() {
        if (documentLinks != null)
            numberOfLinksFound = documentLinks.size();
    }

    //---------------------------------------------------------------------------
    @Override
    public String toString() {
        String result = super.toString();

        result += "     dataset Identifier: " + datasetIdentifier + "\n";
        if (numberOfLinksFound != null)
            result += "     number of links found: "+ numberOfLinksFound+"\n";

        return result;
    }
}
