package lhc.com.otherRessources;

public class ApplicationConstants {


    public static final String MyPREFERENCES_CREDENTIALS = "MyPreferencesCredentials" ;
    public static final String MyPREFERENCES_COMPETITION = "MyPreferencesCompetition" ;

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String IS_USER_LOGGED_IN = "isUserLoggedIn";
    public static final String IS_REMEMBER_ME = "isRememberMe";

    public static final String COMPETITION_REF = "competition_ref";
    public static final String MATCH_REF = "match_ref";
    public static final String MATCH_STATUS = "match_status";
    public static final String MATCH_CREATOR = "match_creator";
    public static final String NUMBER_VOTE_TOP = LabelType.NUMBER_VOTE_TOP.toString();
    public static final String NUMBER_VOTE_FLOP = LabelType.NUMBER_VOTE_FLOP.toString();
    public static final String RULES =  "rules";
    public static final String JSON_LIST_VOTES_BUNDLE =  "json_list_votes_bundle";
    public static final String JSON_LIST_VOTES_INTENT =  "json_list_votes_intent";



    public final static String URL_BASE = "http://ec2-52-47-206-114.eu-west-3.compute.amazonaws.com:8080/RestServices/";
    public final static String URL_LOGIN = "login";
    public final static String URL_SIGN_UP = "signUp";
    public final static String URL_COMPETITION_GET = "competition/get";
    public final static String URL_COMPETITION_POST = "competition/save";
    public final static String URL_COMPETITION_ADD_USER = "competition/addUser";
    public final static String URL_MATCH_GET = "match/get";
    public final static String URL_MATCH_POST = "match/save";
    public final static String URL_MATCH_CLOSE = "match/close";
    public final static String URL_MATCH_OPEN = "match/open";
    public final static String URL_BALLOT_POST = "ballot/save";
    public final static String URL_BALLOT_GET = "ballot/get";
    public final static String URL_USER_GET = "user/get";
    public final static String URL_RANKING_TOP = "ranking/top";
    public final static String URL_RANKING_FLOP = "ranking/flop";

    public final static String OPEN = "OPEN";
    public final static String CLOSED = "CLOSE";
    public final static String ON_HOLD = "ON_HOLD";
    public final static String ON_HOLD_TEXT = "ON HOLD";

    public final static String TOP = "TOP";
    public final static String FLOP = "FLOP";

    public final static String WITH_COMMENTS = "withComments";
    public final static String HAS_VOTED = "hasVoted";


    public final static String GOD = "Almighty God";


    public static String createURL(String URL_SPEC, Parameter... parameters){
        String url = URL_BASE + URL_SPEC + "?";
        for (int i = 0; i < parameters.length - 1 ; i++) {
            Parameter parameter = parameters[i];
            url += parameter.getKey() + "=" + parameter.getValue()+ "&";
        }
        url += parameters[parameters.length-1].getKey() + "=" + parameters[parameters.length-1].getValue();
        return url;
    }


    public static class Parameter{
        String key;
        String value;

        public Parameter(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
