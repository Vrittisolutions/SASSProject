package com.vritti.sass.model;

/**
 * Created by sharvari on 04-Dec-18.
 */

public class WebAPIUrl {
    //public static final String CompanyURL = "192.168.1.221";

   // public static String CompanyURL = "http://z207.ekatm.co.in";
    public static String CompanyURL = "https://ail.ekatm.co.in";
    //public static final String CompanyURL ="192.168.1.99:81";
    public static final String api_GetSessions = "/api/LoginAPI/GetSessions";
    public static final String api_GetIsValidUser = "/api/LoginAPI/GetIsValidUser";
    public static final String FCMurl = "/api/PushNotificationAPI/PostDeviceMaster";
    public static final String api_GetUserMasterId = "/api/TimesheetAPI/GetUserMasterId";
    public static final String api_getPlants = "/api/LoginAPI/GetPlants";
    public static final String api_GetCompletePermit = "/api/sassapi/GetPermitComplete";
    public static final String api_getEnv = "/api/LoginAPI/GetEnvis";
    public static final String api_GetUserMasterIdAndroid = "/api/TimesheetAPI/GetUserMasterIdForAndroid";
    public static final String api_GetStationList = "/api/MaterialIssueAPI/GetWarehouseList";
    public static final String api_GetApproverPerson = "/api/AssetTransferAPI/GetFillUser";
    public static final String api_GetUserListByDepo = "/api/SASSAPI/GetUserListByDepo";
    public static final String api_GetContractorList = "/api/CommonPurchaseAPI/GetSupplier";
    public static final String api_GetOperationList = "/api/LMAPI/getOperationTypeList";
    public static final String api_GetLocationOperation = "/api/MaterialRequisitionNoteAPI/GetLocationList";
    public static final String api_GetGoldenRules = "/api/sassapi/GETGoldenRules";
    public static final String api_GetPermitNo = "/api/SASSAPI/GetWA_No";/*GetAutoCode*//*api/sassapi/GetEP_No*/
    public static final String api_GetHWPermitNo = "/api/SASSAPI/GetHWP_No";
    public static final String api_GetCADPermitNo = "/api/SASSAPI/GetCAD_No";
    public static final String api_GetWAHPermitNo = "/api/SASSAPI/GetWAH_No";
    public static final String api_GetCSEPermitNo = "/api/SASSAPI/GetCSE_No";
    public static final String api_GetLPPermitNo = "/api/SASSAPI/GetLP_No";
    public static final String api_GetEPNo = "/api/SASSAPI/GetEP_No";
    public static final String api_FileUpload = "/api/ChatRoomAPI/UploadFiles";
    public static final String api_GetDataSheet = "/api/SASSAPI/GetDataSheet";
    public static final String api_GetPermitData = "/api/SASSAPI/GetPermitData";
    public static final String api_GetInstallationOperation = "/api/SASSAPI/GetInstallationOperation";
    public static final String api_GetSafetyTools = "/api/SASSAPI/GetSafetyTools";
    public static final String api_GetWANo = "/api/SASSAPI/getsuppliercontractorlist";
    public static final String api_GETWA_PermitNoDetail = "/api/SASSAPI/GETWA_PermitNoDetail";
    public static final String api_GetWAImages = "/api/SASSAPI/GetWAImages";

    public static final String api_PostWorkAuthorization = "/api/SASSAPI/POSTWA";
    public static final String api_PostHOTWork = "/api/SASSAPI/POSTHWP";
    public static final String api_PostConfinedSpaceEntry = "/api/SASSAPI/POSTCSE";
    public static final String api_LiftingPermit = "/api/SASSAPI/POSTLP";
    public static final String api_CleansingPermit = "/api/SASSAPI/POSTCAD";
    public static final String api_PostExcavationPermit = "/api/SASSAPI/POSTEP";
    public static final String api_PostWorkAtHeight = "/api/SASSAPI/POSTWAH";

    /*******************************************EDIT***************************************************/
    public static final String api_PosteditWorkAuthorization = "/api/SASSAPI/EditWA";
    public static final String api_PosteditHotWork = "/api/SASSAPI/EditHWP";
    public static final String api_PosteditCleansing = "/api/SASSAPI/EditCAD";
    public static final String api_PosteditConfined = "/api/SASSAPI/EditCSE";
    public static final String api_PosteditWAH = "/api/SASSAPI/EditWAH";
    public static final String api_PosteditLifting = "/api/SASSAPI/EditLP";
    public static final String api_PosteditExcavation = "/api/SASSAPI/EditEP";

    public static final String api_WAStatus = "/api/sassapi/WAStatus";
    public static final String api_HWStatus = "/api/SASSAPI/HWPStatus";
    public static final String api_CADStatus = "/api/SASSAPI/CADStatus";
    public static final String api_CSEStatus = "/api/SASSAPI/CSEStatus";
    public static final String api_WAHStatus = "/api/SASSAPI/WAHStatus";
    public static final String api_LPStatus = "/api/SASSAPI/LPStatus";
    public static final String api_EPStatus = "/api/SASSAPI/EPStatus";


    public static final String api_LotoPermit_Attachment = "/api/SASSAPI/lotoPermit";
    public static final String api_SpotImageDetails = "/api/SASSAPI/SpotImageDetails";
    public static final String api_EntityPost = "/api/EntityMasterAPI/Post";
    public static final String api_POSTUpdateEntity = "/api/EntityMasterAPI/POSTUpdateEntity";
    public static final String api_PostInsurance="/api/SASSAPI/PostInsurance";
    public static final String apiGetInsuranceData="/api/SASSAPI/GetInsuranceData";

    public static final String api_TagList = "/api/SASSAPI/GETTagData";

    public static final String api_getHWDetails = "/api/SASSAPI/PermitDetails";
    public static final String api_ChildPermitDetails = "/api/SASSAPI/ChildPermitDetails";
    public static final String Errormsg = "true";
    public static final String MyPREFERENCES = "LogginPreference";
    public static final String MyPREFERENCES_FIREBASE_TOKEN_KEY = "token";
    public static final String AppName = "SASS";
    public static final String GetRandomQuestionnaireData="/api/SASSAPI/GetRandomQuestionnaireData";
    public static final String api_PostShiftHandOver = "/api/SASSAPI/POSTShiftHandData";
    public static final String api_GetMaintenanceData = "/api/SASSAPI/GetMaintenanceData";
    public static final String GetShifthandoverData = "/api/SASSAPI/GetShifthandoverData";
    public static final String GetDowngradData = "/api/sassapi/GetDowngradData";


    public static final String GetTaskAutority_OnEncrptNm = "/api/TaskAuthorityAPI/GetTaskAutority?Mode=A&TechnicalName=Contractorlist";
    public static final String GetAlarmTagData = "/api/AlarmTagAPI/GetAlarmTagData";

}


