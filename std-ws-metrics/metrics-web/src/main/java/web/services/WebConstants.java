package web.services;

public class WebConstants {

    static final String METHOD_DELETE                   = "DELETE";
    static final String METHOD_GET                      = "GET";
    static final String METHOD_POST                     = "POST";
    static final String METHOD_PUT                      = "PUT";

    static final String HEADER_USER_ID                  = "user-id";

    static final String PARAM_TRUTH_ID                  = "truthId";
    static final String PARAM_ID                        = "id";
    static final String PARAM_TRUTH_SET                 = "truthSet";


    static final String PATH_TRUTH_ID                    = "{truthId:\\d+}";
    static final String PATH_NAME                       = "{name}";
    static final String PATH_ID                         = "{id:\\d+}";

    static final String MSG_NOT_FOUND                   = "not found";
    static final String MSG_STATUS                      = "status";
    static final String MSG_SYSTEM                      = "system";
    static final String MSG_TIME                        = "time";
    static final String MSG_USER_ERROR                  = "user_error";
    static final String MSG_SYSTEM_ERROR                = "system_error";
    static final String MSG_USER_ID                     = "userId";

}
