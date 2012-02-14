package org.archenos.qfj.test.integration;

public enum ProtocolVersion {
    FIX_4_0("FIX.4.0"),
    FIX_4_1("FIX.4.1"),
    FIX_4_2("FIX.4.2"),
    FIX_4_3("FIX.4.3"),
    FIX_4_4("FIX.4.4"),
    FIX_5_0("FIX.5.0"),
    FIX_5_0_SP1("FIX.5.0SP1"),
    FIX_5_0_SP2("FIX.5.0SP2");

    private final String beginString;
    
    private ProtocolVersion(String beginString) {
        this.beginString = beginString;
    }

    public String getBeginString() {
        return beginString;
    }
}
