package com.tcl.dias.oms.gsc.util;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Utility class to help with country related operations for GSC
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class GscIsoCountries {

    public static class GscCountry {
        private String name;
        private String code;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    private Map<String, GscCountry> countries;

    private Map<String, GscCountry> codes;

    private Map<String, Locale> locales;

    @PostConstruct
    public void loadIsoCountries() {

        this.countries = GscUtils.fromJsonFile("gsc/gsc_countries_iso.json", new TypeReference<List<GscCountry>>() {
        }).stream().collect(Collectors.toMap(gscCountry -> gscCountry.name.toLowerCase(), Function.identity()));

        this.codes = GscUtils.fromJsonFile("gsc/gsc_countries_iso.json", new TypeReference<List<GscCountry>>() {
        }).stream().collect(Collectors.toMap(gscCountry -> gscCountry.code, Function.identity()));

        this.locales = Arrays.stream(Locale.getISOCountries()).map(code -> new Locale("", code))
                .collect(Collectors.toMap(locale -> locale.getISO3Country().toUpperCase(), Function.identity()));
    }


    /**
     * Get the country object for given country name
     *
     * @param countryName
     * @return
     */
    public GscCountry forName(String countryName) {
        return countries.get(countryName.toLowerCase());
    }

    /**
     * Get the country object for given country code
     *
     * @param countryCode
     * @return
     */
    public GscCountry forCode(String countryCode) {
        return codes.get(countryCode);
    }

    /**
     * Get iso 2 digit country code for given 3 digit country code
     *
     * @param iso3Code
     * @return
     */
    public String iso2ForISO3Code(String iso3Code) {
        return Optional.ofNullable(locales.get(iso3Code)).map(Locale::getCountry).orElse(null);
    }
}
