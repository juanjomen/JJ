package org.familysearch.standards.place.ws.model.integration;

import org.familysearch.standards.place.ws.model.RootModel;
import org.familysearch.standards.place.ws.model.marshal.BaseTest;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;

public class UnmarshalJson extends BaseTest {
    public static void main(String... args) {
        String json = "{\"rep\":{\"revision\":1,\"display\":{\"name\":\"Orem\",\"locale\":\"en\"},\"fullDisplay\":{\"name\":\"Orem, Utah, Utah, United States\",\"locale\":\"en\"},\"displayNames\":[{\"name\":\"Orem\",\"locale\":\"en\"}],\"jurisdiction\":{\"id\":393779,\"name\":\"Utah\",\"locale\":\"en\",\"jurisdiction\":{\"id\":333,\"name\":\"Utah\",\"locale\":\"en\",\"jurisdiction\":{\"id\":1,\"name\":\"United States\",\"locale\":\"en\",\"link\":{\"href\":\"/std-ws-place/places/reps/1\",\"rel\":\"parent\"}},\"link\":{\"href\":\"/std-ws-place/places/reps/333\",\"rel\":\"parent\"}},\"link\":{\"href\":\"/std-ws-place/places/reps/393779\",\"rel\":\"parent\"}},\"type\":{\"id\":\"193\",\"localizedName\":[]},\"preferredLocale\":\"0\",\"location\":{\"centroid\":{\"latitude\":\"40.29694\",\"longitude\":\"-111.69389\"}},\"published\":true,\"validated\":true,\"link\":[{\"href\":\"/std-ws-place/places/reps/5040168\",\"rel\":\"self\"},{\"href\":\"/std-ws-place/places/1337080\",\"rel\":\"related\"}],\"id\":5040168,\"uuid\":\"4eb231c8-9a5d-43ad-9734-90e37b091df6\",\"place\":1337080,\"fromYear\":\"1900\",\"toYear\":\"2000\",\"typeGroup\":{\"id\":\"\"}}}";
        RootModel model = POJOMarshalUtil.fromJSON(json, RootModel.class);
        System.out.println("Model: " + model);
    }
}
