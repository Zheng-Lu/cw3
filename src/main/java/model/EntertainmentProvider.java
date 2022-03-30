package model;
import external.EntertainmentProviderSystem;

import java.util.ArrayList;
import java.util.List;

public class EntertainmentProvider extends User {

    private String orgName, orgAddress, mainRepName, mainRepEmail;
    private List<String>otherRepNames = new ArrayList<>();
    private List<String>otherRepEmails = new ArrayList<>();
    private List<Event>events = new ArrayList<>();


    public EntertainmentProvider(String orgName,
                                  String orgAddress,
                                  String paymentAccountEmail,
                                  String mainRepName,
                                  String mainRepEmail,
                                  String password,
                                  List<String> otherRepNames,
                                  List<String> otherRepEmails){

        super(mainRepEmail,password,paymentAccountEmail);
        this.orgName = orgName;
        this.orgAddress = orgAddress;
        this.mainRepName = mainRepName;
        this.mainRepEmail = mainRepEmail;
        this.otherRepNames = otherRepNames;
        this.otherRepEmails = otherRepEmails;
    }

    public void addEvent(Event event){
        this.events.add(event);
    }

    public String getOrgName(){
        return this.orgName;
    }

    public void setOrgName(String orgName){
        this.orgName = orgName;
    }

    public String getOrgAddress(){
        return this.orgAddress;
    }

    public void setOrgAddress(String orgAddress){
        this.orgAddress = orgAddress;
    }

    public List<Event> getEvents(){
        return events;
    }

    public void setMainRepName(String mainRepName){
        this.mainRepName = mainRepName;
    }

    public void setMainRepEmail(String mainRepEmail){
        this.mainRepEmail = mainRepEmail;
    }

    public void setOtherRepNames(List<String> otherRepNames){
        this.otherRepNames = otherRepNames;
    }

    public void setOtherRepEmails(List<String> otherRepEmails){
        this.otherRepEmails = otherRepEmails;
    }

    public EntertainmentProviderSystem getProviderSystem(){
        return new EntertainmentProviderSystem(){

        };
    }

    @Override
    public String toString(){
        return  "";
    }

}
