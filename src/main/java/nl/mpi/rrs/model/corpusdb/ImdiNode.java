/*
 * ImdiResourceNode.java
 *
 * Created on February 7, 2007, 12:03 PM
 *
 */

package nl.mpi.rrs.model.corpusdb;

/**
 *
 * @author kees
 */
import nl.mpi.corpusstructure.NodeIdUtils;
import nl.mpi.corpusstructure.UnknownNodeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ImdiNode {
    private final static Log logger = LogFactory.getLog(ImdiNode.class);

    private static String openPathPrefix;
    
    /** Creates a new instance of ImdiNode */
    public ImdiNode() {
    }
    
    private int imdiNodeIdAsInt;         // imdiNodeId integer version e.g.    923456
    private String imdiNodeIdWithPrefix; // imdiNodeId full version    e.g. MPI923456#
    private String imdiNodeName;
    private String imdiNodeUrl;
    private String imdiNodeUri;
    private int imdiNodeType;
    private String imdiNodeFormat;
    
    public String getImdiNodeName() {
        return imdiNodeName;
    }
    
    public void setImdiNodeName(String ImdiNodeName) {
        this.imdiNodeName = ImdiNodeName;
    }
    
    public String getImdiNodeUrl() {
        return imdiNodeUrl;
    }
    
    public void setImdiNodeUrl(String ImdiNodeUrl) {
        this.imdiNodeUrl = ImdiNodeUrl;
    }
    
    public String getImdiNodeIdWithPrefix() {
        return imdiNodeIdWithPrefix;
    }
    
    public void setImdiNodeIdWithPrefix(String imdiNodeIdWithPrefix) {
        try {
            if (NodeIdUtils.isNodeId(imdiNodeIdWithPrefix)) {
                this.imdiNodeIdWithPrefix = imdiNodeIdWithPrefix;
                this.imdiNodeIdAsInt = NodeIdUtils.TOINT(imdiNodeIdWithPrefix);
            } else if (imdiNodeIdWithPrefix.matches("[0-9]+")) {
                this.imdiNodeIdWithPrefix = NodeIdUtils.TONODEID(Integer.parseInt(imdiNodeIdWithPrefix));
                this.imdiNodeIdAsInt = Integer.parseInt(imdiNodeIdWithPrefix);
            }
        } catch (NumberFormatException ex) {
            logger.error("ImdiNode:setImdiNodeIdWithPrefix(1)", ex);
            //ex.printStackTrace();
        } catch (UnknownNodeException ex) {
            logger.error("ImdiNode:setImdiNodeIdWithPrefix(2)", ex);
            //ex.printStackTrace();
        }
    }
    
    public int getImdiNodeIdAsInt() {
        return imdiNodeIdAsInt;
    }
    
    public void setImdiNodeIdAsInt(String imdiNodeId) {
        this.imdiNodeIdAsInt = NodeIdUtils.TOINT(imdiNodeId);
        this.imdiNodeIdWithPrefix = NodeIdUtils.TONODEID(imdiNodeIdAsInt);
    }

    public int getImdiNodeType() {
        return imdiNodeType;
    }

    public void setImdiNodeType(int imdiNodeType) {
        this.imdiNodeType = imdiNodeType;
    }

    public String getImdiNodeFormat() {
        return imdiNodeFormat;
    }

    public void setImdiNodeFormat(String imdiNodeFormat) {
        this.imdiNodeFormat = imdiNodeFormat;
    }

    public String getImdiNodeUri() {
        return imdiNodeUri;
    }

    public void setImdiNodeUri(String imdiNodeUri) {
        this.imdiNodeUri = imdiNodeUri;
    }

    public static String getOpenPathPrefix() {
        return openPathPrefix;
    }

    public static void setOpenPathPrefix(String aOpenPathPrefix) {
        openPathPrefix = aOpenPathPrefix;
    }
    
    
}
