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

package com.geocat.ingester.model.linkchecker;

import com.geocat.ingester.model.linkchecker.helper.DatasetMetadataRecord;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Entity
@DiscriminatorValue("RemoteDatasetMetadataRecord")
public class OperatesOnRemoteDatasetMetadataRecord extends DatasetMetadataRecord {

    @OneToOne(mappedBy = "datasetMetadataRecord", fetch = FetchType.EAGER)
    //@JoinColumn(name="operatesOnLinkId")
    private OperatesOnLink operatesOnLink;


    @Column(columnDefinition = "text")
    private String summary;


    //PASS if you can find a cap doc with a layer DS record with the same fileID and datasetID
    // null = not evaluated
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(5)")
    IndicatorStatus INDICATOR_MATCHES_A_CAP_DATASET_LAYER;

    //---------------------------------------------------------------------------


    public IndicatorStatus getINDICATOR_MATCHES_A_CAP_DATASET_LAYER() {
        return INDICATOR_MATCHES_A_CAP_DATASET_LAYER;
    }

    public void setINDICATOR_MATCHES_A_CAP_DATASET_LAYER(IndicatorStatus INDICATOR_MATCHES_A_CAP_DATASET_LAYER) {
        this.INDICATOR_MATCHES_A_CAP_DATASET_LAYER = INDICATOR_MATCHES_A_CAP_DATASET_LAYER;
    }

    public OperatesOnLink getOperatesOnLink() {
        return operatesOnLink;
    }

    public void setOperatesOnLink(OperatesOnLink operatesOnLink) {
        this.operatesOnLink = operatesOnLink;
    }


    //---------------------------------------------------------------------------

    @PreUpdate
    protected void onUpdate() {
        super.onUpdate();
        summary = toString();
    }

    @PrePersist
    protected void onInsert() {
        super.onInsert();
        summary = toString();
    }

    //---------------------------------------------------------------------------

    @Override
    public String toString() {
        String result = "RemoteDatasetMetadataRecord {\n";
        result += "      DatasetMetadataDocumentId: " + getDatasetMetadataDocumentId() + "\n";

        result += super.toString();

        result += "\n";

        result += " }";
        return result;
    }

}
