package org.familysearch.standards.place.ws.service;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;
import org.familysearch.standards.place.ws.model.RootModel;

public class TestDeserialize {
    public static String jsonString =
            "{ \n" +
            "    \"types\": [], \n" +
            "    \"typeGroups\": [], \n" +
            "    \"place\": [], \n" +
            "    \"requestResponse\": [], \n" +
            "    \"request\": [], \n" +
            "    \"rep\": [ \n" +
            "        { \n" +
            "            \"fromYear\": null, \n" +
            "            \"endYear\": null, \n" +
            "            \"type\": { \n" +
            "                \"id\": 86, \n" +
            "                \"name\": \"Cemetery\", \n" +
            "                \"locale\": \"en-US\" \n" +
            "            }, \n" +
            "            \"preferredLocale\": \"en\", \n" +
            "            \"display\": [ \n" +
            "                { \n" +
            "                    \"name\": \"Bowman Memorial Park\", \n" +
            "                    \"locale\": \"en\" \n" +
            "                } \n" +
            "            ], \n" +
            "            \"fullDisplay\": { \n" +
            "                \"name\": \"Bowman Memorial Park, Apache, Arizona, United States\", \n" +
            "                \"locale\": \"en-US\" \n" +
            "            }, \n" +
            "            \"location\": { \n" +
            "                \"centroid\": { \n" +
            "                    \"longitude\": -109.10722, \n" +
            "                    \"latitude\": 35.41917 \n" +
            "                } \n" +
            "            }, \n" +
            "            \"jurisdiction\": { \n" +
            "                \"link\": [ \n" +
            "                    { \n" +
            "                        \"href\": \"8\", \n" +
            "                        \"rel\": \"parent\", \n" +
            "                        \"title\": \"Apache\" \n" +
            "                    }, \n" +
            "                    { \n" +
            "                        \"href\": \"2\", \n" +
            "                        \"rel\": \"parent\", \n" +
            "                        \"title\": \"Arizona\" \n" +
            "                    }, \n" +
            "                    { \n" +
            "                        \"href\": \"1\", \n" +
            "                        \"rel\": \"parent\", \n" +
            "                        \"title\": \"United States\" \n" +
            "                    } \n" +
            "                ] \n" +
            "            }, \n" +
            "            \"status\": { \n" +
            "                \"published\": true, \n" +
            "                \"validated\": true \n" +
            "            }, \n" +
            "            \"link\": [ \n" +
            "                { \n" +
            "                    \"href\": \"23\", \n" +
            "                    \"rel\": \"self\", \n" +
            "                    \"title\": \"Bowman Memorial Park\" \n" +
            "                }, \n" +
            "                { \n" +
            "                    \"href\": \"4229\", \n" +
            "                    \"rel\": \"ownerLink\", \n" +
            "                    \"title\": null \n" +
            "                } \n" +
            "            ], \n" +
            "            \"id\": 23, \n" +
            "            \"owner\": 4229 \n" +
            "        } \n" +
            "    ] \n" +
            "} \n";

    public static void main(String[] args) throws Exception {
        JsonFactory factory = new JsonFactory();
        JsonParser  parser  = factory.createJsonParser(jsonString);
        JsonToken token = parser.nextToken();
        
        if (token == JsonToken.START_OBJECT) {
            ObjectMapper mapper = new ObjectMapper();
            RootModel places = mapper.readValue(parser, RootModel.class);
            System.out.println("Places: " + places);
        }
    }
}
