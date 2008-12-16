/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mpi.rrs.model.ams;

import java.util.Iterator;
import java.util.List;

import java.util.Set;
import nl.mpi.lat.ams.model.License;
import nl.mpi.lat.ams.model.NodePcplLicense;
import nl.mpi.lat.ams.model.NodeLicense;
import nl.mpi.lat.auth.authorization.AdvAuthorizationService;
import nl.mpi.lat.auth.federation.FedUID;
import nl.mpi.lat.auth.principal.LatUser;
import nl.mpi.lat.dao.DataSourceException;
import nl.mpi.lat.dao.UnknownNodeException;
import nl.mpi.lat.fabric.Node;
import nl.mpi.lat.fabric.NodeID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * AMS License related functions
 * @author kees
 */
public class AmsLicense {

    private AmsServices services;
    private static Log _log = LogFactory.getLog(AmsLicense.class);

    public AmsLicense() {
        this.services = AmsServicesSingleton.getInstance();
    }

    /**
     * transcribes given userName to FedUID signature,
     * ensures valid fed-id part (using default in case)
     * @param userName
     * @return
     */
    private FedUID toFedUid(String userName) {
        FedUID result = services.getPcplSrv().newDamLrID(userName);
        return result;
    }

    /**
     * 
     */
    public LatUser toLatUser(String userName) {
        LatUser latUser = null;

        try {
            FedUID uid = this.toFedUid(userName);
            latUser = services.getPcplSrv().getUser(uid);
        } catch (DataSourceException ex) {
            _log.info("username: " + userName + "can't be converted to LatUser!");

        }

        return latUser;
    }

    /**
     * logs all licences under node targetNodeID
     * logs all accepted licenses under node targetNodeID for user 
     * @param userName ams2 login name 
     * @param targetNodeID  node-ID of the node=domain to get license acceptance information 
     * 
     */
    public String getLicenseInfo(String userName, NodeID targetNodeID) {
        String result = "";
        //String dobesLicenceName = "Dobes Code of Conduct v2";

        LatUser targetUser = this.toLatUser(userName);
        if (targetUser != null) {
            try {
                // load the needed service:
                AdvAuthorizationService authzSrv = services.getAuthzSrv();

                // NOTE: considering the root node will provide ALL available information 
                // for the given user (= for the whole archive)
                if (targetNodeID == null) {
                    targetNodeID = services.getFabricSrv().getRootNode();
                    Node node = services.getFabricSrv().getNode(targetNodeID);
                    _log.info("RootNode: " + node.getName());

                /*
                NodeID dobesRootID = services.getFabricSrv().newNodeID("MPI77915#");
                node = services.getFabricSrv().getNode(dobesRootID);
                _log.info("dobesRootID: " + node.getName());
                targetNodeID = dobesRootID;
                 */

                /*
                NodeID lamsDemoRootID = services.getFabricSrv().newNodeID("MPI1#");
                node = services.getFabricSrv().getNode(lamsDemoRootID);
                _log.info("lamsDemoRootID: "  + node.getName());
                targetNodeID = lamsDemoRootID;
                 */
                }

                // let's get the information: 
                List<NodeLicense> nls = authzSrv.getLicenseAcceptance(targetNodeID, targetUser);           

                if (nls == null || nls.isEmpty()) {
                    _log.info("nls isEmpt: ");
                } else {
                    // just to demonstrate how to get acceptance information
                    // cycling on all the general licenses
                    for (Iterator<NodeLicense> iter = nls.iterator(); iter.hasNext();) {
                        NodeLicense nl = iter.next();
                        License lics = nl.getLicense();

                        _log.info("NodeLicense:  LicName: " + lics.getName());
                        _log.info("+++ LicLabel: " + lics.getLabel());
                        _log.info("+++ LicFile: " + lics.getFile());
                        _log.info("+++ userName: " + userName);

                        result += "NodeLicense:  LicName: " + lics.getName() + "\n";
                        result += "+++ LicFile: " + lics.getFile() + "\n";
                        
                        //this.acceptLicenseInfo(userName, targetNodeID, lics.getID());

                    }
                }
                
                // provides a representation of all licenses the given user HAS ALREADY ACCEPTED
                Set<NodePcplLicense> npcpls = authzSrv.getLicenseAcceptance(targetUser);

                // NOTE:
                // all information is wrapped into node-principal-license relation entities:
                // represented by value-object class NodePcplLicense

                if (npcpls == null || npcpls.isEmpty()) {
                    _log.info("npcpls isEmpt: ");
                    return "";
                }

                // just to demonstrate how to get acceptance information
                for (Iterator<NodePcplLicense> iter = npcpls.iterator(); iter.hasNext();) {
                    NodePcplLicense npl = iter.next();

                    // this is the license which is assigned (and (maybe still) needs acceptance):
                    License lics = npl.getLicense();
                    _log.info("NodePcplLicense LicName: " + lics.getName());
                    _log.info("+++ LicFile: " + lics.getFile());
                    _log.info("+++ Accepted: " + npl.isAccepted());
                    _log.info("+++ userName: " + userName);

                    result += "NodePcplLicense LicName: " + lics.getName() + "\n";
                    result += "+++ LicFile: " + lics.getFile() + "\n";
                    result += "+++ Accepted: " + npl.isAccepted() + "\n";

                }
            } catch (UnknownNodeException unE) {
                _log.fatal("encountered stale node-ID", unE);
                services.getAuthzSrv().staleNode(unE.getNodeID());
            }
        } else {
            _log.info("Can't get license info for unknown user: " + userName);
        }

        return result;
    }

