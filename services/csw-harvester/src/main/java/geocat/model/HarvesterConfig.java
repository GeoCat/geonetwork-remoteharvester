package geocat.model;


import geocat.csw.csw.XMLTools;
import org.w3c.dom.Document;

import java.net.URL;

public class HarvesterConfig {

    // tag so you can refer to "previous run results"
    //ie. country name
    private String longTermTag;

    // looks for nested discovery service
    //true for poland, otherwise false
    private boolean lookForNestedDiscoveryService;

    //CSW <ogc:Filter>
    private String filter;

    // endpoint for GetCapabilities
    private String url;

    // GUID for the harvest (used as JMS Correlation ID).  Provided by server (do not specify)
    private String processID;

    // how many records to retrieve in a single GetRecords request
    // defaults to 20 records in a GetRecords request (see DEFAULT_NRECORDS)
    private int numberOfRecordsPerRequest;


    // which queue set to use.  blank=auto determined.  Otherwise "PARALLEL#" #=2,3,4
    // usually, you want to make this blank.  Except for large servers
    //   (i.e. lots of records and can handle multiple simutaneous requests)
    private String getRecordQueueHint;

    //what to do in the case an error condition occurs.
    // See the ProblematicResultsConfiguration class for details
    private ProblematicResultsConfiguration problematicResultsConfiguration;


    //if true then do NOT add the SORT BY to the individual getRecord requests
    // if false (or null), then DO SORT/
    public Boolean doNotSort;


    // if numberOfRecordsPerRequest is not specified, use this
    public static int DEFAULT_NRECORDS = 20;

    Integer storeAtMostNHistoricalRuns;


    //--


    public Integer getStoreAtMostNHistoricalRuns() {
        return storeAtMostNHistoricalRuns;
    }

    public void setStoreAtMostNHistoricalRuns(Integer storeAtMostNHistoricalRuns) {
        this.storeAtMostNHistoricalRuns = storeAtMostNHistoricalRuns;
    }

    public Boolean getDoNotSort() {
        return doNotSort;
    }

    public void setDoNotSort(Boolean doNotSort) {
        this.doNotSort = doNotSort;
    }

    public String getGetRecordQueueHint() {
        return getRecordQueueHint;
    }

    public void setGetRecordQueueHint(String getRecordQueueHint) {
        this.getRecordQueueHint = getRecordQueueHint;
    }

    public ProblematicResultsConfiguration getProblematicResultsConfiguration() {
        return problematicResultsConfiguration;
    }

    public void setProblematicResultsConfiguration(ProblematicResultsConfiguration problematicResultsConfiguration) {
        this.problematicResultsConfiguration = problematicResultsConfiguration;
    }

    public boolean isLookForNestedDiscoveryService() {
        return lookForNestedDiscoveryService;
    }

    public void setLookForNestedDiscoveryService(boolean lookForNestedDiscoveryService) {
        this.lookForNestedDiscoveryService = lookForNestedDiscoveryService;
    }

    public String getLongTermTag() {
        return longTermTag;
    }

    public void setLongTermTag(String longTermTag) {
        this.longTermTag = longTermTag;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProcessID() {
        return processID;
    }

    public void setProcessID(String processID) {
        this.processID = processID;
    }

    public void validate() throws Exception {
        if ((url == null) || (url.isEmpty()))
            throw new Exception("No URL given for remote CSW");
        URL _url = new URL(url);
        if (!_url.getProtocol().equalsIgnoreCase("HTTP") && !_url.getProtocol().equalsIgnoreCase("HTTPS"))
            throw new Exception("URL isn't http or https");

        if ((filter != null) && (!filter.isEmpty())) { //filter present
            Document filterDoc = XMLTools.parseXML(filter);
            String rootNodeName = filterDoc.getFirstChild().getNodeName();
            if (rootNodeName != "ogc:Filter")
                throw new Exception("filter doesn't start with <ogc:Filter>");
        }
        if (problematicResultsConfiguration == null)
            problematicResultsConfiguration = new ProblematicResultsConfiguration();
        problematicResultsConfiguration.validate();

        if (numberOfRecordsPerRequest <= 0)
            numberOfRecordsPerRequest = DEFAULT_NRECORDS;

        if (numberOfRecordsPerRequest > 500) // unreasonable
            numberOfRecordsPerRequest = 500;

        if (storeAtMostNHistoricalRuns == null)
            storeAtMostNHistoricalRuns = 10000;
    }

    @Override
    public String toString() {
        return "{processID=" + processID + ", urls=" + url + "}";
    }

    public int getNumberOfRecordsPerRequest() {
        return numberOfRecordsPerRequest;
    }

    public void setNumberOfRecordsPerRequest(int numberOfRecordsPerRequest) {
        this.numberOfRecordsPerRequest = numberOfRecordsPerRequest;
    }
}
