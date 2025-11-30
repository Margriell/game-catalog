package com.gamecatalog.dto.importdata;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SteamGamesSalesDTO {
    @JsonProperty("Year")
    private String year;
    @JsonProperty("Genre")
    private String genre;
    @JsonProperty("Publisher")
    private String publisher;
    @JsonProperty("Platform")
    private String platform;
    @JsonProperty("NA_Sales")
    private Double naSales;
    @JsonProperty("EU_Sales")
    private Double euSales;
    @JsonProperty("JP_Sales")
    private Double jpSales;
    @JsonProperty("Other_Sales")
    private Double otherSales;
    @JsonProperty("Global_Sales")
    private Double globalSales;
}