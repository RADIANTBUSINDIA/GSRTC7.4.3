package com.gsrtc.mobileweb.models;

public class DivyangStore {

    private static DivyangDetails divyangDetails;

    // Save the model to global memory
    public static void setDivyangDetails(DivyangDetails details) {
        divyangDetails = details;
    }

    // Retrieve the model anywhere in the app
    public static DivyangDetails getDivyangDetails() {
        return divyangDetails;
    }
}
