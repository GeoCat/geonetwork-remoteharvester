package geocat.model;

import geocat.database.entities.HarvestJob;

import java.util.ArrayList;
import java.util.List;

public class HarvestStatus {
    public String processID;
    public String url;
    public String longTermTag;
    public String state;
    public String createTimeUTC;
    public String lastUpdateUTC;
    public String errorMessages;

    public List<EndpointStatus> endpoints;


    public HarvestStatus(HarvestJob job) {
        this.processID = job.getJobId();
        this.url = job.getInitialUrl();
        this.longTermTag = job.getLongTermTag();
        this.state = job.getState().toString();
        this.createTimeUTC = job.getCreateTimeUTC().toInstant().toString();
        this.lastUpdateUTC = job.getLastUpdateUTC().toInstant().toString();
        this.errorMessages = job.getMessages();
        endpoints = new ArrayList<>();
    }
}
