package com.gamecatalog.dto.importdata;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamGamesDTO {
    @JsonProperty("app_id")
    private Long appId;
    private String name;
    @JsonProperty("short_description")
    private String shortDescription;
    @JsonProperty("is_free")
    private Boolean isFree;
    private Double price;
    private String currency;
    @JsonProperty("price_status")
    private String priceStatus;
    @JsonProperty("controller_support")
    private String controllerSupport;
    @JsonProperty("required_age")
    private Integer requiredAge;
    @JsonProperty("is_windows_compatibile")
    private Boolean isWindows;
    @JsonProperty("is_mac_compatibile")
    private Boolean isMac;
    @JsonProperty("is_linux_compatibile")
    private Boolean isLinux;
}