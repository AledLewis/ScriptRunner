package com.fivium.scriptrunner2;

/**
 * Created with IntelliJ IDEA.
 * User: aled2
 * Date: 03/09/14
 * Time: 21:04
 * To change this template use File | Settings | File Templates.
 */
public interface DatabaseConnectionParams {
    public boolean shouldConnectAsSysDBA();

    public String getPromoteUser();

    public String getConnectionSting();

    public String getPromoteUserPassword();
}