    /**
     * accept licence to node for user and save
     * @param userName ams2 login name 
     * @param targetNodeID  node-ID of the node=domain to get and modify license acceptance information 
     * @param licName name of license e.g. Dobes Code of Conduct v2
     */
    public boolean acceptLicenseInfo(String userName, NodeID targetNodeID, String licName) {
        Integer licId = this.getLicId(licName, userName);
        _log.info("licId: " + licId.toString());
        if (licId.compareTo(-1) == 0) {
            _log.info("licenseId not found for: " + licName);
            return false;
        }
        
        return this.acceptLicenseInfo(userName, targetNodeID, licId);
    }

    /**
     * accept licence to node for user and save
     * @param userName ams2 login name 
     * @param targetNodeID  node-ID of the node=domain to get and modify license acceptance information 
     * @param licId  license Id in AMS DB e.g. 10
     */
    public boolean acceptLicenseInfo(String userName, NodeID targetNodeID, Integer licId) {
        boolean success = false;

        LatUser targetUser = this.toLatUser(userName);

        try {
            // load the needed service:
            AdvAuthorizationService authzSrv = services.getAuthzSrv();

            // NOTE: considering the root node will provide ALL available information 
            // for the given user (= for the whole archive)
            if (targetNodeID == null) {
                targetNodeID = services.getFabricSrv().getRootNode();
                _log.info("Root targetNode: " + targetNodeID.getMpiID());
            } else {
                _log.info("targetNode: " + targetNodeID.getMpiID());
            }

            // select a list of licences that user has to sign for this node
            List<NodeLicense> nls = authzSrv.getLicenseAcceptance(targetNodeID, targetUser);
            if (nls == null || nls.isEmpty()) {
                _log.info("No NodeLicense found under node: " + targetNodeID.getMpiID());
                return false;
            }

            for (Iterator<NodeLicense> iter = nls.iterator(); iter.hasNext();) {
                NodeLicense nl = iter.next();
                if (nl.getLicense().getID().compareTo(licId) == 0) {
                    _log.info("Licence found; try to save .. ");

                    try {
                        NodePcplLicense npl = authzSrv.saveLicenseAcceptance(nl.getNodeID(), nl.getLicense(), targetUser);
                        _log.info("license saved: " + licId);
                        _log.info("license accepted: " + npl.isAccepted());

                        return true;
                    } catch (DataSourceException dE) {
                        _log.error("Can't save license: " + licId);
                        _log.error(dE.getMessage());
                        return false;
                    }
                }
            }
        } catch (UnknownNodeException unE) {
            _log.fatal("encountered stale node-ID", unE);
            services.getAuthzSrv().staleNode(unE.getNodeID());
        }

        return success;
    }

