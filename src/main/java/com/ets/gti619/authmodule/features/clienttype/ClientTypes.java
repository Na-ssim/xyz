package com.ets.gti619.authmodule.features.clienttype;

public enum ClientTypes {
    CLIENTS_RESIDENTIELS("Un client residentiel", "Liste de clients residentiels"),
    CLIENTS_AFFAIRE("Un client d'affaire", "Liste de clients d'affaire");

    ClientTypes(String clientType, String tableTitle) {
        this.clientType = clientType;
        this.tableTitle = tableTitle;
    }

    private final String clientType;
    private final String tableTitle;

    public String getType() {
        return clientType;
    }
    public String getTableTitle() {
        return tableTitle;
    }
}
