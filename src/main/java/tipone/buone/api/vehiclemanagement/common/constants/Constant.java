package tipone.buone.api.vehiclemanagement.common.constants;

import tipone.buone.api.vehiclemanagement.models.entities.*;

import java.util.Map;
import java.util.Set;

public class Constant {
    public static final Map<Class<?>, String> PREFIX_ENTITIES = Map.of(
            Account.class, "ACC",
            RefreshToken.class, "RTK",
            Role.class, "ROL",
            Country.class, "COU",
            Customer.class, "CUS",
            VehicleModel.class, "VHM",
            Warehouse.class, "WH"
    );
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String TIMEZONE_VIETNAM = "Asia/Ho_Chi_Minh";
    public static final String[] PUBLIC_ENDPOINTS = {
            "/api/awake",
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/forgot-password",
            "/api/auth/verify-otp",
            "/api/auth/reset-password",
            "/api/auth/verify-registration",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/api/v3/api-docs/**"
    };
    public static final String REFRESH_TOKEN_ENDPOINT = "/api/auth/refresh-token";
    public static final String REFRESH_TOKEN = "Refresh-Token";
    public static final Set<String> CUSTOMER_SORT_FIELDS = Set.of(
            "id","fullName", "email", "address", "taxNumber", "phone", "country.dial", "createdAt"
    );
    public static final Set<String> CUSTOMER_SORT_FIELDS_FOR_MODEL  = Set.of(
            "fullName"
    );
    public static final Set<String> CUSTOMER_SEARCH_FIELDS = Set.of(
            "id","fullName", "email", "address", "taxNumber", "phone", "country.dial"
    );
    public static final Set<String> CUSTOMER_SEARCH_FIELDS_FOR_MODEL = Set.of(
            "fullName"
    );
    public static final Set<String> FETCH_RELATIONS_WITH_CUSTOMER  = Set.of(
            "country"
    );
    public static final Set<String> VEHICLE_MODEL_SORT_FIELDS = Set.of(
            "code", "name", "type", "customer.fullName", "createdAt"
    );
    public static final Set<String> VEHICLE_MODEL_SEARCH_FIELDS = Set.of(
            "code", "name", "type", "customer.fullName"
    );
    public static final Set<String> FETCH_RELATIONS_WITH_VEHICLE_MODEL = Set.of(
            "customer"
    );
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._]+@[a-zA-Z0-9.]+\\.[a-zA-Z]{2,}$";
    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,50}$";
    public static final String CUSTOMER_NAME_REGEX = "^(?=.{3,500}$)[a-zA-Z0-9]+( [a-zA-Z0-9]+)*$";
    public static final String PHONE_REGEX = "^\\d{4,20}$";
    public static final String TAX_NUMBER_REGEX = "^(?=.{5,20}$)[0-9]+(-[0-9]+)*$";
    public static final String MODEL_CODE_REGEX = "^[A-Z0-9]{8,20}$";
    public static final String MODEL_NAME_REGEX = "^[A-Za-z0-9\\s.-]{3,}$";
    public static final String WAREHOUSE_NAME_REGEX = "^(?=.{4,255})(?! )[A-Za-z]+(?: [A-Za-z]+)*(?<! )$";
}