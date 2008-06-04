/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mpi.rrs.model.ams;

import java.util.Iterator;
import java.util.List;

import nl.mpi.lat.ams.model.License;
import nl.mpi.lat.ams.model.NodeAuth;
import nl.mpi.lat.ams.model.NodePcplLicense;
import nl.mpi.lat.auth.authorization.AdvAuthorizationService;
import nl.mpi.lat.auth.federation.FedUID;
import nl.mpi.lat.auth.principal.LatPrincipal;
import nl.mpi.lat.auth.principal.LatUser;
import nl.mpi.lat.dao.DataSourceException;
import nl.mpi.lat.dao.UnknownNodeException;
import nl.mpi.lat.fabric.Node;
import nl.mpi.lat.fabric.NodeID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
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
                _log.info("dobesRootID: "  + node.getName());
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
                List<NodePcplLicense> npls = authzSrv.getLicenseAcceptance(targetUser, targetNodeID);

                // NOTE:
                // all information is wrapped into node-principal-license relation entities:
                // represented by value-object class NodePcplLicense

                if (npls == null || npls.isEmpty()) {
                    return "";
                }

                // just to demonstrate how to get and manipulate acceptance information
                for (Iterator<NodePcplLicense> iter = npls.iterator(); iter.hasNext();) {
                    NodePcplLicense npl = iter.next();

                    // this is the license which is assigned (and (maybe still) needs acceptance):
                    License lics = npl.getLicense();

                    result += "LicName: " + lics.getName() + "\n";
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
     * example: get license acceptance information 
     * and save (new) acceptance by/for a given user
     * @param targetUser the user to retrieve license-acceptance info for and save acceptance of such
     * @param targetNodeID  node-ID of the node=domain to get and modify license acceptance information 
     */
    public boolean acceptLicenseInfo(String userName, NodeID targetNodeID, String licName) {
        boolean success = false;
        
        LatUser targetUser = this.toLatUser(userName);

        try {
            // load the needed service:
            AdvAuthorizationService authzSrv = services.getAuthzSrv();

            // NOTE: considering the root node will provide ALL available information 
            // for the given user (= for the whole archive)
            if (targetNodeID == null) {
                targetNodeID = services.getFabricSrv().getRootNode();
            }

            // let's get the information: 
            List<NodePcplLicense> npls = authzSrv.getLicenseAcceptance(targetUser, targetNodeID);

            // NOTE:
            // all information is wrapped into node-principal-license relation entities:
            // represented by value-object class NodePcplLicense

            if (npls == null || npls.isEmpty()) {
                return false;
            }

            // just to demonstrate how to get and manipulate acceptance information
            for (Iterator<NodePcplLicense> iter = npls.iterator(); iter.hasNext();) {
                NodePcplLicense npl = iter.next();

                // this is the parent node-user relation 
                // which bears/represents the current node and target user 
                NodeAuth parent = npl.getParent();

                // this is the (current) node the license is assigned to: 
                Node node = npl.getParent().getNode();

                // this is again the considered target user: 
                LatPrincipal theUser = npl.getParent().getPcpl();

                // this is the license which is assigned (and (maybe still) needs acceptance):
                License lics = npl.getLicense();

                if (lics.getName().equalsIgnoreCase(licName)) {

                    if (npl.isAccepted()) {
                        _log.info("User: " + userName + " had already accepted licence: " + licName);
                    } else {
                        // to accept use the NPL:
                        npl.setAccepted();
                        // and set/update the modifier 
                        LatUser modifier = services.getPcplSrv().getSystemUser();

                        npl.setModifier(modifier);

                        // for SAVING then use the PARENT node-principal relation!!
                        try {
                            authzSrv.save(parent);  // throws DataSourceException (unchecked)
                            success = true;
                            _log.info("Acceptance of licence: " + licName + " by user: " + userName + " has been saved to AMS DB." );
                        } catch (DataSourceException dsE) {
                            _log.fatal("failed to save license acceptance for " + npl, dsE);
                        }
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
        try {
            // load the needed service:
            AdvAuthorizationService authzSrv = services.getAuthzSrv();

            // NOTE: considering the root node will provide ALL available information 
            // for the given user (= for the whole archive)
            if (targetNodeID == null) {
                targetNodeID = services.getFabricSrv().getRootNode();
            }

            // let's get the information: 
            List<NodePcplLicense> npls = authzSrv.getLicenseAcceptance(targetUser, targetNodeID);

            // NOTE:
            // all information is wrapped into node-principal-license relation entities:
            // represented by value-object class NodePcplLicense

            if (npls == null || npls.isEmpty()) {
                return;
            }

            // just to demonstrate how to get and manipulate acceptance information
            for (Iterator<NodePcplLicense> iter = npls.iterator(); iter.hasNext();) {
                NodePcplLicense npl = iter.next();

                // this is the parent node-user relation 
                // which bears/represents the current node and target user 
                NodeAuth parent = npl.getParent();

                // this is the (current) node the license is assigned to: 
                Node node = npl.getParent().getNode();

                // this is again the considered target user: 
                LatPrincipal theUser = npl.getParent().getPcpl();

                // this is the license which is assigned (and (maybe still) needs acceptance):
                License lics = npl.getLicense();

                // has targetUser accepted this license (on that node) ?
                boolean accepted = npl.isAccepted();

                // to accept use the NPL:
                npl.setAccepted();
                // and set/update the modifier 
                LatUser modifier = services.getPcplSrv().getSystemUser();

                npl.setModifier(modifier);

                // for SAVING then use the PARENT node-principal relation!!
                try {
                    authzSrv.save(parent);  // throws DataSourceException (unchecked)
                } catch (DataSourceException dsE) {
                    _log.fatal("failed to save license acceptance for " + npl, dsE);
                }
            }
        } catch (UnknownNodeException unE) {
            _log.fatal("encountered stale node-ID", unE);
            services.getAuthzSrv().staleNode(unE.getNodeID());
        }
    }
}
