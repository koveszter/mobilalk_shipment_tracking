package com.example.shipmenttracking;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class ShipmentTracking {
    private String href; // Not on creation, yes on retrieval
    private String id; // Not on creation, yes on retrieval
    private String trackingCode;
    private String carrier;
    private Map<String, String> addressTo = new HashMap<>();

    public ShipmentTracking() {
    }

    public ShipmentTracking(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public ShipmentTracking(String trackingCode, String carrier, String country, String locality,
                            String postcode, String stateOrProvince, String streetNr,
                            String streetName, String streetType) {
        this.trackingCode = trackingCode;
        this.carrier = carrier;
        this.addressTo.put("country", country);
        this.addressTo.put("locality", locality);
        this.addressTo.put("postcode", postcode);
        this.addressTo.put("stateOrProvince", stateOrProvince);
        this.addressTo.put("streetNr", streetNr);
        this.addressTo.put("streetName", streetName);
        this.addressTo.put("streetType", streetType);
    }

    public ShipmentTracking(String trackingCode, String carrier, String country, String locality,
                            String postcode, String stateOrProvince, String streetNr,
                            String streetName, String streetType, String id, String href) {
        this.href = href;
        this.id = id;
        this.trackingCode = trackingCode;
        this.carrier = carrier;
        this.addressTo.put("country", country);
        this.addressTo.put("locality", locality);
        this.addressTo.put("postcode", postcode);
        this.addressTo.put("stateOrProvince", stateOrProvince);
        this.addressTo.put("streetNr", streetNr);
        this.addressTo.put("streetName", streetName);
        this.addressTo.put("streetType", streetType);
    }

    public String getId() {
        return id;
    }
    public String getTrackingCode() {
        return trackingCode;
    }
    public String getCarrier() {
        return carrier;
    }
    public Map<String, String> getAddressTo() {
        return addressTo;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setAddressTo(String id, String country, String locality, String postcode,
                             String stateOrProvince, String streetNr, String streetName,
                             String streetType) {
        this.setId(id);
        this.addressTo.put("country", country);
        this.addressTo.put("locality", locality);
        this.addressTo.put("postcode", postcode);
        this.addressTo.put("stateOrProvince", stateOrProvince);
        this.addressTo.put("streetNr", streetNr);
        this.addressTo.put("streetName", streetName);
        this.addressTo.put("streetType", streetType);
    }

    @NonNull
    @Override
    public String toString() {
        return "Tracking code: " + this.trackingCode + "\n" + "Carrier: " + this.carrier + "\n" +
                "AddressTo: " + getAddressTo();
    }
}