    /**
     * example: get license acceptance information
     * and save (new) acceptance by/for a given user
     * @param targetUser the user to retrieve license-acceptance info for and save acceptance of such
     * @param targetNodeID  node-ID of the node=domain to get and modify license acceptance information
     */
    private void demoLicenseInfo(LatUser targetUser, NodeID targetNodeID) {
        // load the needed service:
        AdvAuthorizationService authzSrv = services.getAuthzSrv();

        // NOTE: considering the root node will provide ALL available information
        // for the given user (= for the whole archive)
        if (targetNodeID == null) {
            targetNodeID = services.getFabricSrv().getRootNode();

        }
// let's get the information:
// let's get the list of node-license relations set for the user under the targetNodeID
        List<NodeLicense> nls = authzSrv.getLicenseAcceptance(targetNodeID, targetUser);


        // provides a representation of all licenses the given user HAS ALREADY ACCEPTED
        Set<NodePcplLicense> npcpls = authzSrv.getLicenseAcceptance(targetUser);
        // NOTE:                // all information is wrapped into node-principal-license relation entities:
        // represented by value-object class NodePcplLicense
        if (nls == null || nls.isEmpty()) {
            return;
        }
// just to demonstrate how to get and manipulate acceptance information
// cycling on all the general licenses
        for (Iterator<NodeLicense> iter = nls.iterator(); iter.hasNext();) {
            NodeLicense nl = iter.next();
            // cycling on all the licenses for that user starting from the targetNodeID
            // to automatically accept all the couple <node,licenses> for the licenses already accepted
            for (Iterator<NodePcplLicense> iternpl = npcpls.iterator(); iter.hasNext();) {
                NodePcplLicense npl = (NodePcplLicense) iternpl.next();
                // if they have the same license
                if (nl.getLicense().getID().compareTo(npl.getLicense().getID()) == 0) {
                    // this is the license which is assigned (and (maybe still) needs acceptance):
                    License lics = npl.getLicense();
                    // to accept use the NPL:
                    npl.setAccepted();
                    // and set/update the modifier

                    LatUser modifier = services.getPcplSrv().getSystemUser();
                    //LatUser modifier = this.getCurrentUser(); // you should have it from somewhere
                    // for SAVING then use the proper method in the authorization service!!
                    authzSrv.saveLicenseAcceptance(nl.getNodeID(), nl.getLicense(), modifier);  // throws DataSourceException (unchecked)
                }

            }
        }
    }

    /**
     * get license Id of licName by comparing all licenses names under root node with licName
     * @param licName name of license e.g. Dobes Code of Conduct v2
     * @param userName ams2 login name
     * @return license Id or -1 if not found
     */
    public Integer getLicId(String licName, String userName) {
        Integer notFound = -1;
        AdvAuthorizationService authzSrv = services.getAuthzSrv();

        NodeID targetNodeID = AmsUtils.getCorpusRootNode();

        LatUser targetUser = this.toLatUser(userName);
        List<NodeLicense> nls = authzSrv.getLicenseAcceptance(targetNodeID, targetUser);

        if (nls == null || nls.isEmpty()) {
            return notFound;
        }

        for (Iterator<NodeLicense> iter = nls.iterator(); iter.hasNext();) {
            NodeLicense nl = iter.next();
            if (nl.getLicense().getName().equalsIgnoreCase(licName)) {
                return nl.getLicense().getID();
            }


        }
        return notFound;
    }
}
