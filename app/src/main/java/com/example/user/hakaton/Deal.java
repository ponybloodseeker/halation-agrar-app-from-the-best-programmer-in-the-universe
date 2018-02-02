package com.example.user.hakaton;

public class Deal {
    private String whatOffer;
    private String ownersUID;

    public Deal(String whatOffer, String ownersUID) {
        this.whatOffer = whatOffer;
        this.ownersUID = ownersUID;
    }

    public String getWhatOffer() {
        return whatOffer;
    }

    public String getOwnersUID() {
        return ownersUID;
    }
}
