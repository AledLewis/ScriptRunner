package com.fivium.scriptrunner2;

import com.fivium.scriptrunner2.util.XFUtil;

/**
 * Created with IntelliJ IDEA.
 * User: aled2
 * Date: 03/09/14
 * Time: 21:46
 * To change this template use File | Settings | File Templates.
 */
public class CommandLineDatabaseConnectionParams implements DatabaseConnectionParams {
    private CommandLineWrapper mCommandLineOptions;
    private static final String JDBC_PREFIX =  "jdbc:oracle:thin:@";

    public CommandLineDatabaseConnectionParams (CommandLineWrapper commandLineWrapper){
        this.mCommandLineOptions = commandLineWrapper;
    }

    @Override
    public boolean shouldConnectAsSysDBA() {
        return this.mCommandLineOptions.hasOption(CommandLineOption.DB_SYSDBA);
    }

    /**
     * Establishes a JDBC connection string from the various combinations of arguments that can be provided to the ScriptRunner
     * command line. In order of precedence, these are:
     * <ol>
     * <li>A fully-specified JDBC connection string</li>
     * <li>Individual arguments for host, port and SID or service name (SID takes priority if both are specified)</li>
     * <li>Prompted stdin input for host, port and SID</li>
     * </ol>
     * @return A JDBC connection string.
     */
    @Override
    public String getConnectionSting() {
        String lConnectionString;
        String lCmdLineJDBC = mCommandLineOptions.getOption(CommandLineOption.JDBC_CONNECT_STRING);
        if(!XFUtil.isNull(lCmdLineJDBC)){
            //1) if specified in full, use that
            lConnectionString = lCmdLineJDBC;
        }
        else {
            //2) if not specified, look for individual arguments
            String lHostName = mCommandLineOptions.getOption(CommandLineOption.DB_HOST);
            String lPort = mCommandLineOptions.getOption(CommandLineOption.DB_PORT);
            String lSID = mCommandLineOptions.getOption(CommandLineOption.DB_SID);
            String lServiceName = mCommandLineOptions.getOption(CommandLineOption.DB_SERVICE_NAME);

            //3) If still not specified, prompt user
            if(XFUtil.isNull(lHostName)){
                lHostName = CommandLineWrapper.readArg("Enter database hostname", false);
            }

            if(XFUtil.isNull(lPort)){
                lPort = CommandLineWrapper.readArg("Enter database port", false);
            }

            if(XFUtil.isNull(lSID) && XFUtil.isNull(lServiceName)){
                lSID = CommandLineWrapper.readArg("Enter database SID (for service name, specify -service argument)", false);
            }

            if(!XFUtil.isNull(lSID)){
                //Construct SID connect syntax if a SID was specified
                lConnectionString = JDBC_PREFIX + lHostName + ":" + lPort + ":" + lSID;
            }
            else {
                //Otherwise construct service name connect syntax
                lConnectionString = JDBC_PREFIX + "//" + lHostName + ":" + lPort + "/" + lServiceName;
            }

        }
        return lConnectionString;
    }

    @Override
    public String getPromoteUser() {
        return this.mCommandLineOptions.getOption(CommandLineOption.PROMOTE_USER);
    }

    @Override
    public String getPromoteUserPassword() {
        return this.mCommandLineOptions.getOption(CommandLineOption.PROMOTE_PASSWORD);
    }
}
